package graphql.java.generator.field;

public class FieldStrategies {
    private final FieldObjectsStrategy fieldObjectsStrategy;
    private final FieldDataFetcherStrategy fieldDataFetcherStrategy;
    private final FieldNameStrategy fieldNameStrategy;
    private final FieldTypeStrategy fieldTypeStrategy;
    private final FieldDescriptionStrategy fieldDescriptionStrategy;
    private final FieldArgumentsStrategy fieldArgumentsStrategy;
    
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
    
    public FieldDescriptionStrategy getFieldDescriptionStrategy() {
        return fieldDescriptionStrategy;
    }
    
    public FieldArgumentsStrategy getFieldArgumentsStrategy() {
        return fieldArgumentsStrategy;
    }
    
    
    public static class Builder {
        private FieldObjectsStrategy fieldObjectsStrategy;
        private FieldDataFetcherStrategy fieldDataFetcherStrategy;
        private FieldNameStrategy fieldNameStrategy;
        private FieldTypeStrategy fieldTypeStrategy;
        private FieldDescriptionStrategy fieldDescriptionStrategy;
        private FieldArgumentsStrategy fieldArgumentsStrategy;
        
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
        
        public Builder fieldDescriptionStrategy(FieldDescriptionStrategy fieldDescriptionStrategy) {
            this.fieldDescriptionStrategy = fieldDescriptionStrategy;
            return this;
        }
        
        public Builder fieldArgumentsStrategy(FieldArgumentsStrategy fieldArgumentsStrategy) {
            this.fieldArgumentsStrategy = fieldArgumentsStrategy;
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
        this.fieldDescriptionStrategy = builder.fieldDescriptionStrategy;
        this.fieldArgumentsStrategy = builder.fieldArgumentsStrategy;
    }
}
