package graphql.java.generator.field;

import java.util.List;

import graphql.java.generator.BuildContext;
import graphql.schema.GraphQLFieldDefinition;

/**
 * Given any object, decide how you wish fields to be created
 * @author dwinsor
 *
 * @param <T>
 */
public interface FieldsGenerator<T> {
    List<GraphQLFieldDefinition> getFields(T object, BuildContext parentContext);
    List<GraphQLFieldDefinition> getFields(T object);
    void setFieldsStrategies();
}
