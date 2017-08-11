package com.windjammer.zetascale.type;

import com.windjammer.zetascale.ZSNativeContainer;
import com.windjammer.zetascale.exception.ZSContainerException;
import com.windjammer.zetascale.exception.ZSExceptionHandler;

import java.util.Map;

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

    private int flags;

    private static final String SIZE = "size_gb";
    private static final String FIFO = "fifo";
    private static final String EVICTING = "evicting";
    private static final String PERSISTENT = "persistent";
    private static final String ASYNC_WRITES = "async_writes";
    private static final String WRITETHRU = "writethru";
    private static final String DURABILITY_LEVEL = "durability_level";
    private static final String NUM_SHARDS = "num_shards";
    private static final String FLASH_ONLY = "flash_only";
    private static final String CACHE_ONLY = "cache_only";
    private static final String COMPRESSION = "compression";
    private static final String FLAGS = "flags";

    public static ContainerProperty getDefautProperty() throws ZSContainerException {
        ContainerProperty property = new ContainerProperty();
        int resultCode = ZSNativeContainer.ZSLoadCntrPropDefaults(property);
        ZSExceptionHandler.handleContainer(resultCode);
        return property;
    }

    public static ContainerProperty getProperty(Map<String, String> props) throws ZSContainerException {
        ContainerProperty cProperty = ContainerProperty.getDefautProperty();

        if (!(props.isEmpty()))
        {
            if (props.get(SIZE) != null)
            {
                cProperty.setSize(Integer.parseInt(props.get(SIZE)) * 1024 * 1024);
            }
            if (props.get(FIFO) != null)
            {
                cProperty.setFifoMode(Boolean.parseBoolean(props.get(FIFO)));
            }
            if (props.get(EVICTING) != null)
            {
                cProperty.setEvicting(Boolean.parseBoolean(props.get(EVICTING)));
            }
            if (props.get(PERSISTENT) != null)
            {
                cProperty.setPersistent(Boolean.parseBoolean(props.get(PERSISTENT)));
            }
            if (props.get(ASYNC_WRITES) != null)
            {
                cProperty.setAsyncWrite(Boolean.parseBoolean(props.get(ASYNC_WRITES)));
            }
            if (props.get(WRITETHRU) != null)
            {
                cProperty.setWritethru(Boolean.parseBoolean(props.get(WRITETHRU)));
            }
            if (props.get(DURABILITY_LEVEL) != null)
            {
                cProperty.setDurabilityLevel(Integer.parseInt(props.get(DURABILITY_LEVEL)));
            }
            if (props.get(NUM_SHARDS) != null)
            {
                cProperty.setShardNumber(Integer.parseInt(props.get(NUM_SHARDS)));
            }
            if (props.get(FLASH_ONLY) != null)
            {
                cProperty.setFlashOnly(Boolean.parseBoolean(props.get(FLASH_ONLY)));
            }
            if (props.get(CACHE_ONLY) != null)
            {
                cProperty.setCacheOnly(Boolean.parseBoolean(props.get(CACHE_ONLY)));
            }
            if (props.get(COMPRESSION) != null)
            {
                cProperty.setCompression(Boolean.parseBoolean(props.get(COMPRESSION)));
            }
            if (props.get(FLAGS) != null)
            {
                cProperty.setFlags(Integer.parseInt(props.get(FLAGS)));
            }
        }
        return cProperty;
    }

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
        flags = 0;
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

    public String getContainerName() {
        return this.containerName;
    }

    public void setContainerName(String containerName) {
        this.containerName = containerName;
    }

    public void setCompression(boolean compression) {
        this.compression = compression;
    }

    public int getFlags() {
        return flags;
    }

    public void setFlags(int flags) {
        this.flags = flags;
    }

    public String toString() {
        return this.size + "-size\n" + this.durabilityLevel + "-durabilityLevel\n" + this.fifoMode + "-fifo\n" + this.persistent + "-persisitent\n" + this.shardNumber + "-shardNumber\n" + this.evicting + "-evict\n" + this.writethru + "-writethru\n" + this.flashOnly + "-flashOnly\n" + this.cacheOnly + "-cacheOnly\n";
    }
}
