package graphql.java.generator.argument;

import graphql.schema.GraphQLInputType;

public interface ArgumentTypeStrategy {

    GraphQLInputType getArgumentType(Object object);
    
}
