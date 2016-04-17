package graphql.java.generator.field;

import java.util.List;

import graphql.java.generator.BuildContext;
import graphql.schema.GraphQLFieldDefinition;


public interface IContextualFieldGenerator {
    
    List<GraphQLFieldDefinition> getFields(Object object);
    
    List<GraphQLFieldDefinition> getFields(Object object, BuildContext parentContext);
    
}