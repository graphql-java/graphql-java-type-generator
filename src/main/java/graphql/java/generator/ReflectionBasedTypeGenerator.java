package graphql.java.generator;

import static graphql.schema.GraphQLFieldDefinition.newFieldDefinition;
import static graphql.schema.GraphQLObjectType.newObject;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import graphql.schema.FieldDataFetcher;
import graphql.schema.GraphQLFieldDefinition;
import graphql.schema.GraphQLList;
import graphql.schema.GraphQLObjectType;
import graphql.schema.GraphQLOutputType;
import graphql.schema.GraphQLScalarType;
import graphql.schema.GraphQLTypeReference;

/**
 * Not yet certified with Interfaces, Enums, or arrays.
 * @author dwinsor
 *
 */
public class ReflectionBasedTypeGenerator implements TypeGenerator<Class<?>> {
    private static Logger logger = LoggerFactory.getLogger(ReflectionBasedTypeGenerator.class);
    
    protected static final BuildContext staticContext = new BuildContext();
    protected final BuildContext currentContext;
    
    protected final Map<String, GraphQLOutputType> generatedOutputTypes;
    protected final Set<String> outputTypesBeingBuilt;
    
    public ReflectionBasedTypeGenerator() {
        this(staticContext);
    }
    
    public ReflectionBasedTypeGenerator(BuildContext buildContext) {
        this.currentContext = buildContext;
        //access these only once; for performance,
        //and distrust that BuildContext will return the same one through a getter later
        //TODO get strategies
        
        outputTypesBeingBuilt = TypeRepository.getOutputTypesBeingBuilt();
        generatedOutputTypes = TypeRepository.getGeneratedOutputTypes();
    }
    
    @Override
    public final GraphQLOutputType getOutputType(Class<?> clazz) {
        logger.debug("clazz is [{}]", clazz);
        //short circuit if it's a primitive type
        GraphQLScalarType scalarType = Scalars.getScalarType(clazz);
        if (scalarType != null) {
            logger.debug("scalarType is [{}]", scalarType);
            return scalarType;
        }
        
        String typeName = getGraphQLTypeName(clazz);
        if (typeName == null) {
            logger.debug("TypeName was null for class [{}]. "
                    + "Type will be built but not place in the TypeRepository", clazz);
            return generateOutputTypeFromClass(clazz);
        }
        logger.debug("TypeName for class [{}]", typeName);
        
        //this check must come before generatedOutputTypes.get
        //necessary for synchronicity to avoid duplicate object creations
        if (outputTypesBeingBuilt.contains(typeName)) {
            logger.debug("Using a reference to: [{}]", typeName);
            return new GraphQLTypeReference(typeName);
        }

        if (generatedOutputTypes.containsKey(typeName)) {
            return generatedOutputTypes.get(typeName);
        }
        
        outputTypesBeingBuilt.add(typeName);
        GraphQLOutputType type = generateOutputTypeFromClass(clazz);
        generatedOutputTypes.put(typeName, type);
        outputTypesBeingBuilt.remove(typeName);
        return type;
    }
    
    @SuppressWarnings("unchecked")
    protected String getGraphQLTypeName(Class<?> clazz) {
        return currentContext.getTypeNameStrategy().getTypeName(clazz);
    }
    
    protected GraphQLOutputType generateOutputTypeFromClass(Class<?> clazz) {
        GraphQLObjectType.Builder builder = newObject();
        String typeName = getGraphQLTypeName(clazz);
        builder.name(typeName);
        builder.fields(getFieldDefinitions(clazz));
        return builder.build();
    }
    
    
    /**
     * May return null should this field be disallowed
     * @param field
     * @param clazz
     * @return
     */
    protected GraphQLFieldDefinition.Builder getFieldDefinition(
            final Field field, final Class<?> clazz) {
        if (field.isSynthetic()) {
            //The compiler added this field.
            return null;
        }
        //TODO don't do static fields.
        //TODO check for annotation that excludes this.
        
        logger.debug("Field named [{}] is of type [{}]",
                field.getName(), field.getType());
        GraphQLOutputType fieldType = getOutputTypeOfField(field);
        logger.debug("Field will be of GraphQL type [{}]", fieldType);
        GraphQLFieldDefinition.Builder fieldBuilder = newFieldDefinition()
                .name(field.getName())//TODO configurable
                .type(fieldType);
        Method getter = getGetterMethod(field, clazz);
        if (getter != null) {
            //minor drawback in that the declared field in a superclass
            //cannot be accessed by getter in subclass
            //TODO EDIT: now check config for whitelist/blacklist, and based
            //on that, choose whether to exclude or not.
            
            //This is commented out because it is redundant, but included
            //here for documentation, to be changed later
            //fieldBuilder.dataFetcher(new PropertyDataFetcher(field.getName()));
        }
        else {
            if (!field.isAccessible()) {
                logger.debug("field [{}] is not accessible and has no getter", field);
                return null;
            }
            logger.debug("Direct field access Class [{}], field [{}], type [{}]",
                    clazz, field.getName(), field.getType());
            fieldBuilder.dataFetcher(new FieldDataFetcher(field.getName()));
        }
        return fieldBuilder;
    }
    
    /**
     * @param clazz
     * @return
     */
    protected List<GraphQLFieldDefinition> getFieldDefinitions(Class<?> clazz) {
        List<GraphQLFieldDefinition> definitions = new ArrayList<GraphQLFieldDefinition>();
        Set<String> fieldNames = new HashSet<String>();
        while (clazz != null && !clazz.isAssignableFrom(Object.class)) {
            Field[] fields = clazz.getDeclaredFields();
            for (int index = 0; index < fields.length; ++index) {
                Field field = fields[index];
                GraphQLFieldDefinition.Builder fieldBuilder =
                        getFieldDefinition(field, clazz);
                if (fieldBuilder == null) {
                    continue;
                }
                GraphQLFieldDefinition fieldDef = fieldBuilder.build();
                //check for shadowed fields, where field "item" in superclass
                //is shadowed by field "item" in subclass
                if (fieldNames.contains(fieldDef.getName())) {
                    continue;
                }
                fieldNames.add(fieldDef.getName());
                definitions.add(fieldDef);
            }
            //we need to expose inherited fields
            clazz = clazz.getSuperclass();
        }
        return definitions;
    }
    
    /**
     * Works with fields that are Lists
     * @param field
     * @return
     */
    protected GraphQLOutputType getOutputTypeOfField(Field field) {
        Class<?> fieldClazz = field.getType();
        if (List.class.isAssignableFrom(fieldClazz)) {
            Type listType = field.getGenericType();
            if (listType instanceof ParameterizedType) {
                Type listGenericType = ((ParameterizedType) listType).getActualTypeArguments()[0];
                if (listGenericType instanceof Class<?>) {
                    logger.debug("Field [{}] is a list of generic type [{}]",
                            field.getName(), listGenericType);
                    return new GraphQLList(getOutputType((Class<?>)listGenericType));
                }
            }
            //TODO test on List<?>, where ? is not an instanceof Class
            //TODO test on raw List, should not work
        }

        return getOutputType(fieldClazz);
    }

    /**
     * TODO fix when cases don't match, or other getter
     * @param field
     * @param clazzContainingField
     * @return
     */
    protected Method getGetterMethod(Field field, Class<?> clazzContainingField) {
        Class<?> type = field.getType();
        String fieldName = field.getName();
        String prefix = (type.isAssignableFrom(Boolean.class) || type.isAssignableFrom(boolean.class))
                ? "is" : "get";
        String getterName = prefix + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
        try {
            Method getter = clazzContainingField.getMethod(getterName);
            return getter;
        }
        catch (NoSuchMethodException e) {
            return null;
        }
    }
}
