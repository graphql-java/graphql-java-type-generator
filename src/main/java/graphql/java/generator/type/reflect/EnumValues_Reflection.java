package graphql.java.generator.type.reflect;

import java.util.ArrayList;
import java.util.List;

import graphql.java.generator.type.strategies.EnumValuesStrategy;
import graphql.schema.GraphQLEnumValueDefinition;

public class EnumValues_Reflection implements EnumValuesStrategy {
    
    @Override
    public List<GraphQLEnumValueDefinition> getEnumValueDefinitions(Object object) {
        if (object == null) {
            return null;
        }
        if (!(object instanceof Class<?>)) {
            object = object.getClass();
        }
        Class<?> clazz = (Class<?>) object;
        if (!clazz.isEnum()) {
            return null;
        }
        
        List<GraphQLEnumValueDefinition> valueObjects =
                new ArrayList<GraphQLEnumValueDefinition>();
        for (Object value : clazz.getEnumConstants()) {
            valueObjects.add(new GraphQLEnumValueDefinition(
                    value.toString(), "Autogen " + value.toString(), value));
        }
        return valueObjects;
    }
}
