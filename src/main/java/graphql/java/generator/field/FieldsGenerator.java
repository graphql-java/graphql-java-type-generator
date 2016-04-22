package graphql.java.generator.field;

import static graphql.schema.GraphQLFieldDefinition.newFieldDefinition;
import static graphql.schema.GraphQLInputObjectField.newInputObjectField;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import graphql.java.generator.BuildContext;
import graphql.java.generator.BuildContextStorer;
import graphql.schema.DataFetcher;
import graphql.schema.GraphQLArgument;
import graphql.schema.GraphQLFieldDefinition;
import graphql.schema.GraphQLInputObjectField;
import graphql.schema.GraphQLInputType;
import graphql.schema.GraphQLOutputType;

/**
 * @author dwinsor
 *
 */
public class FieldsGenerator
        extends BuildContextStorer
        implements IFieldsGenerator {
    private static Logger logger = LoggerFactory.getLogger(
            FieldsGenerator.class);
    
    private FieldStrategies strategies;
    
    public FieldsGenerator(FieldStrategies strategies) {
        this.setStrategies(strategies);
    }
    
    @Override
    public List<GraphQLFieldDefinition> getOutputFields(Object object) {
        List<GraphQLFieldDefinition> fieldDefs = new ArrayList<GraphQLFieldDefinition>();
        Set<String> fieldNames = new HashSet<String>();
        List<Object> fieldObjects = getFieldRepresentativeObjects(object);
        if (fieldObjects == null) {
            return fieldDefs;
        }
        for (Object field : fieldObjects) {
            GraphQLFieldDefinition.Builder fieldBuilder =
                    getOutputFieldDefinition(field);
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
    
    @Override
    public List<GraphQLInputObjectField> getInputFields(Object object) {
        List<GraphQLInputObjectField> fieldDefs = new ArrayList<GraphQLInputObjectField>();
        Set<String> fieldNames = new HashSet<String>();
        List<Object> fieldObjects = getFieldRepresentativeObjects(object);
        if (fieldObjects == null) {
            return fieldDefs;
        }
        for (Object field : fieldObjects) {
            GraphQLInputObjectField.Builder fieldBuilder =
                    getInputFieldDefinition(field);
            if (fieldBuilder == null) {
                continue;
            }
            
            GraphQLInputObjectField fieldDef = fieldBuilder.build();
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
    protected GraphQLFieldDefinition.Builder getOutputFieldDefinition(
            final Object object) {
        String fieldName = getFieldName(object);
        GraphQLOutputType fieldType = getOutputTypeOfField(object);
        if (fieldName == null || fieldType == null) {
            return null;
        }
        Object fieldFetcher = getFieldFetcher(object);
        String fieldDescription  = getFieldDescription(object);
        String fieldDeprecation  = getFieldDeprecation(object);
        List<GraphQLArgument> fieldArguments  = getFieldArguments(object);
        logger.debug("GraphQL field will be of type [{}] and name [{}] and fetcher [{}] with description [{}]",
                fieldType, fieldName, fieldFetcher, fieldDescription);
        
        GraphQLFieldDefinition.Builder fieldBuilder = newFieldDefinition()
                .name(fieldName)
                .type(fieldType)
                .description(fieldDescription)
                .deprecate(fieldDeprecation);
        if (fieldArguments != null) {
            fieldBuilder.argument(fieldArguments);
        }
        if (fieldFetcher instanceof DataFetcher) {
            fieldBuilder.dataFetcher((DataFetcher)fieldFetcher);
        }
        else if (fieldFetcher != null) {
            fieldBuilder.staticValue(fieldFetcher);
        }
        return fieldBuilder;
    }
    
    /**
     * May return null should this field be disallowed
     * @param field
     * @param clazz
     * @return
     */
    protected GraphQLInputObjectField.Builder getInputFieldDefinition(
            final Object object) {
        String fieldName = getFieldName(object);
        GraphQLInputType fieldType = getInputTypeOfField(object);
        if (fieldName == null || fieldType == null) {
            return null;
        }
        String fieldDescription  = getFieldDescription(object);
        String fieldDefaultValue  = getFieldDefaultValue(object);
        logger.debug("GraphQL field will be of type [{}] and name [{}] with description [{}]",
                fieldType, fieldName, fieldDescription);
        
        GraphQLInputObjectField.Builder fieldBuilder = newInputObjectField()
                .name(fieldName)
                .type(fieldType)
                .description(fieldDescription)
                .defaultValue(fieldDefaultValue);
        return fieldBuilder;
    }
    
    protected List<Object> getFieldRepresentativeObjects(Object object) {
        return getStrategies().getFieldObjectsStrategy()
                .getFieldRepresentativeObjects(object);
    }
    
    protected GraphQLOutputType getOutputTypeOfField(final Object object) {
        return getStrategies().getFieldTypeStrategy().getOutputTypeOfField(object);
    }
    
    protected GraphQLInputType getInputTypeOfField(final Object object) {
        return getStrategies().getFieldTypeStrategy().getInputTypeOfField(object);
    }
    
    protected Object getFieldFetcher(final Object object) {
        return getStrategies().getFieldDataFetcherStrategy().getFieldFetcher(object);
    }
    protected String getFieldName(final Object object) {
        return getStrategies().getFieldNameStrategy().getFieldName(object);
    }
    protected String getFieldDescription(final Object object) {
        return getStrategies().getFieldDescriptionStrategy().getFieldDescription(object);
    }
    protected String getFieldDeprecation(Object object) {
        return getStrategies().getFieldDeprecationStrategy().getFieldDeprecation(object);
    }
    protected String getFieldDefaultValue(Object object) {
        return getStrategies().getFieldDefaultValueStrategy().getFieldDefaultValue(object);
    }
    protected List<GraphQLArgument> getFieldArguments(Object object) {
        return getContext().getArgumentsGeneratorStrategy().getArguments(object);
    }

    @Override
    public void setContext(BuildContext context) {
        super.setContext(context);
        getStrategies().setContext(context);
    }

    @Override
    public FieldStrategies getStrategies() {
        return strategies;
    }

    private void setStrategies(FieldStrategies strategies) {
        this.strategies = strategies;
    }
}
