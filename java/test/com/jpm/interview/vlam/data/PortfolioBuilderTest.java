package com.jpm.interview.vlam.data;


import com.jpm.interview.vlam.PortfolioBuilder;
import com.jpm.interview.vlam.util.InputParser;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class PortfolioBuilderTest {

    private static PortfolioBuilder sBuilder;

    @BeforeClass
    public static void setUpClass() {
        sBuilder = new PortfolioBuilder();
    }

    @Before
    public void setUp() {
        sBuilder.reset();
    }


    private void testInput1(List<Fund> portfolio) {
        // we only have one top-level element...
        Assert.assertEquals(1, portfolio.size());

        // and the top-level is A...
        Fund fundA = portfolio.get(0);
        Assert.assertEquals("A", fundA.getName());
        Assert.assertEquals(300000d, fundA.getValue(), 0);

        // check first level children (B and C)
        List<Fund> childrenA = fundA.getFunds();
        Assert.assertEquals(2, childrenA.size());
        // we need to sort because the builder maintains order as it parses...
        Collections.sort(childrenA, Comparator.comparing(Fund::getName));
        Fund fundB = childrenA.get(0);
        Fund fundC = childrenA.get(1);
        Assert.assertEquals("B", fundB.getName());
        Assert.assertEquals("C", fundC.getName());
        Assert.assertEquals(100000d, fundB.getValue(), 0);
        Assert.assertEquals(200000d, fundC.getValue(), 0);

        // children of B...
        List<Fund> childrenB = fundB.getFunds();
        Collections.sort(childrenB, Comparator.comparing(Fund::getName));
        Assert.assertEquals(3, childrenB.size());
        Fund fundD = childrenB.get(0);
        Fund fundE = childrenB.get(1);
        Fund fundF = childrenB.get(2);
        Assert.assertEquals("D", fundD.getName());
        Assert.assertEquals("E", fundE.getName());
        Assert.assertEquals("F", fundF.getName());
        Assert.assertEquals(0, fundD.getFunds().size());
        Assert.assertEquals(0, fundE.getFunds().size());
        Assert.assertEquals(0, fundF.getFunds().size());
        Assert.assertEquals(50000d, fundD.getValue(), 0);
        Assert.assertEquals(25000d, fundE.getValue(), 0);
        Assert.assertEquals(25000d, fundF.getValue(), 0);

        // children of C...
        List<Fund> childrenC = fundC.getFunds();
        Collections.sort(childrenC, Comparator.comparing(Fund::getName));
        Assert.assertEquals(2, childrenC.size());
        Fund fundG = childrenC.get(0);
        Fund fundH = childrenC.get(1);
        Assert.assertEquals("G", fundG.getName());
        Assert.assertEquals("H", fundH.getName());
        Assert.assertEquals(0, fundG.getFunds().size());
        Assert.assertEquals(0, fundH.getFunds().size());
        Assert.assertEquals(100000d, fundG.getValue(), 0);
        Assert.assertEquals(100000d, fundH.getValue(), 0);
    }

    private void testInput2(List<Fund> portfolio) {
        Collections.sort(portfolio, Comparator.comparing(Fund::getName));
        // we have two top-level element - A and C
        Assert.assertEquals(2, portfolio.size());

        Fund fundA = portfolio.get(0);
        Fund fundC = portfolio.get(1);
        Assert.assertEquals(130000d, fundA.getValue(), 0);
        Assert.assertEquals(40000d, fundC.getValue(), 0);

        // children of A...
        List<Fund> childrenA = fundA.getFunds();
        Collections.sort(childrenA, Comparator.comparing(Fund::getName));
        Assert.assertEquals(1, childrenA.size());
        Assert.assertEquals(130000d, fundA.getValue(), 0);
        Fund fundB = childrenA.get(0);

        // children of B...
        List<Fund> childrenB = fundB.getFunds();
        Collections.sort(childrenB, Comparator.comparing(Fund::getName));
        Assert.assertEquals(2, childrenB.size());
        Assert.assertEquals(130000d, fundB.getValue(), 0);
        Fund fundD = childrenB.get(0);
        Fund fundE = childrenB.get(1);
        Assert.assertEquals("D", fundD.getName());
        Assert.assertEquals("E", fundE.getName());
        Assert.assertEquals(0, fundD.getFunds().size());
        Assert.assertEquals(0, fundE.getFunds().size());
        Assert.assertEquals(50000d, fundD.getValue(), 0);
        Assert.assertEquals(80000d, fundE.getValue(), 0);

        // children of C...
        List<Fund> childrenC = fundC.getFunds();
        Collections.sort(childrenC, Comparator.comparing(Fund::getName));
        Assert.assertEquals(2, childrenC.size());
        Assert.assertEquals(40000d, fundC.getValue(), 0);
        Fund fundG = childrenC.get(0);
        Fund fundH = childrenC.get(1);
        Assert.assertEquals("G", fundG.getName());
        Assert.assertEquals("H", fundH.getName());
        Assert.assertEquals(0, fundG.getFunds().size());
        Assert.assertEquals(0, fundH.getFunds().size());
        Assert.assertEquals(20000d, fundG.getValue(), 0);
        Assert.assertEquals(20000d, fundH.getValue(), 0);
    }


    @Test
    public void testInput1_1() throws Exception {
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
        this.testInput1(sBuilder.get());
    }

    @Test
    public void testInput1_2() throws Exception {
        // same input as testInput1_1 above but re-order the lines...
        String input = "" +
                "B,E,250\n" +
                "C,G,1000\n" +
                "A,B,1000\n" +
                "B,D,500\n" +
                "B,F,250\n" +
                "C,H,1000\n" +
                "A,C,2000\n" +
                "";
        InputStream inputStream = new ByteArrayInputStream(input.getBytes("utf-8"));
        InputParser.parse(sBuilder, inputStream);
        this.testInput1(sBuilder.get());
    }

    @Test
    public void testInput1_3() throws Exception {
        // same input as testInput1_1 above but only the leaf node values are correct (check if normalization works)
        String input = "" +
                "A,B,0\n" +
                "A,C,0\n" +
                "B,D,500\n" +
                "B,E,250\n" +
                "B,F,250\n" +
                "C,G,1000\n" +
                "C,H,1000\n" +
                "";
        InputStream inputStream = new ByteArrayInputStream(input.getBytes("utf-8"));
        InputParser.parse(sBuilder, inputStream);
        this.testInput1(sBuilder.get());
    }

    @Test
    public void testInput2_1() throws Exception {
        // more than one top-level elements...
        String input = "" +
                "A,B,1000\n" +
                "B,D,500\n" +
                "B,E,800\n" +
                "C,G,200\n" +
                "C,H,200\n" +
                "";
        InputStream inputStream = new ByteArrayInputStream(input.getBytes("utf-8"));
        InputParser.parse(sBuilder, inputStream);
        this.testInput2(sBuilder.get());

        /*
        PortfolioCalculator calculator = new PortfolioCalculator(sBuilder);
        calculator.calculateWeighting();
        */
    }


}
