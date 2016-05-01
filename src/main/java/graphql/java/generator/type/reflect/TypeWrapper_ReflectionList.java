package graphql.java.generator.type.reflect;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import graphql.introspection.Introspection.TypeKind;
import graphql.java.generator.UnsharableBuildContextStorer;
import graphql.java.generator.type.TypeWrapperStrategy;
import graphql.schema.GraphQLList;
import graphql.schema.GraphQLType;

public class TypeWrapper_ReflectionList
        extends UnsharableBuildContextStorer
        implements TypeWrapperStrategy {
    
    @Override
    public GraphQLType getWrapperAroundType(Object object,
            ParameterizedType genericType, TypeKind typeKind) {
        if (!(object instanceof Class<?>)) {
            object = object.getClass();
        }
        Class<?> typeClazz = (Class<?>) object;
        return getListGraphQLType(typeClazz, genericType, typeKind);
        //TODO some annotations like NotNull
        //TODO wrap arrays too
    }
    
    protected GraphQLType getListGraphQLType(Class<?> typeClazz,
            ParameterizedType genericType, TypeKind typeKind) {
        if (genericType == null) return null;
        if (!List.class.isAssignableFrom(typeClazz)) return null;
        
        Type listGenericType = genericType.getActualTypeArguments()[0];
        //TODO list of list of int
//        if (listGenericType instanceof ParameterizedType) {
//            return new GraphQLList(getContext().getParameterizedType(
//                    listGenericType, (ParameterizedType) listGenericType, typeKind));
//        }
        if (listGenericType instanceof Class<?>) {
            switch (typeKind) {
            case OBJECT:
                return new GraphQLList(getContext().getOutputType(listGenericType));
            case INTERFACE:
                return new GraphQLList(getContext().getInterfaceType(listGenericType));
            case INPUT_OBJECT:
                return new GraphQLList(getContext().getInputType(listGenericType));
            default:
                break;
            }
        }
        return null;
    }
}
