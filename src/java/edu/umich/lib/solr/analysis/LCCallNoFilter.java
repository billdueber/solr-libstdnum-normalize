/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.umich.lib.solr.analysis;

import edu.umich.lib.normalizers.ISBNLongifier;
import edu.umich.lib.normalizers.LCCallNumberNormalizer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.Token;
import org.apache.lucene.analysis.TokenStream;
import java.io.IOException;

/**
 *
 * @author dueberb
 */
public final class LCCallNoFilter extends TokenFilter {
	private final CharTermAttribute termAtt = addAttribute(CharTermAttribute.class);

    public LCCallNoFilter(TokenStream in) {
        super(in);
    }

	@Override
	public boolean incrementToken() throws IOException {
	    if (!input.incrementToken()) {                           
	        return false;
	    }
	    String t = termAtt.toString().toUpperCase();
	    if (t != null && t.length() != 0) {
	        if (LCCallNumberNormalizer.match(t)) {
		    	String normalized = LCCallNumberNormalizer.normalize(t);
		        termAtt.setEmpty().append(normalized);
		    }
	    }
	    return true;
	}
}
