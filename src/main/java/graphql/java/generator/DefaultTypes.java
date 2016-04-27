package graphql.java.generator;

import graphql.schema.GraphQLObjectType;

public class DefaultTypes {
    
    private static final GraphQLObjectType emptyJavaObject = GraphQLObjectType.newObject()
            .name("java_lang_Object")
            .description("Default Type for java.lang.Object")
            .build();

    public static GraphQLObjectType getDefaultType(Class<?> clazz) {
        if (Object.class.equals(clazz)) {
            return emptyJavaObject;
        }
        return null;
    }
    
}
