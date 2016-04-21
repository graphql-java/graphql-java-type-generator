package graphql.java.generator.type;

import graphql.schema.TypeResolver;

public interface TypeResolverStrategy {

    TypeResolver getTypeResolver(Object object);
    
}
