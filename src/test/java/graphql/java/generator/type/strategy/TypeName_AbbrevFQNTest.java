package graphql.java.generator.type.strategy;

import graphql.java.generator.ClassWithLists;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

public class TypeName_AbbrevFQNTest {
    @Test
    public void testObjects() {
        TypeNameStrategy strategy = new TypeName_AbbrevFQN();
        String typeName = strategy.getTypeName(new Object());
        assertThat(typeName, is("jlObject"));
        typeName = strategy.getTypeName(new ClassWithLists());
        assertThat(typeName, is("gjgClassWithLists"));
    }
    
    @Test
    public void testClasses() throws ClassNotFoundException {
        TypeNameStrategy strategy = new TypeName_AbbrevFQN();
        String typeName = strategy.getTypeName(Object.class);
        assertThat(typeName, is("jlObject"));
        typeName = strategy.getTypeName(ClassWithLists.class);
        assertThat(typeName, is("gjgClassWithLists"));
        typeName = strategy.getTypeName(Class.forName("DefaultPackageClass"));
        assertThat(typeName, is("DefaultPackageClass"));
    }
}
