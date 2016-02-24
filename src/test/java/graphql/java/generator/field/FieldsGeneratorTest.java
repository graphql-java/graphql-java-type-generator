package graphql.java.generator.field;

import java.util.List;

import graphql.java.generator.BuildContext;
import graphql.java.generator.RecursiveClass;
import graphql.java.generator.type.TypeRepository;
import graphql.schema.GraphQLFieldDefinition;

import org.hamcrest.beans.HasPropertyWithValue;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.beans.HasPropertyWithValue.hasProperty;
import static org.junit.Assert.*;

@SuppressWarnings("unchecked")
public class FieldsGeneratorTest {
    private static Logger logger = LoggerFactory.getLogger(
            FieldsGeneratorTest.class);
    
    @Before
    public void before() {
        TypeRepository.clear();
    }
    
    @Test
    public void testRecursion() {
        logger.debug("testRecursion");
        FieldsGenerator generator = BuildContext.defaultContext.getFieldsGeneratorStrategy();
        Object object = generator.getFields(RecursiveClass.class);
        Assert.assertThat(object, instanceOf(List.class));
        List<GraphQLFieldDefinition> recursiveFields = (List<GraphQLFieldDefinition>) object;
        assertThat(recursiveFields.size(), is(2));
        assertThat(recursiveFields, hasItems(HasPropertyWithValue.<GraphQLFieldDefinition>
                hasProperty("name", is("recursionLevel")),
                hasProperty("name", is("recursive"))
                ));
    }
}
