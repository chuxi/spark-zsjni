package com.windjammer.zetascale;

import com.github.fommil.jni.JniLoader;

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
        } catch (Exception e) {

        }
    }

}
