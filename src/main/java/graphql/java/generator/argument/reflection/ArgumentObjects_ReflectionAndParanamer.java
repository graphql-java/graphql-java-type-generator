package graphql.java.generator.argument.reflection;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import com.thoughtworks.paranamer.CachingParanamer;
import com.thoughtworks.paranamer.Paranamer;

import graphql.java.generator.argument.ArgContainer;
import graphql.java.generator.argument.strategies.ArgumentObjectsStrategy;

public class ArgumentObjects_ReflectionAndParanamer
        implements ArgumentObjectsStrategy {
    
    protected Paranamer paranamer = new CachingParanamer();
    
    @Override
    public List<ArgContainer> getArgumentRepresentativeObjects(Object object) {
        if (object instanceof Method) {
            Method method = (Method) object;
            return getObjectsFromMethod(method);
        }

        return null;
    }

    protected List<ArgContainer> getObjectsFromMethod(Method method) {
        List<ArgContainer> argObjects = new ArrayList<ArgContainer>();
        Class<?>[] params = method.getParameterTypes();
        String[] paramNames = paranamer.lookupParameterNames(method, false);
        
        if (paramNames == null || params.length != paramNames.length) {
            paramNames = new String[params.length];
        }
        
        for (int index = 0; index < params.length; ++index) {
            Class<?> param = params[index];
            String name = paramNames[index];
            argObjects.add(new ArgContainer(method, param, name, index));
        }
        return argObjects;
    }
}
