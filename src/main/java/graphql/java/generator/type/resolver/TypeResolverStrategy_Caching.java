package graphql.java.generator.type.resolver;

import graphql.java.generator.UnsharableBuildContextStorer;
import graphql.java.generator.type.TypeResolverStrategy;
import graphql.java.generator.type.resolver.TypeResolver_BuildContextAware;
import graphql.schema.TypeResolver;

/**
 * TODO Caching.
 * @author dwinsor
 *
 */
public class TypeResolverStrategy_Caching
        extends UnsharableBuildContextStorer
        implements TypeResolverStrategy {

    @Override
    public TypeResolver getTypeResolver(Object object) {
        return new TypeResolver_BuildContextAware(getContext());
    }
}
