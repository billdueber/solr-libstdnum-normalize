/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.umich.lib.solr.analysis;

import edu.umich.lib.normalizers.LCCallNumberNormalizer;
import org.apache.lucene.analysis.tokenattributes.TermAttribute;
import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.Token;
import org.apache.lucene.analysis.TokenStream;
import java.io.IOException;

/**
 *
 * @author dueberb
 */
public class LCCallNoFilter extends TokenFilter {

    public LCCallNoFilter(TokenStream in) {
        super(in);
//        TokenStream.setUseNewAPIDefault(true);
//        in.setUseNewAPIDefault(true);
//        TermAttribute termAtt = (TermAttribute) addAttribute(TermAttribute.class);
    }
//    private TermAttribute termAtt;

//    @Override
//    public final boolean incrementToken() throws IOException {
//        if (!input.incrementToken()) {
//            return false;
//        }
//
//        if (termAtt == null || termAtt.term() == null || termAtt.term().length() == 0) {
//            return false;
//        }
//        // Does it match? If not, return true
//        String s = termAtt.term();
//        String normalized = LCCallNumberNormalizer.normalize(s);
//        if (!s.equals(normalized)) {
//            termAtt.setTermBuffer(normalized);
//        }
//
//        return true;
//    }

    public Token next() throws IOException {

        return normalize(this.input.next());
    }

    public Token next(Token result) throws IOException {
        return normalize(this.input.next());

    }

    public Token normalize(final Token t) throws IOException {
        if (null == t || null == t.termBuffer() || t.termLength() == 0) {
            //log.info("Got an empty token");
            return t;
        }

        String s = t.term().toUpperCase();
        String normalized = LCCallNumberNormalizer.normalize(s);
        if (LCCallNumberNormalizer.match(s)) {
	    //            System.out.println("matched; normalizing");
            t.setTermBuffer(normalized);
        }
        return t;
    }
}
