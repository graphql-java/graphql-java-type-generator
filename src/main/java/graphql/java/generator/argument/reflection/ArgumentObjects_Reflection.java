package graphql.java.generator.argument.reflection;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import graphql.java.generator.BuildContextStorer;
import graphql.java.generator.argument.ArgumentObjectsStrategy;

public class ArgumentObjects_Reflection
        extends BuildContextStorer
        implements ArgumentObjectsStrategy {
    
    @Override
    public List<Object> getArgumentRepresentativeObjects(Object object) {
        if (object instanceof Method) {
            Method method = (Method) object;
            return getObjectsFromMethod(method);
        }

        return null;
    }

    protected List<Object> getObjectsFromMethod(Method method) {
        List<Object> argObjects = new ArrayList<Object>();
        Class<?>[] params = method.getParameterTypes();
        for (int index = 0; index < params.length; ++index) {
            Class<?> param = params[index];
            argObjects.add(param);
        }
        return argObjects;
    }
}
