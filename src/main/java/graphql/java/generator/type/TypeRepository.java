package graphql.java.generator.type;

import graphql.schema.GraphQLOutputType;

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class TypeRepository {
    private static Map<String, GraphQLOutputType> generatedOutputTypes =
            new ConcurrentHashMap<String, GraphQLOutputType>();
    private static Set<String> outputTypesBeingBuilt = Collections.newSetFromMap(
            new ConcurrentHashMap<String, Boolean>());
    
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
    public static Set<String> getOutputTypesBeingBuilt() {
        return outputTypesBeingBuilt;
    }
    
    /**
     * Resets the internal data of the TypeRepository to empty
     */
    public static void clear() {
        //anyone working on the old types doesn't want to have
        //their generatedOutputTypes .clear()ed out from under them. 
        generatedOutputTypes =
                new ConcurrentHashMap<String, GraphQLOutputType>();
        outputTypesBeingBuilt = Collections.newSetFromMap(
                new ConcurrentHashMap<String, Boolean>());
    }
}
