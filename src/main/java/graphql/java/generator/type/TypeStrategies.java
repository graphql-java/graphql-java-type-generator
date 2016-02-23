package graphql.java.generator.type;

import graphql.java.generator.type.reflect.TypeName_FQNReplaceDotWithChar;

public class TypeStrategies {
    //TODO register all strategies, should this be a Builder, or should this
    //be something that users can extend?
    //TODO another strat where TypeName from annotation
    private TypeNameStrategy typeNameStrat = new TypeName_FQNReplaceDotWithChar();
    public TypeNameStrategy getTypeNameStrategy() {
        return typeNameStrat;
    }
}
