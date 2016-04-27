package graphql.java.generator.field;

import graphql.java.generator.strategies.Strategy;

public interface FieldDefaultValueStrategy extends Strategy {

    String getFieldDefaultValue(Object object);
    
}
