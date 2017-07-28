package com.windjammer.zetascale.exception;

import com.windjammer.zetascale.ZSNativeObejct;

import static com.windjammer.zetascale.ZSNativeUtils.ZSErrorString;

/**
 * Created by king on 17-7-24.
 */
public class ZSExceptionHandler extends ZSNativeObejct {
    private ZSExceptionHandler() {}

    public static void handleContainer(int resultCode) throws ZSContainerException {
        if (resultCode != 1) {
            String message = ZSErrorString(resultCode);
            throw new ZSContainerException(message, resultCode);
        }
    }

    public static void handleThread(int resultCode) throws ZSThreadException {
        if (resultCode != 1) {
            String message = ZSErrorString(resultCode);
            throw new ZSThreadException(message, resultCode);
        }
    }

    public static void handleClient(int resultCode) throws ZSException {
        if (resultCode != 1) {
            String message = ZSErrorString(resultCode);
            throw new ZSException(message, resultCode);
        }
    }
}
