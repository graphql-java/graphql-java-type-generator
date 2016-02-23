package graphql.java.generator.type.strategy;

/**
 * Given any object, decide how you wish the GraphQL type to be named
 * @author dwinsor
 *
 */
public interface TypeNameStrategy {
    /**
     * May return null if no suitable type name is possible
     * @param object
     * @return
     */
    public String getTypeName(Object object);
}
