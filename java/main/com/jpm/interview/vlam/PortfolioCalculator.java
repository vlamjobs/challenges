package com.jpm.interview.vlam;


import com.jpm.interview.vlam.data.Fund;
import com.jpm.interview.vlam.util.Logger;

import java.util.List;


public class PortfolioCalculator {
    private PortfolioBuilder builder;

    /**
     *
     * @param builder
     */
    public PortfolioCalculator(PortfolioBuilder builder) {
        this.builder = builder;
    }


    /**
     * TODO: not the best approach here, the traversal use System.out.println() but perhaps it better to have weighting stored in the Fund object...
     *
     */
    public void calculateWeighting() {
        List<Fund> funds = this.builder.get();      // this will make sure we "normalized" all nodes and only get the top-level elements...
        Logger.debug(funds.toString());

        funds.forEach(f -> {
            f.depthFirstTraversal(e -> {
                // see if leaf node...
                if (e.getFunds().isEmpty()) {
                    Fund root = e.findRoot();
                    double weight = e.getValue() / root.getValue();
                    String output = String.format("%s,%s,%.3f", root.getName(), e.getName(), weight);
                    System.out.println(output);
                }
            });
        });
    }

    public void calculateX() {

    }


}
