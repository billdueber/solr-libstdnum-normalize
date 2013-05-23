
package edu.umich.lib.solr.analysis;

import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;

import java.util.regex.*;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Trims leading and trailing whitespace from Tokens in the stream.
 *
 * @version $Id:$
 */
public final class LCCNNormalizerFilter extends org.apache.lucene.analysis.TokenFilter {


	//  final static Logger log = LoggerFactory.getLogger(LCCNNormalizerFilter.class);
	private final CharTermAttribute termAtt = addAttribute(CharTermAttribute.class);

	// Normalization pattern from http://www.loc.gov/marc/lccn-namespace.html#syntax
	public final Pattern trailingslashpattern = Pattern.compile("^(.*?)/.*$");
	public final Pattern lccndashpattern = Pattern.compile("^(\\w+)(?:-(\\d+))?.*$");

	public LCCNNormalizerFilter(TokenStream in) {
		super(in);
	}

	@Override
	public boolean incrementToken() throws IOException {
	    if (!input.incrementToken()) {                           
	        return false;
	    }
	    String t = new String(termAtt.toString());
	    
	    if (t == null || t.length() == 0) {
	    	return true;
	    }
	    
	    // First, ditch the spaces
	    t = t.replaceAll("\\s+", "");
	    
	    // .. and lowercase
	    t = t.toLowerCase();
	    
	    // Lose any trailing slash-plus-characters
	    Matcher ts = trailingslashpattern.matcher(t);
	    if (ts.matches()) {
			// log.info("Found a slash; turning '" + t + "' into '" + ts.group(1) + "'");
	    	t = ts.group(1);
	    }
	    
	    // Attempt to match
	    Matcher m = lccndashpattern.matcher(t);
	    
	    // if it doesn't match abort processing
	    if (!(m.matches())) {
	    	// log.info("'" + val + "' doesn't match!");
	    	return true;
	    }
	    
	    // If it does, see what we've got
	    String prefix = m.group(1);
	    String postHyphenDigits = m.group(2);
	    
	    // No hyphen (or nothing after it)? Just return the first bit
	    if (postHyphenDigits == null || postHyphenDigits.length() == 0) {
	    	termAtt.setEmpty().append(prefix);
	    } else { 
		    if (postHyphenDigits.length() < 6) {
		    	postHyphenDigits = String.format("%06d", Integer.parseInt(postHyphenDigits));
		    }
		    termAtt.setEmpty().append(prefix + postHyphenDigits);
	    }
        
	    return true;
	}
}
