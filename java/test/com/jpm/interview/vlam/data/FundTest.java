package com.jpm.interview.vlam.data;


import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;


public class FundTest {

    private static List<Fund> sFunds;

    @BeforeClass
    public static void setUpClass() {
        sFunds = new ArrayList<>();

        // S0..S7...
        for (int i=0; i<8; i++) {
            double value = 10000;
            sFunds.add( new Fund("S" + i, value + (i*1000)) );
        }

        // duplicate some of the above with different values...
        Fund fundS1Dup = new Fund("S1", 21000);
        Fund fundS4Dup = new Fund("S4", 24000);
        sFunds.add(fundS1Dup);
        sFunds.add(fundS4Dup);
    }

    @Test
    public void testDummy() {
        Fund fundA = new Fund("A", 0);

        fundA.addFund(sFunds.get(2));
        fundA.addFund(sFunds.get(3));

        Assert.assertEquals((12000+13000), fundA.getValue(), 0);

        fundA.depthFirstTraversal(System.out::println);
    }
/*
    @Test
    public void testFundAggregateValue1() {
        Fund fundA = new Fund("A");

        fundA.addFund(sFunds.get(2));
        fundA.addFund(sFunds.get(3));

        Assert.assertEquals((12000+13000), fundA.getValue(), 0);
    }

    @Test
    public void testFundAggregateValue2() {
        Fund fundA = new Fund("A");
        Fund fundA1 = new Fund("A1");
        Fund fundA2 = new Fund("A2");
        fundA.addFund(fundA1);
        fundA.addFund(fundA2);

        fundA1.addFund(sFunds.get(2));
        fundA1.addFund(sFunds.get(3));

        fundA2.addFund(sFunds.get(0));

        Assert.assertEquals((12000+13000), fundA.getValue(), 0);
    }
*/

}
