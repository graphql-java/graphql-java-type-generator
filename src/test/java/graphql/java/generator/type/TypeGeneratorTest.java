package graphql.java.generator.type;

import graphql.Scalars;
import graphql.java.generator.BuildContext;
import graphql.schema.GraphQLScalarType;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.hamcrest.CoreMatchers.*;

public class TypeGeneratorTest {
    private static Logger logger = LoggerFactory.getLogger(
            TypeGeneratorTest.class);
    
    ITypeGenerator generator = BuildContext.defaultTypeGenerator;
    
    @Before
    public void before() {
        TypeRepository.clear();
    }
    
    @Test
    public void testScalars() {
        logger.debug("testScalars");
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
