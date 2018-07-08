package com.jpm.interview.vlam.exception;

import java.io.IOException;

/**
 *
 */
public class ParseException extends IOException {

    private String input;

    /**
     *
     * @param message
     */
    public ParseException(String message, String input) {
        super(message);

        this.input = input;
    }

    /**
     *
     * @return the original input
     */
    public String getInput() {
        return this.input;
    }


}
