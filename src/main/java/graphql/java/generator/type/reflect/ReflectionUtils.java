package graphql.java.generator.type.reflect;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.WildcardType;

public class ReflectionUtils {
    public static Class<?> extractClassFromSupportedObject(Object object) {
        if (object == null) return null;
        if (object instanceof ParameterizedType) {
            object = ((ParameterizedType) object).getRawType();
        }
        else if (object instanceof WildcardType) {
            WildcardType type = (WildcardType) object;
            Type upperBounds[] = type.getUpperBounds();
            if (upperBounds.length == 0) {
                object = Object.class;
            }
            else {
                object = upperBounds[0];
            }
        }
        if (!(object instanceof Class<?>)) {
            object = object.getClass();
        }
        Class<?> clazz = (Class<?>) object;
        return clazz;
    }
    
    public static Type extractInteriorType(Type object) {
        if (object instanceof ParameterizedType) {
            Type type = ((ParameterizedType) object).getActualTypeArguments()[0];
            if (type instanceof WildcardType) {
                return wildcardToInteriorType((WildcardType) type);
            }
            return type;
        }
        if (object instanceof WildcardType) {
            return wildcardToInteriorType((WildcardType) object);
        }
        return null;
    }
    
    public static Type wildcardToInteriorType(WildcardType object) {
        Type arr[] = ((WildcardType) object).getUpperBounds();
        if (arr.length == 0) {
            return Object.class;
        }
        return arr[0];
    }
}
