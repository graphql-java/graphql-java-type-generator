package graphql.java.generator.field.reflect;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import graphql.java.generator.BuildContext;
import graphql.java.generator.field.FieldTypeStrategy;
import graphql.java.generator.type.TypeGenerator;
import graphql.schema.GraphQLList;
import graphql.schema.GraphQLOutputType;

public class FieldType_Reflection implements FieldTypeStrategy {
    private static Logger logger = LoggerFactory.getLogger(
            FieldType_Reflection.class);
    
    @Override
    public GraphQLOutputType getOutputTypeOfField(
            Object object, BuildContext currentContext) {
        if (object instanceof Field) {
            Field field = (Field) object;
            return getOutputTypeOfFieldFromField(field, currentContext);
        }

        if (object instanceof Method) {
            Method method = (Method) object;
            return getOutputTypeOfFieldFromMethod(method, currentContext);
        }

        return null;
    }
    
    protected GraphQLOutputType getOutputTypeOfFieldFromField(
            Field field, BuildContext currentContext) {
        Class<?> fieldClazz = field.getType();
        Type genericType = field.getGenericType();
        return getOutputTypeOfFieldFromSignature(
                fieldClazz, genericType, field.toGenericString(), currentContext);
    }
    
    protected GraphQLOutputType getOutputTypeOfFieldFromMethod(
            Method method, BuildContext currentContext) {
        Class<?> returnTypeClazz = method.getReturnType();
        Type genericType = method.getGenericReturnType();
        return getOutputTypeOfFieldFromSignature(
                returnTypeClazz, genericType, method.toGenericString(), currentContext);
    }
    
    protected GraphQLOutputType getOutputTypeOfFieldFromSignature(
            Class<?> typeClazz, Type genericType,
            String name, BuildContext currentContext) {
        //attempt GraphQLList from types
        if (List.class.isAssignableFrom(typeClazz)) {
            if (genericType instanceof ParameterizedType) {
                Type listGenericType = ((ParameterizedType) genericType).getActualTypeArguments()[0];
                if (listGenericType instanceof Class<?>) {
                    logger.debug("this [{}] is a list of generic type [{}], and will be made a GraphQLList",
                            name, listGenericType);
                    TypeGenerator typeGen = currentContext.getTypeGeneratorStrategy();
                    return new GraphQLList(typeGen.getOutputType((Class<?>)listGenericType, currentContext));
                }
            }
        }

        TypeGenerator typeGen = currentContext.getTypeGeneratorStrategy();
        return typeGen.getOutputType(typeClazz, currentContext);
    }
}
