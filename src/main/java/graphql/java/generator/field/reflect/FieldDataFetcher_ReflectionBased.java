package graphql.java.generator.field.reflect;

import graphql.java.generator.field.FieldDataFetcherStrategy;
import graphql.schema.FieldDataFetcher;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FieldDataFetcher_ReflectionBased implements FieldDataFetcherStrategy {
    private static Logger logger = LoggerFactory.getLogger(
            FieldDataFetcher_ReflectionBased.class);
    
    @Override
    public Object getFieldFetcher(Object object) {
        if (!(object instanceof Field)) {
            return null;
        }
        Field field = (Field) object;
        Class<?> clazz = field.getDeclaringClass();
        Method getter = getGetterMethod(field, clazz);
        if (getter != null) {
            //This is commented out because it is redundant, but included
            //here for documentation, to be changed later
            //fieldBuilder.dataFetcher(new PropertyDataFetcher(field.getName()));
        }
        else {
            if (!field.isAccessible()) {
                logger.debug("field [{}] is not accessible and has no getter", field);
                return null;
            }
            logger.debug("Direct field access Class [{}], field [{}], type [{}]",
                    clazz, field.getName(), field.getType());
            return new FieldDataFetcher(field.getName());
        }
        return null;
    }
    
    /**
     * @param field
     * @param clazzContainingField
     * @return
     */
    protected Method getGetterMethod(Field field, Class<?> clazzContainingField) {
        Class<?> type = field.getType();
        String fieldName = field.getName();
        String prefix = (type.isAssignableFrom(Boolean.class) || type.isAssignableFrom(boolean.class))
                ? "is" : "get";
        String getterName = prefix + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
        try {
            Method getter = clazzContainingField.getMethod(getterName);
            return getter;
        }
        catch (NoSuchMethodException e) {
            return null;
        }
    }
}
