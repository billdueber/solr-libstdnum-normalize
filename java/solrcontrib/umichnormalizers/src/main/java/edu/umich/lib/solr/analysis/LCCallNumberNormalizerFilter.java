/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.umich.lib.solr.analysis;

import edu.umich.lib.normalizers.LCCallNumberNormalizer;
import org.apache.lucene.analysis.Token;
import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.TokenStream;
import java.util.regex.*;
import java.io.IOException;

/**
 *
 * @author dueberb
 */
public final class LCCallNumberNormalizerFilter extends org.apache.lucene.analysis.TokenFilter {

    private LCCallNumberNormalizer LC = new edu.umich.lib.normalizers.LCCallNumberNormalizer();

    public LCCallNumberNormalizerFilter(TokenStream in) {
        super(in);
    }

    public Token next() throws IOException {

        return normalize(this.input.next());
    }

    public Token next(Token result) throws IOException {
        return normalize(this.input.next());

    }

    public Token normalize(Token t) {
        if (null == t || null == t.termBuffer() || t.termLength() == 0) {
            //log.info("Got an empty token");
            return t;
        }
        String val = new String(t.termBuffer());
        t.setTermBuffer(this.LC.normalize(val));
        return t;
    }
}
