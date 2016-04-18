package graphql.java.generator.field.reflect;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import graphql.java.generator.BuildContext;
import graphql.java.generator.InputOutput;
import graphql.java.generator.field.FieldTypeStrategy;
import graphql.java.generator.type.IContextualTypeGenerator;
import graphql.schema.GraphQLInputType;
import graphql.schema.GraphQLList;
import graphql.schema.GraphQLOutputType;
import graphql.schema.GraphQLType;

public class FieldType_Reflection implements FieldTypeStrategy {
    private static Logger logger = LoggerFactory.getLogger(
            FieldType_Reflection.class);
    
    @Override
    public GraphQLOutputType getOutputTypeOfField(
            Object object, BuildContext currentContext) {
        return (GraphQLOutputType) getTypeOfField(
                object, currentContext, InputOutput.OUTPUT);
    }
    
    @Override
    public GraphQLInputType getInputTypeOfField(
            Object object, BuildContext currentContext) {
        return (GraphQLInputType) getTypeOfField(
                object, currentContext, InputOutput.INPUT);
    }
    
    public GraphQLType getTypeOfField(
            Object object, BuildContext currentContext, InputOutput ioFlag) {
        if (object instanceof Field) {
            Field field = (Field) object;
            return getTypeOfFieldFromField(field, currentContext, ioFlag);
        }

        if (object instanceof Method) {
            Method method = (Method) object;
            return getTypeOfFieldFromMethod(method, currentContext, ioFlag);
        }

        return null;
    }
    
    protected GraphQLType getTypeOfFieldFromField(
            Field field, BuildContext currentContext, InputOutput ioFlag) {
        Class<?> fieldClazz = field.getType();
        Type genericType = field.getGenericType();
        return getTypeOfFieldFromSignature(
                fieldClazz, genericType, field.toGenericString(), currentContext, ioFlag);
    }
    
    protected GraphQLType getTypeOfFieldFromMethod(
            Method method, BuildContext currentContext, InputOutput ioFlag) {
        Class<?> returnTypeClazz = method.getReturnType();
        Type genericType = method.getGenericReturnType();
        return getTypeOfFieldFromSignature(
                returnTypeClazz, genericType, method.toGenericString(), currentContext, ioFlag);
    }
    
    protected GraphQLType getTypeOfFieldFromSignature(
            Class<?> typeClazz, Type genericType,
            String name, BuildContext currentContext,
            InputOutput ioFlag) {
        IContextualTypeGenerator typeGen = currentContext.getTypeGeneratorStrategy();

        //attempt GraphQLList from types
        Class<?> listGenericType = getListGenericType(
                typeClazz, genericType);
        if (listGenericType != null) {
            logger.debug("this [{}] is an {} list of generic type [{}], and will be made a GraphQLList",
                    name, ioFlag, listGenericType);
            if (InputOutput.OUTPUT.equals(ioFlag)) {
                return new GraphQLList(typeGen.getOutputType(listGenericType, currentContext));
            }
            else {
                return new GraphQLList(typeGen.getInputType(listGenericType, currentContext));
            }
        }

        if (InputOutput.OUTPUT.equals(ioFlag)) {
            return typeGen.getOutputType(typeClazz, currentContext);
        }
        else {
            return typeGen.getInputType(typeClazz, currentContext);
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
