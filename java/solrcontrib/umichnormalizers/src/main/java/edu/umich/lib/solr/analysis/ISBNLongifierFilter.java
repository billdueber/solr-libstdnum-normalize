/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.umich.lib.solr.analysis;
import edu.umich.lib.normalizers.ISBNLongifier;
import org.apache.lucene.analysis.Token;
import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.TokenStream;
import java.util.regex.*;
import java.io.IOException;
/**
 *
 * @author dueberb
 */
public class ISBNLongifierFilter extends org.apache.lucene.analysis.TokenFilter {

    public ISBNLongifierFilter(TokenStream in) {
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
        try {
            t.setTermBuffer(ISBNLongifier.longify(val));
        } catch (IllegalArgumentException e) {
            // Do nothing -- leave the token alone
        }
        return t;
    }


}
