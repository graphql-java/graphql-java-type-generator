package graphql.java.generator.type.reflect;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import graphql.java.generator.BuildContextStorer;
import graphql.java.generator.type.InterfacesStrategy;
import graphql.schema.GraphQLFieldDefinition;
import graphql.schema.GraphQLInterfaceType;
import graphql.schema.TypeResolver;

public class Interfaces_Reflection
        extends BuildContextStorer
        implements InterfacesStrategy {
    
    @Override
    public GraphQLInterfaceType[] getInterfaces(Object object) {
        if (object instanceof Class<?>) {
            if (((Class<?>) object).isInterface()) {
                return null;
            }
            
            Map<Class<?>, GraphQLInterfaceType> interfaces = new HashMap<Class<?>, GraphQLInterfaceType>();
            getInterfaces(interfaces, (Class<?>) object);
            return interfaces.values().toArray(new GraphQLInterfaceType[0]);
        }
        return null;
    }

    @Override
    public GraphQLInterfaceType getFromJavaInterface(Object object) {
        if (object instanceof Class<?>) {
            if (((Class<?>) object).isInterface()) {
                return generateInterface((Class<?>) object);
            }
        }
        return null;
    }

    protected void getInterfaces(
            final Map<Class<?>, GraphQLInterfaceType> interfaceMap,
            final Class<?> clazz) {
        
        for (Class<?> intf : clazz.getInterfaces()) {        
            if (interfaceMap.containsKey(intf)) {
                continue;
            }
            GraphQLInterfaceType iType = generateInterface(intf);
            if (iType != null) {
                interfaceMap.put(intf, iType);
            }
            getInterfaces(interfaceMap, intf);
        }
        Class<?> superClazz = clazz.getSuperclass();
        if (superClazz != null && superClazz != Object.class) {
            getInterfaces(interfaceMap, superClazz);
        }
    }

    protected GraphQLInterfaceType generateInterface(Class<?> intf) {
        String name = getContext().getTypeGeneratorStrategy().getStrategies().getTypeNameStrategy().getTypeName(intf);
        List<GraphQLFieldDefinition> fieldDefinitions = getContext().getFieldsGeneratorStrategy().getOutputFields(intf);
        TypeResolver typeResolver = getContext().getTypeGeneratorStrategy().getStrategies().getTypeResolverStrategy().getTypeResolver(intf);
        String description = getContext().getTypeGeneratorStrategy().getStrategies().getTypeDescriptionStrategy().getTypeDescription(intf);
        if (name == null || fieldDefinitions == null || typeResolver == null) {
            return null;
        }
        GraphQLInterfaceType.Builder builder = GraphQLInterfaceType.newInterface()
                .description(description)
                .fields(fieldDefinitions)
                .name(name)
                .typeResolver(typeResolver);
        return builder.build();
    }
}
