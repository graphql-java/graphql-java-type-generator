package graphql.java.generator;

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import graphql.java.generator.field.FieldStrategies;
import graphql.java.generator.field.FieldsGenerator;
import graphql.java.generator.field.IContextualFieldGenerator;
import graphql.java.generator.field.reflect.FieldDataFetcher_Reflection;
import graphql.java.generator.field.reflect.FieldDescription_ReflectionAutogen;
import graphql.java.generator.field.reflect.FieldName_Reflection;
import graphql.java.generator.field.reflect.FieldObjects_ReflectionClassMethods;
import graphql.java.generator.field.reflect.FieldType_Reflection;
import graphql.java.generator.type.ITypeGenerator;
import graphql.java.generator.type.IContextualTypeGenerator;
import graphql.java.generator.type.TypeGenerator;
import graphql.java.generator.type.TypeStrategies;
import graphql.java.generator.type.reflect.DefaultType_ReflectionScalarsLookup;
import graphql.java.generator.type.reflect.EnumValues_Reflection;
import graphql.java.generator.type.reflect.TypeDescription_ReflectionAutogen;
import graphql.java.generator.type.reflect.TypeName_ReflectionFQNReplaceDotWithChar;
import graphql.schema.GraphQLOutputType;

public class BuildContext implements ITypeGenerator {
    public static final IContextualTypeGenerator defaultTypeGenerator = 
            new TypeGenerator(new TypeStrategies.Builder()
                    .defaultTypeStrategy(new DefaultType_ReflectionScalarsLookup())
                    .typeNameStrategy(new TypeName_ReflectionFQNReplaceDotWithChar())
                    .typeDescriptionStrategy(new TypeDescription_ReflectionAutogen())
                    .enumValuesStrategy(new EnumValues_Reflection())
                    .build());
    public static final IContextualFieldGenerator defaultFieldsGenerator = 
            new FieldsGenerator(new FieldStrategies.Builder()
                    .fieldObjectsStrategy(new FieldObjects_ReflectionClassMethods())
                    .fieldNameStrategy(new FieldName_Reflection())
                    .fieldTypeStrategy(new FieldType_Reflection())
                    .fieldDataFetcherStrategy(new FieldDataFetcher_Reflection())
                    .fieldDescriptionStrategy(new FieldDescription_ReflectionAutogen())
                    .build());
     public static final BuildContext defaultContext = new Builder()
            .setTypeGeneratorStrategy(defaultTypeGenerator)
            .setFieldsGeneratorStrategy(defaultFieldsGenerator)
            .usingTypeRepository(true)
            .build();

    BuildContext(IContextualTypeGenerator typeGenerator, IContextualFieldGenerator fieldsGenerator, boolean usingTypeRepository) {
        this.typeGenerator = typeGenerator;
        this.fieldsGenerator = fieldsGenerator;
        this.usingTypeRepository = usingTypeRepository;
    }
    
    private final IContextualTypeGenerator typeGenerator;
    private final IContextualFieldGenerator fieldsGenerator;
    private final boolean usingTypeRepository;

    private final Set<String> outputTypesBeingBuilt = Collections.newSetFromMap(
            new ConcurrentHashMap<String, Boolean>());
    
    public IContextualTypeGenerator getTypeGeneratorStrategy() {
        return typeGenerator;
    }
    public IContextualFieldGenerator getFieldsGeneratorStrategy() {
        return fieldsGenerator;
    }

    public Set<String> getOutputTypesBeingBuilt() {
        return outputTypesBeingBuilt;
    }
    
    public boolean isUsingTypeRepository() {
        return usingTypeRepository;
    }


    /**
     * Uses the current build context to generate types, where the build
     * context contains all build strategies.
     * @param object A representative "object" from which to construct
     * a {@link GraphQLOutputType}, the exact type of which is contextual
     * @return
     */
    @Override
    public GraphQLOutputType getOutputType(Object object) {
        return getTypeGeneratorStrategy().getOutputType(object, this);
    }
    
    
    public static class Builder {
        private IContextualTypeGenerator typeGenerator;
        private IContextualFieldGenerator fieldsGenerator;
        private boolean usingTypeRepository;
        
        public Builder setTypeGeneratorStrategy(IContextualTypeGenerator defaulttypegenerator) {
            this.typeGenerator = defaulttypegenerator;
            return this;
        }
        public Builder usingTypeRepository(boolean usingTypeRepository) {
            this.usingTypeRepository = usingTypeRepository;
            return this;
        }
        public Builder setFieldsGeneratorStrategy(IContextualFieldGenerator fieldsGenerator) {
            this.fieldsGenerator = fieldsGenerator;
            return this;
        }
        public BuildContext build() {
            return new BuildContext(typeGenerator, fieldsGenerator, usingTypeRepository);
        }
    }
}
