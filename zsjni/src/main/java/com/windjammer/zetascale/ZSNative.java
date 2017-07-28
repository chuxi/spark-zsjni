package com.windjammer.zetascale;

/**
 * Created by king on 17-7-24.
 */
public class ZSNative extends ZSNativeObejct {

    private ZSNative() {}

    public static native int ZSLoadProperties(String prop_filename);

    public static native void ZSSetProperty(String property, String value);

    public static native int ZSInit(ZSState state);

    public static native int ZSShutdown();

    public static native String ZSGetProperty(String property, String defaultValue);
}
