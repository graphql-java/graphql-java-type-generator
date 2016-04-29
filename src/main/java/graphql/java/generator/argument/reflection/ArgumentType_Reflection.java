package graphql.java.generator.argument.reflection;

import graphql.java.generator.UnsharableBuildContextStorer;
import graphql.java.generator.argument.ArgumentTypeStrategy;
import graphql.schema.GraphQLInputType;

public class ArgumentType_Reflection
        extends UnsharableBuildContextStorer
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
