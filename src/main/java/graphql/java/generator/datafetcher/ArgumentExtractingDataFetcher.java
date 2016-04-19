package graphql.java.generator.datafetcher;

import graphql.schema.DataFetchingEnvironment;

public class ArgumentExtractingDataFetcher extends ArgumentAwareDataFetcher {
    
    private ArgAwareDataFetcher nextFetcher;

    public ArgumentExtractingDataFetcher(final ArgAwareDataFetcher nextFetcher) {
        this.setNextFetcher(nextFetcher);
    }
    
    public ArgumentExtractingDataFetcher(final ArgAwareDataFetcher nextFetcher, final Object[] argNames) {
        this.setNextFetcher(nextFetcher);
        this.setArgValues(argNames);
    }
    
    @Override
    public Object get(DataFetchingEnvironment environment) {
        String[] originalArgNames = getArgNames();
        Object[] extractedArgs = new Object[originalArgNames.length];
        for (int index = 0; index < originalArgNames.length; ++index) {
            extractedArgs[index] = environment.getArgument(originalArgNames[index]);
        }
        getNextFetcher().setArgValues(extractedArgs);
        getNextFetcher().setArgNames(originalArgNames);
        return getNextFetcher().get(environment);
    }

    public ArgAwareDataFetcher getNextFetcher() {
        return nextFetcher;
    }

    public void setNextFetcher(ArgAwareDataFetcher nextFetcher) {
        this.nextFetcher = nextFetcher;
    }
}
