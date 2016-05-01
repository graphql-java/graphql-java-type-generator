package graphql.java.generator.type;

import java.lang.reflect.ParameterizedType;

import graphql.introspection.Introspection.TypeKind;
import graphql.java.generator.strategies.Strategy;
import graphql.schema.GraphQLType;

public interface TypeWrapperStrategy extends Strategy {

    /**
     * If the given {@code object} is a representative "type"
     * object that should be wrapped (e.g. class of {@code List<Integer>})
     * then return a GraphQL Wrapper around the generated GraphQLType
     * (e.g. GraphQLList(GraphQLInt) ).
     * Return null if no wrapper is applicable.
     * @param object
     * @param genericType A Type object representing the Generic arguments,
     * if known from the signature of a method or field. Can be null.
     * @param typeKind The kind to build, whether input, output, etc.
     * @return
     */
    GraphQLType getWrapperAroundType(Object object, ParameterizedType genericType, TypeKind typeKind);
    
}
