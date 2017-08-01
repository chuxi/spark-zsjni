package com.windjammer.zetascale;

import java.io.IOException;

/**
 * Created by king on 17-7-24.
 */
public class ZSNativeObejct {

    public ZSNativeObejct() {
    }

    static {
        try {
//            JniLoader.load("libzsjni.so");
            System.loadLibrary("zsjni");
        } catch (UnsatisfiedLinkError e) {
            try {
                ZSNativeUtils.loadLibraryFromJar("/" + System.mapLibraryName("zsjni"));
            } catch (IOException err) {
                throw new RuntimeException("ZSNativeUtils load library from jar failed.", err);
            }

        }
    }

}
