package com.windjammer.zetascale;

/**
 * Created by king on 17-7-28.
 */
public class ZSThreadState {
    private long threadStateHandler = 0;      // handler

    public long getThreadStateHandler() {
        return threadStateHandler;
    }

    public void setThreadStateHandler(long threadStateHandler) {
        this.threadStateHandler = threadStateHandler;
    }

    public boolean isInitialized() {
        return threadStateHandler != 0;
    }
}
