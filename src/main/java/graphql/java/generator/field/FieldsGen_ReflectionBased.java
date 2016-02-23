package graphql.java.generator.field;

import static graphql.schema.GraphQLFieldDefinition.newFieldDefinition;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import graphql.java.generator.BuildContext;
import graphql.java.generator.type.TypeGenerator;
import graphql.schema.FieldDataFetcher;
import graphql.schema.GraphQLFieldDefinition;
import graphql.schema.GraphQLList;
import graphql.schema.GraphQLOutputType;

/**
 * @author dwinsor
 *
 */
public class FieldsGen_ReflectionBased implements FieldsGenerator<Class<?>> {
    private static Logger logger = LoggerFactory.getLogger(
            FieldsGen_ReflectionBased.class);
    
    @Override
    public List<GraphQLFieldDefinition> getFields(Class<?> clazz) {
        return getFields(clazz, new BuildContext());
    }
    
    @Override
    public List<GraphQLFieldDefinition> getFields(Class<?> clazz, BuildContext parentContext) {
        List<GraphQLFieldDefinition> fieldDefs = new ArrayList<GraphQLFieldDefinition>();
        Set<String> fieldNames = new HashSet<String>();
        while (clazz != null && !clazz.isAssignableFrom(Object.class)) {
            Field[] fields = clazz.getDeclaredFields();
            for (int index = 0; index < fields.length; ++index) {
                Field field = fields[index];
                GraphQLFieldDefinition fieldDef = getFieldDefinition(field, clazz, parentContext).build();
                //check for shadowed fields, where field "item" in superclass
                //is shadowed by field "item" in subclass
                if (fieldNames.contains(fieldDef.getName())) {
                    continue;
                }
                fieldNames.add(fieldDef.getName());
                fieldDefs.add(fieldDef);
            }
            //we need to expose inherited fields
            clazz = clazz.getSuperclass();
        }
        return fieldDefs;
    }
    
    /**
     * May return null should this field be disallowed
     * @param field
     * @param clazz
     * @return
     */
    protected GraphQLFieldDefinition.Builder getFieldDefinition(
            final Field field, final Class<?> clazz, BuildContext currentContext) {
        if (field.isSynthetic()) {
            //The compiler added this field.
            return null;
        }
        //TODO don't do static fields.
        //TODO check for annotation that excludes this.
        
        logger.debug("Field named [{}] is of type [{}]",
                field.getName(), field.getType());
        GraphQLOutputType fieldType = getOutputTypeOfField(field, currentContext);
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
    
    /**
     * Works with fields that are Lists
     * @param field
     * @return
     */
    protected GraphQLOutputType getOutputTypeOfField(Field field, BuildContext currentContext) {
        Class<?> fieldClazz = field.getType();
        if (List.class.isAssignableFrom(fieldClazz)) {
            Type listType = field.getGenericType();
            if (listType instanceof ParameterizedType) {
                Type listGenericType = ((ParameterizedType) listType).getActualTypeArguments()[0];
                if (listGenericType instanceof Class<?>) {
                    logger.debug("Field [{}] is a list of generic type [{}]",
                            field.getName(), listGenericType);
                    TypeGenerator typeGen = currentContext
                            .getTypeGeneratorStrategy((Class<?>)listGenericType);
                    return new GraphQLList(typeGen.getOutputType((Class<?>)listGenericType, currentContext));
                }
            }
            //TODO test on List<?>, where ? is not an instanceof Class
            //TODO test on raw List, should not work
        }

        TypeGenerator typeGen = currentContext
                .getTypeGeneratorStrategy(fieldClazz);
        return typeGen.getOutputType(fieldClazz, currentContext);
    }

    @Override
    public void setFieldsStrategies() {
        // TODO Auto-generated method stub
        
    }
}
