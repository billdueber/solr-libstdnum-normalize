/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.umich.lib.solr.analysis;
import java.util.Map;
import org.apache.solr.analysis.BaseTokenFilterFactory;
import org.apache.lucene.analysis.TokenStream;
/**
 *
 * @author dueberb
 */
public class ISBNLongifierFilterFactory extends BaseTokenFilterFactory {
    Map<String,String> args;

    public Map<String,String> getArgs()
    {
        return args;
    }
    public void init(Map<String,String> args)
    {
        this.args = args;
    }
    public ISBNLongifierFilter create(TokenStream input)
    {
        return new ISBNLongifierFilter(input);
    }
}