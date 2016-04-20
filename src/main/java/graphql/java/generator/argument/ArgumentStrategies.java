package graphql.java.generator.argument;

import graphql.java.generator.BuildContext;
import graphql.java.generator.BuildContextAware;

public class ArgumentStrategies implements BuildContextAware {
    private final ArgumentObjectsStrategy argumentObjectsStrategy;
    private final ArgumentNameStrategy argumentNameStrategy;
    private final ArgumentDescriptionStrategy argumentDescriptionStrategy;
    private final ArgumentTypeStrategy argumentTypeStrategy;
    private final ArgumentDefaultValueStrategy argumentDefaultValueStrategy;
    
    public ArgumentObjectsStrategy getArgumentObjectsStrategy() {
        return argumentObjectsStrategy;
    }
    
    public ArgumentNameStrategy getArgumentNameStrategy() {
        return argumentNameStrategy;
    }
    
    public ArgumentDescriptionStrategy getArgumentDescriptionStrategy() {
        return argumentDescriptionStrategy;
    }
    
    public ArgumentTypeStrategy getArgumentTypeStrategy() {
        return argumentTypeStrategy;
    }
    
    public ArgumentDefaultValueStrategy getArgumentDefaultValueStrategy() {
        return argumentDefaultValueStrategy;
    }
    
    @Override
    public BuildContext getContext() {
        return null;
    }
    
    @Override
    public void setContext(BuildContext context) {
        if (argumentObjectsStrategy instanceof BuildContextAware) {
            ((BuildContextAware) argumentObjectsStrategy).setContext(context);
        }
        if (argumentNameStrategy instanceof BuildContextAware) {
            ((BuildContextAware) argumentNameStrategy).setContext(context);
        }
        if (argumentDescriptionStrategy instanceof BuildContextAware) {
            ((BuildContextAware) argumentDescriptionStrategy).setContext(context);
        }
        if (argumentTypeStrategy instanceof BuildContextAware) {
            ((BuildContextAware) argumentTypeStrategy).setContext(context);
        }
        if (argumentDefaultValueStrategy instanceof BuildContextAware) {
            ((BuildContextAware) argumentDefaultValueStrategy).setContext(context);
        }
    }
    
    public static class Builder {
        private ArgumentObjectsStrategy argumentObjectsStrategy;
        private ArgumentNameStrategy argumentNameStrategy;
        private ArgumentDescriptionStrategy argumentDescriptionStrategy;
        private ArgumentTypeStrategy argumentTypeStrategy;
        private ArgumentDefaultValueStrategy argumentDefaultValueStrategy;
        
        public Builder argumentObjectsStrategy(ArgumentObjectsStrategy argumentObjectsStrategy) {
            this.argumentObjectsStrategy = argumentObjectsStrategy;
            return this;
        }
        
        public Builder argumentNameStrategy(ArgumentNameStrategy argumentNameStrategy) {
            this.argumentNameStrategy = argumentNameStrategy;
            return this;
        }
        
        public Builder argumentDescriptionStrategy(ArgumentDescriptionStrategy argumentDescriptionStrategy) {
            this.argumentDescriptionStrategy = argumentDescriptionStrategy;
            return this;
        }
        
        public Builder argumentTypeStrategy(ArgumentTypeStrategy argumentTypeStrategy) {
            this.argumentTypeStrategy = argumentTypeStrategy;
            return this;
        }
        
        public Builder argumentDefaultValueStrategy(ArgumentDefaultValueStrategy argumentDefaultValueStrategy) {
            this.argumentDefaultValueStrategy = argumentDefaultValueStrategy;
            return this;
        }
        
        public ArgumentStrategies build() {
            return new ArgumentStrategies(this);
        }
    }
    
    private ArgumentStrategies(Builder builder) {
        this.argumentObjectsStrategy = builder.argumentObjectsStrategy;
        this.argumentNameStrategy = builder.argumentNameStrategy;
        this.argumentDescriptionStrategy = builder.argumentDescriptionStrategy;
        this.argumentTypeStrategy = builder.argumentTypeStrategy;
        this.argumentDefaultValueStrategy = builder.argumentDefaultValueStrategy;
    }
}
