package graphql.java.generator.type;

import graphql.introspection.Introspection.TypeKind;
import graphql.schema.GraphQLInputType;
import graphql.schema.GraphQLOutputType;
import graphql.schema.GraphQLType;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The master place to store prebuilt, reusable GraphQL types.
 * @author dwinsor
 *
 */
public class StaticTypeRepository implements TypeRepository {
    private static Map<String, GraphQLType> generatedOutputTypes =
            new ConcurrentHashMap<String, GraphQLType>();
    private static Map<String, GraphQLType> generatedInputTypes =
            new ConcurrentHashMap<String, GraphQLType>();
    
    @Override
    public GraphQLType registerType(String typeName, GraphQLType graphQlType, TypeKind typeKind) {
        switch (typeKind) {
        case OBJECT:
        case INTERFACE:
            return registerType(typeName, (GraphQLOutputType) graphQlType);
        case INPUT_OBJECT:
            return registerType(typeName, (GraphQLInputType) graphQlType);
        default:
            return null;
        }
    }

    public GraphQLOutputType registerType(String typeName,
            GraphQLOutputType graphQlOutputType) {
        return (GraphQLOutputType) syncRegisterType(
                typeName, graphQlOutputType, generatedOutputTypes);
    }

    public GraphQLInputType registerType(String typeName,
            GraphQLInputType graphQlInputType) {
        return (GraphQLInputType) syncRegisterType(
                typeName, graphQlInputType, generatedInputTypes);
    }
    
    private GraphQLType syncRegisterType(String typeName,
            GraphQLType graphQlType, Map<String, GraphQLType> map) {
        synchronized (map) {
            if (!map.containsKey(typeName)) {
                map.put(typeName, graphQlType);
                return graphQlType;
            }
        }
        return map.get(typeName);

    }

    public Map<String, GraphQLType> getGeneratedOutputTypes() {
        return generatedOutputTypes;
    }
    public Map<String, GraphQLType> getGeneratedInputTypes() {
        return generatedInputTypes;
    }
    
    /**
     * Resets the internal data of the TypeRepository to empty
     */
    public void clear() {
        //anyone working on the old types doesn't want to have
        //their generated*Types .clear()ed out from under them. 
        generatedOutputTypes =
                new ConcurrentHashMap<String, GraphQLType>();
        generatedInputTypes =
                new ConcurrentHashMap<String, GraphQLType>();
    }

    @Override
    public GraphQLType getGeneratedType(String typeName, TypeKind typeKind) {
        switch (typeKind) {
        case OBJECT:
        case INTERFACE:
            return generatedOutputTypes.get(typeName);
        case INPUT_OBJECT:
            return generatedInputTypes.get(typeName);
        default:
            return null;
        }
    }
}
