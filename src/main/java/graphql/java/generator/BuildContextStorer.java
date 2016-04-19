package graphql.java.generator;

public class BuildContextStorer implements BuildContextAware {
    private BuildContext context;
    
    @Override
    public BuildContext getContext() {
        return context;
    }
    
    @Override
    public void setContext(BuildContext context) {
        if (this.context == null) {
            this.context = context;
        }
    }
}
