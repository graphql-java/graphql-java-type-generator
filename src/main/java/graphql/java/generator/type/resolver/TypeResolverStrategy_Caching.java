package graphql.java.generator.type.resolver;

import graphql.java.generator.UnsharableBuildContextStorer;
import graphql.java.generator.type.TypeResolverStrategy;
import graphql.java.generator.type.resolver.TypeResolver_BuildContextAware;
import graphql.schema.TypeResolver;

/**
 * Reuses 1 {@link TypeResolver_BuildContextAware} per {@link BuildContext}
 * since this TypeResolverStrategy_Caching itself is tied to only 1 BuildContext.
 * @author dwinsor
 *
 */
public class TypeResolverStrategy_Caching
        extends UnsharableBuildContextStorer
        implements TypeResolverStrategy {
    
    TypeResolver_BuildContextAware typeResolver = null;

    @Override
    public TypeResolver getTypeResolver(Object object) {
        if (typeResolver == null) {
            typeResolver = new TypeResolver_BuildContextAware(getContext());
        }
        return typeResolver;
    }
}
