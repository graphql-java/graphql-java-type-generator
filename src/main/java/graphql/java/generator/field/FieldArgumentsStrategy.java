package graphql.java.generator.field;

import java.util.List;

import graphql.schema.GraphQLArgument;

public interface FieldArgumentsStrategy {

    List<GraphQLArgument> getFieldArguments(Object object);
    
}
