package com.jpm.interview.vlam.data;


import com.jpm.interview.vlam.exception.ParseException;
import com.jpm.interview.vlam.util.InputParser;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;


public class InputParserTest {

    @Test(expected = ParseException.class)
    public void testInvalidLine1() throws Exception {
        InputParser.parse("A,B");           // not enough arguments...
    }

    @Test(expected = ParseException.class)
    public void testInvalidLine2() throws Exception {
        InputParser.parse("A,B,C,D");       // too many arguments...
    }

    @Test(expected = ParseException.class)
    public void testInvalidLine3() throws Exception {
        InputParser.parse("A,B,C");         // number format...
    }

    @Test(expected = ParseException.class)
    public void testInvalidLine4() throws Exception {
        InputParser.parse("garbage...");    // something completely wrong...
    }

    @Test
    public void testNormalInput1() throws Exception {
        Fund fund = InputParser.parse("A,B,1000");
        Assert.assertEquals("A", fund.getName());
        Assert.assertEquals(100000d, fund.getValue(), 0d);      // Fund use cents...

        List<Fund> children = fund.getFunds();
        Assert.assertEquals(1, children.size());
        Assert.assertEquals("B", children.get(0).getName());
    }

}
