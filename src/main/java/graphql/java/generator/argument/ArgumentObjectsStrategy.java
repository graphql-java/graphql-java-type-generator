package graphql.java.generator.argument;

import java.util.List;

public interface ArgumentObjectsStrategy {

    List<ArgContainer> getArgumentRepresentativeObjects(Object object);
    
}
