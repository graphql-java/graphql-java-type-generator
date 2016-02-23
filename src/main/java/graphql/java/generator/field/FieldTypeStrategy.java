package graphql.java.generator.field;

import graphql.java.generator.BuildContext;
import graphql.schema.GraphQLOutputType;

public interface FieldTypeStrategy {
    GraphQLOutputType getOutputTypeOfField(Object object, BuildContext currentContext);
}
