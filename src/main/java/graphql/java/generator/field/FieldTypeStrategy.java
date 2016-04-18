package graphql.java.generator.field;

import graphql.java.generator.BuildContext;
import graphql.schema.GraphQLInputType;
import graphql.schema.GraphQLOutputType;

public interface FieldTypeStrategy {
    /**
     * 
     * @param object A representative "field" object, the exact type of which is contextual
     * @param currentContext
     * @return
     */
    GraphQLOutputType getOutputTypeOfField(Object object, BuildContext currentContext);

    /**
     * 
     * @param object A representative "field" object, the exact type of which is contextual
     * @param currentContext
     * @return
     */
    GraphQLInputType getInputTypeOfField(Object object, BuildContext currentContext);
}
