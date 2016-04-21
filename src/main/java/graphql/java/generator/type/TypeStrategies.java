package graphql.java.generator.type;

import graphql.java.generator.BuildContext;
import graphql.java.generator.BuildContextAware;

public class TypeStrategies implements BuildContextAware {
    private final TypeNameStrategy typeNameStrategy;
    private final TypeDescriptionStrategy typeDescriptionStrategy;
    private final DefaultTypeStrategy defaultTypeStrategy;
    private final EnumValuesStrategy enumValuesStrategy;
    private final InterfacesStrategy interfacesStrategy;
    private final TypeResolverStrategy typeResolverStrategy;
    
    public TypeNameStrategy getTypeNameStrategy() {
        return typeNameStrategy;
    }
    public TypeDescriptionStrategy getTypeDescriptionStrategy() {
        return typeDescriptionStrategy;
    }
    public DefaultTypeStrategy getDefaultTypeStrategy() {
        return defaultTypeStrategy;
    }
    public EnumValuesStrategy getEnumValuesStrategy() {
        return enumValuesStrategy;
    }
    public InterfacesStrategy getInterfacesStrategy() {
        return interfacesStrategy;
    }
    public TypeResolverStrategy getTypeResolverStrategy() {
        return typeResolverStrategy;
    }
    
    public static class Builder {
        private TypeNameStrategy typeNameStrategy;
        private TypeDescriptionStrategy typeDescriptionStrategy;
        private DefaultTypeStrategy defaultTypeStrategy;
        private EnumValuesStrategy enumValuesStrategy;
        private InterfacesStrategy interfacesStrategy;
        private TypeResolverStrategy typeResolverStrategy;
        
        public Builder typeNameStrategy(TypeNameStrategy typeNameStrategy) {
            this.typeNameStrategy = typeNameStrategy;
            return this;
        }
        
        public Builder typeDescriptionStrategy(TypeDescriptionStrategy typeDescriptionStrategy) {
            this.typeDescriptionStrategy = typeDescriptionStrategy;
            return this;
        }
        
        public Builder defaultTypeStrategy(DefaultTypeStrategy defaultTypeStrategy) {
            this.defaultTypeStrategy = defaultTypeStrategy;
            return this;
        }
        
        public Builder enumValuesStrategy(EnumValuesStrategy enumValuesStrategy) {
            this.enumValuesStrategy = enumValuesStrategy;
            return this;
        }
        
        public Builder interfacesStrategy(InterfacesStrategy interfacesStrategy) {
            this.interfacesStrategy = interfacesStrategy;
            return this;
        }
        
        public Builder typeResolverStrategy(TypeResolverStrategy typeResolverStrategy) {
            this.typeResolverStrategy = typeResolverStrategy;
            return this;
        }
        
        public TypeStrategies build() {
            return new TypeStrategies(this);
        }
    }
    
    private TypeStrategies(Builder builder) {
        this.typeNameStrategy = builder.typeNameStrategy;
        this.typeDescriptionStrategy = builder.typeDescriptionStrategy;
        this.defaultTypeStrategy = builder.defaultTypeStrategy;
        this.enumValuesStrategy = builder.enumValuesStrategy;
        this.interfacesStrategy = builder.interfacesStrategy;
        this.typeResolverStrategy = builder.typeResolverStrategy;
    }
    
    @Override
    public BuildContext getContext() {
        return null;
    }

    @Override
    public void setContext(BuildContext context) {
        if (typeNameStrategy instanceof BuildContextAware) {
            ((BuildContextAware) typeNameStrategy).setContext(context);
        }
        if (typeDescriptionStrategy instanceof BuildContextAware) {
            ((BuildContextAware) typeDescriptionStrategy).setContext(context);
        }
        if (defaultTypeStrategy instanceof BuildContextAware) {
            ((BuildContextAware) defaultTypeStrategy).setContext(context);
        }
        if (enumValuesStrategy instanceof BuildContextAware) {
            ((BuildContextAware) enumValuesStrategy).setContext(context);
        }
        if (interfacesStrategy instanceof BuildContextAware) {
            ((BuildContextAware) interfacesStrategy).setContext(context);
        }
        if (typeResolverStrategy instanceof BuildContextAware) {
            ((BuildContextAware) typeResolverStrategy).setContext(context);
        }
    }
}
