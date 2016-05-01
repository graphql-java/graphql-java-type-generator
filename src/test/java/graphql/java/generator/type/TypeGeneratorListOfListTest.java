package graphql.java.generator.type;

import graphql.ExecutionResult;
import graphql.GraphQL;
import graphql.Scalars;
import graphql.java.generator.BuildContext;
import graphql.java.generator.ClassWithListOfList;
import graphql.schema.GraphQLList;
import graphql.schema.GraphQLObjectType;
import graphql.schema.GraphQLSchema;

import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import static graphql.schema.GraphQLFieldDefinition.newFieldDefinition;
import static graphql.schema.GraphQLObjectType.newObject;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.Matchers.empty;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@SuppressWarnings({"unchecked", "serial"})
public class TypeGeneratorListOfListTest {
    private static Logger logger = LoggerFactory.getLogger(
            TypeGeneratorListOfListTest.class);
    
    ITypeGenerator generator = BuildContext.defaultContext;
    
    @Before
    public void before() {
        BuildContext.defaultTypeRepository.clear();
    }
    
    //@Test
    public void testCanonicalListOfList() {
        logger.debug("testCanonicalListOfList");
        List<Integer> listOfInts = new ArrayList<Integer>() {{
            add(0);
        }};
        List<List<Integer>> listOfListOfInts = new ArrayList<List<Integer>>() {{
            add(new ArrayList<Integer>() {{
                add(0);
            }});
        }};
        
        GraphQLObjectType queryType = newObject()
                .name("testQuery")
                .field(newFieldDefinition()
                        .type(new GraphQLList(Scalars.GraphQLInt))
                        .name("testObj")
                        .staticValue(listOfInts)
                        .build())
                .build();
        GraphQLSchema listTestSchema = GraphQLSchema.newSchema()
                .query(queryType)
                .build();
        
        String queryString = 
        "{"
        + "  testObj "
        + "  "
        + "}";
        ExecutionResult queryResult = new GraphQL(listTestSchema).execute(queryString);
        assertThat(queryResult.getErrors(), is(empty()));
        Map<String, Object> resultMap = (Map<String, Object>) queryResult.getData();
        logger.debug("testCanonicalListOfList resultMap {}", TypeGeneratorTest.prettyPrint(resultMap));
        
        final ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> expectedQueryData = mapper
                .convertValue(new ClassWithListOfList(), Map.class);
        assertThat(((Map<String, Object>)resultMap.get("testObj")),
                equalTo(expectedQueryData));
    }
}
