package graphql.java.generator.argument;

import java.util.HashMap;

import graphql.java.generator.strategies.AbstractStrategiesContainer;
import graphql.java.generator.strategies.Strategy;

public class ArgumentStrategies extends AbstractStrategiesContainer {
    public ArgumentObjectsStrategy getArgumentObjectsStrategy() {
        return (ArgumentObjectsStrategy) allStrategies.get(ArgumentObjectsStrategy.class);
    }
    
    public ArgumentNameStrategy getArgumentNameStrategy() {
        return (ArgumentNameStrategy) allStrategies.get(ArgumentNameStrategy.class);
    }
    
    public ArgumentDescriptionStrategy getArgumentDescriptionStrategy() {
        return (ArgumentDescriptionStrategy) allStrategies.get(ArgumentDescriptionStrategy.class);
    }
    
    public ArgumentTypeStrategy getArgumentTypeStrategy() {
        return (ArgumentTypeStrategy) allStrategies.get(ArgumentTypeStrategy.class);
    }
    
    public ArgumentDefaultValueStrategy getArgumentDefaultValueStrategy() {
        return (ArgumentDefaultValueStrategy) allStrategies.get(ArgumentDefaultValueStrategy.class);
    }
    
    public static class Builder {
        private HashMap<Class<? extends Strategy>, Strategy> strategies =
                new HashMap<Class<? extends Strategy>, Strategy>(); 
        
        public Builder argumentObjectsStrategy(ArgumentObjectsStrategy argumentObjectsStrategy) {
            strategies.put(ArgumentObjectsStrategy.class, argumentObjectsStrategy);
            return this;
        }
        
        public Builder argumentNameStrategy(ArgumentNameStrategy argumentNameStrategy) {
            strategies.put(ArgumentNameStrategy.class, argumentNameStrategy);
            return this;
        }
        
        public Builder argumentDescriptionStrategy(ArgumentDescriptionStrategy argumentDescriptionStrategy) {
            strategies.put(ArgumentDescriptionStrategy.class, argumentDescriptionStrategy);
            return this;
        }
        
        public Builder argumentTypeStrategy(ArgumentTypeStrategy argumentTypeStrategy) {
            strategies.put(ArgumentTypeStrategy.class, argumentTypeStrategy);
            return this;
        }
        
        public Builder argumentDefaultValueStrategy(ArgumentDefaultValueStrategy argumentDefaultValueStrategy) {
            strategies.put(ArgumentDefaultValueStrategy.class, argumentDefaultValueStrategy);
            return this;
        }
        
        public Builder additionalStrategy(Strategy strategy) {
            strategies.put(strategy.getClass(), strategy);
            return this;
        }
        
        public ArgumentStrategies build() {
            return new ArgumentStrategies(this);
        }
    }
    
    private ArgumentStrategies(Builder builder) {
        allStrategies.putAll(builder.strategies);
    }
}
