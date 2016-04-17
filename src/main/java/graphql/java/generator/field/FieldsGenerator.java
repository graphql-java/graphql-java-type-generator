package graphql.java.generator.field;

import static graphql.schema.GraphQLFieldDefinition.newFieldDefinition;

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
public class FieldsGenerator {
    private static Logger logger = LoggerFactory.getLogger(
            FieldsGenerator.class);
    
    private FieldStrategies strategies;
    
    public FieldsGenerator(FieldStrategies strategies) {
        this.strategies = strategies;
    }
    
    public List<GraphQLFieldDefinition> getFields(Object object) {
        return getFields(object, BuildContext.defaultContext);
    }
    
    public List<GraphQLFieldDefinition> getFields(Object object, BuildContext parentContext) {
        List<GraphQLFieldDefinition> fieldDefs = new ArrayList<GraphQLFieldDefinition>();
        Set<String> fieldNames = new HashSet<String>();
        List<Object> fieldObjects = getFieldRepresentativeObjects(object);
        for (Object field : fieldObjects) {
            GraphQLFieldDefinition.Builder fieldBuilder =
                    getFieldDefinition(field, parentContext);
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
        return fieldDefs;
    }
    
    /**
     * May return null should this field be disallowed
     * @param field
     * @param clazz
     * @return
     */
    protected GraphQLFieldDefinition.Builder getFieldDefinition(
            final Object object, BuildContext currentContext) {
        String fieldName = getFieldName(object);
        GraphQLOutputType fieldType = getOutputTypeOfField(object, currentContext);
        if (fieldName == null || fieldType == null) {
            return null;
        }
        Object fieldFetcher = getFieldFetcher(object);
        String fieldDescription  = getFieldDescription(object);
        logger.debug("GraphQL field will be of type [{}] and name [{}] and fetcher [{}] with description [{}]",
                fieldType, fieldName, fieldFetcher, fieldDescription);
        
        GraphQLFieldDefinition.Builder fieldBuilder = newFieldDefinition()
                .name(fieldName)
                .type(fieldType)
                .description(fieldDescription);
        if (fieldFetcher instanceof DataFetcher) {
            fieldBuilder.dataFetcher((DataFetcher)fieldFetcher);
        }
        else if (fieldFetcher != null) {
            fieldBuilder.staticValue(fieldFetcher);
        }
        return fieldBuilder;
    }
    
    protected List<Object> getFieldRepresentativeObjects(Object object) {
        return strategies.getFieldObjectsStrategy()
                .getFieldRepresentativeObjects(object);
    }
    
    protected GraphQLOutputType getOutputTypeOfField(
            final Object object, final BuildContext currentContext) {
        return strategies.getFieldTypeStrategy()
                .getOutputTypeOfField(object, currentContext);
    }
    protected Object getFieldFetcher(final Object object) {
        return strategies.getFieldDataFetcherStrategy().getFieldFetcher(object);
    }
    protected String getFieldName(final Object object) {
        return strategies.getFieldNameStrategy().getFieldName(object);
    }
    protected String getFieldDescription(final Object object) {
        return strategies.getFieldDescriptionStrategy().getFieldDescription(object);
    }
}
