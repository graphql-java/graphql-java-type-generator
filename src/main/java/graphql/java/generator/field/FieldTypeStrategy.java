package graphql.java.generator.field;

import graphql.schema.GraphQLInputType;
import graphql.schema.GraphQLOutputType;

public interface FieldTypeStrategy {
    /**
     * 
     * @param object A representative "field" object, the exact type of which is contextual
     * @return
     */
    GraphQLOutputType getOutputTypeOfField(Object object);

    /**
     * 
     * @param object A representative "field" object, the exact type of which is contextual
     * @return
     */
    GraphQLInputType getInputTypeOfField(Object object);
}
