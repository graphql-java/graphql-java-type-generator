package graphql.java.generator.field;

import graphql.java.generator.field.reflect.FieldName_ReflectionBased;
import graphql.java.generator.field.reflect.FieldType_ReflectionBased;
import graphql.java.generator.field.reflect.FieldDataFetcher_ReflectionBased;

public class FieldStrategies {
    public FieldDataFetcherStrategy getFieldDataFetcherStrategy() {
        return new FieldDataFetcher_ReflectionBased();
    }
    public FieldNameStrategy getFieldNameStrategy() {
        return new FieldName_ReflectionBased();
    }
    public FieldTypeStrategy getFieldTypeStrategy() {
        return new FieldType_ReflectionBased();
    }
}
