package graphql.java.generator;

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import graphql.java.generator.argument.ArgumentName_Simple;
import graphql.java.generator.argument.ArgumentStrategies;
import graphql.java.generator.argument.ArgumentsGenerator;
import graphql.java.generator.argument.IArgumentsGenerator;
import graphql.java.generator.argument.reflection.ArgumentDefaultValue_Reflection;
import graphql.java.generator.argument.reflection.ArgumentDescription_ReflectionAutogen;
import graphql.java.generator.argument.reflection.ArgumentObjects_ReflectionAndParanamer;
import graphql.java.generator.argument.reflection.ArgumentType_Reflection;
import graphql.java.generator.field.FieldDataFetcher_InputAndArgAware;
import graphql.java.generator.field.FieldStrategies;
import graphql.java.generator.field.FieldsGenerator;
import graphql.java.generator.field.IFieldsGenerator;
import graphql.java.generator.field.reflect.FieldDataFetcher_Reflection;
import graphql.java.generator.field.reflect.FieldDefaultValue_Reflection;
import graphql.java.generator.field.reflect.FieldDeprecation_Reflection;
import graphql.java.generator.field.reflect.FieldDescription_ReflectionAutogen;
import graphql.java.generator.field.reflect.FieldName_Reflection;
import graphql.java.generator.field.reflect.FieldObjects_Reflection;
import graphql.java.generator.field.reflect.FieldType_Reflection;
import graphql.java.generator.type.ITypeGenerator;
import graphql.java.generator.type.StaticTypeRepository;
import graphql.java.generator.type.TypeGenerator;
import graphql.java.generator.type.TypeRepository;
import graphql.java.generator.type.TypeStrategies;
import graphql.java.generator.type.reflect.DefaultType_ReflectionScalarsLookup;
import graphql.java.generator.type.reflect.EnumValues_Reflection;
import graphql.java.generator.type.reflect.Interfaces_Reflection;
import graphql.java.generator.type.reflect.TypeDescription_ReflectionAutogen;
import graphql.java.generator.type.reflect.TypeName_ReflectionFQNReplaceDotWithChar;
import graphql.java.generator.type.resolver.TypeResolverStrategy_Caching;
import graphql.schema.GraphQLInputType;
import graphql.schema.GraphQLInterfaceType;
import graphql.schema.GraphQLOutputType;

public class BuildContext implements ITypeGenerator, BuildContextAware {
    public static final TypeRepository defaultTypeRepository =
            new StaticTypeRepository();
    private static final TypeGenerator defaultTypeGenerator = 
            new TypeGenerator(new TypeStrategies.Builder()
                    .usingTypeRepository(defaultTypeRepository)
                    .defaultTypeStrategy(new DefaultType_ReflectionScalarsLookup())
                    .typeNameStrategy(new TypeName_ReflectionFQNReplaceDotWithChar())
                    .typeDescriptionStrategy(new TypeDescription_ReflectionAutogen())
                    .enumValuesStrategy(new EnumValues_Reflection())
                    .interfacesStrategy(new Interfaces_Reflection())
                    .typeResolverStrategy(new TypeResolverStrategy_Caching())
                    .build());
    private static final FieldsGenerator defaultFieldsGenerator = 
            new FieldsGenerator(new FieldStrategies.Builder()
                    .fieldObjectsStrategy(new FieldObjects_Reflection())
                    .fieldNameStrategy(new FieldName_Reflection())
                    .fieldTypeStrategy(new FieldType_Reflection())
                    .fieldDataFetcherStrategy(new FieldDataFetcher_InputAndArgAware(new FieldDataFetcher_Reflection()))
                    .fieldDescriptionStrategy(new FieldDescription_ReflectionAutogen())
                    .fieldDefaultValueStrategy(new FieldDefaultValue_Reflection())
                    .fieldDeprecationStrategy(new FieldDeprecation_Reflection())
                    .build());
    private static final ArgumentsGenerator defaultArgumentsGenerator = 
            new ArgumentsGenerator(new ArgumentStrategies.Builder()
                    .argumentDefaultValueStrategy(new ArgumentDefaultValue_Reflection())
                    .argumentDescriptionStrategy(new ArgumentDescription_ReflectionAutogen())
                    .argumentNameStrategy(new ArgumentName_Simple())
                    .argumentObjectsStrategy(new ArgumentObjects_ReflectionAndParanamer())
                    .argumentTypeStrategy(new ArgumentType_Reflection())
                    .build());
    public static final BuildContext defaultContext = new Builder()
            .setTypeGeneratorStrategy(defaultTypeGenerator)
            .setFieldsGeneratorStrategy(defaultFieldsGenerator)
            .setArgumentsGeneratorStrategy(defaultArgumentsGenerator)
            .build();

    protected BuildContext(TypeGenerator typeGenerator,
            FieldsGenerator fieldsGenerator,
            ArgumentsGenerator argumentsGenerator) {
        this.typeGenerator = typeGenerator;
        this.fieldsGenerator = fieldsGenerator;
        this.argumentsGenerator = argumentsGenerator;
        setContext(this);
    }
    
    private final TypeGenerator typeGenerator;
    private final FieldsGenerator fieldsGenerator;
    private final ArgumentsGenerator argumentsGenerator;

    private final Set<String> typesBeingBuilt = Collections.newSetFromMap(
            new ConcurrentHashMap<String, Boolean>());
    
    public ITypeGenerator getTypeGeneratorStrategy() {
        return typeGenerator;
    }
    public IFieldsGenerator getFieldsGeneratorStrategy() {
        return fieldsGenerator;
    }
    public IArgumentsGenerator getArgumentsGeneratorStrategy() {
        return argumentsGenerator;
    }

    public Set<String> getTypesBeingBuilt() {
        return typesBeingBuilt;
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
        return getTypeGeneratorStrategy().getOutputType(object);
    }
    
    /**
     * Uses the current build context to generate types, where the build
     * context contains all build strategies.
     * @param object A representative "object" from which to construct
     * a {@link GraphQLInterfaceType}, the exact type of which is contextual
     * @return
     */
    @Override
    public GraphQLInterfaceType getInterfaceType(Object object) {
        return getTypeGeneratorStrategy().getInterfaceType(object);
    }

    /**
     * Uses the current build context to generate types, where the build
     * context contains all build strategies.
     * @param object A representative "object" from which to construct
     * a {@link GraphQLInputType}, the exact type of which is contextual
     * @return
     */
    @Override
    public GraphQLInputType getInputType(Object object) {
        return getTypeGeneratorStrategy().getInputType(object);
    }
    
    @Override
    public TypeStrategies getStrategies() {
        return getTypeGeneratorStrategy().getStrategies();
    }

    
    public static class Builder {
        protected TypeGenerator typeGenerator;
        protected FieldsGenerator fieldsGenerator;
        protected ArgumentsGenerator argumentsGenerator;
        
        public Builder setTypeGeneratorStrategy(TypeGenerator typeGenerator) {
            this.typeGenerator = typeGenerator;
            return this;
        }
        public Builder setFieldsGeneratorStrategy(FieldsGenerator fieldsGenerator) {
            this.fieldsGenerator = fieldsGenerator;
            return this;
        }
        public Builder setArgumentsGeneratorStrategy(ArgumentsGenerator argumentsGenerator) {
            this.argumentsGenerator = argumentsGenerator;
            return this;
        }
        public BuildContext build() {
            return new BuildContext(typeGenerator, fieldsGenerator,
                    argumentsGenerator);
        }
    }


    @Override
    public BuildContext getContext() {
        return this;
    }
    @Override
    public void setContext(BuildContext ignored) {
        if (typeGenerator instanceof BuildContextAware) {
            ((BuildContextAware) typeGenerator).setContext(this);
        }
        if (fieldsGenerator instanceof BuildContextAware) {
            ((BuildContextAware) fieldsGenerator).setContext(this);
        }
        if (argumentsGenerator instanceof BuildContextAware) {
            ((BuildContextAware) argumentsGenerator).setContext(this);
        }
    }
}
