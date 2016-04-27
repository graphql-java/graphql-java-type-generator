package graphql.java.generator.field;

import java.util.HashMap;
import graphql.java.generator.strategies.AbstractStrategiesContainer;
import graphql.java.generator.strategies.Strategy;

public class FieldStrategies extends AbstractStrategiesContainer {
    public FieldObjectsStrategy getFieldObjectsStrategy() {
        return (FieldObjectsStrategy) allStrategies.get(FieldObjectsStrategy.class);
    }

    public FieldDataFetcherStrategy getFieldDataFetcherStrategy() {
        return (FieldDataFetcherStrategy) allStrategies.get(FieldDataFetcherStrategy.class);
    }
    
    public FieldNameStrategy getFieldNameStrategy() {
        return (FieldNameStrategy) allStrategies.get(FieldNameStrategy.class);
    }
    
    public FieldTypeStrategy getFieldTypeStrategy() {
        return (FieldTypeStrategy) allStrategies.get(FieldTypeStrategy.class);
    }
    
    public FieldDescriptionStrategy getFieldDescriptionStrategy() {
        return (FieldDescriptionStrategy) allStrategies.get(FieldDescriptionStrategy.class);
    }
    
    public FieldDeprecationStrategy getFieldDeprecationStrategy() {
        return (FieldDeprecationStrategy) allStrategies.get(FieldDeprecationStrategy.class);
    }
    
    public FieldDefaultValueStrategy getFieldDefaultValueStrategy() {
        return (FieldDefaultValueStrategy) allStrategies.get(FieldDefaultValueStrategy.class);
    }
    
    
    public static class Builder {
        private HashMap<Class<? extends Strategy>, Strategy> strategies =
                new HashMap<Class<? extends Strategy>, Strategy>(); 

        public Builder fieldObjectsStrategy(FieldObjectsStrategy fieldObjectsStrategy) {
            strategies.put(FieldObjectsStrategy.class, fieldObjectsStrategy);
            return this;
        }
        
        public Builder fieldDataFetcherStrategy(FieldDataFetcherStrategy fieldDataFetcherStrategy) {
            strategies.put(FieldDataFetcherStrategy.class, fieldDataFetcherStrategy);
            return this;
        }
        
        public Builder fieldNameStrategy(FieldNameStrategy fieldNameStrategy) {
            strategies.put(FieldNameStrategy.class, fieldNameStrategy);
            return this;
        }
        
        public Builder fieldTypeStrategy(FieldTypeStrategy fieldTypeStrategy) {
            strategies.put(FieldTypeStrategy.class, fieldTypeStrategy);
            return this;
        }
        
        public Builder fieldDescriptionStrategy(FieldDescriptionStrategy fieldDescriptionStrategy) {
            strategies.put(FieldDescriptionStrategy.class, fieldDescriptionStrategy);
            return this;
        }
        
        public Builder fieldDeprecationStrategy(FieldDeprecationStrategy fieldDeprecationStrategy) {
            strategies.put(FieldDeprecationStrategy.class, fieldDeprecationStrategy);
            return this;
        }
        
        public Builder fieldDefaultValueStrategy(FieldDefaultValueStrategy fieldDefaultValueStrategy) {
            strategies.put(FieldDefaultValueStrategy.class, fieldDefaultValueStrategy);
            return this;
        }
        
        public Builder additionalStrategy(Strategy strategy) {
            strategies.put(strategy.getClass(), strategy);
            return this;
        }
        
        public FieldStrategies build() {
            return new FieldStrategies(this);
        }
    }
    
    private FieldStrategies(Builder builder) {
        allStrategies.putAll(builder.strategies);
    }
}
