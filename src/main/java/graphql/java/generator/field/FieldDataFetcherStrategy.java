package graphql.java.generator.field;

public interface FieldDataFetcherStrategy {
    Object getFieldFetcher(Object object);
}
