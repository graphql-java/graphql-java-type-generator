package graphql.java.generator.type;

import static graphql.schema.GraphQLObjectType.newObject;
import static graphql.schema.GraphQLEnumType.newEnum;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import graphql.java.generator.BuildContext;
import graphql.schema.GraphQLEnumType;
import graphql.schema.GraphQLEnumValueDefinition;
import graphql.schema.GraphQLFieldDefinition;
import graphql.schema.GraphQLObjectType;
import graphql.schema.GraphQLOutputType;
import graphql.schema.GraphQLTypeReference;

/**
 * Given any object, decide how you wish the GraphQL type to be generated
 * Not yet certified with InputTypes, Interfaces, or arrays.
 * @author dwinsor
 *
 */
public class TypeGenerator {
    private static Logger logger = LoggerFactory.getLogger(TypeGenerator.class);
    
    protected final Map<String, GraphQLOutputType> generatedOutputTypes;
    protected TypeStrategies strategies;
    
    public TypeGenerator(TypeStrategies strategies) {
        this.strategies = strategies;
        generatedOutputTypes = TypeRepository.getGeneratedOutputTypes();
    }
    
    /**
     * Will use some default context if necessary
     * @param object A representative "object" from which to construct
     * a {@link GraphQLOutputType}, the exact type of which is contextual
     * @return
     */
    public GraphQLOutputType getOutputType(Object object) {
        return getOutputType(object, BuildContext.defaultContext);
    }

    /**
     * 
     * @param object A representative "object" from which to construct
     * a {@link GraphQLOutputType}, the exact type of which is contextual
     * @param currentContext
     * @return
     */
    public final GraphQLOutputType getOutputType(Object object, BuildContext currentContext) {
        logger.debug("object is [{}]", object);
        //short circuit if it's a primitive type or some other user defined default
        GraphQLOutputType defaultType = getDefaultOutputType(object);
        if (defaultType != null) {
            logger.debug("defaultType is [{}]", defaultType);
            return defaultType;
        }
        
        String typeName = getGraphQLTypeName(object);
        if (typeName == null) {
            logger.debug("TypeName was null for object [{}]. "
                    + "Type will be built but not place in the TypeRepository", object);
            return generateOutputType(object, currentContext);
        }
        logger.debug("TypeName for object [{}]", typeName);
        
        //this check must come before generatedOutputTypes.get
        //necessary for synchronicity to avoid duplicate object creations
        final Set<String> outputTypesBeingBuilt = currentContext.getOutputTypesBeingBuilt();
        if (outputTypesBeingBuilt.contains(typeName)) {
            logger.debug("Using a reference to: [{}]", typeName);
            return new GraphQLTypeReference(typeName);
        }

        if (currentContext.isUsingTypeRepository()) {
            if (generatedOutputTypes.containsKey(typeName)) {
                return generatedOutputTypes.get(typeName);
            }
        }
        
        outputTypesBeingBuilt.add(typeName);
        GraphQLOutputType type = generateOutputType(object, currentContext);
        if (currentContext.isUsingTypeRepository()) {
            TypeRepository.registerType(typeName, type);
        }
        outputTypesBeingBuilt.remove(typeName);
        return type;
    }
    
    protected GraphQLOutputType generateOutputType(Object object, BuildContext currentContext) {
        String typeName = getGraphQLTypeName(object);
        if (typeName == null) {
            typeName = "Object_" + String.valueOf(System.identityHashCode(object));
        }
        
        //An enum is a special case in both java and graphql
        List<GraphQLEnumValueDefinition> enumValues = getEnumValues(object);
        if (enumValues != null) {
            GraphQLEnumType.Builder builder = newEnum();
            builder.name(typeName);
            builder.description(getTypeDescription(object));
            for (GraphQLEnumValueDefinition value : enumValues) {
                builder.value(value.getName(), value.getValue(), value.getDescription());
            }
            return builder.build();
        }
        
        GraphQLObjectType.Builder builder = newObject();
        builder.name(typeName);
        builder.fields(getFieldDefinitions(object, currentContext));
        builder.description(getTypeDescription(object));
        return builder.build();
    }
    
    protected List<GraphQLFieldDefinition> getFieldDefinitions(Object object, BuildContext currentContext) {
        List<GraphQLFieldDefinition> definitions = 
                currentContext.getFieldsGeneratorStrategy()
                        .getFields(object, currentContext);
        return definitions;
    }
    
    protected GraphQLOutputType getDefaultOutputType(Object object) {
        return strategies.getDefaultTypeStrategy().getDefaultOutputType(object);
    }
    
    protected String getGraphQLTypeName(Object object) {
        return strategies.getTypeNameStrategy().getTypeName(object);
    }
    
    protected String getTypeDescription(Object object) {
        return strategies.getTypeDescriptionStrategy().getTypeDescription(object);
    }
    
    private List<GraphQLEnumValueDefinition> getEnumValues(Object object) {
        return strategies.getEnumValuesStrategy().getEnumValueDefinitions(object);
    }
}
