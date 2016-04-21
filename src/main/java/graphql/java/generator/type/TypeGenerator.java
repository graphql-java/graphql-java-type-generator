package graphql.java.generator.type;

import static graphql.schema.GraphQLObjectType.newObject;
import static graphql.schema.GraphQLEnumType.newEnum;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import graphql.java.generator.BuildContext;
import graphql.java.generator.BuildContextAware;
import graphql.java.generator.BuildContextStorer;
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
public class TypeGenerator
        extends BuildContextStorer
        implements ITypeGenerator, BuildContextAware {
    private static Logger logger = LoggerFactory.getLogger(TypeGenerator.class);
    
    protected final Map<String, GraphQLOutputType> generatedOutputTypes;
    protected final Map<String, GraphQLInputType> generatedInputTypes;
    private TypeStrategies strategies;
    
    public TypeGenerator(TypeStrategies strategies) {
        this.setStrategies(strategies);
        generatedOutputTypes = TypeRepository.getGeneratedOutputTypes();
        generatedInputTypes = TypeRepository.getGeneratedInputTypes();
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
        
        String typeName = getGraphQLTypeName(object);
        if (typeName == null) {
            logger.debug("TypeName was null for object [{}]. "
                    + "Type will be built but not place in the TypeRepository", object);
            return generateOutputType(object);
        }
        logger.debug("TypeName for object [{}]", typeName);
        
        //this check must come before generatedOutputTypes.get
        //necessary for synchronicity to avoid duplicate object creations
        final Set<String> outputTypesBeingBuilt = getContext().getOutputTypesBeingBuilt();
        if (outputTypesBeingBuilt.contains(typeName)) {
            logger.debug("Using a reference to: [{}]", typeName);
            return new GraphQLTypeReference(typeName);
        }

        if (getContext().isUsingTypeRepository()) {
            if (generatedOutputTypes.containsKey(typeName)) {
                return generatedOutputTypes.get(typeName);
            }
        }
        
        outputTypesBeingBuilt.add(typeName);
        GraphQLOutputType type = generateOutputType(object);
        if (getContext().isUsingTypeRepository()) {
            TypeRepository.registerType(typeName, type);
        }
        outputTypesBeingBuilt.remove(typeName);
        return type;
    }
    
    /**
     * 
     * @param object A representative "object" from which to construct
     * a {@link GraphQLInputType}, the exact type of which is contextual
     * @param getContext()
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
        
        String typeName = getGraphQLTypeName(object);
        if (typeName == null) {
            logger.debug("TypeName was null for object [{}]. "
                    + "Type will be built but not place in the TypeRepository", object);
            return generateInputType(object);
        }
        logger.debug("TypeName for object [{}]", typeName);
        
        //this check must come before generatedInputTypes.get
        //necessary for synchronicity to avoid duplicate object creations
        final Set<String> inputTypesBeingBuilt = getContext().getInputTypesBeingBuilt();
        if (inputTypesBeingBuilt.contains(typeName)) {
            logger.error("While constructing input type, using a reference to: [{}]", typeName);
            throw new RuntimeException("Cannot put type-cycles into input types, "
                    + "there is no GraphQLTypeReference");
        }

        if (getContext().isUsingTypeRepository()) {
            if (generatedInputTypes.containsKey(typeName)) {
                return generatedInputTypes.get(typeName);
            }
        }
        
        inputTypesBeingBuilt.add(typeName);
        GraphQLInputType type = generateInputType(object);
        if (getContext().isUsingTypeRepository()) {
            TypeRepository.registerType(typeName, type);
        }
        inputTypesBeingBuilt.remove(typeName);
        return type;
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
        
        GraphQLObjectType.Builder builder = newObject();
        builder.name(typeName);
        builder.fields(getOutputFieldDefinitions(object));
        builder.description(getTypeDescription(object));
        return builder.build();
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
