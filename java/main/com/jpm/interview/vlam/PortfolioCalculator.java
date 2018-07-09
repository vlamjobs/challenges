package com.jpm.interview.vlam;


import com.jpm.interview.vlam.data.Fund;
import com.jpm.interview.vlam.util.Logger;

import java.io.OutputStream;
import java.io.PrintStream;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;


/**
 * TODO: may not be the best approach here, the traversal use OutputStream to print the result but perhaps it better
 * TODO: to have weighting or other attributes stored in the Fund object or return some other data structure?
 *
 */
public class PortfolioCalculator {

    private PortfolioBuilder builder;
    private PrintStream output;


    /**
     *
     * @param builder
     * @param output
     */
    public PortfolioCalculator(PortfolioBuilder builder, OutputStream output) {
        this.builder = builder;
        this.output = new PrintStream(output, true);
    }


    /**
     * visit each of the base fund and perform #Consumer.accept()
     *
     * @param consumer
     */
    private void visitBaseFunds(Consumer<Fund> consumer) {
        // this will make sure we "normalized" all nodes and only get the top-level elements...
        List<Fund> funds = this.builder.get();
        Logger.debug(funds.toString());

        funds.forEach(f -> f.depthFirstTraversal(e -> {
            // see if leaf node...
            if (e.getFunds().isEmpty()) {
                consumer.accept(e);
            }
        }));
    }


    /**
     *
     */
    public void calculateWeighting() {
        this.visitBaseFunds(e -> {
            Fund root = e.findRoot();
            double weight = e.getValue() / root.getValue();
            String output = String.format("%s,%s,%.3f", root.getName(), e.getName(), weight);
            this.output.println(output);
        });
    }

    /**
     * TODO: without being able to clarify the requirement, assume weighted return is calculated after Fund structure
     * TODO: is parsed and normalized, then some ending market values (e.g. CLOSE price) are given;
     * TODO: visit the tree and for each base fund (leaf node), check the PnL against the ending market values...
     * TODO:
     * TODO: also - no input parsing of endingMarketValues has been implemented (only test in #PortfolioCalculatorTest)
     */
    public void calculateWeightedReturn(Map<String,Double> endingMarketValues) {
        this.visitBaseFunds(e -> {
            Fund root = e.findRoot();
            double weight = e.getValue() / root.getValue();
            double pnl = 0;

            // see if we can find ending value in the map...
            Double endMV = endingMarketValues.get(e.getName());
            if (endMV != null) {
                pnl = (endMV - e.getValue()) / e.getValue();
                Logger.debug( String.format("endMV: %f, e.getValue(): %f - pnl: %f", endMV, e.getValue(), pnl) );
            }

            String output = String.format("%s,%s,%.3f", root.getName(), e.getName(), (weight * pnl));
            this.output.println(output);
        });
    }


}
