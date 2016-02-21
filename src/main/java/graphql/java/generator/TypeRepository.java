package graphql.java.generator;

import graphql.schema.GraphQLOutputType;

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class TypeRepository {
    private static final Map<String, GraphQLOutputType> generatedOutputTypes =
            new ConcurrentHashMap<String, GraphQLOutputType>();
    private static final Set<String> outputTypesBeingBuilt = Collections.newSetFromMap(
            new ConcurrentHashMap<String, Boolean>());
    
//  public void registerType(String registrantName,
//          GraphQLOutputType graphQlOutputType);

    public static Map<String, GraphQLOutputType> getGeneratedOutputTypes() {
        return generatedOutputTypes;
    }
    public static Set<String> getOutputTypesBeingBuilt() {
        return outputTypesBeingBuilt;
    }
}
