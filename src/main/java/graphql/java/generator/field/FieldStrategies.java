package graphql.java.generator.field;

public class FieldStrategies {
    private FieldObjectsStrategy fieldObjectsStrategy;
    private FieldDataFetcherStrategy fieldDataFetcherStrategy;
    private FieldNameStrategy fieldNameStrategy;
    private FieldTypeStrategy fieldTypeStrategy;
    
    public FieldObjectsStrategy getFieldObjectsStrategy() {
        return fieldObjectsStrategy;
    }

    public FieldDataFetcherStrategy getFieldDataFetcherStrategy() {
        return fieldDataFetcherStrategy;
    }
    
    public FieldNameStrategy getFieldNameStrategy() {
        return fieldNameStrategy;
    }
    
    public FieldTypeStrategy getFieldTypeStrategy() {
        return fieldTypeStrategy;
    }
    
    
    public static class Builder {
        private FieldObjectsStrategy fieldObjectsStrategy;
        private FieldDataFetcherStrategy fieldDataFetcherStrategy;
        private FieldNameStrategy fieldNameStrategy;
        private FieldTypeStrategy fieldTypeStrategy;
        
        public Builder fieldObjectsStrategy(FieldObjectsStrategy fieldObjectsStrategy) {
            this.fieldObjectsStrategy = fieldObjectsStrategy;
            return this;
        }
        
        public Builder fieldDataFetcherStrategy(FieldDataFetcherStrategy fieldDataFetcherStrategy) {
            this.fieldDataFetcherStrategy = fieldDataFetcherStrategy;
            return this;
        }
        
        public Builder fieldNameStrategy(FieldNameStrategy fieldNameStrategy) {
            this.fieldNameStrategy = fieldNameStrategy;
            return this;
        }
        
        public Builder fieldTypeStrategy(FieldTypeStrategy fieldTypeStrategy) {
            this.fieldTypeStrategy = fieldTypeStrategy;
            return this;
        }
        
        public FieldStrategies build() {
            return new FieldStrategies(this);
        }
    }
    
    private FieldStrategies(Builder builder) {
        this.fieldObjectsStrategy = builder.fieldObjectsStrategy;
        this.fieldDataFetcherStrategy = builder.fieldDataFetcherStrategy;
        this.fieldNameStrategy = builder.fieldNameStrategy;
        this.fieldTypeStrategy = builder.fieldTypeStrategy;
    }
}
