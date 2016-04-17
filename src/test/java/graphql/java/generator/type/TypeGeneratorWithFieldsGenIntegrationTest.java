package graphql.java.generator.type;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import graphql.ExecutionResult;
import graphql.GraphQL;
import graphql.java.generator.BuildContext;
import graphql.java.generator.ClassWithListOfList;
import graphql.java.generator.ClassWithLists;
import graphql.java.generator.RecursiveClass;
import graphql.java.generator.BuildContext.Builder;
import graphql.java.generator.field.FieldStrategies;
import graphql.java.generator.field.FieldsGenerator;
import graphql.java.generator.field.reflect.FieldDataFetcher_Reflection;
import graphql.java.generator.field.reflect.FieldDescription_ReflectionAutogen;
import graphql.java.generator.field.reflect.FieldName_Reflection;
import graphql.java.generator.field.reflect.FieldObjects_Reflection;
import graphql.java.generator.field.reflect.FieldObjects_ReflectionClassFields;
import graphql.java.generator.field.reflect.FieldObjects_ReflectionClassMethods;
import graphql.java.generator.field.reflect.FieldType_Reflection;
import graphql.schema.GraphQLEnumType;
import graphql.schema.GraphQLEnumValueDefinition;
import graphql.schema.GraphQLObjectType;
import graphql.schema.GraphQLOutputType;
import graphql.schema.GraphQLSchema;

import org.hamcrest.Matcher;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;

import static graphql.schema.GraphQLFieldDefinition.newFieldDefinition;
import static graphql.schema.GraphQLObjectType.newObject;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.beans.HasPropertyWithValue.hasProperty;
import static org.junit.Assert.*;

@SuppressWarnings("unchecked")
@RunWith(Parameterized.class)
public class TypeGeneratorWithFieldsGenIntegrationTest {
    private static Logger logger = LoggerFactory.getLogger(
            TypeGeneratorWithFieldsGenIntegrationTest.class);
    
    TypeGenerator generator;
    BuildContext testContext;
    
    public TypeGeneratorWithFieldsGenIntegrationTest(FieldsGenerator fieldsGen) {
        generator = BuildContext.defaultTypeGenerator;
        testContext = new Builder()
                .setTypeGeneratorStrategy(generator)
                .setFieldsGeneratorStrategy(fieldsGen)
                .usingTypeRepository(true)
                .build();
    }
    
    @Before
    public void before() {
        TypeRepository.clear();
    }
    
    @Parameters
    public static Collection<Object[]> data() {
        final FieldsGenerator fieldsByJavaMethods = new FieldsGenerator(
                new FieldStrategies.Builder()
                        .fieldObjectsStrategy(new FieldObjects_ReflectionClassMethods())
                        .fieldNameStrategy(new FieldName_Reflection())
                        .fieldTypeStrategy(new FieldType_Reflection())
                        .fieldDataFetcherStrategy(new FieldDataFetcher_Reflection())
                        .fieldDescriptionStrategy(new FieldDescription_ReflectionAutogen())
                        .build());
        final FieldsGenerator fieldsByJavaFields = new FieldsGenerator(
                new FieldStrategies.Builder()
                        .fieldObjectsStrategy(new FieldObjects_ReflectionClassFields())
                        .fieldNameStrategy(new FieldName_Reflection())
                        .fieldTypeStrategy(new FieldType_Reflection())
                        .fieldDataFetcherStrategy(new FieldDataFetcher_Reflection())
                        .fieldDescriptionStrategy(new FieldDescription_ReflectionAutogen())
                        .build());
        final FieldsGenerator fieldsCombined = new FieldsGenerator(
                new FieldStrategies.Builder()
                        .fieldObjectsStrategy(new FieldObjects_Reflection())
                        .fieldNameStrategy(new FieldName_Reflection())
                        .fieldTypeStrategy(new FieldType_Reflection())
                        .fieldDataFetcherStrategy(new FieldDataFetcher_Reflection())
                        .fieldDescriptionStrategy(new FieldDescription_ReflectionAutogen())
                        .build());
        @SuppressWarnings("serial")
        ArrayList<Object[]> list = new ArrayList<Object[]>() {{
            add(new Object[] {fieldsByJavaMethods});
            add(new Object[] {fieldsByJavaFields});
            add(new Object[] {fieldsCombined});
        }};
        return list;
    }
    
    @Test
    public void testEnum() {
        logger.debug("testEnum");
        Object enumObj = generator.getOutputType(graphql.java.generator.Enum.class, testContext);
        Assert.assertThat(enumObj, instanceOf(GraphQLEnumType.class));
        Matcher<Iterable<GraphQLEnumValueDefinition>> hasItemsMatcher =
                hasItems(
                        hasProperty("name", is("A")),
                        hasProperty("name", is("B")),
                        hasProperty("name", is("C")));
        assertThat(((GraphQLEnumType)enumObj).getValues(), hasItemsMatcher
        );
        
        enumObj = generator.getOutputType(graphql.java.generator.EmptyEnum.class, testContext);
        Assert.assertThat(enumObj, instanceOf(GraphQLEnumType.class));
        assertThat(((GraphQLEnumType)enumObj).getValues(),
                instanceOf(List.class));
        assertThat(((GraphQLEnumType)enumObj).getValues().size(),
                is(0));
    }
    
    @Test
    public void testRecursion() {
        logger.debug("testRecursion");
        Object recursiveClass = generator.getOutputType(RecursiveClass.class, testContext);
        Assert.assertThat(recursiveClass, instanceOf(GraphQLOutputType.class));
        GraphQLObjectType queryType = newObject()
                .name("testQuery")
                .field(newFieldDefinition()
                        .type((GraphQLOutputType) recursiveClass)
                        .name("testObj")
                        .staticValue(new RecursiveClass(2))
                        .build())
                .build();
        GraphQLSchema recursiveTestSchema = GraphQLSchema.newSchema()
                .query(queryType)
                .build();
        
        String queryString = 
        "{"
        + "  testObj {"
        + "    recursionLevel"
        + "    recursive {"
        + "      recursionLevel"
        + "    recursive {"
        + "      recursionLevel"
        + "    recursive {"
        + "      recursionLevel"
        + "    recursive {"
        + "      recursionLevel"
        + "    }"
        + "    }"
        + "    }"
        + "    }"
        + "  }"
        + "}";
        ExecutionResult queryResult = new GraphQL(recursiveTestSchema).execute(queryString);
        assertThat(queryResult.getErrors(), is(empty()));
        Map<String, Object> resultMap = (Map<String, Object>) queryResult.getData();

        final ObjectMapper mapper = new ObjectMapper();
        final RecursiveClass data = mapper.convertValue(
                resultMap.get("testObj"), RecursiveClass.class);
        assertThat(data, equalTo(new RecursiveClass(2)));
        
        Map<String, Object> expectedQueryData = mapper
                .convertValue(new RecursiveClass(2), Map.class);
        assertThat(((Map<String, Object>)resultMap.get("testObj")),
                equalTo(expectedQueryData));
    }
    
    @Test
    public void testList() {
        logger.debug("testList");
        Object listType = generator.getOutputType(ClassWithLists.class, testContext);
        Assert.assertThat(listType, instanceOf(GraphQLOutputType.class));
        
        GraphQLObjectType queryType = newObject()
                .name("testQuery")
                .field(newFieldDefinition()
                        .type((GraphQLOutputType) listType)
                        .name("testObj")
                        .staticValue(new ClassWithLists())
                        .build())
                .build();
        GraphQLSchema listTestSchema = GraphQLSchema.newSchema()
                .query(queryType)
                .build();
        
        String queryString = 
        "{"
        + "  testObj {"
        + "    strings"
        + "    ints"
        + "    objects {"
        + "      objName"
        + "      objIndex"
        + "      simple"
        + "      littleBBoolean"
        + "    }"
        + "  }"
        + "}";
        ExecutionResult queryResult = new GraphQL(listTestSchema).execute(queryString);
        assertThat(queryResult.getErrors(), is(empty()));
        Map<String, Object> resultMap = (Map<String, Object>) queryResult.getData();
        
        final ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> expectedQueryData = mapper
                .convertValue(new ClassWithLists(), Map.class);
        assertThat(((Map<String, Object>)resultMap.get("testObj")),
                equalTo(expectedQueryData));
    }
    
    @Ignore("No idea what SHOULD happen here, what the canonical behaviour is")
    @Test
    public void testListOfList() {
        logger.debug("testListOfList");
        Object listType = generator.getOutputType(ClassWithListOfList.class, testContext);
        Assert.assertThat(listType, instanceOf(GraphQLOutputType.class));
        
        GraphQLObjectType queryType = newObject()
                .name("testQuery")
                .field(newFieldDefinition()
                        .type((GraphQLOutputType) listType)
                        .name("testObj")
                        .staticValue(new ClassWithLists())
                        .build())
                .build();
        GraphQLSchema listTestSchema = GraphQLSchema.newSchema()
                .query(queryType)
                .build();
        
        String queryString = 
        "{"
        + "  testObj {"
        + "    listOfListOfInts {"
        + "      empty"
        + "    }"
        + "  }"
        + "}";
        ExecutionResult queryResult = new GraphQL(listTestSchema).execute(queryString);
        //At this time, this query only works for "Method" strategy
//        assertThat(queryResult.getErrors(), is(empty()));
//        Map<String, Object> resultMap = (Map<String, Object>) queryResult.getData();
//        
//        final ObjectMapper mapper = new ObjectMapper();
//        Map<String, Object> expectedQueryData = mapper
//                .convertValue(new ClassWithListOfList(), Map.class);
//        assertThat(((Map<String, Object>)resultMap.get("testObj")),
//                equalTo(expectedQueryData));
    }
    
    @Ignore
    @Test
    public void testMaps() {
        logger.debug("testMaps");
        //Assert.fail();
    }
}
