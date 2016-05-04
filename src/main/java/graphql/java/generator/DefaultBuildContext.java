package graphql.java.generator;

import graphql.java.generator.argument.ArgumentsGenerator;
import graphql.java.generator.argument.strategies.ArgumentStrategies;
import graphql.java.generator.field.FieldsGenerator;
import graphql.java.generator.field.strategies.FieldStrategies;
import graphql.java.generator.type.FullTypeGenerator;
import graphql.java.generator.type.StaticTypeRepository;
import graphql.java.generator.type.TypeGenerator;
import graphql.java.generator.type.TypeRepository;
import graphql.java.generator.type.WrappingTypeGenerator;
import graphql.java.generator.type.strategies.TypeStrategies;

public class DefaultBuildContext {
    public static final TypeRepository defaultTypeRepository =
            new StaticTypeRepository();
    private static final TypeGenerator defaultTypeGenerator =
            new WrappingTypeGenerator(new FullTypeGenerator(new TypeStrategies.Builder()
                    .usingTypeRepository(defaultTypeRepository)
                    .build()));
    private static final FieldsGenerator defaultFieldsGenerator = 
            new FieldsGenerator(new FieldStrategies.Builder()
                    .build());
    private static final ArgumentsGenerator defaultArgumentsGenerator = 
            new ArgumentsGenerator(new ArgumentStrategies.Builder()
                    .build());
    public static final BuildContext defaultContext = new BuildContext.Builder()
            .setTypeGeneratorStrategy(defaultTypeGenerator)
            .setFieldsGeneratorStrategy(defaultFieldsGenerator)
            .setArgumentsGeneratorStrategy(defaultArgumentsGenerator)
            .build();
}
