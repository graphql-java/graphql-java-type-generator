package graphql.java.generator.type;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import graphql.ExecutionResult;
import graphql.GraphQL;
import graphql.GraphQLError;
import graphql.Scalars;
import graphql.java.generator.BuildContext;
import graphql.java.generator.ClassWithLists;
import graphql.java.generator.RecursiveClass;
import graphql.schema.GraphQLEnumType;
import graphql.schema.GraphQLEnumValueDefinition;
import graphql.schema.GraphQLFieldDefinition;
import graphql.schema.GraphQLObjectType;
import graphql.schema.GraphQLOutputType;
import graphql.schema.GraphQLScalarType;
import graphql.schema.GraphQLSchema;

import org.hamcrest.beans.HasPropertyWithValue;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;

import static graphql.schema.GraphQLFieldDefinition.newFieldDefinition;
import static graphql.schema.GraphQLObjectType.newObject;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.beans.HasPropertyWithValue.hasProperty;
import static org.junit.Assert.*;

@SuppressWarnings("unchecked")
public class TypeGeneratorTest {
    private static Logger logger = LoggerFactory.getLogger(
            TypeGeneratorTest.class);
    
    @Before
    public void before() {
        TypeRepository.clear();
    }
    
    @Test
    public void testEnum() {
        logger.debug("testEnum");
        TypeGenerator generator = BuildContext.defaultContext.getTypeGeneratorStrategy();
        Object enumObj = generator.getOutputType(graphql.java.generator.Enum.class);
        Assert.assertThat(enumObj, instanceOf(GraphQLEnumType.class));
        assertThat(((GraphQLEnumType)enumObj).getValues(),
                hasItems(HasPropertyWithValue.<GraphQLEnumValueDefinition>
                        hasProperty("name", is("A")),
                        hasProperty("name", is("B")),
                        hasProperty("name", is("C"))
        ));
        enumObj = generator.getOutputType(graphql.java.generator.EmptyEnum.class);
        Assert.assertThat(enumObj, instanceOf(GraphQLEnumType.class));
        assertThat(((GraphQLEnumType)enumObj).getValues(),
                instanceOf(List.class));
        assertThat(((GraphQLEnumType)enumObj).getValues().size(),
                is(0));
    }
    
    @Test
    public void testRecursion() {
        logger.debug("testRecursion");
        TypeGenerator generator = BuildContext.defaultContext.getTypeGeneratorStrategy();
        Object recursiveClass = generator.getOutputType(RecursiveClass.class);
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
        Map<String, Object> resultMap = (Map<String, Object>) queryResult.getData();
        assertEquals(new ArrayList<GraphQLError>(0), queryResult.getErrors());
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
        TypeGenerator generator = BuildContext.defaultContext.getTypeGeneratorStrategy();
        Object listType = generator.getOutputType(ClassWithLists.class);
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
        Map<String, Object> resultMap = (Map<String, Object>) queryResult.getData();
        assertEquals(new ArrayList<GraphQLError>(0), queryResult.getErrors());
        final ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> expectedQueryData = mapper
                .convertValue(new ClassWithLists(), Map.class);
        assertThat(((Map<String, Object>)resultMap.get("testObj")),
                equalTo(expectedQueryData));

    }
    
    @Test
    public void testMaps() {
        logger.debug("testMaps");
        TypeGenerator generator = BuildContext.defaultContext.getTypeGeneratorStrategy();
        //Assert.fail();
    }
    
    @Test
    public void testScalars() {
        logger.debug("testScalars");
        TypeGenerator generator = BuildContext.defaultContext.getTypeGeneratorStrategy();
        Assert.assertThat(generator.getOutputType(Boolean.class),
                instanceOf(GraphQLScalarType.class));
        Assert.assertThat((GraphQLScalarType)generator.getOutputType(Boolean.class),
                is(Scalars.GraphQLBoolean));
        Assert.assertThat((GraphQLScalarType)generator.getOutputType(String.class),
                is(Scalars.GraphQLString));
        Assert.assertThat((GraphQLScalarType)generator.getOutputType(Double.class),
                is(Scalars.GraphQLFloat));
        Assert.assertThat((GraphQLScalarType)generator.getOutputType(Float.class),
                is(Scalars.GraphQLFloat));
        Assert.assertThat((GraphQLScalarType)generator.getOutputType(Integer.class),
                is(Scalars.GraphQLInt));
        Assert.assertThat((GraphQLScalarType)generator.getOutputType(Long.class),
                is(Scalars.GraphQLLong));
        Assert.assertThat((GraphQLScalarType)generator.getOutputType(boolean.class),
                is(Scalars.GraphQLBoolean));
        Assert.assertThat((GraphQLScalarType)generator.getOutputType(double.class),
                is(Scalars.GraphQLFloat));
        Assert.assertThat((GraphQLScalarType)generator.getOutputType(float.class),
                is(Scalars.GraphQLFloat));
        Assert.assertThat((GraphQLScalarType)generator.getOutputType(int.class),
                is(Scalars.GraphQLInt));
        Assert.assertThat((GraphQLScalarType)generator.getOutputType(long.class),
                is(Scalars.GraphQLLong));
    }
}
