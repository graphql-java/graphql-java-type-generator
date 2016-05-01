package graphql.java.generator.type;

public interface ChainableTypeGenerator {
    TypeGenerator getNextGen();
    void setNextGen(TypeGenerator nextTypeGen);
}
