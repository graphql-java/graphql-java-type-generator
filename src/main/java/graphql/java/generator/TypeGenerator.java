package graphql.java.generator;

import graphql.schema.GraphQLOutputType;


public interface TypeGenerator<T> {
    
    /**
     * 
     * @param object A representative object from which to construct
     * a {@link GraphQLOutputType}
     * @return
     */
    public abstract GraphQLOutputType getOutputType(T object);
}