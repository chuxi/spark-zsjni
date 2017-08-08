package com.windjammer.zetascale;

import com.windjammer.zetascale.exception.ZSContainerException;
import com.windjammer.zetascale.exception.ZSExceptionHandler;
import com.windjammer.zetascale.type.ContainerProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by king on 17-7-24.
 */
public class ZSContainer {
    private long containerId;
    private String containerName;
    private long threadStateHandler;
    private boolean isActive;
    private static Logger logger = LoggerFactory.getLogger(ZSContainer.class);

    public long getContainerId() {
        return this.containerId;
    }

    public String getContainerName() {
        return this.containerName;
    }

    public long getThreadStateHandler() {
        return threadStateHandler;
    }

    public boolean isActive() {
        return isActive;
    }

    private ZSContainer() {}

    public ZSContainer(long threadStateHandler, String containerName, ContainerProperty containerProp)
            throws ZSContainerException {
        this.isActive = true;
        this.threadStateHandler = threadStateHandler;
        this.containerName = containerName;
        this.handleCreate(containerProp);
    }

    private void handleCreate(ContainerProperty containerProp) throws ZSContainerException {
//        logger.warn("create container " + containerName + "'s thread handler: " + threadStateHandler);
        int resultCode = ZSNativeContainer.ZSOpenContainer(threadStateHandler, containerName, 1, containerProp);
        ZSExceptionHandler.handleContainer(resultCode);
        this.containerId = containerProp.getContainerId();

    }

    public static ZSContainer openContainer(long threadStateHandler, String containerName, ContainerProperty containerProp)
            throws ZSContainerException {
//        logger.warn("open container " + containerName + "'s thread handler: " + threadStateHandler);
        int resultCode = ZSNativeContainer.ZSOpenContainer(threadStateHandler, containerName, 2, containerProp);
        ZSExceptionHandler.handleContainer(resultCode);
        ZSContainer container = new ZSContainer();
        container.isActive = true;
        container.threadStateHandler = threadStateHandler;
        container.containerName = containerName;
        container.containerId = containerProp.getContainerId();
        return container;
    }

    public void flushContainer() throws ZSContainerException {
        int resultCode = ZSNativeContainer.ZSFlushContainer(threadStateHandler, this.containerId);
        ZSExceptionHandler.handleContainer(resultCode);
    }

    public void closeContainer() throws ZSContainerException {
        int resultCode = ZSNativeContainer.ZSCloseContainer(threadStateHandler, this.containerId);
        isActive = false;
        ZSExceptionHandler.handleContainer(resultCode);
    }

    public void deleteContainer() throws ZSContainerException {
        int resultCode = ZSNativeContainer.ZSDeleteContainer(threadStateHandler, this.containerId);
        ZSExceptionHandler.handleContainer(resultCode);
    }

    public ContainerProperty getContainerProperties() throws ZSContainerException {
        ContainerProperty containerProp = new ContainerProperty();
        int resultCode = ZSNativeContainer.ZSGetContainerProps(threadStateHandler, this.containerId, containerProp);
        ZSExceptionHandler.handleContainer(resultCode);
        return containerProp;
    }

    public void setContainerProperties(ContainerProperty containerProp) throws ZSContainerException {
        int resultCode = ZSNativeContainer.ZSSetContainerProps(threadStateHandler, this.containerId, containerProp);
        ZSExceptionHandler.handleContainer(resultCode);
    }

    public byte[] read(byte[] key) throws ZSContainerException {
        byte[] data = ZSNativeContainer.ZSReadObject(threadStateHandler, this.containerId, key);
        return data;
    }

    public void write(byte[] key, byte[] data) throws ZSContainerException {
        this.write(key, data, 0);
    }

    public void write(byte[] key, byte[] data, int writeobjectMode) throws ZSContainerException {
        if(key != null && data != null && key.length != 0 && data.length != 0) {
            int resultCode = ZSNativeContainer.ZSWriteObject(threadStateHandler, this.containerId, key, data, writeobjectMode);
            ZSExceptionHandler.handleContainer(resultCode);
        } else {
            throw new ZSContainerException("Key or value can not be null.");
        }
    }

    @Override
    public String toString() {
        return "containerName: " + containerName +
                ", threadStateHandler: " + threadStateHandler +
                ", containerId: " + containerId;
    }
}
