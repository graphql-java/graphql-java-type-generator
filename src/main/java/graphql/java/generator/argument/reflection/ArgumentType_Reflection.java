package graphql.java.generator.argument.reflection;

import graphql.java.generator.BuildContextStorer;
import graphql.java.generator.argument.ArgumentTypeStrategy;
import graphql.schema.GraphQLInputType;

public class ArgumentType_Reflection
        extends BuildContextStorer
        implements ArgumentTypeStrategy {
    
    @Override
    public GraphQLInputType getArgumentType(Object object) {
        if (object instanceof Class<?>) {
            return getArgumentType((Class<?>) object);
        }
        return null;
    }
    
    protected GraphQLInputType getArgumentType(Class<?> clazz) {
        return getContext().getInputType(clazz);
    }
}
