package graphql.java.generator;

import graphql.java.generator.field.FieldStrategies;
import graphql.java.generator.field.FieldsGenerator;
import graphql.java.generator.field.reflect.FieldDataFetcher_ReflectionBased;
import graphql.java.generator.field.reflect.FieldName_ReflectionBased;
import graphql.java.generator.field.reflect.FieldObjects_ReflectionClass;
import graphql.java.generator.field.reflect.FieldType_ReflectionBased;
import graphql.java.generator.type.TypeGenerator;
import graphql.java.generator.type.TypeStrategies;
import graphql.java.generator.type.reflect.DefaultType_ScalarsLookup;
import graphql.java.generator.type.reflect.TypeName_FQNReplaceDotWithChar;

public class BuildContext {
    public static final BuildContext defaultContext = new Builder()
            .setTypeGeneratorStrategy(new TypeGenerator(new TypeStrategies.Builder()
                    .defaultTypeStrategy(new DefaultType_ScalarsLookup())
                    .typeNameStrategy(new TypeName_FQNReplaceDotWithChar())
                    .build()))
            .setFieldsGeneratorStrategy(new FieldsGenerator(new FieldStrategies.Builder()
                    .fieldObjectsStrategy(new FieldObjects_ReflectionClass())
                    .fieldNameStrategy(new FieldName_ReflectionBased())
                    .fieldTypeStrategy(new FieldType_ReflectionBased())
                    .fieldDataFetcherStrategy(new FieldDataFetcher_ReflectionBased())
                    .build()))
            .build();

    BuildContext(TypeGenerator typeGenerator, FieldsGenerator fieldsGenerator) {
        this.typeGenerator = typeGenerator;
        this.fieldsGenerator = fieldsGenerator;
    }
    
    private TypeGenerator typeGenerator;
    private FieldsGenerator fieldsGenerator;
    
    public TypeGenerator getTypeGeneratorStrategy() {
        return typeGenerator;
    }
    public FieldsGenerator getFieldsGeneratorStrategy() {
        return fieldsGenerator;
    }
    
    
    public static class Builder {
        private TypeGenerator typeGenerator;
        private FieldsGenerator fieldsGenerator;
        
        public Builder setTypeGeneratorStrategy(TypeGenerator typeGenerator) {
            this.typeGenerator = typeGenerator;
            return this;
        }
        public Builder setFieldsGeneratorStrategy(FieldsGenerator fieldsGenerator) {
            this.fieldsGenerator = fieldsGenerator;
            return this;
        }
        public BuildContext build() {
            return new BuildContext(typeGenerator, fieldsGenerator);
        }
    }
}
