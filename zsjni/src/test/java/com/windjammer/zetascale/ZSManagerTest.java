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
        ZSContainer container = manager.getContainer("container-test");
        byte[] key = "key-test".getBytes();
        String data = "data-test";
        container.write(key, data.getBytes());
        String result = new String(container.read(key));
        Assert.assertEquals(data, result);
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
            } catch (ZSContainerException e) {
                throw new RuntimeException("container error.", e);
            } catch (ZSThreadException e) {
                throw new RuntimeException("thread error.", e);
            } finally {
                try {
                    manager.releasePerThreadState();
                } catch (ZSThreadException e) {
                    throw new RuntimeException("release thread state failed.", e);
                }
            }
        }
    }
}
