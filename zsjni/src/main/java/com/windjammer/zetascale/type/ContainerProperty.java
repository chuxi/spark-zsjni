package com.windjammer.zetascale.type;

import com.windjammer.zetascale.ZSNativeContainer;
import com.windjammer.zetascale.exception.ZSContainerException;
import com.windjammer.zetascale.exception.ZSExceptionHandler;

/**
 * Created by king on 17-7-24.
 */
public class ContainerProperty {
    // cguid
    private long containerId;
    private String containerName;

    // property
    private long size;
    private boolean fifoMode;
    private boolean persistent;
    private boolean evicting;
    private boolean writethru;
    private boolean asyncWrite;
    private int durabilityLevel;
    private long containerNumber;
    private int shardNumber;

    private boolean flashOnly;
    private boolean cacheOnly;
    private boolean compression;

    public ContainerProperty() {
        size = 2 * 1024 * 1024;
        fifoMode = false;
        persistent = true;
        evicting = false;
        writethru = true;
        asyncWrite = false;
        durabilityLevel = 1;
        shardNumber = 1;
        flashOnly = false;
        cacheOnly = false;
        compression = false;
    }

    public boolean getFlashOnly() {
        return this.flashOnly;
    }

    public void setFlashOnly(boolean flashOnly) {
        this.flashOnly = flashOnly;
    }

    public boolean getCacheOnly() {
        return this.cacheOnly;
    }

    public void setCacheOnly(boolean cacheOnly) {
        this.cacheOnly = cacheOnly;
    }

    public long getSize() {
        return this.size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public boolean getFifoMode() {
        return this.fifoMode;
    }

    public void setFifoMode(boolean fifoMode) {
        this.fifoMode = fifoMode;
    }

    public boolean getPersistent() {
        return this.persistent;
    }

    public void setPersistent(boolean persistent) {
        this.persistent = persistent;
    }

    public boolean getEvicting() {
        return this.evicting;
    }

    public void setEvicting(boolean evicting) {
        this.evicting = evicting;
    }

    public boolean getWritethru() {
        return this.writethru;
    }

    public void setWritethru(boolean writethru) {
        this.writethru = writethru;
    }

    public int getDurabilityLevel() {
        return this.durabilityLevel;
    }

    public void setDurabilityLevel(int durabilityLevel) {
        this.durabilityLevel = durabilityLevel;
    }

    public long getContainerId() {
        return this.containerId;
    }

//    public long getContainerNumber() {
//        return this.containerNumber;
//    }

    public int getShardNumber() {
        return this.shardNumber;
    }

    public void setShardNumber(int shardNumber) {
        this.shardNumber = shardNumber;
    }

    public boolean getAsyncWrite() {
        return this.asyncWrite;
    }

    public void setAsyncWrite(boolean asyncWrite) {
        this.asyncWrite = asyncWrite;
    }

    public static ContainerProperty getDefautProperty() throws ZSContainerException {
        ContainerProperty property = new ContainerProperty();
        int resultCode = ZSNativeContainer.ZSLoadCntrPropDefaults(property);
        ZSExceptionHandler.handleContainer(resultCode);
        return property;
    }

    public String getContainerName() {
        return this.containerName;
    }

    public void setContainerName(String containerName) {
        this.containerName = containerName;
    }

    public void setCompression(boolean compression) {
        this.compression = compression;
    }

    public String toString() {
        return this.size + "-size\n" + this.durabilityLevel + "-durabilityLevel\n" + this.fifoMode + "-fifo\n" + this.persistent + "-persisitent\n" + this.shardNumber + "-shardNumber\n" + this.evicting + "-evict\n" + this.writethru + "-writethru\n" + this.flashOnly + "-flashOnly\n" + this.cacheOnly + "-cacheOnly\n";
    }
}
