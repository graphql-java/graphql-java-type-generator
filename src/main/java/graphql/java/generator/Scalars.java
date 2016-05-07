package graphql.java.generator;

import java.math.BigDecimal;
import java.math.BigInteger;

import graphql.GraphQLException;
import graphql.language.FloatValue;
import graphql.language.IntValue;
import graphql.language.StringValue;
import graphql.schema.Coercing;
import graphql.schema.GraphQLScalarType;

public class Scalars {
    
    private static final int BYTE_MAX = Byte.MAX_VALUE;
    private static final int BYTE_MIN = Byte.MIN_VALUE;
    private static final int CHAR_MAX = Character.MAX_VALUE;
    private static final int CHAR_MIN = Character.MIN_VALUE;
    private static final int SHORT_MAX = Short.MAX_VALUE;
    private static final int SHORT_MIN = Short.MIN_VALUE;

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

    public static GraphQLScalarType GraphQLByte = new GraphQLScalarType("Byte", "Built-in Byte as Int", new Coercing() {
        public Object serialize(Object input) {
            if (input instanceof String) {
                return Byte.parseByte((String) input);
            } else if (input instanceof Byte) {
                return input;
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
            if (!(input instanceof IntValue)) return null;
            Integer value = ((IntValue) input).getValue();
            if (value.compareTo(BYTE_MIN) < 0 || value.compareTo(BYTE_MAX) > 0) {
                throw new GraphQLException("Int literal is too big or too small for a byte, would cause overflow");
            }
            return value.byteValue();
        }
    });

    public static GraphQLScalarType GraphQLChar = new GraphQLScalarType("Char", "Built-in Char as Int", new Coercing() {
        public Object serialize(Object input) {
            if (input instanceof String) {
                return (char) Integer.parseInt((String) input);
            } else if (input instanceof Character) {
                return input;
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
            if (!(input instanceof IntValue)) return null;
            Integer value = ((IntValue) input).getValue();
            if (value.compareTo(CHAR_MIN) < 0 || value.compareTo(CHAR_MAX) > 0) {
                throw new GraphQLException("Int literal is too big or too small for a char, would cause overflow");
            }
            return value.shortValue();
        }
    });

    public static GraphQLScalarType GraphQLShort = new GraphQLScalarType("Short", "Built-in Short as Int", new Coercing() {
        public Object serialize(Object input) {
            if (input instanceof String) {
                return Short.parseShort((String) input);
            } else if (input instanceof Character) {
                return input;
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
            if (!(input instanceof IntValue)) return null;
            Integer value = ((IntValue) input).getValue();
            if (value.compareTo(SHORT_MIN) < 0 || value.compareTo(SHORT_MAX) > 0) {
                throw new GraphQLException("Int literal is too big or too small for a short, would cause overflow");
            }
            return value.shortValue();
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
        if (Byte.class.isAssignableFrom(clazz) || byte.class.isAssignableFrom(clazz)) {
            return GraphQLByte;
        }
        if (Character.class.isAssignableFrom(clazz) || char.class.isAssignableFrom(clazz)) {
            return GraphQLChar;
        }
        if (Short.class.isAssignableFrom(clazz) || short.class.isAssignableFrom(clazz)) {
            return GraphQLShort;
        }
        return null;
    }
}
