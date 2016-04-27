package graphql.java.generator.argument;

import graphql.java.generator.strategies.Strategy;

public interface ArgumentNameStrategy extends Strategy {

    String getArgumentName(ArgContainer object);
    
}
