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
     * @param message
     * @param cause
     * @param input
     */
    public ParseException(String message, Exception cause, String input) {
        super(message, cause);

        this.input = input;
    }


}
