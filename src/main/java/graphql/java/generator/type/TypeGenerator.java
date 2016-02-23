package graphql.java.generator.type;

import static graphql.schema.GraphQLObjectType.newObject;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import graphql.java.generator.BuildContext;
import graphql.schema.GraphQLFieldDefinition;
import graphql.schema.GraphQLObjectType;
import graphql.schema.GraphQLOutputType;
import graphql.schema.GraphQLTypeReference;

/**
 * Given any object, decide how you wish the GraphQL type to be generated
 * Not yet certified with Interfaces, Enums, or arrays.
 * @author dwinsor
 *
 */
@SuppressWarnings({"unchecked"})
public class TypeGenerator {
    private static Logger logger = LoggerFactory.getLogger(TypeGenerator.class);
    
    protected final Map<String, GraphQLOutputType> generatedOutputTypes;
    protected final Set<String> outputTypesBeingBuilt;
    protected TypeStrategies strategies = new TypeStrategies();
    
    public TypeGenerator() {
        outputTypesBeingBuilt = TypeRepository.getOutputTypesBeingBuilt();
        generatedOutputTypes = TypeRepository.getGeneratedOutputTypes();
    }
    
    /**
     * Will use some default context if necessary
     * @param object A representative object from which to construct
     * a {@link GraphQLOutputType}
     * @return
     */
    public GraphQLOutputType getOutputType(Class<?> object) {
        return getOutputType(object, new BuildContext());
    }

    /**
     * 
     * @param object A representative object from which to construct
     * a {@link GraphQLOutputType}
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
        if (outputTypesBeingBuilt.contains(typeName)) {
            logger.debug("Using a reference to: [{}]", typeName);
            return new GraphQLTypeReference(typeName);
        }

        if (generatedOutputTypes.containsKey(typeName)) {
            return generatedOutputTypes.get(typeName);
        }
        
        outputTypesBeingBuilt.add(typeName);
        GraphQLOutputType type = generateOutputType(object, currentContext);
        TypeRepository.registerType(typeName, type);
        outputTypesBeingBuilt.remove(typeName);
        return type;
    }
    
    protected GraphQLOutputType getDefaultOutputType(Object object) {
        return strategies.getDefaultTypeStrategy().getDefaultOutputType(object);
    }
    
    protected String getGraphQLTypeName(Object object) {
        return strategies.getTypeNameStrategy().getTypeName(object);
    }
    
    protected GraphQLOutputType generateOutputType(Object object, BuildContext currentContext) {
        GraphQLObjectType.Builder builder = newObject();
        String typeName = getGraphQLTypeName(object);
        builder.name(typeName);
        builder.fields(getFieldDefinitions(object, currentContext));
        return builder.build();
    }
    
    protected List<GraphQLFieldDefinition> getFieldDefinitions(Object object, BuildContext currentContext) {
        List<GraphQLFieldDefinition> definitions = 
                currentContext.getFieldsGeneratorStrategy()
                        .getFields(object, currentContext);
        return definitions;
    }

    public void setTypeStrategies(TypeStrategies strategies) {
        this.strategies = strategies;
    }
}
