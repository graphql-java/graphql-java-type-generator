package graphql.java.generator.type;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import graphql.introspection.Introspection.TypeKind;

/**
 * A container that contains enough objects to fully specify a type to
 * build or already built.
 * @author dwinsor
 *
 */
public class TypeSpecContainer {
    private Object representativeObject;
    private Type genericType;
    private TypeKind typeKind;
    
    /**
     * @param representativeObject
     * @param genericType
     * @param typeKind
     */
    public TypeSpecContainer(Object representativeObject, Type genericType, TypeKind typeKind) {
        this.representativeObject = representativeObject;
        this.genericType = genericType;
        this.typeKind = typeKind;
    }
    public Object getRepresentativeObject() {
        return representativeObject;
    }
    public void setRepresentativeObject(Object representativeObject) {
        this.representativeObject = representativeObject;
    }
    /**
     * A Type object representing the Generic arguments,
     * if known from the signature of a method or field. Can be null.
     * Expected to be a ParameterizedType or WildcardType
     * @return
     */
    public Type getGenericType() {
        return genericType;
    }
    /**
     * A Type object representing the Generic arguments,
     * if known from the signature of a method or field. Can be null.
     * Expected to be a ParameterizedType or WildcardType
     * @param genericType
     */
    public void setGenericType(Type genericType) {
        this.genericType = genericType;
    }
    /**
     * The kind to build, whether input, output, etc.
     * @return
     */
    public TypeKind getTypeKind() {
        return typeKind;
    }
    /**
     * The kind to build, whether input, output, etc.
     * @param typeKind
     */
    public void setTypeKind(TypeKind typeKind) {
        this.typeKind = typeKind;
    }
}
