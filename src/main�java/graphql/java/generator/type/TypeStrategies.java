package graphql.java.generator.type;

public class TypeStrategies {
    private final TypeNameStrategy typeNameStrategy;
    private final TypeDescriptionStrategy typeDescriptionStrategy;
    private final DefaultTypeStrategy defaultTypeStrategy;
    
    public TypeNameStrategy getTypeNameStrategy() {
        return typeNameStrategy;
    }
    
    public TypeDescriptionStrategy getTypeDescriptionStrategy() {
        return typeDescriptionStrategy;
    }
    
    public DefaultTypeStrategy getDefaultTypeStrategy() {
        return defaultTypeStrategy;
    }
    
    public static class Builder {
        private TypeNameStrategy typeNameStrategy;
        private TypeDescriptionStrategy typeDescriptionStrategy;
        private DefaultTypeStrategy defaultTypeStrategy;
        
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
        
        public TypeStrategies build() {
            return new TypeStrategies(this);
        }
    }
    
    private TypeStrategies(Builder builder) {
        this.typeNameStrategy = builder.typeNameStrategy;
        this.typeDescriptionStrategy = builder.typeDescriptionStrategy;
        this.defaultTypeStrategy = builder.defaultTypeStrategy;
    }
}
