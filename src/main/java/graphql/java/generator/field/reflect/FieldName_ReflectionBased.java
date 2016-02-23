package graphql.java.generator.field.reflect;

import graphql.java.generator.field.FieldNameStrategy;

import java.lang.reflect.Field;

public class FieldName_ReflectionBased implements FieldNameStrategy {

    @Override
    public String getFieldName(Object object) {
        if (!(object instanceof Field)) {
            return null;
        }
        return ((Field)object).getName();
    }
}
