package graphql.java.generator.field.reflect;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import graphql.java.generator.BuildContext;
import graphql.java.generator.field.FieldTypeStrategy;
import graphql.java.generator.type.TypeGenerator;
import graphql.schema.GraphQLList;
import graphql.schema.GraphQLOutputType;

public class FieldType_ReflectionBased implements FieldTypeStrategy {
    private static Logger logger = LoggerFactory.getLogger(
            FieldType_ReflectionBased.class);
    
    @Override
    public GraphQLOutputType getOutputTypeOfField(Object object, BuildContext currentContext) {
        if (!(object instanceof Field)) {
            return null;
        }
        Field field = (Field) object;
        Class<?> fieldClazz = field.getType();
        if (List.class.isAssignableFrom(fieldClazz)) {
            Type listType = field.getGenericType();
            if (listType instanceof ParameterizedType) {
                Type listGenericType = ((ParameterizedType) listType).getActualTypeArguments()[0];
                if (listGenericType instanceof Class<?>) {
                    logger.debug("Field [{}] is a list of generic type [{}]",
                            field.getName(), listGenericType);
                    TypeGenerator typeGen = currentContext.getTypeGeneratorStrategy();
                    return new GraphQLList(typeGen.getOutputType((Class<?>)listGenericType, currentContext));
                }
            }
            //TODO test on raw List, should not work
        }

        TypeGenerator typeGen = currentContext.getTypeGeneratorStrategy();
        return typeGen.getOutputType(fieldClazz, currentContext);
    }
}
