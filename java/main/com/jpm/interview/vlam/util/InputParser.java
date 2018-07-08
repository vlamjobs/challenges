package com.jpm.interview.vlam.util;

import com.jpm.interview.vlam.data.Fund;
import com.jpm.interview.vlam.exception.ParseException;

/**
 *
 */
public class InputParser {

    /**
     *
     * @param line input string format is "Parent,Child,MV (of child)"
     * @throws ParseException throw if input format not correct
     */
    public static Fund parse(String line) throws ParseException {
        String args[] = line.split("\\s*,\\s*");
        if (args.length != 3)
            throw new ParseException("number of arguments must be 3", line);

        try {
            double value = Double.parseDouble(args[2]);
            Fund fund = new Fund(args[1], value * 100);       // Fund deals with cents...
            Fund parent = new Fund(args[0]);
            parent.addFund(fund);
            return parent;
        } catch(NumberFormatException nfe) {
            throw new ParseException("invalid format for Market Value", line);
        }
    }


}
