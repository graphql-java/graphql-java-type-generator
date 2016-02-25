package graphql.java.generator.type;

public class TypeStrategies {
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
}
