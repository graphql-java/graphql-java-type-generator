package graphql.java.generator.field;

import static graphql.schema.GraphQLFieldDefinition.newFieldDefinition;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import graphql.java.generator.BuildContext;
import graphql.schema.DataFetcher;
import graphql.schema.GraphQLFieldDefinition;
import graphql.schema.GraphQLOutputType;

/**
 * @author dwinsor
 *
 */
public class FieldsGen_ReflectionBased implements FieldsGenerator<Class<?>> {
    private static Logger logger = LoggerFactory.getLogger(
            FieldsGen_ReflectionBased.class);
    
    private FieldStrategies strategies = new FieldStrategies();
    
    @Override
    public List<GraphQLFieldDefinition> getFields(Class<?> clazz) {
        return getFields(clazz, new BuildContext());
    }
    
    @Override
    public List<GraphQLFieldDefinition> getFields(Class<?> clazz, BuildContext parentContext) {
        List<GraphQLFieldDefinition> fieldDefs = new ArrayList<GraphQLFieldDefinition>();
        Set<String> fieldNames = new HashSet<String>();
        while (clazz != null && !clazz.isAssignableFrom(Object.class)) {
            Field[] fields = clazz.getDeclaredFields();
            for (int index = 0; index < fields.length; ++index) {
                Field field = fields[index];
                GraphQLFieldDefinition.Builder fieldBuilder =
                        getFieldDefinition(field, clazz, parentContext);
                if (fieldBuilder == null) {
                    continue;
                }
                
                GraphQLFieldDefinition fieldDef = fieldBuilder.build();
                //check for shadowed fields, where field "item" in superclass
                //is shadowed by field "item" in subclass
                if (fieldNames.contains(fieldDef.getName())) {
                    continue;
                }
                fieldNames.add(fieldDef.getName());
                fieldDefs.add(fieldDef);
            }
            //we need to expose inherited fields
            clazz = clazz.getSuperclass();
        }
        return fieldDefs;
    }
    
    /**
     * May return null should this field be disallowed
     * @param field
     * @param clazz
     * @return
     */
    protected GraphQLFieldDefinition.Builder getFieldDefinition(
            final Field field, final Class<?> clazz, BuildContext currentContext) {
        if (field.isSynthetic()) {
            //The compiler added this field.
            return null;
        }
        
        logger.debug("Field named [{}] is of java type [{}]",
                field.getName(), field.getType());
        String fieldName = getFieldName(field);
        GraphQLOutputType fieldType = getOutputTypeOfField(field, currentContext);
        if (fieldName == null || fieldType == null) {
            return null;
        }
        
        Object fieldFetcher = getFieldFetcher(field);
        logger.debug("GraphQL field will be of type [{}] and name [{}] and fetcher [{}]",
                fieldType, fieldName, fieldFetcher);
        GraphQLFieldDefinition.Builder fieldBuilder = newFieldDefinition()
                .name(fieldName)
                .type(fieldType);
        if (fieldFetcher instanceof DataFetcher) {
            fieldBuilder.dataFetcher((DataFetcher)fieldFetcher);
        }
        else if (fieldFetcher != null) {
            fieldBuilder.staticValue(fieldFetcher);
        }
        return fieldBuilder;
    }
    
    protected GraphQLOutputType getOutputTypeOfField(
            final Field field, final BuildContext currentContext) {
        return strategies.getFieldTypeStrategy()
                .getOutputTypeOfField(field, currentContext);
    }
    protected Object getFieldFetcher(final Field field) {
        return strategies.getFieldDataFetcherStrategy().getFieldFetcher(field);
    }
    protected String getFieldName(Field field) {
        return strategies.getFieldNameStrategy().getFieldName(field);
    }

    @Override
    public void setFieldsStrategies(FieldStrategies strategies) {
        this.strategies = strategies;
    }
}
