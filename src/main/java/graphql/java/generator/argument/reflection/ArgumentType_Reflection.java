package graphql.java.generator.argument.reflection;

import java.lang.reflect.Type;

import graphql.java.generator.UnsharableBuildContextStorer;
import graphql.java.generator.argument.strategies.ArgumentTypeStrategy;
import graphql.schema.GraphQLInputType;

/**
 * @author dwinsor
 *
 */
public class ArgumentType_Reflection
        extends UnsharableBuildContextStorer
        implements ArgumentTypeStrategy {
    
    @Override
    public GraphQLInputType getArgumentType(Object object) {
        if (object instanceof Type) {
            return getArgumentType((Type) object);
        }
        return null;
    }
    
    protected GraphQLInputType getArgumentType(Type type) {
        return getContext().getInputType(type);
    }
}
