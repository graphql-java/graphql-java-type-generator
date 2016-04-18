package graphql.java.generator.type;

import graphql.schema.GraphQLInputType;
import graphql.schema.GraphQLOutputType;

/**
 * If an Object is a scalar or some similar default type, it may be replaced
 * with a canonical default type, such as a GraphQLScalarType.
 * @author dwinsor
 *
 */
public interface DefaultTypeStrategy {
    /**
     * Returns a canonical default GraphQL type, if applicable for the given input object
     * @param object A representative "type" object, the exact type of which is contextual
     * @return May return null to indicate this type has no scalar or other default
     * prebuilt representation, and must be manually created.
     */
    GraphQLOutputType getDefaultOutputType(Object object);
    
    /**
     * Returns a canonical default GraphQL type, if applicable for the given input object
     * @param object A representative "type" object, the exact type of which is contextual
     * @return May return null to indicate this type has no scalar or other default
     * prebuilt representation, and must be manually created.
     */
    GraphQLInputType getDefaultInputType(Object object);
}
