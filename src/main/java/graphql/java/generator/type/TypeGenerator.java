package graphql.java.generator.type;

import graphql.java.generator.BuildContext;
import graphql.schema.GraphQLOutputType;

/**
 * Given any object, decide how you wish the GraphQL type to be generated
 * @author dwinsor
 *
 * @param <T>
 */
public interface TypeGenerator<T> {
    /**
     * 
     * @param object A representative object from which to construct
     * a {@link GraphQLOutputType}
     * @param currentContext
     * @return
     */
    GraphQLOutputType getOutputType(T object, BuildContext currentContext);
    
    /**
     * Will use some default context if necessary
     * @param object A representative object from which to construct
     * a {@link GraphQLOutputType}
     * @return
     */
    GraphQLOutputType getOutputType(T object);
    
    void setTypeStrategies(TypeStrategies strategies);
}
