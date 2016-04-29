package graphql.java.generator.type;

import graphql.schema.GraphQLInputType;
import graphql.schema.GraphQLOutputType;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The master place to store prebuilt, reusable GraphQL types.
 * @author dwinsor
 *
 */
public class StaticTypeRepository implements TypeRepository {
    private static Map<String, GraphQLOutputType> generatedOutputTypes =
            new ConcurrentHashMap<String, GraphQLOutputType>();
    private static Map<String, GraphQLInputType> generatedInputTypes =
            new ConcurrentHashMap<String, GraphQLInputType>();
    
    public GraphQLOutputType registerType(String typeName,
            GraphQLOutputType graphQlOutputType) {
        synchronized (generatedOutputTypes) {
            if (!generatedOutputTypes.containsKey(typeName)) {
                generatedOutputTypes.put(typeName, graphQlOutputType);
                return graphQlOutputType;
            }
        }
        return generatedOutputTypes.get(typeName);
    }

    public GraphQLInputType registerType(String typeName,
            GraphQLInputType graphQlInputType) {
        synchronized (generatedInputTypes) {
            if (!generatedInputTypes.containsKey(typeName)) {
                generatedInputTypes.put(typeName, graphQlInputType);
                return graphQlInputType;
            }
        }
        return generatedInputTypes.get(typeName);
    }

    public Map<String, GraphQLOutputType> getGeneratedOutputTypes() {
        return generatedOutputTypes;
    }
    public Map<String, GraphQLInputType> getGeneratedInputTypes() {
        return generatedInputTypes;
    }
    
    /**
     * Resets the internal data of the TypeRepository to empty
     */
    public void clear() {
        //anyone working on the old types doesn't want to have
        //their generated*Types .clear()ed out from under them. 
        generatedOutputTypes =
                new ConcurrentHashMap<String, GraphQLOutputType>();
        generatedInputTypes =
                new ConcurrentHashMap<String, GraphQLInputType>();
    }
}
