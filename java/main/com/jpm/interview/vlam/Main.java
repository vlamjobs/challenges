package com.jpm.interview.vlam;

import com.jpm.interview.vlam.data.Fund;
import com.jpm.interview.vlam.util.InputParser;
import com.jpm.interview.vlam.util.Logger;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 *
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
            PortfolioBuilder portfBuilder = new PortfolioBuilder();

            // start reading...
            try ( BufferedReader reader = new BufferedReader(new InputStreamReader(input, "UTF-8")) ) {
                String ln = "";
                while (ln != null) {
                    ln = reader.readLine();
                    if (ln == null) break;

                    ln = ln.trim();
                    if (!ln.isEmpty()) {
                        Fund fund = InputParser.parse(ln);
                        portfBuilder.add(fund);
                    }
                }   // end while...
            }

            System.out.println(portfBuilder.get());

        } catch (Throwable t) {
            Logger.error("error running main() - quit.", t);
        }
    }
}
