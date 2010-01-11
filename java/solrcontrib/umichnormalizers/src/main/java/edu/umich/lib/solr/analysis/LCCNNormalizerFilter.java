
package edu.umich.lib.solr.analysis;

import org.apache.lucene.analysis.Token;
import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.TokenStream;
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
  
  // Normalization pattern from http://www.loc.gov/marc/lccn-namespace.html#syntax
  public final Pattern trailingslashpattern = Pattern.compile("^(.*?)/.*$");
  public final Pattern lccndashpattern = Pattern.compile("^(\\w+)(?:-(\\d+))?.*$");

  public LCCNNormalizerFilter(TokenStream in) {
    super(in);
  }


  public Token next() throws IOException
  {
    return normalize(this.input.next());
  }
  
  public Token next(Token result) throws IOException
  {
    return normalize(this.input.next());
    
  }
  
  protected Token normalize(Token t) 
  {
    if (null == t || null == t.termBuffer() || t.termLength() == 0){
      //log.info("Got an empty token");
      return t;
    }
    String val = new String(t.termBuffer());
    
    
    // First, ditch the spaces
    val = val.replaceAll("\\s+", "");
    
    // .. and lowercase
    
    val = val.toLowerCase();
    
    // Lose any trailing slash-plus-characters
    Matcher ts = trailingslashpattern.matcher(val);
    if (ts.matches()) {
	//      log.info("Found a slash; turning '" + val + "' into '" + ts.group(1) + "'");
      val = ts.group(1);
    }
    
    // Attempt to match
    Matcher m = lccndashpattern.matcher(val);
    
    // if it doesn't match, just return the input value
    if (!(m.matches())) {
	//      log.info("'" + val + "' doesn't match!");
      return t;
    }
    
    // If it does, see what we've got
    String prefix = m.group(1);
    String postHyphenDigits = m.group(2);
    
    // No hyphen (or nothing after it)? Just return the first bit
    if (postHyphenDigits == null || postHyphenDigits.length() == 0) {
      t.setTermBuffer(prefix);      
      return t;
    }
    
    if (postHyphenDigits.length() < 6) {
      postHyphenDigits = String.format("%06d", Integer.parseInt(postHyphenDigits));
    }
    
    t.setTermBuffer(prefix + postHyphenDigits);
    return t;
  }

}
