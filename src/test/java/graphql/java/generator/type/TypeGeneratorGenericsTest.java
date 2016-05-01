package graphql.java.generator.type;

import graphql.ExecutionResult;
import graphql.GraphQL;
import graphql.java.generator.BuildContext;
import graphql.java.generator.ClassWithListOfGenerics;
import graphql.schema.GraphQLFieldDefinition;
import graphql.schema.GraphQLList;
import graphql.schema.GraphQLObjectType;
import graphql.schema.GraphQLOutputType;
import graphql.schema.GraphQLSchema;
import graphql.schema.GraphQLType;

import static org.junit.Assert.assertThat;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static graphql.schema.GraphQLFieldDefinition.newFieldDefinition;
import static graphql.schema.GraphQLObjectType.newObject;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.Matchers.empty;

import java.util.Map;

@SuppressWarnings({"unchecked"})
public class TypeGeneratorGenericsTest {
    private static Logger logger = LoggerFactory.getLogger(
            TypeGeneratorGenericsTest.class);
    
    ITypeGenerator generator = BuildContext.defaultContext;
    
    @Before
    public void before() {
        BuildContext.defaultTypeRepository.clear();
    }
    
    //@Test
    public void testGeneratedListOfParamQuery() {
        logger.debug("testGeneratedListOfParamQuery");
        Object objectType = generator.getOutputType(ClassWithListOfGenerics.class);
        GraphQLObjectType queryType = newObject()
                .name("testQuery")
                .field(newFieldDefinition()
                        .type((GraphQLOutputType) objectType)
                        .name("testObj")
                        .staticValue(new ClassWithListOfGenerics())
                        .build())
                .build();
        GraphQLSchema testSchema = GraphQLSchema.newSchema()
                .query(queryType)
                .build();
        
        String queryString = 
        "{"
        + "  testObj {"
        + "    listOfParamOfInts {"
        + "      t"
        + "    }"
        + "  }"
        + "}";
        ExecutionResult queryResult = new GraphQL(testSchema).execute(queryString);
        assertThat(queryResult.getErrors(), is(empty()));
        Map<String, Object> resultMap = (Map<String, Object>) queryResult.getData();
        logger.debug("testGeneratedListOfParamQuery results {}",
                TypeGeneratorTest.prettyPrint(resultMap));

//        final ObjectMapper mapper = new ObjectMapper();
//        final RecursiveClass data = mapper.convertValue(
//                resultMap.get("testObj"), RecursiveClass.class);
//        assertThat(data, equalTo(new RecursiveClass(2)));
//        
//        Map<String, Object> expectedQueryData = mapper
//                .convertValue(new RecursiveClass(2), Map.class);
//        assertThat(((Map<String, Object>)resultMap.get("testObj")),
//                equalTo(expectedQueryData));
    }
    
    //@Test
    public void testGeneratedListOfParam() {
        logger.debug("testGeneratedListOfParam");
        Object objectType = generator.getOutputType(ClassWithListOfGenerics.class);
        Assert.assertThat(objectType, instanceOf(GraphQLObjectType.class));
        Assert.assertThat(objectType, not(instanceOf(GraphQLList.class)));
        GraphQLFieldDefinition field = ((GraphQLObjectType) objectType)
                .getFieldDefinition("listOfParamOfInts");
        
        Assert.assertThat(field, notNullValue());
        GraphQLOutputType listType = field.getType();
        Assert.assertThat(listType, instanceOf(GraphQLList.class));
        GraphQLType wrappedType = ((GraphQLList) listType).getWrappedType();
        Assert.assertThat(wrappedType, instanceOf(GraphQLObjectType.class));
    }
}
