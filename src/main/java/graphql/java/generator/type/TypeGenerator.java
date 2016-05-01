package graphql.java.generator.type;

import static graphql.schema.GraphQLObjectType.newObject;
import static graphql.schema.GraphQLEnumType.newEnum;

import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import graphql.java.generator.BuildContext;
import graphql.java.generator.BuildContextAware;
import graphql.introspection.Introspection.TypeKind;
import graphql.java.generator.UnsharableBuildContextStorer;
import graphql.schema.GraphQLEnumType;
import graphql.schema.GraphQLEnumValueDefinition;
import graphql.schema.GraphQLFieldDefinition;
import graphql.schema.GraphQLInputObjectField;
import graphql.schema.GraphQLInputObjectType;
import graphql.schema.GraphQLInputType;
import graphql.schema.GraphQLInterfaceType;
import graphql.schema.GraphQLObjectType;
import graphql.schema.GraphQLOutputType;
import graphql.schema.GraphQLType;
import graphql.schema.GraphQLTypeReference;
import graphql.schema.TypeResolver;

/**
 * Given any object, decide how you wish the GraphQL type to be generated.
 * Not yet certified with arrays.
 * @author dwinsor
 *
 */
public class TypeGenerator
        extends UnsharableBuildContextStorer
        implements ITypeGenerator, BuildContextAware {
    private static Logger logger = LoggerFactory.getLogger(TypeGenerator.class);
    
    private TypeStrategies strategies;
    private TypeRepository typeRepository;
    
    public TypeGenerator(TypeStrategies strategies) {
        this.setStrategies(strategies);
        this.typeRepository = strategies.getTypeRepository();
    }
    
    /**
     * 
     * @param object A representative "object" from which to construct
     * a {@link GraphQLOutputType}, the exact type of which is contextual
     * @return
     */
    @Override
    public final GraphQLOutputType getOutputType(Object object) {
        return (GraphQLOutputType) getType(object, null, TypeKind.OBJECT);
    }
    
    /**
     * @param object A representative "object" from which to construct
     * a {@link GraphQLInterfaceType}, the exact type of which is contextual,
     * but MUST represent a java interface, NOT an object or class with an interface.
     * Will be stored internally as a {@link GraphQLOutputType}, so its name
     * must not clash with any GraphQLOutputType.
     * 
     * @return
     */
    @Override
    public final GraphQLInterfaceType getInterfaceType(Object object) {
        return (GraphQLInterfaceType) getType(object, null, TypeKind.INTERFACE);
    }

    /**
     * @param object A representative "object" from which to construct
     * a {@link GraphQLInputType}, the exact type of which is contextual
     * @return
     */
    @Override
    public final GraphQLInputType getInputType(Object object) {
        return (GraphQLInputType) getType(object, null, TypeKind.INPUT_OBJECT);
    }
    
    
    
    @Override
    public final GraphQLType getParameterizedType(Object object,
            ParameterizedType genericType, TypeKind typeKind) {
        return getType(object, genericType, typeKind);
    }
    
    protected final GraphQLType getType(Object object,
            ParameterizedType genericType, TypeKind typeKind) {
        logger.debug("{} object is [{}]", typeKind, object);
        
        //short circuit if it's a primitive type or some other user defined default
        GraphQLType defaultType = getDefaultType(object, typeKind);
        if (defaultType != null) {
            return defaultType;
        }
        
        
        GraphQLType wrappedType = getTypeWrapper(object, genericType, typeKind);
        if (wrappedType != null) {
            return wrappedType;
        }
        
        
        String typeName = getGraphQLTypeName(object);
        if (typeName == null) {
            logger.debug("TypeName was null for object [{}]. "
                    + "Type will attempt to be built but not placed in the TypeRepository", object);
            return generateType(object, typeKind);
        }
        
        
        //this check must come before generated*Types.get
        //necessary for synchronicity to avoid duplicate object creations
        Set<String> typesBeingBuilt = getContext().getTypesBeingBuilt();
        if (typesBeingBuilt.contains(typeName)) {
            logger.debug("Using a reference to: [{}]", typeName);
            if (TypeKind.OBJECT.equals(typeKind)) {
                return new GraphQLTypeReference(typeName);
            }
            logger.error("While constructing type, using a reference to: [{}]", typeName);
            throw new RuntimeException("Cannot put type-cycles into input or interface types, "
                    + "there is no GraphQLTypeReference");
        }
        
        
        GraphQLType prevType = typeRepository.getGeneratedType(typeName, typeKind);
        if (prevType != null) {
            return prevType;
        }
        
        
        typesBeingBuilt.add(typeName);
        try {
            GraphQLType type = generateType(object, typeKind);
            if (typeRepository != null) {
                typeRepository.registerType(typeName, type, typeKind);
            }
            return type;
        }
        catch (RuntimeException e) {
            logger.warn("Failed to generate type named {} with kind {}", typeName, typeKind);
            logger.debug("Failed to generate type, exception is ", e);
            throw e;
        }
        finally {
            typesBeingBuilt.remove(typeName);
        }
    }

    protected GraphQLType generateType(Object object, TypeKind typeKind) {
        switch (typeKind) {
        case OBJECT:
            return generateOutputType(object);
        case INTERFACE:
            return generateInterfaceType(object);
        case INPUT_OBJECT:
            return generateInputType(object);
        default:
            return null;
        }
    }
    
    protected GraphQLOutputType generateOutputType(Object object) {
        String typeName = getGraphQLTypeNameOrIdentityCode(object);
        
        GraphQLEnumType enumType = buildEnumType(object, typeName);
        if (enumType != null) {
            return enumType;
        }
        
        GraphQLObjectType.Builder builder = newObject()
                .name(typeName)
                .fields(getOutputFieldDefinitions(object))
                .description(getTypeDescription(object));
        
        GraphQLInterfaceType[] interfaces = getInterfaces(object);
        if (interfaces != null) {
            builder.withInterfaces(interfaces);
        }
        return builder.build();
    }
    
    protected GraphQLInterfaceType generateInterfaceType(Object object) {
        String name = getGraphQLTypeNameOrIdentityCode(object);
        List<GraphQLFieldDefinition> fieldDefinitions = getOutputFieldDefinitions(object);
        TypeResolver typeResolver = getTypeResolver(object);
        String description = getTypeDescription(object);
        if (name == null || fieldDefinitions == null || typeResolver == null) {
            return null;
        }
        GraphQLInterfaceType.Builder builder = GraphQLInterfaceType.newInterface()
                .description(description)
                .fields(fieldDefinitions)
                .name(name)
                .typeResolver(typeResolver);
        return builder.build();
    }
    
    protected GraphQLInputType generateInputType(Object object) {
        String typeName = getGraphQLTypeNameOrIdentityCode(object);
        
        //An enum is a special case in both java and graphql
        GraphQLEnumType enumType = buildEnumType(object, typeName);
        if (enumType != null) {
            return enumType;
        }
        
        GraphQLInputObjectType.Builder builder = new GraphQLInputObjectType.Builder();
        builder.name(typeName);
        builder.fields(getInputFieldDefinitions(object));
        builder.description(getTypeDescription(object));
        return builder.build();
    }

    protected GraphQLEnumType buildEnumType(Object object, String typeName) {
        List<GraphQLEnumValueDefinition> enumValues = getEnumValues(object);
        if (enumValues == null) {
            return null;
        }
        GraphQLEnumType.Builder builder = newEnum();
        builder.name(typeName);
        builder.description(getTypeDescription(object));
        for (GraphQLEnumValueDefinition value : enumValues) {
            builder.value(value.getName(), value.getValue(), value.getDescription());
        }
        return builder.build();
    }

    
    
    
    
    protected List<GraphQLFieldDefinition> getOutputFieldDefinitions(Object object) {
        List<GraphQLFieldDefinition> definitions = 
                getContext().getFieldsGeneratorStrategy()
                        .getOutputFields(object);
        return definitions;
    }
    
    protected List<GraphQLInputObjectField> getInputFieldDefinitions(Object object) {
        List<GraphQLInputObjectField> definitions = 
                getContext().getFieldsGeneratorStrategy()
                        .getInputFields(object);
        return definitions;
    }
    
    protected GraphQLType getDefaultType(Object object, TypeKind typeKind) {
        return getStrategies().getDefaultTypeStrategy().getDefaultType(object, typeKind);
    }
    
    protected String getGraphQLTypeNameOrIdentityCode(Object object) {
        String typeName = getGraphQLTypeName(object);
        if (typeName == null) {
            typeName = "Object_" + String.valueOf(System.identityHashCode(object));
        }
        return typeName;
    }
    protected String getGraphQLTypeName(Object object) {
        return getStrategies().getTypeNameStrategy().getTypeName(object);
    }
    
    protected String getTypeDescription(Object object) {
        return getStrategies().getTypeDescriptionStrategy().getTypeDescription(object);
    }
    
    protected List<GraphQLEnumValueDefinition> getEnumValues(Object object) {
        return getStrategies().getEnumValuesStrategy().getEnumValueDefinitions(object);
    }
    
    protected GraphQLInterfaceType[] getInterfaces(Object object) {
        return getStrategies().getInterfacesStrategy().getInterfaces(object);
    }

    protected GraphQLType getTypeWrapper(Object object, ParameterizedType type, TypeKind typeKind) {
        return getStrategies().getTypeWrapperStrategy().getWrapperAroundType(object, type, typeKind);
    }
    
    protected TypeResolver getTypeResolver(Object object) {
        return getStrategies().getTypeResolverStrategy().getTypeResolver(object);
    }

    
    @Override
    public void setContext(BuildContext context) {
        super.setContext(context);
        getStrategies().setContext(context);
    }

    @Override
    public TypeStrategies getStrategies() {
        return strategies;
    }

    protected void setStrategies(TypeStrategies strategies) {
        this.strategies = strategies;
    }
}
