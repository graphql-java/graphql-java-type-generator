package graphql.java.generator.field;

import java.util.List;

public interface FieldObjectsStrategy {
    /**
     * Given an object derived from the {@link TypeGenerator}, find out what
     * should become GraphQL Fields.
     * @param object
     * @return null to indicate this object should not be built,
     * or List (of any size) containing objects that will be passed to other strategies.
     */
    List<Object> getFieldRepresentativeObjects(Object object);
}
