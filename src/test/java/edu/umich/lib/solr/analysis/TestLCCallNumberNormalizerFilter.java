package edu.umich.lib.solr.analysis;

import java.io.StringReader;

import org.apache.lucene.analysis.BaseTokenStreamTestCase;
import org.apache.lucene.analysis.MockTokenizer;
import org.apache.lucene.analysis.Tokenizer;

public class TestLCCallNumberNormalizerFilter extends BaseTokenStreamTestCase {
	
	public void testLCCallNumberNormalize() throws Exception {
		assertNormalizesTo("lc1", new String[] {"LC@0001"});
		assertNormalizesTo("lc1.1", new String[] {"LC@00011"});
		assertNormalizesTo("lc1.01", new String[] {"LC@000101"});
		assertNormalizesTo("a", new String[] {"A"});
		assertNormalizesTo("AB", new String[] {"AB"});
		assertNormalizesTo("AB11", new String[] {"AB@0011"});
		assertNormalizesTo("AB111.22", new String[] {"AB@011122"});
		assertNormalizesTo("AB1.C4", new String[] {"AB@0001000C4"});
		assertNormalizesTo("AB111.22 C44 D3", new String[] {"AB@0111220C440D3"});
		assertNormalizesTo("A1C3", new String[] {"A@@0001000C3"});
	}
	
	static void assertNormalizesTo(String input, String[] expected) throws Exception {
		Tokenizer tokenizer = new MockTokenizer(new StringReader(input), MockTokenizer.KEYWORD, false);
		LCCallNumberNormalizerFilter filter = new LCCallNumberNormalizerFilter(tokenizer);
	    assertTokenStreamContents(filter, expected);
	}
	
}
