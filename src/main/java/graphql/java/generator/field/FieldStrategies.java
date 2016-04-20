package graphql.java.generator.field;

import graphql.java.generator.BuildContext;
import graphql.java.generator.BuildContextAware;

public class FieldStrategies implements BuildContextAware {
    private final FieldObjectsStrategy fieldObjectsStrategy;
    private final FieldDataFetcherStrategy fieldDataFetcherStrategy;
    private final FieldNameStrategy fieldNameStrategy;
    private final FieldTypeStrategy fieldTypeStrategy;
    private final FieldDescriptionStrategy fieldDescriptionStrategy;
    
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
    
    
    public static class Builder {
        private FieldObjectsStrategy fieldObjectsStrategy;
        private FieldDataFetcherStrategy fieldDataFetcherStrategy;
        private FieldNameStrategy fieldNameStrategy;
        private FieldTypeStrategy fieldTypeStrategy;
        private FieldDescriptionStrategy fieldDescriptionStrategy;
        
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
    }

    @Override
    public BuildContext getContext() {
        return null;
    }

    @Override
    public void setContext(BuildContext context) {
        if (fieldObjectsStrategy instanceof BuildContextAware) {
            ((BuildContextAware) fieldObjectsStrategy).setContext(context);
        }
        if (fieldDataFetcherStrategy instanceof BuildContextAware) {
            ((BuildContextAware) fieldDataFetcherStrategy).setContext(context);
        }
        if (fieldNameStrategy instanceof BuildContextAware) {
            ((BuildContextAware) fieldNameStrategy).setContext(context);
        }
        if (fieldTypeStrategy instanceof BuildContextAware) {
            ((BuildContextAware) fieldTypeStrategy).setContext(context);
        }
        if (fieldDescriptionStrategy instanceof BuildContextAware) {
            ((BuildContextAware) fieldDescriptionStrategy).setContext(context);
        }
    }
}
