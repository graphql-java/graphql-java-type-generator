package graphql.java.generator.type;

import graphql.schema.GraphQLInterfaceType;

public interface InterfacesStrategy {

    /**
     * Should return all interfaces, including those from superclasses
     * and from superinterfaces.
     * For objects representing interfaces, see {@link #getFromJavaInterface(Object)}
     * @param object an object representing a java class, but not itself an interface.
     * @return
     */
    GraphQLInterfaceType[] getInterfaces(Object object);

    /**
     * 
     * @param object an object representing a java interface, but not a concrete
     * or abstract class.
     * For classes, see {@link #getInterfaces(Object)}
     * @return
     */
    GraphQLInterfaceType getFromJavaInterface(Object object);
    
}
