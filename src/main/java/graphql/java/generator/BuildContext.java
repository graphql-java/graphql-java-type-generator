package graphql.java.generator;

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import graphql.java.generator.field.FieldStrategies;
import graphql.java.generator.field.FieldsGenerator;
import graphql.java.generator.field.reflect.FieldDataFetcher_ReflectionFieldBased;
import graphql.java.generator.field.reflect.FieldName_ReflectionBased;
import graphql.java.generator.field.reflect.FieldObjects_ReflectionClass;
import graphql.java.generator.field.reflect.FieldType_ReflectionBased;
import graphql.java.generator.type.TypeGenerator;
import graphql.java.generator.type.TypeStrategies;
import graphql.java.generator.type.reflect.DefaultType_ScalarsLookup;
import graphql.java.generator.type.reflect.TypeDescription_AutogenClass;
import graphql.java.generator.type.reflect.TypeName_FQNReplaceDotWithChar;

public class BuildContext {
    public static final BuildContext defaultContext = new Builder()
            .setTypeGeneratorStrategy(new TypeGenerator(new TypeStrategies.Builder()
                    .defaultTypeStrategy(new DefaultType_ScalarsLookup())
                    .typeNameStrategy(new TypeName_FQNReplaceDotWithChar())
                    .typeDescriptionStrategy(new TypeDescription_AutogenClass())
                    .build()))
            .setFieldsGeneratorStrategy(new FieldsGenerator(new FieldStrategies.Builder()
                    .fieldObjectsStrategy(new FieldObjects_ReflectionClass())
                    .fieldNameStrategy(new FieldName_ReflectionBased())
                    .fieldTypeStrategy(new FieldType_ReflectionBased())
                    .fieldDataFetcherStrategy(new FieldDataFetcher_ReflectionFieldBased())
                    .build()))
            .usingTypeRepository(true)
            .build();

    BuildContext(TypeGenerator typeGenerator, FieldsGenerator fieldsGenerator, boolean usingTypeRepository) {
        this.typeGenerator = typeGenerator;
        this.fieldsGenerator = fieldsGenerator;
        this.usingTypeRepository = usingTypeRepository;
    }
    
    private final TypeGenerator typeGenerator;
    private final FieldsGenerator fieldsGenerator;
    private final boolean usingTypeRepository;

    private final Set<String> outputTypesBeingBuilt = Collections.newSetFromMap(
            new ConcurrentHashMap<String, Boolean>());
    
    public TypeGenerator getTypeGeneratorStrategy() {
        return typeGenerator;
    }
    public FieldsGenerator getFieldsGeneratorStrategy() {
        return fieldsGenerator;
    }

    public Set<String> getOutputTypesBeingBuilt() {
        return outputTypesBeingBuilt;
    }
    
    public boolean isUsingTypeRepository() {
        return usingTypeRepository;
    }
    
    
    public static class Builder {
        private TypeGenerator typeGenerator;
        private FieldsGenerator fieldsGenerator;
        private boolean usingTypeRepository;
        
        public Builder setTypeGeneratorStrategy(TypeGenerator typeGenerator) {
            this.typeGenerator = typeGenerator;
            return this;
        }
        public Builder usingTypeRepository(boolean usingTypeRepository) {
            this.usingTypeRepository = usingTypeRepository;
            return this;
        }
        public Builder setFieldsGeneratorStrategy(FieldsGenerator fieldsGenerator) {
            this.fieldsGenerator = fieldsGenerator;
            return this;
        }
        public BuildContext build() {
            return new BuildContext(typeGenerator, fieldsGenerator, usingTypeRepository);
        }
    }
}
