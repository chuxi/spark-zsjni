package com.windjammer.zetascale;

import com.windjammer.zetascale.exception.ZSContainerException;
import com.windjammer.zetascale.type.ContainerProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by king on 17-7-28.
 */
public class ZSContainerManager {
    private static final Logger logger = LoggerFactory.getLogger(ZSContainerManager.class);
    // container register table
    private ConcurrentHashMap<String, ZSContainer> containers = new ConcurrentHashMap<>();
    private Set<String> existsContainers = new HashSet<>();

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
        if (existsContainers.contains(containerName)) {
            return openContainer(threadStateHandler, containerName, property);
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
        try {
            ZSContainer container = ZSContainer.openContainer(
                    threadStateHandler, containerName, property);
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
        ZSContainer container = containers.get(containerName);
        try {
            container.closeContainer();
            existsContainers.add(containerName);
        } catch (ZSContainerException e) {
            throw new RuntimeException("close container failed.", e);
        }
    }

    public boolean containerExists(String containerName) {
        return existsContainers.contains(containerName);
    }


}
