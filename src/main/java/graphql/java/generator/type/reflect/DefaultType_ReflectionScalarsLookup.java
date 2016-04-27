package graphql.java.generator.type.reflect;

import graphql.java.generator.DefaultTypes;
import graphql.java.generator.Scalars;
import graphql.java.generator.type.DefaultTypeStrategy;
import graphql.schema.GraphQLInputType;
import graphql.schema.GraphQLOutputType;
import graphql.schema.GraphQLScalarType;

public class DefaultType_ReflectionScalarsLookup implements DefaultTypeStrategy {

    @Override
    public GraphQLOutputType getDefaultOutputType(Object object) {
        GraphQLScalarType scalar = getDefaultScalarType(object);
        if (scalar != null) return scalar;
        return getDefaultType(object);
    }

    @Override
    public GraphQLInputType getDefaultInputType(Object object) {
        return getDefaultScalarType(object);
    }

    protected GraphQLOutputType getDefaultType(Object object) {
        if (object == null) {
            return null;
        }
        if (!(object instanceof Class<?>)) {
            object = object.getClass();
        }
        Class<?> clazz = (Class<?>) object;
        return DefaultTypes.getDefaultType(clazz);
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
