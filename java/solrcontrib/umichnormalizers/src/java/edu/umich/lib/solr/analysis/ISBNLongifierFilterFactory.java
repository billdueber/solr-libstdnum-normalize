/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.umich.lib.solr.analysis;
import java.util.Map;
import org.apache.lucene.analysis.util.TokenFilterFactory;
import org.apache.lucene.analysis.TokenStream;

/**
 *
 * @author dueberb
 */
public final class ISBNLongifierFilterFactory extends TokenFilterFactory {
	
	protected ISBNLongifierFilterFactory(Map<String, String> args) {
		super(args);
	}

    public ISBNLongifierFilter create(TokenStream input)
    {
        return new ISBNLongifierFilter(input);
    }
}