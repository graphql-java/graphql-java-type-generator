package graphql.java.generator.field.reflect;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import graphql.java.generator.field.FieldObjectsStrategy;

/**
 * A reflection based, java Method+Field way of generating data
 * on GraphQL fields.  This combines the output of 
 * {@link FieldObjects_ReflectionClassMethods} with {@link FieldObjects_ReflectionClassFields}
 * based upon the name given by {@link FieldName_Reflection}
 * where priority is given to methods.
 * @author dwinsor
 *
 */
public class FieldObjects_Reflection implements FieldObjectsStrategy {
    private final FieldObjects_ReflectionClassFields fieldStrategy;
    private final FieldObjects_ReflectionClassMethods methodStrategy;
    private final FieldName_Reflection fieldNameStrategy;
    
    public FieldObjects_Reflection() {
        this(new FieldObjects_ReflectionClassFields(),
                new FieldObjects_ReflectionClassMethods(),
                new FieldName_Reflection());
    }
    public FieldObjects_Reflection(
            final FieldObjects_ReflectionClassFields fieldStrategy,
            final FieldObjects_ReflectionClassMethods methodStrategy,
            final FieldName_Reflection fieldNameStrategy) {
        this.fieldStrategy = fieldStrategy;
        this.methodStrategy = methodStrategy;
        this.fieldNameStrategy = fieldNameStrategy;
    }
    
    /**
     * Delegates to {@link FieldObjects_ReflectionClassMethods}
     * and {@link FieldObjects_ReflectionClassFields}
     * then returns a combined output.
     */
    @Override
    public List<Object> getFieldRepresentativeObjects(Object object) {
        if (object == null) return null;
        if (!(object instanceof Class<?>)) {
            object = object.getClass();
        }
        
        List<Object> fieldObjectsFromMethods
                = methodStrategy.getFieldRepresentativeObjects(object);
        List<Object> fieldObjectsFromFields
                = fieldStrategy.getFieldRepresentativeObjects(object);
        List<Object> fieldObjectsOutput = new ArrayList<Object>(fieldObjectsFromMethods);
        Set<String> fieldNames = new HashSet<String>();
        if (fieldObjectsFromMethods != null) {
            for (Object method : fieldObjectsFromMethods) {
                fieldNames.add(fieldNameStrategy.getFieldName(method));
                fieldObjectsOutput.add(method);
            }
        }
        if (fieldObjectsFromFields != null) {
            for (Object field : fieldObjectsFromFields) {
                String fieldName = fieldNameStrategy.getFieldName(field);
                if (fieldNames.contains(fieldName)) {
                    continue;
                }
                fieldNames.add(fieldName);
                fieldObjectsOutput.add(field);
            }
        }
        
        return fieldObjectsOutput;
    }
}
