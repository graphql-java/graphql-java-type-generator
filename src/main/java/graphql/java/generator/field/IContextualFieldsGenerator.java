package graphql.java.generator.field;

import java.util.List;

import graphql.java.generator.BuildContext;
import graphql.schema.GraphQLFieldDefinition;
import graphql.schema.GraphQLInputObjectField;


public interface IContextualFieldsGenerator {
    
    List<GraphQLFieldDefinition> getOutputFields(Object object);
    
    List<GraphQLFieldDefinition> getOutputFields(Object object, BuildContext parentContext);
    
    List<GraphQLInputObjectField> getInputFields(Object object);
    
    List<GraphQLInputObjectField> getInputFields(Object object, BuildContext parentContext);
    
}