package graphql.java.generator.argument.reflection;

import graphql.java.generator.BuildContextStorer;
import graphql.java.generator.argument.ArgumentNameStrategy;
import graphql.schema.GraphQLInputType;

public class ArgumentName_Reflection
        extends BuildContextStorer
        implements ArgumentNameStrategy {
    
    @Override
    public String getArgumentName(Object object) {
        if (object instanceof Class<?>) {
            return getArgumentName((Class<?>) object);
        }
        return null;
    }
    
    protected String getArgumentName(Class<?> clazz) {
        //TODO technically I need the ArgumentTypeStrategy's type
        GraphQLInputType type = getContext().getInputType(clazz);
        return type.getName();
    }
}
