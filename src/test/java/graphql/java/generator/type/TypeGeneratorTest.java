package graphql.java.generator.type;

import graphql.ExecutionResult;
import graphql.GraphQL;
import graphql.java.generator.BuildContext;
import graphql.java.generator.InterfaceChild;
import graphql.java.generator.InterfaceImpl;
import graphql.java.generator.InterfaceImplSecondary;
import graphql.java.generator.InterfaceSecondary;
import graphql.schema.GraphQLObjectType;
import graphql.schema.GraphQLOutputType;
import graphql.schema.GraphQLSchema;

import static org.junit.Assert.assertThat;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

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
    public void testGraphQLInterfaces() throws JsonProcessingException {
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
        + "      name"
        + "      kind"
        + "      description"
        + "      interfaces {"
        + "        name"
        + "      }"
        + "      fields {"
        + "        name"
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
        if (logger.isDebugEnabled()) {
            logger.debug("testGraphQLInterfaces resultMap {}", prettyPrint(resultMap));
        }
        
//        final ObjectMapper mapper = new ObjectMapper();
//        Map<String, Object> expectedQueryData = mapper
//                .convertValue(new InterfaceImplSecondary(), Map.class);
//        assertThat(((Map<String, Object>)resultMap.get("testObj")),
//                equalTo(expectedQueryData));
//        assertThat(((Map<String, Object>)resultMap.get("testObj")).size(),
//                is(2));
    }
    
    public static ObjectMapper mapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);
    public static String prettyPrint(Map<String, Object> resultMap) {
        try {
            return mapper.writeValueAsString(resultMap);
        }
        catch (JsonProcessingException e) {
            e.printStackTrace();
            return resultMap.toString();
        }
    }
}
