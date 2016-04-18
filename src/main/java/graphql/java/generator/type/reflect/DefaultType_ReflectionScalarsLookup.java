package graphql.java.generator.type.reflect;

import graphql.java.generator.Scalars;
import graphql.java.generator.type.DefaultTypeStrategy;
import graphql.schema.GraphQLInputType;
import graphql.schema.GraphQLOutputType;
import graphql.schema.GraphQLScalarType;

public class DefaultType_ReflectionScalarsLookup implements DefaultTypeStrategy {

    @Override
    public GraphQLOutputType getDefaultOutputType(Object object) {
        return getDefaultScalarType(object);
    }

    @Override
    public GraphQLInputType getDefaultInputType(Object object) {
        return getDefaultScalarType(object);
    }

    protected GraphQLScalarType getDefaultScalarType(Object object) {
        if (object == null) {
            return null;
        }
        if (!(object instanceof Class<?>)) {
            object = object.getClass();
        }
        Class<?> clazz = (Class<?>) object;
        return Scalars.getScalarType(clazz);
    }
}
