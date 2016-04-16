package graphql.java.generator.type.reflect;

import graphql.java.generator.Scalars;
import graphql.java.generator.type.DefaultTypeStrategy;
import graphql.schema.GraphQLOutputType;

public class DefaultType_ReflectionScalarsLookup implements DefaultTypeStrategy {

    @Override
    public GraphQLOutputType getDefaultOutputType(Object object) {
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
