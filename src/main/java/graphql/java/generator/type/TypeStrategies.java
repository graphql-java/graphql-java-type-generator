package graphql.java.generator.type;

import graphql.java.generator.type.reflect.DefaultType_ScalarsLookup;
import graphql.java.generator.type.reflect.TypeName_FQNReplaceDotWithChar;

public class TypeStrategies {
    //TODO another strat where TypeName from annotation
    private TypeNameStrategy typeNameStrat = new TypeName_FQNReplaceDotWithChar();
    public TypeNameStrategy getTypeNameStrategy() {
        return typeNameStrat;
    }
    
    public DefaultTypeStrategy getDefaultTypeStrategy() {
        return new DefaultType_ScalarsLookup();
    }
}
