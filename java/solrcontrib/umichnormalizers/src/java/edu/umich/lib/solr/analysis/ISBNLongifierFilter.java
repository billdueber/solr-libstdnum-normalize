/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.umich.lib.solr.analysis;
import edu.umich.lib.normalizers.ISBNLongifier;
import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;

import java.io.IOException;
/**
 *
 * @author dueberb
 */
public final class ISBNLongifierFilter extends TokenFilter {
	
	private final CharTermAttribute termAtt = addAttribute(CharTermAttribute.class);

    public ISBNLongifierFilter(TokenStream in) {
        super(in);
    }

	@Override
	public boolean incrementToken() throws IOException {
	    if (!input.incrementToken()) {                           
	        return false;
	    }
	    String t = termAtt.toString();
	    if (t != null && t.length() != 0) {
		    try {
		        termAtt.setEmpty().append(ISBNLongifier.longify(t));
		    } catch (IllegalArgumentException e) {
		    	// Do nothing -- leave the token alone
		    }
	    }
	    return true;
	}


}
