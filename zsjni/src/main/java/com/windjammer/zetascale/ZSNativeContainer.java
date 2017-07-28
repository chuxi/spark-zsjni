package com.windjammer.zetascale;

import com.windjammer.zetascale.exception.ZSContainerException;
import com.windjammer.zetascale.type.ContainerProperty;

import java.util.Comparator;

/**
 * Created by king on 17-7-24.
 */
public class ZSNativeContainer {
    private ZSNativeContainer() {}

    public static native int ZSLoadCntrPropDefaults(ContainerProperty containerProp);

    public static native int ZSOpenContainer(long thread_state, String cname, int flag, ContainerProperty containerProp);

    // public static native int ZSOpenContainerSpecial(long var0, String var2, int var3, ContainerProperty var4, Comparator var5);

    public static native int ZSCloseContainer(long thread_state, long cguid);

    public static native int ZSDeleteContainer(long thread_state, long cguid);

    public static native long[] ZSGetContainers(long thread_state);

    public static native int ZSFlushContainer(long thread_state, long cguid);

    public static native int ZSGetContainerProps(long thread_state, long cguid, ContainerProperty containerProp);

    public static native int ZSSetContainerProps(long thread_state, long cguid, ContainerProperty containerProp);

    // public static native int ZSGetContainerStats(long var0, long var2, ZSStatistics var4);

    public static native int ZSWriteObject(long thread_state, long cguid, byte[] key, byte[] data, int flags);

    // public static native int ZSWriteObjectExpiry(long var0, byte[] var2, byte[] var3, long var4, int var6, int var7);

    public static native byte[] ZSReadObject(long thread_state, long cguid, byte[] key);

    // public static native byte[] ZSReadObjectExpiry(long var0, byte[] var2, long var3) throws ZSContainerException;

    public static native int ZSDeleteObject(long thread_state, long cguid, byte[] key);

    public static native int ZSFlushObject(long thread_state, long cguid, byte[] key);

    public static native int ZSFlushCache(long thread_state);

    // public static native int ZSMPut(long var0, long var2, ZSMData[] var4, int var5);
}
