package edu.umich.lib.solr.analysis;
import java.util.Map;
import org.apache.solr.analysis.BaseTokenFilterFactory;
import org.apache.lucene.analysis.TokenStream;

public class LCCNNormalizerFilterFactory extends BaseTokenFilterFactory
{
    Map<String,String> args;
    public Map<String,String> getArgs()
    {
        return args;
    }
    public void init(Map<String,String> args)
    {
        this.args = args;
    }
    public LCCNNormalizerFilter create(TokenStream input)
    {
        return new LCCNNormalizerFilter(input);
    }
}

