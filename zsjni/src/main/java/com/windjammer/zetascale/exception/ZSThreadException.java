package com.windjammer.zetascale.exception;

/**
 * Created by king on 17-7-24.
 */
public class ZSThreadException extends ZSException {
    private static final long serialVersionUID = 7850708146858474586L;

    public ZSThreadException(String message, int resultCode) {
        super(message, resultCode);
    }
}
