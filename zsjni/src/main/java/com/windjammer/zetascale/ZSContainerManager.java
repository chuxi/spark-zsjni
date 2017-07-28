package com.windjammer.zetascale;

import com.windjammer.zetascale.exception.ZSContainerException;
import com.windjammer.zetascale.type.ContainerProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by king on 17-7-28.
 */
public class ZSContainerManager {
    private static final Logger logger = LoggerFactory.getLogger(ZSContainerManager.class);
    // container register table
    private ConcurrentHashMap<String, ZSContainer> containers = new ConcurrentHashMap<>();

    // avoid to use this
    public ZSContainer getContainer(long threadStateHandler, String containerName) {
        try {
            ContainerProperty property = ContainerProperty.getDefautProperty();
            return getContainer(threadStateHandler, containerName, property);
        } catch (ZSContainerException e) {
            throw new RuntimeException("get default container property failed.", e);
        }
    }

    public ZSContainer getContainer(long threadStateHandler, String containerName,
                                    ContainerProperty property) {
        if (containers.containsKey(containerName)) {
            return containers.get(containerName);
        }
        try {
            ZSContainer container = new ZSContainer(threadStateHandler, containerName, property);
            containers.put(containerName, container);
            return container;
        } catch (ZSContainerException e) {
            throw new RuntimeException("get container failed.", e);
        }
    }

    // for read only mode
    public ZSContainer openContainer(long threadStateHandler, String containerName,
                                     ContainerProperty property) {
        if (containers.containsKey(containerName)) {
            logger.warn("container " + containerName + " is open, close it first.");
            return null;
        }
        try {
            ZSContainer container = ZSContainer.openContainer(
                    threadStateHandler, containerName, property);
            containers.put(containerName, container);
            return container;
        } catch (ZSContainerException e) {
            throw new RuntimeException("open container failed.", e);
        }
    }

    // for read only mode
    public ZSContainer openContainer(long threadStateHandler, String containerName) {
        try {
            ContainerProperty property = ContainerProperty.getDefautProperty();
            return openContainer(threadStateHandler, containerName, property);
        } catch (ZSContainerException e) {
            throw new RuntimeException("open container failed.", e);
        }
    }


    public void closeContainer(String containerName) {
        if (!containers.containsKey(containerName)) {
            logger.warn("container " + containerName + " has closed.");
            return;
        }
        ZSContainer container = containers.get(containerName);
        try {
            container.closeContainer();
            containers.remove(containerName);
        } catch (ZSContainerException e) {
            throw new RuntimeException("close container failed.", e);
        }
    }


}
