package graphql.java.generator.argument;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import graphql.java.generator.BuildContext;
import graphql.java.generator.BuildContextAware;
import graphql.schema.GraphQLArgument;
import graphql.schema.GraphQLInputType;

public class ArgumentsGenerator implements BuildContextAware {
    
    private ArgumentStrategies strategies;

    public ArgumentsGenerator(ArgumentStrategies strategies) {
        this.strategies = strategies;
    }

    public List<GraphQLArgument> getArguments(Object object) {
        List<GraphQLArgument> arguments = new ArrayList<GraphQLArgument>();
        List<Object> argObjects = getArgRepresentativeObjects(object);
        if (argObjects == null) {
            return arguments;
        }
        
        Set<String> argNames = new HashSet<String>();
        for (Object argObject : argObjects) {
            GraphQLArgument.Builder argBuilder = getArgument(argObject);
            if (argBuilder == null) {
                continue;
            }
            
            GraphQLArgument arg = argBuilder.build();
            if (argNames.contains(arg.getName())) {
                continue;
            }
            argNames.add(arg.getName());
            arguments.add(arg);
        }
        return arguments;
    }
    
    protected GraphQLArgument.Builder getArgument(Object argObject) {
        String name = strategies.getArgumentNameStrategy().getArgumentName(argObject);
        GraphQLInputType type = strategies.getArgumentTypeStrategy().getArgumentType(argObject);
        if (name == null || type == null) {
            return null;
        }
        
        String description = strategies.getArgumentDescriptionStrategy().getArgumentDescription(argObject);
        Object defaultValue = strategies.getArgumentDefaultValueStrategy().getArgumentDefaultValue(argObject);
        GraphQLArgument.Builder builder = GraphQLArgument.newArgument()
                .name(name)
                .type(type)
                .defaultValue(defaultValue)
                .description(description);
        return builder;
    }

    protected List<Object> getArgRepresentativeObjects(Object object) {
        return strategies.getArgumentObjectsStrategy()
                .getArgumentRepresentativeObjects(object);
    }

    @Override
    public BuildContext getContext() {
        return null;
    }

    @Override
    public void setContext(BuildContext context) {
        strategies.setContext(context);
    }    
}
