package com.windjammer.zetascale.exception;

/**
 * Created by king on 17-7-24.
 */
public class ZSException extends Exception {
    private static final long serialVersionUID = -5408847886731734158L;

    private int resultCode;
    public int getResultCode() {
        return this.resultCode;
    }

    public ZSException(String message, int resultCode) {
        super(message);
        this.resultCode = resultCode;
    }
}
