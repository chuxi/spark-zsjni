package com.windjammer.zetascale;

import com.windjammer.zetascale.exception.ZSContainerException;
import com.windjammer.zetascale.exception.ZSThreadException;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by king on 17-7-31.
 */
public class ZSBenchmark {
    private static int N = 4;
    private static int M = 100 * 1000;
    private static ExecutorService service = Executors.newFixedThreadPool(N);
    private static ZSManager manager = ZSManager.getInstance();

    public static void main(String[] args) throws Exception {
        manager.init("zs.prop");
        manager.initPerThreadState();

        Thread[] threads = new Thread[N];
        for (int i = 0; i < N; i++) {
            Thread t = new ZSThread(i);
            t.start();
            threads[i] = t;
        }
        for (Thread t: threads) {
            t.join();
        }

        manager.releasePerThreadState();
        manager.shutdown();
    }


    public static class ZSThread extends Thread {
        private final int index;

        ZSThread(int index) throws ZSThreadException {
            this.index = index;
        }

        @Override
        public void run() {
            try {
                manager.initPerThreadState();
                ZSContainer container = manager.getContainer("container-" + index);
                for (int i = 0; i < M; i++) {
                    byte[] key = ("key-" + index + "-" + i).getBytes();
                    String data = "data" + index + "-" + i;
                    container.write(key, data.getBytes());
                    container.read(key);
                }
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
