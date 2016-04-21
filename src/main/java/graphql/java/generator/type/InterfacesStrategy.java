package graphql.java.generator.type;

import graphql.schema.GraphQLInterfaceType;

public interface InterfacesStrategy {

    GraphQLInterfaceType[] getInterfaces(Object object);
    
}
