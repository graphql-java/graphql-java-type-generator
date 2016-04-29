package graphql.java.generator.type;

import graphql.schema.GraphQLInputType;
import graphql.schema.GraphQLOutputType;

import java.util.Map;

/**
 * The master place to store prebuilt, reusable GraphQL types.
 * Must support concurrency.
 * @author dwinsor
 *
 */
public interface TypeRepository {
    public GraphQLOutputType registerType(String typeName,
            GraphQLOutputType graphQlOutputType);

    public GraphQLInputType registerType(String typeName,
            GraphQLInputType graphQlInputType);

    public Map<String, GraphQLOutputType> getGeneratedOutputTypes();
    public Map<String, GraphQLInputType> getGeneratedInputTypes();
    
    /**
     * Resets the internal data of the TypeRepository to empty.
     * Must be done in a way that does not impact BuildContexts currently running.
     */
    public void clear();
}
