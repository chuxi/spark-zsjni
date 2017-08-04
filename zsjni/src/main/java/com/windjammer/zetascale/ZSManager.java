package com.windjammer.zetascale;

import com.windjammer.zetascale.exception.ZSException;
import com.windjammer.zetascale.exception.ZSExceptionHandler;
import com.windjammer.zetascale.exception.ZSThreadException;
import com.windjammer.zetascale.type.ContainerProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.function.Supplier;

/**
 * Created by king on 17-7-28.
 * we do not need to pass zsState to ZSManager
 */
public class ZSManager {
    private static Logger logger = LoggerFactory.getLogger(ZSManager.class);
    private static ZSManager instance = null;
    private static final ThreadLocal<ZSThreadState> threadStateLocal = ThreadLocal.withInitial(new Supplier<ZSThreadState>() {
        @Override
        public ZSThreadState get() {
            return new ZSThreadState();
        }
    });
    private static ZSState zsState = null;
    private static ZSContainerManager containerManager = new ZSContainerManager();

    private ZSManager() {}

    public static ZSManager getInstance() {
        if (instance == null) {
            instance = new ZSManager();
        }
        return instance;
    }

    ///////////////////////////////////// Zetascale Instance /////////////////////////////////////

    // initial zs_state, load properties from file in classpath
    public void init(String propFileName) throws ZSException {
        init(propFileName, true);
    }

    public void init(String propFileName, boolean format) throws ZSException {
        this.loadProperties(getPropFilePath(propFileName));
        if (!format) {
            String flashFilename = getProperty("ZS_FLASH_FILENAME", null);
            File flashFile = new File(flashFilename);
            if (flashFile.exists() && flashFile.isFile()) {
                // set the format property as false
                setProperty("ZS_REFORMAT", "0");
            }
        }
        zsState = new ZSState();
        int resultCode = ZSNative.ZSInit(zsState);
        ZSExceptionHandler.handleClient(resultCode);
    }

    public void shutdown() throws ZSException {
        int resultCode = ZSNative.ZSShutdown();
        ZSExceptionHandler.handleClient(resultCode);
    }

    public void setProperty(String propertyName, String value) throws ZSException {
        ZSNative.ZSSetProperty(propertyName, value);
    }

    public String getProperty(String propertyName) {
        return getProperty(propertyName, null);
    }

    public String getProperty(String propertyName, String defaultValue) {
        return ZSNative.ZSGetProperty(propertyName, defaultValue);
    }

    private void loadProperties(String propertiesFile) throws ZSException {
        int resultCode = ZSNative.ZSLoadProperties(propertiesFile);
        ZSExceptionHandler.handleClient(resultCode);
    }

    private String getPropFilePath(String propFileName) {
        String filePath = this.getClass().getResource("/" + propFileName).getPath();
        logger.debug("property file: " + propFileName + ", absolute path: " + filePath);
        return filePath;
    }

    /////////////////////////// Zetascale Thread Instance /////////////////////////////
    // all these functions are used in threads

    public void initPerThreadState() throws ZSThreadException {
        ZSThreadState threadState = threadStateLocal.get();
        if (!threadState.isInitialized()) {
            int resultCode = ZSNativeThread.ZSInitPerThreadState(threadState);
            logger.info("init threadStateHandler: " + threadState.getThreadStateHandler());
            ZSExceptionHandler.handleThread(resultCode);
            threadStateLocal.set(threadState);
        }
    }

    public void releasePerThreadState() throws ZSThreadException {
        ZSThreadState threadState = threadStateLocal.get();
        if (threadState.isInitialized()) {
            int resultCode = ZSNativeThread.ZSReleasePerThreadState(threadState.getThreadStateHandler());
            logger.info("release threadStateHandler: " + threadState.getThreadStateHandler());
            ZSExceptionHandler.handleThread(resultCode);
            threadStateLocal.remove();
        }
    }

    /////////////////////////// Containers //////////////////////////////////
    public ZSContainer getContainer(String containerName) {
        long threadStateHandler = currentThreadStateHandler();
        return containerManager.getContainer(threadStateHandler, containerName);
    }

    public ZSContainer getContainer(String containerName, ContainerProperty containerProp) {
        long threadStateHandler = currentThreadStateHandler();
        return containerManager.getContainer(threadStateHandler, containerName, containerProp);
    }

    public ZSContainer openContainer(String containerName) {
        long threadStateHandler = currentThreadStateHandler();
        return containerManager.openContainer(threadStateHandler, containerName);
    }

    public ZSContainer openContainer(String containerName, ContainerProperty containerProp) {
        long threadStateHandler = currentThreadStateHandler();
        return containerManager.openContainer(threadStateHandler, containerName, containerProp);
    }

    public void closeContainer(String containerName) {
        containerManager.closeContainer(containerName);
    }

    public boolean containerExists(String containerName) {
        return containerManager.containerExists(containerName);
    }

    private long currentThreadStateHandler() {
        return threadStateLocal.get().getThreadStateHandler();
    }
}
