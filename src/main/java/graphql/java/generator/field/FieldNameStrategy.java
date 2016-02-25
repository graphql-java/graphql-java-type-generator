package graphql.java.generator.field;

public interface FieldNameStrategy {
    /**
     * 
     * @param object A representative "field" object, the exact type of which is contextual
     * @return May return null to indicate this GraphQL field should be skipped.
     */
    String getFieldName(Object object);
}
