package graphql.java.generator.type;

import graphql.java.generator.BuildContext;
import graphql.schema.GraphQLInputType;
import graphql.schema.GraphQLOutputType;


public interface IContextualTypeGenerator extends ITypeGenerator {
    
    /**
     * 
     * @param object A representative "object" from which to construct
     * a {@link GraphQLOutputType}, the exact type of which is contextual
     * @param currentContext
     * @return
     */
    GraphQLOutputType getOutputType(Object object, BuildContext currentContext);

    /**
     * 
     * @param object A representative "object" from which to construct
     * a {@link GraphQLInputType}, the exact type of which is contextual
     * @param currentContext
     * @return
     */
    GraphQLInputType getInputType(Object object, BuildContext currentContext);
    
}