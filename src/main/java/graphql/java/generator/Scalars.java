package graphql.java.generator;

import java.math.BigDecimal;
import java.math.BigInteger;

import graphql.language.FloatValue;
import graphql.language.IntValue;
import graphql.language.StringValue;
import graphql.schema.Coercing;
import graphql.schema.GraphQLScalarType;

public class Scalars {
    
    public static GraphQLScalarType GraphQLBigInteger = new GraphQLScalarType("BigInteger", "Built-in java.math.BigInteger", new Coercing() {
        @Override
        public Object serialize(Object input) {
            if (input instanceof BigInteger) {
                return (BigInteger) input;
            } else if (input instanceof String) {
                return new BigInteger((String) input);
            } else if (input instanceof Integer) {
                return BigInteger.valueOf((Integer) input);
            } else if (input instanceof Long) {
                return BigInteger.valueOf((Long) input);
            } else {
                return null;
            }
        }

        @Override
        public Object parseValue(Object input) {
            return serialize(input);
        }

        @Override
        public Object parseLiteral(Object input) {
            if (input instanceof StringValue) {
                return BigInteger.valueOf(Long.parseLong(((StringValue) input).getValue()));
            } else if (input instanceof IntValue) {
                return BigInteger.valueOf(((IntValue) input).getValue());
            }
            return null;
        }
    });

    public static GraphQLScalarType GraphQLBigDecimal = new GraphQLScalarType("BigDecimal", "Built-in java.math.BigDecimal", new Coercing() {
        @Override
        public Object serialize(Object input) {
            if (input instanceof BigDecimal) {
                return (BigDecimal) input;
            } else if (input instanceof String) {
                return new BigDecimal((String) input);
            } else if (input instanceof Integer) {
                return BigDecimal.valueOf((Integer) input);
            } else if (input instanceof Long) {
                return BigDecimal.valueOf((Long) input);
            } else {
                return null;
            }
        }

        @Override
        public Object parseValue(Object input) {
            return serialize(input);
        }

        @Override
        public Object parseLiteral(Object input) {
            if (input instanceof StringValue) {
                return BigDecimal.valueOf(Long.parseLong(((StringValue) input).getValue()));
            } else if (input instanceof IntValue) {
                return BigDecimal.valueOf(((IntValue) input).getValue());
            } else if (input instanceof FloatValue) {
                return ((FloatValue) input).getValue();
            }
            return null;
        }
    });

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
        if (BigInteger.class.isAssignableFrom(clazz)) {
            return GraphQLBigInteger;
        }
        if (BigDecimal.class.isAssignableFrom(clazz)) {
            return GraphQLBigDecimal;
        }
        return null;
    }
}
