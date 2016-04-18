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
import graphql.schema.GraphQLInputObjectField;
import graphql.schema.GraphQLInputObjectType;
import graphql.schema.GraphQLInputType;
import graphql.schema.GraphQLObjectType;
import graphql.schema.GraphQLOutputType;
import graphql.schema.GraphQLTypeReference;

/**
 * Given any object, decide how you wish the GraphQL type to be generated
 * Not yet certified with InputTypes or arrays.
 * @author dwinsor
 *
 */
public class TypeGenerator implements IContextualTypeGenerator {
    private static Logger logger = LoggerFactory.getLogger(TypeGenerator.class);
    
    protected final Map<String, GraphQLOutputType> generatedOutputTypes;
    protected final Map<String, GraphQLInputType> generatedInputTypes;
    protected TypeStrategies strategies;
    
    public TypeGenerator(TypeStrategies strategies) {
        this.strategies = strategies;
        generatedOutputTypes = TypeRepository.getGeneratedOutputTypes();
        generatedInputTypes = TypeRepository.getGeneratedInputTypes();
    }
    
    /**
     * Will use some default context if necessary
     * @param object A representative "object" from which to construct
     * a {@link GraphQLOutputType}, the exact type of which is contextual
     * @return
     */
    @Override
    public GraphQLOutputType getOutputType(Object object) {
        return getOutputType(object, BuildContext.defaultContext);
    }

    /**
     * Will use some default context if necessary
     * @param object A representative "object" from which to construct
     * a {@link GraphQLInputType}, the exact type of which is contextual
     * @return
     */
    @Override
    public GraphQLInputType getInputType(Object object) {
        return getInputType(object, BuildContext.defaultContext);
    }
    
    /**
     * 
     * @param object A representative "object" from which to construct
     * a {@link GraphQLOutputType}, the exact type of which is contextual
     * @param currentContext
     * @return
     */
    public final GraphQLOutputType getOutputType(Object object, BuildContext currentContext) {
        logger.debug("output object is [{}]", object);
        //short circuit if it's a primitive type or some other user defined default
        GraphQLOutputType defaultType = getDefaultOutputType(object);
        if (defaultType != null) {
            logger.debug("output defaultType is [{}]", defaultType);
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
    
    /**
     * 
     * @param object A representative "object" from which to construct
     * a {@link GraphQLInputType}, the exact type of which is contextual
     * @param currentContext
     * @return
     */
    @Override
    public GraphQLInputType getInputType(Object object, BuildContext currentContext) {
        logger.debug("input object is [{}]", object);
        //short circuit if it's a primitive type or some other user defined default
        GraphQLInputType defaultType = getDefaultInputType(object);
        if (defaultType != null) {
            logger.debug("input defaultType is [{}]", defaultType);
            return defaultType;
        }
        
        String typeName = getGraphQLTypeName(object);
        if (typeName == null) {
            logger.debug("TypeName was null for object [{}]. "
                    + "Type will be built but not place in the TypeRepository", object);
            return generateInputType(object, currentContext);
        }
        logger.debug("TypeName for object [{}]", typeName);
        
        //this check must come before generatedInputTypes.get
        //necessary for synchronicity to avoid duplicate object creations
        final Set<String> inputTypesBeingBuilt = currentContext.getInputTypesBeingBuilt();
        if (inputTypesBeingBuilt.contains(typeName)) {
            logger.error("While constructing input type, using a reference to: [{}]", typeName);
            throw new RuntimeException("Cannot put type-cycles into input types, "
                    + "there is no GraphQLTypeReference");
        }

        if (currentContext.isUsingTypeRepository()) {
            if (generatedInputTypes.containsKey(typeName)) {
                return generatedInputTypes.get(typeName);
            }
        }
        
        inputTypesBeingBuilt.add(typeName);
        GraphQLInputType type = generateInputType(object, currentContext);
        if (currentContext.isUsingTypeRepository()) {
            TypeRepository.registerType(typeName, type);
        }
        inputTypesBeingBuilt.remove(typeName);
        return type;
    }

    protected GraphQLOutputType generateOutputType(Object object, BuildContext currentContext) {
        String typeName = getGraphQLTypeName(object);
        if (typeName == null) {
            typeName = "Object_" + String.valueOf(System.identityHashCode(object));
        }
        
        GraphQLEnumType enumType = buildEnumType(object, typeName);
        if (enumType != null) {
            return enumType;
        }
        
        GraphQLObjectType.Builder builder = newObject();
        builder.name(typeName);
        builder.fields(getOutputFieldDefinitions(object, currentContext));
        builder.description(getTypeDescription(object));
        return builder.build();
    }
    
    protected GraphQLInputType generateInputType(Object object, BuildContext currentContext) {
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
        builder.fields(getInputFieldDefinitions(object, currentContext));
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

    protected List<GraphQLFieldDefinition> getOutputFieldDefinitions(Object object, BuildContext currentContext) {
        List<GraphQLFieldDefinition> definitions = 
                currentContext.getFieldsGeneratorStrategy()
                        .getOutputFields(object, currentContext);
        return definitions;
    }
    
    protected List<GraphQLInputObjectField> getInputFieldDefinitions(Object object, BuildContext currentContext) {
        List<GraphQLInputObjectField> definitions = 
                currentContext.getFieldsGeneratorStrategy()
                        .getInputFields(object, currentContext);
        return definitions;
    }
    
    protected GraphQLOutputType getDefaultOutputType(Object object) {
        return strategies.getDefaultTypeStrategy().getDefaultOutputType(object);
    }
    
    protected GraphQLInputType getDefaultInputType(Object object) {
        return strategies.getDefaultTypeStrategy().getDefaultInputType(object);
    }
    
    protected String getGraphQLTypeName(Object object) {
        return strategies.getTypeNameStrategy().getTypeName(object);
    }
    
    protected String getTypeDescription(Object object) {
        return strategies.getTypeDescriptionStrategy().getTypeDescription(object);
    }
    
    protected List<GraphQLEnumValueDefinition> getEnumValues(Object object) {
        return strategies.getEnumValuesStrategy().getEnumValueDefinitions(object);
    }
}
