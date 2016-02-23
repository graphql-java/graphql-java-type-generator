package graphql.java.generator;

import graphql.java.generator.field.FieldsGen_ReflectionBased;
import graphql.java.generator.field.FieldsGenerator;
import graphql.java.generator.type.TypeGenerator;

public class BuildContext {
    public FieldsGenerator getFieldsGeneratorStrategy() {
        // TODO Auto-generated method stub
        return new FieldsGen_ReflectionBased();
    }
    
    private TypeGenerator typeGenerator = new TypeGenerator();
    public TypeGenerator getTypeGeneratorStrategy(Object object) {
        return typeGenerator;
    }
}
