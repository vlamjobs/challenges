package com.jpm.interview.vlam.data;


import com.jpm.interview.vlam.PortfolioBuilder;
import com.jpm.interview.vlam.PortfolioCalculator;
import com.jpm.interview.vlam.util.InputParser;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;


public class PortfolioCalculatorTest {

    private static PortfolioBuilder sBuilder;
    private static PortfolioCalculator sCalculator;
    private static ByteArrayOutputStream sOutput;

    @BeforeClass
    public static void setUpClass() {
        sOutput = new ByteArrayOutputStream();
        sBuilder = new PortfolioBuilder();
        sCalculator = new PortfolioCalculator(sBuilder, sOutput);
    }

    @Before
    public void setUp() {
        sBuilder.reset();
        sOutput.reset();
    }


    @Test
    public void testCalculateWeightedReturn() throws Exception {
        String input = "" +
                "A,B,1000\n" +
                "A,C,2000\n" +
                "B,D,500\n" +
                "B,E,250\n" +
                "B,F,250\n" +
                "C,G,1000\n" +
                "C,H,1000\n" +
                "";

        InputStream inputStream = new ByteArrayInputStream(input.getBytes("utf-8"));
        InputParser.parse(sBuilder, inputStream);
        sCalculator.calculateWeighting();

        String e1 = "" +
                "A,D,0.167" + System.lineSeparator() +
                "A,E,0.083" + System.lineSeparator() +
                "A,F,0.083" + System.lineSeparator()  +
                "A,G,0.333" + System.lineSeparator()  +
                "A,H,0.333" + System.lineSeparator()  +
                "";
        String s1 = new String(sOutput.toByteArray(), "utf-8");
        Assert.assertEquals(e1, s1);
        sOutput.reset();

        Map<String,Double> endingMarketValues = new HashMap<>();
        endingMarketValues.put("D", 51000d);
        endingMarketValues.put("H", 80000d);
        sCalculator.calculateWeightedReturn(endingMarketValues);

        String e2 = "" +
                "A,D,0.003" + System.lineSeparator() +
                "A,E,0.000" + System.lineSeparator() +
                "A,F,0.000" + System.lineSeparator()  +
                "A,G,0.000" + System.lineSeparator()  +
                "A,H,-0.067" + System.lineSeparator()  +
                "";
        String s2 = new String(sOutput.toByteArray(), "utf-8");
        Assert.assertEquals(e2, s2);
        sOutput.reset();
    }


}
