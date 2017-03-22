package graphql.java.generator;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("serial")
public class WildcardGenericsClass {
    public List<? extends Integer> extendsInts = new ArrayList<Integer>() {{
        add(0);
    }};
    public List<? super Integer> superInts = new ArrayList<Integer>() {{
        add(1);
    }};
    public List<?> noBounds = new ArrayList<Integer>() {{
        add(2);
    }};
    public List<? extends Integer> getExtendsInts() {
        return extendsInts;
    }
    public List<? super Integer> getSuperInts() {
        return superInts;
    }
    public List<?> getNoBounds() {
        return noBounds;
    }
}
