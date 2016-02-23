package graphql.java.generator.type.reflect;

import graphql.java.generator.type.TypeNameStrategy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Take the fully qualified name of the given Class.class or object's class,
 * and replace all dots with some other character, since dots
 * in the graphql query are not valid.
 * Default character is underscore (_)
 * @author dwinsor
 *
 */
public class TypeName_FQNReplaceDotWithChar  implements TypeNameStrategy {
    private static Logger logger = LoggerFactory.getLogger(
            TypeName_FQNReplaceDotWithChar.class);
    protected char newChar;
    public TypeName_FQNReplaceDotWithChar() {
        this('_');
    }
    public TypeName_FQNReplaceDotWithChar(char newChar) {
        this.newChar = newChar;
    }
    
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
        
        canonicalClassName = canonicalClassName.replace('.', newChar);
        return canonicalClassName;
    }
}
