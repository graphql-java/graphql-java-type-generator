package graphql.java.generator.type.reflect;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.util.List;
import graphql.java.generator.UnsharableBuildContextStorer;
import graphql.java.generator.type.TypeSpecContainer;
import graphql.java.generator.type.strategies.TypeWrapperStrategy;
import graphql.schema.GraphQLList;
import graphql.schema.GraphQLType;

public class TypeWrapper_ReflectionList
        extends UnsharableBuildContextStorer
        implements TypeWrapperStrategy {
    protected boolean canWrapList(final TypeSpecContainer originalObject) {
        Object object = originalObject.getRepresentativeObject();
        Class<?> typeClazz = ReflectionUtils.extractClassFromSupportedObject(object);
        if (typeClazz == null) return false;
        
        //handle raw arrays
        if (typeClazz.isArray()) return true;
        
        //handle List<?> where ? is a class or type, but not raw generics
        if (originalObject.getGenericType() == null) return false;
        if (!List.class.isAssignableFrom(typeClazz)) return false;
        
        Type listGenericType = ReflectionUtils.extractInteriorType(originalObject.getGenericType());
        if (listGenericType instanceof Class<?>) return true;
        if (listGenericType instanceof ParameterizedType) return true;
        if (listGenericType instanceof WildcardType) return true;
        if (listGenericType instanceof TypeVariable) return true;//TODO check if it will happen
        
        return false;
    }

    @Override
    public GraphQLType wrapType(GraphQLType interiorType, TypeSpecContainer originalObject) {
        if (!canWrapList(originalObject)) {
            return interiorType;
        }
        return new GraphQLList(interiorType);
    }

    @Override
    public TypeSpecContainer getInteriorObjectToGenerate(TypeSpecContainer originalObject) {
        if (!canWrapList(originalObject)) return null;
        
        Object object = originalObject.getRepresentativeObject();
        
        Class<?> typeClazz = ReflectionUtils.extractClassFromSupportedObject(object);
        if (typeClazz == null) return null;
        
        Type interiorType;
        if (typeClazz.isArray()) {
            interiorType = typeClazz.getComponentType();
        }
        else {
            interiorType = ReflectionUtils.extractInteriorType(originalObject.getGenericType());
        }
        
        Type interiorGenericType = null;
        if (interiorType instanceof ParameterizedType) {
            interiorGenericType = (ParameterizedType) interiorType;
        }
        else if (interiorType instanceof WildcardType) {
            interiorGenericType = (WildcardType) interiorType;
        }
        else if (interiorType instanceof TypeVariable) {
            interiorGenericType = (TypeVariable<?>) interiorType;
        }
        
        return new TypeSpecContainer(
                interiorType,
                interiorGenericType,
                originalObject.getTypeKind());
    }
}
