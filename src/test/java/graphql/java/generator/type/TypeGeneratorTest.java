package graphql.java.generator.type;

import graphql.ExecutionResult;
import graphql.GraphQL;
import graphql.java.generator.BuildContext;
import graphql.java.generator.InterfaceChild;
import graphql.java.generator.InterfaceImpl;
import graphql.java.generator.InterfaceImplSecondary;
import graphql.schema.GraphQLObjectType;
import graphql.schema.GraphQLOutputType;
import graphql.schema.GraphQLSchema;

import static org.junit.Assert.assertThat;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;

import static graphql.schema.GraphQLFieldDefinition.newFieldDefinition;
import static graphql.schema.GraphQLObjectType.newObject;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.Matchers.empty;

import java.util.Map;

@SuppressWarnings("unchecked")
public class TypeGeneratorTest {
    private static Logger logger = LoggerFactory.getLogger(
            TypeGeneratorTest.class);
    
    ITypeGenerator generator = BuildContext.defaultContext;
    
    @Before
    public void before() {
        TypeRepository.clear();
    }
    
    @Test
    public void testClassesWithInterfaces() {
        logger.debug("testClassesWithInterfaces");
        Object interfaceType = generator.getOutputType(InterfaceChild.class);
        Assert.assertThat(interfaceType, instanceOf(GraphQLOutputType.class));
        
        GraphQLObjectType queryType = newObject()
                .name("testQuery")
                .field(newFieldDefinition()
                        .type((GraphQLOutputType) interfaceType)
                        .name("testObj")
                        .staticValue(new InterfaceImpl())
                        .build())
                .build();
        GraphQLSchema testSchema = GraphQLSchema.newSchema()
                .query(queryType)
                .build();
        
        String queryString = 
        "{"
        + "  testObj {"
        + "    parent"
        + "    child"
        + "  }"
        + "}";
        ExecutionResult queryResult = new GraphQL(testSchema).execute(queryString);
        assertThat(queryResult.getErrors(), is(empty()));
        Map<String, Object> resultMap = (Map<String, Object>) queryResult.getData();
        
        final ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> expectedQueryData = mapper
                .convertValue(new InterfaceImpl(), Map.class);
        assertThat(((Map<String, Object>)resultMap.get("testObj")),
                equalTo(expectedQueryData));
        assertThat(((Map<String, Object>)resultMap.get("testObj")).size(),
                is(2));
    }
    
    @Test
    public void testGraphQLInterfaces() {
        logger.debug("testGraphQLInterfaces");
        Object interfaceType = generator.getOutputType(InterfaceImplSecondary.class);
        Assert.assertThat(interfaceType, instanceOf(GraphQLOutputType.class));
        
        GraphQLObjectType queryType = newObject()
                .name("testQuery")
                .field(newFieldDefinition()
                        .type((GraphQLOutputType) interfaceType)
                        .name("testObj")
                        .staticValue(new InterfaceImplSecondary())
                        .build())
                .build();
        GraphQLSchema testSchema = GraphQLSchema.newSchema()
                .query(queryType)
                .build();
        
        String querySchema = ""
        + "query testSchema {"
        + "  __schema {"
        + "    types {"
        + "      interfaces {"
        + "        name"
        + "      }"
        + "      fields {"
        + "        args {"
        + "          name"
        + "          description"
        + "          type {"
        + "            name"
        + "          }"
        + "        }"
        + "      }"
        + "    }"
        + "  }"
        + "}";
        
        String queryString = 
        "{"
        + "  testObj {"
        + "    parent"
        + "    child"
        + "  }"
        + "}";
        ExecutionResult queryResult = new GraphQL(testSchema).execute(querySchema);
        assertThat(queryResult.getErrors(), is(empty()));
        Map<String, Object> resultMap = (Map<String, Object>) queryResult.getData();
        logger.debug("testGraphQLInterfaces resultMap {}", resultMap);
        
//        final ObjectMapper mapper = new ObjectMapper();
//        Map<String, Object> expectedQueryData = mapper
//                .convertValue(new InterfaceImplSecondary(), Map.class);
//        assertThat(((Map<String, Object>)resultMap.get("testObj")),
//                equalTo(expectedQueryData));
//        assertThat(((Map<String, Object>)resultMap.get("testObj")).size(),
//                is(2));
    }
}
