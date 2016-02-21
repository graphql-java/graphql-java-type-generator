package graphql.java.generator;

import graphql.java.generator.strategy.TypeNameStrategy;
import graphql.java.generator.strategy.TypeName_AbbrevFQN;

public class BuildContext {
    //TODO register all strategies, should this be a Builder, or should this
    //be something that users can extend?
    private TypeNameStrategy abbrevFQN = new TypeName_AbbrevFQN();
    public TypeNameStrategy getTypeNameStrategy() {
        return abbrevFQN;
    }
}
