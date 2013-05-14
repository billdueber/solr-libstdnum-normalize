package edu.umich.lib.solr.analysis;

import java.io.StringReader;

import org.apache.lucene.analysis.BaseTokenStreamTestCase;
import org.apache.lucene.analysis.MockTokenizer;
import org.apache.lucene.analysis.Tokenizer;

public class TestISBNLongifierFilter extends BaseTokenStreamTestCase {

	public void testISBNLongify() throws Exception {
		assertLongifiesTo("1555705189", new String[] {"9781555705183"});
		assertLongifiesTo("1-555-7-05-1-8.9", new String[] {"9781555705183"});
		assertLongifiesTo("0-671-89854-X", new String[] {"9780671898540"});
		assertLongifiesTo("New 067189854-X (online version", new String[] {"9780671898540"});
	}
	
	static void assertLongifiesTo(String input, String[] expected) throws Exception {
		Tokenizer tokenizer = new MockTokenizer(new StringReader(input), MockTokenizer.KEYWORD, false);
		ISBNLongifierFilter filter = new ISBNLongifierFilter(tokenizer);
	    assertTokenStreamContents(filter, expected);
	}
}
