package graphql.java.generator.type.strategy;

import graphql.java.generator.ClassWithLists;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

public class TypeName_FQNReplaceDotWithCharTest {
    @Test
    public void testObjects() {
        TypeNameStrategy strategy = new TypeName_FQNReplaceDotWithChar();
        String typeName = strategy.getTypeName(new Object());
        assertThat(typeName, is("java_lang_Object"));
        typeName = strategy.getTypeName(new ClassWithLists());
        assertThat(typeName, is("graphql_java_generator_ClassWithLists"));
    }
    
    @Test
    public void testClasses() throws ClassNotFoundException {
        TypeNameStrategy strategy = new TypeName_FQNReplaceDotWithChar();
        String typeName = strategy.getTypeName(Object.class);
        assertThat(typeName, is("java_lang_Object"));
        typeName = strategy.getTypeName(ClassWithLists.class);
        assertThat(typeName, is("graphql_java_generator_ClassWithLists"));
        typeName = strategy.getTypeName(Class.forName("DefaultPackageClass"));
        assertThat(typeName, is("DefaultPackageClass"));
    }
}
