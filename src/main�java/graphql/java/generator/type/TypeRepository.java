package graphql.java.generator.type;

import graphql.schema.GraphQLOutputType;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The master place to store prebuilt, reusable GraphQL types.
 * @author dwinsor
 *
 */
public class TypeRepository {
    private static Map<String, GraphQLOutputType> generatedOutputTypes =
            new ConcurrentHashMap<String, GraphQLOutputType>();
    
    public static GraphQLOutputType registerType(String typeName,
            GraphQLOutputType graphQlOutputType) {
        synchronized (generatedOutputTypes) {
            if (!generatedOutputTypes.containsKey(typeName)) {
                generatedOutputTypes.put(typeName, graphQlOutputType);
                return graphQlOutputType;
            }
        }
        return generatedOutputTypes.get(typeName);
    }

    public static Map<String, GraphQLOutputType> getGeneratedOutputTypes() {
        return generatedOutputTypes;
    }
    
    /**
     * Resets the internal data of the TypeRepository to empty
     */
    public static void clear() {
        //anyone working on the old types doesn't want to have
        //their generatedOutputTypes .clear()ed out from under them. 
        generatedOutputTypes =
                new ConcurrentHashMap<String, GraphQLOutputType>();
    }
}
