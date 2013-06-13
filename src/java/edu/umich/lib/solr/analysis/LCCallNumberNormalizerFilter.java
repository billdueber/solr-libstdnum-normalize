/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.umich.lib.solr.analysis;

import edu.umich.lib.normalizers.LCCallNumberNormalizer;
import org.apache.lucene.analysis.TokenStream;
import java.io.IOException;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;

/**
 *
 * @author dueberb
 */
public final class LCCallNumberNormalizerFilter extends org.apache.lucene.analysis.TokenFilter {

	private final CharTermAttribute termAtt = addAttribute(CharTermAttribute.class);
    private LCCallNumberNormalizer LC = new edu.umich.lib.normalizers.LCCallNumberNormalizer();

    public LCCallNumberNormalizerFilter(TokenStream in) {
        super(in);
    }

	@Override
	public boolean incrementToken() throws IOException {
	    if (!input.incrementToken()) {                           
	        return false;
	    }
	    String t = new String(termAtt.toString());
	    if (t != null && t.length() != 0) {
	        termAtt.setEmpty().append(this.LC.normalize(t));
	    }
	    return true;
	}
}
