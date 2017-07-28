package com.windjammer.zetascale.exception;

/**
 * Created by king on 17-7-24.
 */
public class ZSContainerException extends ZSException {
    private static final long serialVersionUID = -118292162906515838L;

    public ZSContainerException(String string, int resultCode) {
        super(string, resultCode);
    }

    public ZSContainerException(String string) {
        super(string, 0);
    }
}
