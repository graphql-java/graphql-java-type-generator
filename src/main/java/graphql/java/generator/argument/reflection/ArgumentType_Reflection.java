package graphql.java.generator.argument.reflection;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;

import graphql.introspection.Introspection.TypeKind;
import graphql.java.generator.UnsharableBuildContextStorer;
import graphql.java.generator.argument.ArgContainer;
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
    public GraphQLInputType getArgumentType(ArgContainer container) {
        if (container == null) return null;
        Object object = container.getRepresentativeObject();
        if (object == null) return null;
        
        if (object instanceof ParameterizedType
                || object instanceof WildcardType
                || object instanceof TypeVariable) {
            return (GraphQLInputType) getContext().getParameterizedType(
                    object,
                    (Type) object,
                    TypeKind.INPUT_OBJECT);
        }
        return getContext().getInputType(object);
    }
}
