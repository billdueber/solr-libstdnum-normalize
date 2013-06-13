/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.umich.lib.normalizers;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import static org.junit.Assert.*;

/**
 *
 * @author dueberb
 */
public class LCCallNumberNormalizerTest
{

    public LCCallNumberNormalizerTest()
    {
    }

    @BeforeClass
    public static void setUpClass() throws Exception
    {
    }

    @AfterClass
    public static void tearDownClass() throws Exception
    {
    }

    @Before
    public void setUp()
    {
    }

    @After
    public void tearDown()
    {
    }

    /**
     * Test of join method, of class LCCallNumberNormalizer.
     */
    @Test
    public void testJoin()
    {
        System.out.println("join");
        ArrayList<String> s = new ArrayList<String>(2);
        s.add("Bill");
        s.add("Dueber");
        String d = "==";
        String expResult = "Bill==Dueber";
        String result = LCCallNumberNormalizer.join(s, d);
        assertEquals(expResult, result);
    }

    /**
     * Test that normalize returns identity when faced with
     * something it can't understand
     */
    @Test
    public void testNormalize_badinput()
    {
        System.out.println("normalize: bad input");
        String s = "301.33A"; // Deweyish
        assertEquals(s.toUpperCase(), LCCallNumberNormalizer.normalize(s));
        s = "DD volume 3 1997";
        assertEquals(s.toUpperCase(), LCCallNumberNormalizer.normalize(s));
        s = "LC 199.2";
        assertFalse(s.toUpperCase().equals(LCCallNumberNormalizer.normalize(s)));
    }

    /**
     * Test of normalize method, of class LCCallNumberNormalizer.
     */
    @Test
    public void testNormalize_String()
    {
        System.out.println("normalize");

        HashMap<String, String> hm = new HashMap<String, String>();
        hm.put("lc1", "LC@0001");
        hm.put("lc1.1", "LC@00011");
        hm.put("lc1.01", "LC@000101");
        hm.put("a", "A");
        hm.put("AB", "AB");
        hm.put("AB11", "AB@0011");
        hm.put("AB111.22", "AB@011122");
        hm.put("AB1.C4", "AB@0001000C4");
        hm.put("AB111.22 C44 D3", "AB@0111220C440D3");
        hm.put("A1C3", "A@@0001000C3");
        hm.put("See URL for access", "SEE URL FOR ACCESS");

        Set<String> keys = hm.keySet();

        for (String k : keys) {
            String expResult = hm.get(k);
            String result = LCCallNumberNormalizer.normalize(k);
            assertEquals(expResult, result);
        }

    }

    @Test
    public void testNormalizePadded()
    {
        System.out.println("normalize");

        HashMap<String, String> hm = new HashMap<String, String>();
        hm.put("lc1",               "LC@0001000@000@000@000");
        hm.put("lc1.1",             "LC@0001100@000@000@000");
        hm.put("lc1.11",            "LC@0001110@000@000@000");
        hm.put("lc1.01",            "LC@0001010@000@000@000");
        hm.put("a",                 "A");
        hm.put("AB",                "AB");
        hm.put("AB11",              "AB@0011000@000@000@000");
        hm.put("AB111.22",          "AB@0111220@000@000@000");
        hm.put("AB1.C4",            "AB@0001000C400@000@000");
        hm.put("AB111.22 C44 D3",   "AB@0111220C440D300@000");
        hm.put("A1C3",              "A@@0001000C300@000@000");
        hm.put("A1C3 1999",         "A@@0001000C300@000@000  1999");

        Set<String> keys = hm.keySet();

        for (String k : keys) {
            String expResult = hm.get(k);
            String result = LCCallNumberNormalizer.normalizeFullLength(k);
            assertEquals(expResult, result);
        }

    }

    @Test
    public void testRangeEnd()
    {
        System.out.println("normalize");

        HashMap<String, String> hm = new HashMap<String, String>();
        hm.put("lc1", "LC@0001~");
        hm.put("lc1.1", "LC@00011~");
        hm.put("lc1.11", "LC@000111~");
        hm.put("lc1.01", "LC@000101~");
        hm.put("a", "A");
        hm.put("AB", "AB");
        hm.put("AB11", "AB@0011~");
        hm.put("AB111.22", "AB@011122~");
        hm.put("AB1.C4", "AB@0001000C4~");
        hm.put("AB111.22 C44 D3", "AB@0111220C440D3~");
        hm.put("A1C3", "A@@0001000C3~");

        Set<String> keys = hm.keySet();

        for (String k : keys) {
            String expResult = hm.get(k);
            String result = LCCallNumberNormalizer.rangeEnd(k);
            assertEquals(expResult, result);
        }

    }

    /**
     * Test of rangeEnd method, of class LCCallNumberNormalizer.
     */
    @Test
    public void testRangeEndPadded()
    {
        System.out.println("rangeEnd");
        HashMap<String, String> hm = new HashMap<String, String>();
        hm.put("lc1",       "LC@0001999~999~999~999");
        hm.put("lc1.1",     "LC@0001199~999~999~999");
        hm.put("lc1.01",    "LC@0001019~999~999~999");
        hm.put("a",         "A");
        hm.put("AB",        "AB");
        hm.put("AB11",      "AB@0011999~999~999~999");
        hm.put("AB111.22",  "AB@0111229~999~999~999");
        hm.put("AB1.C4",    "AB@0001000C499~999~999");
        hm.put("AB111.22 C44 D3", "AB@0111220C440D399~999");
        hm.put("A1C3",      "A@@0001000C399~999~999");

        Set<String> keys = hm.keySet();

        for (String k : keys) {
            String expResult = hm.get(k);
            String result = LCCallNumberNormalizer.rangeEndPadded(k);
            assertEquals(expResult, result);
        }

    }

    @Test
    public void testEndAfterStart()
    {
        System.out.println("end after start");
        HashMap<String, String> hm = new HashMap<String, String>();
        hm.put("lc 1", "LC@000199~999~999~999");
        hm.put("lc 1.1", "LC@000119~999~999~999");
        hm.put("lc 1.01", "LC@000101~999~999~999");
        hm.put("a1", "A@@000199~999~999~999");
        hm.put("AB11", "AN@001199~999~999~999");
        hm.put("AB111.22", "AB@011122~999~999~999");
        hm.put("AB1.C4", "AB@000100C499~999~999");
        hm.put("AB111.22 C44 D3", "AB@011122C440D399~999");
        hm.put("A1C3", "A@@000100C399~999~999");

        Set<String> keys = hm.keySet();

        for (String k : keys) {
            String start = LCCallNumberNormalizer.normalize(k);
            String end = LCCallNumberNormalizer.rangeEnd(k);
            String endPadded = LCCallNumberNormalizer.rangeEndPadded(k);
            assertTrue(start + " < " + end, start.compareTo(end) < 0);
            assertTrue(start + " < " + endPadded, start.compareTo(endPadded) < 0);

        }

    }

    @Test
    public void testLong()
    {
        System.out.println("toLongInt");
        String z = LCCallNumberNormalizer.toLongInt("ZZZ9999.99Z999Z999Z999");
        String a = LCCallNumberNormalizer.toLongInt("A1");
        Long lz = new Long(z);
        Long la = new Long(a);
        assertTrue(z + " > " + a, lz.compareTo(la) > 0);
        assertTrue("Small enough", lz.compareTo(Long.MAX_VALUE) < 0);
        assertTrue("Big enough", la.compareTo(Long.MIN_VALUE) > 0);
        assertEquals(LCCallNumberNormalizer.MINNUM.toString(), LCCallNumberNormalizer.toLongInt("FILM D 113"));
    }

    @Test
    public void testOrdering()
    {
        System.out.println("Ordering");
        String[] test = {
            "a 0",
            "a 1 1923",
            "a 8 f166",
            "a19 f96",
            "a19f99g15",
            "a242 83 i65",
            "a610 h18",
            "a610.5 c75 m5 1910",
            "a610.8 e8f 0",
            "a610.9 c27pr 0",
            "a610.9 c38tr 0",
            "a610.9 f16",
            "a610.9 g38n 0",
            "a610.9 m96",
            "a614.2 f36",
            "a615.1 n84",
            "a615.11 f23",
            "a615.3 s68pl 0",
            "a615.7 o5l 0",
            "a618.2 l58n 0",
            "a618.2 r7g 0",
            "a820.3 b 0",
            "aa 0",
            "aa39",
            "aa102 ottawapt 1-final 0",
            "ab 0",
            "abc 0",
            "ac 1 a52",
            "ac 1 a671 2000",
            "ac 1 a926 r 0",
            "ac 1 b26",
            "ac 1 c2",
            "ac 1 c212",
            "ac 1 e45",
            "ac 1 f142",
            "ac 1 g78",
            "zzz19f99g15",
            "zzz 1995 f99g15 d11 1990",
            "zzz 1995.876 f99g15 d11 1990"
        };
        
        for (Integer j = 0; j < test.length -1; j++) {
            String s1 = test[j];
            String s2 = test[j+1];
            String n1 = LCCallNumberNormalizer.normalize(s1);
            String n2 = LCCallNumberNormalizer.normalize(s2); 
            String f1 = LCCallNumberNormalizer.normalizeFullLength(s1);
            String f2 = LCCallNumberNormalizer.normalizeFullLength(s2);
            Long   l1 = new Long(LCCallNumberNormalizer.toLongInt(s1));
            Long   l2 = new Long(LCCallNumberNormalizer.toLongInt(s2));
            assertTrue(n1 + " < " + n2, n1.compareTo(n2) < 0);
            assertTrue(f1 + " < " + f2, f1.compareTo(f2) < 0);
            assertTrue(s1  + "(long) < " + s2 + " (long)", l1.compareTo(l2) < 0);
        }
        
    }
}
