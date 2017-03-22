package graphql.java.generator.type.reflect;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;

public class ReflectionUtils {
    public static Class<?> extractClassFromSupportedObject(Object object) {
        if (object == null) return null;
        if (object instanceof ParameterizedType) {
            object = ((ParameterizedType) object).getRawType();
        }
        else if (object instanceof WildcardType) {
            Type upperBounds[] = ((WildcardType) object).getUpperBounds();
            object = typeFromBounds(upperBounds);
        }
        else if (object instanceof TypeVariable) {
            Type upperBounds[] = ((TypeVariable<?>) object).getBounds();
            object = typeFromBounds(upperBounds);
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
                return typeFromBounds(((WildcardType) type).getUpperBounds());
            }
            if (type instanceof TypeVariable) {
                return typeFromBounds(((TypeVariable<?>) type).getBounds());
            }
            return type;
        }
        if (object instanceof WildcardType) {
            return typeFromBounds(((WildcardType) object).getUpperBounds());
        }
        if (object instanceof TypeVariable) {
            return typeFromBounds(((TypeVariable<?>) object).getBounds());
        }
        return null;
    }
    
    protected static Type typeFromBounds(Type arr[]) {
        if (arr == null || arr.length == 0) {
            return Object.class;
        }
        return arr[0];
    }
}
