package graphql.java.generator.type;

import graphql.java.generator.BuildContext;
import graphql.java.generator.BuildContextAware;

public class TypeStrategies implements BuildContextAware {
    private final TypeNameStrategy typeNameStrategy;
    private final TypeDescriptionStrategy typeDescriptionStrategy;
    private final DefaultTypeStrategy defaultTypeStrategy;
    private final EnumValuesStrategy enumValuesStrategy;
    
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
    
    public static class Builder {
        private TypeNameStrategy typeNameStrategy;
        private TypeDescriptionStrategy typeDescriptionStrategy;
        private DefaultTypeStrategy defaultTypeStrategy;
        private EnumValuesStrategy enumValuesStrategy;
        
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
        
        public TypeStrategies build() {
            return new TypeStrategies(this);
        }
    }
    
    private TypeStrategies(Builder builder) {
        this.typeNameStrategy = builder.typeNameStrategy;
        this.typeDescriptionStrategy = builder.typeDescriptionStrategy;
        this.defaultTypeStrategy = builder.defaultTypeStrategy;
        this.enumValuesStrategy = builder.enumValuesStrategy;
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
    }
}
