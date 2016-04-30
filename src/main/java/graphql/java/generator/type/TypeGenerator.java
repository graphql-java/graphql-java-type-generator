package graphql.java.generator.type;

import static graphql.schema.GraphQLObjectType.newObject;
import static graphql.schema.GraphQLEnumType.newEnum;

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
        logger.debug("output object is [{}]", object);
        //short circuit if it's a primitive type or some other user defined default
        GraphQLOutputType defaultType = getDefaultOutputType(object);
        if (defaultType != null) {
            logger.debug("output defaultType is [{}]", defaultType);
            return defaultType;
        }
        
        return (GraphQLOutputType) getType(object, TypeKind.OBJECT);
    }
    
    @Override
    public GraphQLInterfaceType getInterfaceType(Object object) {
        return (GraphQLInterfaceType) getType(object, TypeKind.INTERFACE);
    }

    /**
     * @param object A representative "object" from which to construct
     * a {@link GraphQLInputType}, the exact type of which is contextual
     * @return
     */
    @Override
    public GraphQLInputType getInputType(Object object) {
        logger.debug("input object is [{}]", object);
        //short circuit if it's a primitive type or some other user defined default
        GraphQLInputType defaultType = getDefaultInputType(object);
        if (defaultType != null) {
            logger.debug("input defaultType is [{}]", defaultType);
            return defaultType;
        }
        
        return (GraphQLInputType) getType(object, TypeKind.INPUT_OBJECT);
    }
    
    protected GraphQLType getType(Object object, TypeKind typeKind) {
        String typeName = getGraphQLTypeName(object);
        if (typeName == null) {
            logger.debug("TypeName was null for object [{}]. "
                    + "Type will attempt to be built but not placed in the TypeRepository", object);
            return generateType(object, typeKind);
        }
        logger.debug("TypeName for object [{}]", typeName);
        
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
            logger.warn("Failed to generate type named {}", typeName);
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
        String typeName = getGraphQLTypeName(object);
        if (typeName == null) {
            typeName = "Object_" + String.valueOf(System.identityHashCode(object));
        }
        
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
        String typeName = getGraphQLTypeName(object);
        if (typeName == null) {
            typeName = "Object_" + String.valueOf(System.identityHashCode(object));
        }
        return getStrategies().getInterfacesStrategy().getFromJavaInterface(object);
    }
    
    protected GraphQLInputType generateInputType(Object object) {
        String typeName = getGraphQLTypeName(object);
        if (typeName == null) {
            typeName = "Object_" + String.valueOf(System.identityHashCode(object));
        }
        
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
    
    protected GraphQLOutputType getDefaultOutputType(Object object) {
        return getStrategies().getDefaultTypeStrategy().getDefaultOutputType(object);
    }
    
    protected GraphQLInputType getDefaultInputType(Object object) {
        return getStrategies().getDefaultTypeStrategy().getDefaultInputType(object);
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
