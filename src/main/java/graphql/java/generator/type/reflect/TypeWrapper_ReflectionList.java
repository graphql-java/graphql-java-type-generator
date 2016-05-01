package graphql.java.generator.type.reflect;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
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
        if (originalObject.getGenericType() == null) return false;
        
        Object object = originalObject.getRepresentativeObject();
        Class<?> typeClazz = ReflectionUtils.extractClassFromSupportedObject(object);
        if (typeClazz == null) return false;
        if (!List.class.isAssignableFrom(typeClazz)) return false;
        
        Type listGenericType = originalObject.getGenericType().getActualTypeArguments()[0];
        if (listGenericType instanceof Class<?>) return true;
        if (listGenericType instanceof ParameterizedType) return true;
        
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
        
        Type interiorType = originalObject.getGenericType().getActualTypeArguments()[0];
        
        ParameterizedType interiorGenericType = null;
        if (interiorType instanceof ParameterizedType) {
            interiorGenericType = (ParameterizedType) interiorType;
        }
        
        return new TypeSpecContainer(
                interiorType,
                interiorGenericType,
                originalObject.getTypeKind());
    }
}
