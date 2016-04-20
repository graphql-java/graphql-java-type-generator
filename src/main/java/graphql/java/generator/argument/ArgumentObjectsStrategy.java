package graphql.java.generator.argument;

import java.util.List;

public interface ArgumentObjectsStrategy {

    List<Object> getArgumentRepresentativeObjects(Object object);
    
}
