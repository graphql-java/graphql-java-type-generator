package graphql.java.generator.type;

import graphql.schema.GraphQLOutputType;


public interface ITypeGenerator {
    
    /**
     * @param object A representative "object" from which to construct
     * a {@link GraphQLOutputType}, the exact type of which is contextual
     * @return
     */
    GraphQLOutputType getOutputType(Object object);
    
}