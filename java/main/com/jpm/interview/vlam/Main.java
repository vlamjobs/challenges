package com.jpm.interview.vlam;


import com.jpm.interview.vlam.util.InputParser;
import com.jpm.interview.vlam.util.Logger;

import java.io.FileInputStream;
import java.io.InputStream;


/**
 * Main class to portfolio calculation
 */
public class Main {
    public static void main(String argv[]) {
        try
        {
            // if there is no argument, read from System.in, otherwise from the file specified by the argument...
            InputStream input = System.in;
            if (argv.length > 1) {
                System.err.println("usage: main <optional input file>");
                System.exit(1);
            } else if (argv.length == 1) {
                input = new FileInputStream(argv[0]);
            }

            // builder and calculator...
            PortfolioBuilder builder = new PortfolioBuilder();
            PortfolioCalculator calculator = new PortfolioCalculator(builder);

            InputParser.parse(builder, input);
            calculator.calculateWeighting();

        } catch (Throwable t) {
            Logger.error("error running main() - quit.", t);
        }
    }
}
