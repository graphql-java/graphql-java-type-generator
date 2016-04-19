package graphql.java.generator.field;

import java.util.List;

import graphql.java.generator.BuildContext;
import graphql.schema.GraphQLArgument;

public interface FieldArgumentsStrategy {

    List<GraphQLArgument> getFieldArguments(Object object, BuildContext currentContext);
    
}
