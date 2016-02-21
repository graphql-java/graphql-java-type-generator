package graphql.java.generator;

import graphql.schema.GraphQLScalarType;

public class Scalars {
    /**
     * Returns null if not a scalar/primitive type
     * Otherwise returns an instance of GraphQLScalarType from Scalars.
     * @param clazz
     * @return
     */
    public static GraphQLScalarType getScalarType(Class<?> clazz) {
        if (String.class.isAssignableFrom(clazz)) {
            return graphql.Scalars.GraphQLString;
        }
        if (Long.class.isAssignableFrom(clazz) || long.class.isAssignableFrom(clazz)) {
            return graphql.Scalars.GraphQLLong;
        }
        if (Integer.class.isAssignableFrom(clazz) || int.class.isAssignableFrom(clazz)) {
            return graphql.Scalars.GraphQLInt;
        }
        if (Double.class.isAssignableFrom(clazz) || double.class.isAssignableFrom(clazz)
                || Float.class.isAssignableFrom(clazz) || float.class.isAssignableFrom(clazz)) {
            return graphql.Scalars.GraphQLFloat;
        }
        if (Boolean.class.isAssignableFrom(clazz) || boolean.class.isAssignableFrom(clazz)) {
            return graphql.Scalars.GraphQLBoolean;
        }
        return null;
    }
}
