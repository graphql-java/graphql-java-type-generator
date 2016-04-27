package graphql.java.generator.field;

import graphql.java.generator.strategies.Strategy;
import graphql.schema.GraphQLInputType;
import graphql.schema.GraphQLOutputType;

public interface FieldTypeStrategy extends Strategy {
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
