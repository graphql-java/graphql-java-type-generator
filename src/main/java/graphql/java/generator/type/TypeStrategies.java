package graphql.java.generator.type;

public class TypeStrategies {
    private TypeNameStrategy typeNameStrategy;
    private DefaultTypeStrategy defaultTypeStrategy;
    
    public TypeNameStrategy getTypeNameStrategy() {
        return typeNameStrategy;
    }
    
    public DefaultTypeStrategy getDefaultTypeStrategy() {
        return defaultTypeStrategy;
    }
    
    public static class Builder {
        private TypeNameStrategy typeNameStrategy;
        private DefaultTypeStrategy defaultTypeStrategy;
        
        public Builder typeNameStrategy(TypeNameStrategy typeNameStrategy) {
            this.typeNameStrategy = typeNameStrategy;
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
        this.defaultTypeStrategy = builder.defaultTypeStrategy;
    }
}
