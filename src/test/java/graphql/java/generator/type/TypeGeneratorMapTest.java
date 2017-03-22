package graphql.java.generator.type;

import graphql.ExecutionResult;
import graphql.GraphQL;
import graphql.java.generator.DefaultBuildContext;
import graphql.schema.GraphQLObjectType;
import graphql.schema.GraphQLOutputType;
import graphql.schema.GraphQLSchema;
import graphql.schema.GraphQLType;

import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;

import static graphql.schema.GraphQLFieldDefinition.newFieldDefinition;
import static graphql.schema.GraphQLObjectType.newObject;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.Matchers.empty;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings({"unchecked"})
public class TypeGeneratorMapTest {
    private static Logger logger = LoggerFactory.getLogger(
            TypeGeneratorMapTest.class);
    
    @Before
    public void before() {
        DefaultBuildContext.defaultTypeRepository.clear();
    }
    
    //TODO This needs more testing
    @Test
    public void testMapWithTypeVars() throws JsonProcessingException, NoSuchMethodException, SecurityException {
        logger.debug("testMapWithTypeVars");
        Map<Map<? extends String, ? extends String>, Map<? extends String, ? extends String>> map;
        map = new HashMap<Map<? extends String, ? extends String>, Map<? extends String, ? extends String>>();
        Object objectType = DefaultBuildContext.reflectionContext.getOutputType(map);
        assertThat(objectType, instanceOf(GraphQLType.class));
        
        //uncomment this to print out more data
//        GraphQLObjectType queryType = newObject()
//                .name("testQuery")
//                .field(newFieldDefinition()
//                        .type((GraphQLOutputType) objectType)
//                        .name("testObj")
//                        .staticValue(map)
//                        .build())
//                .build();
//        GraphQLSchema testSchema = GraphQLSchema.newSchema()
//                .query(queryType)
//                .build();
//
//        ExecutionResult queryResult = new GraphQL(testSchema).execute(TypeGeneratorTest.querySchema);
//        assertThat(queryResult.getErrors(), is(empty()));
//        Map<String, Object> resultMap = (Map<String, Object>) queryResult.getData();
//        if (logger.isDebugEnabled()) {
//            logger.debug("testMapWithTypeVars resultMap {}", TypeGeneratorTest.prettyPrint(resultMap));
//        }
    }
}
