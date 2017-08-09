package com.windjammer.zetascale;

import com.windjammer.zetascale.exception.ZSContainerException;
import com.windjammer.zetascale.exception.ZSException;
import com.windjammer.zetascale.exception.ZSThreadException;
import org.junit.*;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by king on 17-7-29.
 */
public class ZSManagerTest {
    private int N = 4;
    private ExecutorService service = Executors.newFixedThreadPool(N);
    private static ZSManager manager = ZSManager.getInstance();

    @BeforeClass
    public static void beforeClass() {
        try {
            manager.init("zs.prop");
            manager.initPerThreadState();
        } catch (ZSException e) {
            throw new RuntimeException("initial ZSManager failed.", e);
        }
    }

    @Test
    public void singleThreadOperations() throws Exception {
        String containerName = "container-test";
        ZSContainer container = manager.getContainer(containerName);
        byte[] key = "key-test".getBytes();
        String data = "data-test";
        container.write(key, data.getBytes());
        String result = new String(container.read(key));
        Assert.assertEquals(data, result);
        long cguidHandler = container.getContainerId();
        manager.closeContainer(containerName);
        // reopen
        container = manager.getContainer(containerName);
        result = new String(container.read(key));
        Assert.assertEquals(data, result);
        manager.closeContainer(containerName);
        manager.deleteContainer(containerName);
    }

    @Test
    public void basicContainerOperations() throws Exception {
        Thread[] threads = new Thread[N];
        for (int i = 0; i < N; i++) {
            Thread t = new ZSThread(i);
            t.start();
            threads[i] = t;
        }
        for (Thread t: threads) {
            t.join();
        }
//        Thread.sleep(10000);
    }

    @Test
    public void containerOpenInNewThread() throws ZSException {
        // write some key value in main thread
        ZSContainer container = manager.getContainer("c0");
        byte[] key = new byte[16];
        byte[] data = "d0".getBytes();
        container.write(key, data);
        container.closeContainer();

        // read the value in new thread with new open container
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    manager.initPerThreadState();
                    ZSContainer ct = manager.getContainer("c0");
                    String res = new String(ct.read(key));
                    Assert.assertEquals("d0", res);
                    ct.closeContainer();
                    ct.deleteContainer();
                } catch (ZSException e) {
                    throw new RuntimeException(e);
                } finally {
                    try {
                        manager.releasePerThreadState();
                    } catch (ZSThreadException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });
        t.start();
        try {
            t.join();
        } catch (InterruptedException e) {

        }
    }

    @AfterClass
    public static void afterClass() throws Exception {
        manager.releasePerThreadState();
        manager.shutdown();
    }


    public class ZSThread extends Thread {
        private final int index;

        ZSThread(int index) throws ZSThreadException {
            this.index = index;
        }

        @Override
        public void run() {
            try {
                manager.initPerThreadState();
                ZSContainer container = manager.getContainer("container-" + index);
                byte[] key = ("key-" + index).getBytes();
                String data = "data" + index;
                container.write(key, data.getBytes());
                String result = new String(container.read(key));
                Assert.assertEquals(data, result);
                container.closeContainer();
            } catch (ZSContainerException e) {
                throw new RuntimeException("container error.", e);
            } catch (ZSThreadException e) {
                throw new RuntimeException("thread error.", e);
            } finally {
                try {
                    manager.deleteContainer("container-" + index);
                    manager.releasePerThreadState();
                } catch (ZSThreadException e) {
                    throw new RuntimeException("release thread state failed.", e);
                }
            }
        }
    }
}
