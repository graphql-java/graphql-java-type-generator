package graphql.java.generator.field.reflect;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

import graphql.java.generator.UnsharableBuildContextStorer;
import graphql.introspection.Introspection.TypeKind;
import graphql.java.generator.field.FieldTypeStrategy;
import graphql.java.generator.type.ITypeGenerator;
import graphql.schema.GraphQLInputType;
import graphql.schema.GraphQLOutputType;
import graphql.schema.GraphQLType;

public class FieldType_Reflection
        extends UnsharableBuildContextStorer
        implements FieldTypeStrategy {
    @Override
    public GraphQLOutputType getOutputTypeOfField(Object object) {
        return (GraphQLOutputType) getTypeOfField(
                object, TypeKind.OBJECT);
    }
    
    @Override
    public GraphQLInputType getInputTypeOfField(Object object) {
        return (GraphQLInputType) getTypeOfField(
                object, TypeKind.INPUT_OBJECT);
    }
    
    public GraphQLType getTypeOfField(
            Object object, TypeKind typeKind) {
        if (object instanceof Field) {
            Field field = (Field) object;
            Class<?> fieldClazz = field.getType();
            Type genericType = field.getGenericType();
            return getTypeOfFieldFromSignature(
                    fieldClazz, genericType, field.toGenericString(), typeKind);
        }

        if (object instanceof Method) {
            Method method = (Method) object;
            Class<?> returnTypeClazz = method.getReturnType();
            Type genericType = method.getGenericReturnType();
            return getTypeOfFieldFromSignature(
                    returnTypeClazz, genericType, method.toGenericString(), typeKind);
        }

        return null;
    }
    
    protected GraphQLType getTypeOfFieldFromSignature(
            Class<?> typeClazz, Type genericType,
            String name, TypeKind typeKind) {
        ITypeGenerator typeGen = getContext().getTypeGeneratorStrategy();

        //attempt GraphQLList from types
        if (genericType instanceof ParameterizedType) {
            ParameterizedType pType = (ParameterizedType) genericType;
            GraphQLType type = typeGen.getParameterizedType(typeClazz, pType, typeKind);
            if (type != null) {
                return type;
            }
        }

        if (TypeKind.OBJECT.equals(typeKind)) {
            return typeGen.getOutputType(typeClazz);
        }
        else {
            return typeGen.getInputType(typeClazz);
        }
    }
    
    protected Class<?> getListGenericType(Class<?> typeClazz, Type genericType) {
        if (List.class.isAssignableFrom(typeClazz)) {
            if (genericType instanceof ParameterizedType) {
                Type listGenericType = ((ParameterizedType) genericType).getActualTypeArguments()[0];
                if (listGenericType instanceof Class<?>) {
                    return (Class<?>) listGenericType;
                }
            }
        }
        return null;
    }
}
