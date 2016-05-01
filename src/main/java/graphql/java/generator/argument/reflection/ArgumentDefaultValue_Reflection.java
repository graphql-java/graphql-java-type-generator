package graphql.java.generator.argument.reflection;

import graphql.java.generator.argument.strategies.ArgumentDefaultValueStrategy;

public class ArgumentDefaultValue_Reflection implements ArgumentDefaultValueStrategy {
    
    @Override
    public Object getArgumentDefaultValue(Object object) {
        if (object instanceof Class<?>) {
            return getArgumentDefaultValue((Class<?>) object);
        }
        return null;
    }
    
    protected Object getArgumentDefaultValue(Class<?> clazz) {
        return null;
    }
}
