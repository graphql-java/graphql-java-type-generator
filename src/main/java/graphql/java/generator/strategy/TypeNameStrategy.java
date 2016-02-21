package graphql.java.generator.strategy;

/**
 * Given any object, decide how you wish the GraphQL type to be named
 * @author dwinsor
 *
 * @param <T>
 */
public interface TypeNameStrategy<T> {
    /**
     * May return null if no suitable type name is possible
     * @param object
     * @return
     */
    public String getTypeName(T object);
}
