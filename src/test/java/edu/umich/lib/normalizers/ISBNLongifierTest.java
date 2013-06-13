/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.umich.lib.normalizers;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author dueberb
 */
public class ISBNLongifierTest {

    public ISBNLongifierTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }



    @Test
    public void testLongify() {
        System.out.println("longify");
        String isbn = "0802088511";
        String expResult = "9780802088512";
        String result = ISBNLongifier.longify(isbn);
        assertEquals(expResult, result);
        assertEquals("9781555705183", ISBNLongifier.longify("1555705189"));
        assertEquals("9781555705183", ISBNLongifier.longify("1-555-7-05-1-8.9"));
        assertEquals("9780671898540", ISBNLongifier.longify("0-671-89854-X"));
        assertEquals("9780671898540", ISBNLongifier.longify("New 067189854-X (online version"));
        try {
            result = ISBNLongifier.longify("155570518");
            fail("Should have failed with bad ISBN");
        } catch (IllegalArgumentException e) {
            assertTrue("Caught short ISBN", true);
        }
    }

    @Test
    public void testLongifyTooLong() {
        System.out.println("longifyTooLong");
        String result;
        try {
            result = ISBNLongifier.longify("9780424058603");
            fail("Should have failed with bad ISBN");
        } catch (IllegalArgumentException e) {
            assertTrue("Caught long ISBN", true);
        }
    }


}