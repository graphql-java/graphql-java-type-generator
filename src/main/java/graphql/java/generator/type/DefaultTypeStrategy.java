package graphql.java.generator.type;

import graphql.schema.GraphQLOutputType;

public interface DefaultTypeStrategy {
    GraphQLOutputType getDefaultOutputType(Object object);
}
