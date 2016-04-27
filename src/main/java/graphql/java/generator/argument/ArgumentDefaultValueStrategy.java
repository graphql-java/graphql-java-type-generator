package graphql.java.generator.argument;

import graphql.java.generator.strategies.Strategy;

public interface ArgumentDefaultValueStrategy extends Strategy {

    Object getArgumentDefaultValue(Object object);
    
}
