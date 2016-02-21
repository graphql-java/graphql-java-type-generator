package graphql.java.generator.strategy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Take the fully qualified name of the class of the object,
 * and abbreviates the package names, discarding any periods.
 * This strategy will lead to type-name collisions, but is easy to use.
 * @author dwinsor
 *
 */
public class TypeName_AbbrevFQN  implements TypeNameStrategy<Object> {
    private static Logger logger = LoggerFactory.getLogger(TypeName_AbbrevFQN.class);
    @Override
    public String getTypeName(Object object) {
        if (object == null) {
            return null;
        }
        if (!(object instanceof Class<?>)) {
            object = object.getClass();
        }
        Class<?> clazz = (Class<?>) object;
        String canonicalClassName = clazz.getCanonicalName();
        if (canonicalClassName == null) {
            logger.debug("Name was null for class [{}]. "
                    + "local or anonymous class or an array whose component"
                    + " type does not have a canonical name", clazz);
            return null;
        }
        
        //TODO this is a temporary measure, it strips all packaging, which
        //leads to type collisions
        canonicalClassName = canonicalClassName.substring(
                canonicalClassName.lastIndexOf(".") + 1);
        return canonicalClassName;
    }
}
