package graphql.java.generator.type;

import static graphql.schema.GraphQLObjectType.newObject;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import graphql.java.generator.BuildContext;
import graphql.java.generator.Scalars;
import graphql.java.generator.type.strategy.TypeStrategies;
import graphql.schema.GraphQLFieldDefinition;
import graphql.schema.GraphQLObjectType;
import graphql.schema.GraphQLOutputType;
import graphql.schema.GraphQLScalarType;
import graphql.schema.GraphQLTypeReference;

/**
 * Not yet certified with Interfaces, Enums, or arrays.
 * @author dwinsor
 *
 */
@SuppressWarnings({"unchecked"})
public class TypeGen_ReflectionBased implements TypeGenerator<Class<?>> {
    private static Logger logger = LoggerFactory.getLogger(TypeGen_ReflectionBased.class);
    
    protected final Map<String, GraphQLOutputType> generatedOutputTypes;
    protected final Set<String> outputTypesBeingBuilt;
    protected TypeStrategies strategies = new TypeStrategies();
    
    public TypeGen_ReflectionBased() {
        outputTypesBeingBuilt = TypeRepository.getOutputTypesBeingBuilt();
        generatedOutputTypes = TypeRepository.getGeneratedOutputTypes();
    }
    
    @Override
    public GraphQLOutputType getOutputType(Class<?> object) {
        return getOutputType(object, new BuildContext());
    }

    @Override
    public final GraphQLOutputType getOutputType(Class<?> clazz, BuildContext currentContext) {
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
            return generateOutputTypeFromClass(clazz, currentContext);
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
        GraphQLOutputType type = generateOutputTypeFromClass(clazz, currentContext);
        TypeRepository.registerType(typeName, type);
        outputTypesBeingBuilt.remove(typeName);
        return type;
    }
    
    protected String getGraphQLTypeName(Class<?> clazz) {
        return strategies.getTypeNameStrategy().getTypeName(clazz);
    }
    
    protected GraphQLOutputType generateOutputTypeFromClass(Class<?> clazz, BuildContext currentContext) {
        GraphQLObjectType.Builder builder = newObject();
        String typeName = getGraphQLTypeName(clazz);
        builder.name(typeName);
        builder.fields(getFieldDefinitions(clazz, currentContext));
        return builder.build();
    }
    
    protected List<GraphQLFieldDefinition> getFieldDefinitions(Class<?> clazz, BuildContext currentContext) {
        List<GraphQLFieldDefinition> definitions = 
                currentContext.getFieldsGeneratorStrategy()
                        .getFields(clazz, currentContext);
        return definitions;
    }

    @Override
    public void setTypeStrategies(TypeStrategies strategies) {
        this.strategies = strategies;
    }
}
