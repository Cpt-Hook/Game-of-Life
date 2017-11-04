package Threads;

import Main.Map;

public class CalcRunnable implements Runnable {

    private final Map map;
    private Runnable[] runnables;
    private int threadCount;
    private int doneCheck;

    public CalcRunnable(Map map, int threadCount) {
        this.map = map;
        this.threadCount = threadCount;
        Thread[] threads = new Thread[threadCount];
        runnables = new Runnable[threadCount];

        for (int i = 0; i < threads.length; i++) {
            runnables[i] = new CalcPart(i);
            threads[i] = new Thread(runnables[i]);
            threads[i].start();
        }
        doneCheck = 0;
    }

    @Override
    public void run() {

        for (; ; ) {
            this.waiting();

            for (Runnable run:runnables) {
                synchronized (run){
                    run.notifyAll();
                }
            }

            synchronized (this) {
                while (doneCheck != threadCount) {
                    try {
                        wait();
                    } catch (InterruptedException ignored) {
                        return;
                    }
                }
            }
            doneCheck = 0;

            completed();
        }
    }

    private void completed() {
        synchronized (map) {
            map.doneCheck++;
            map.notifyAll();
        }
    }

    private void waiting() {
        synchronized (this) {
            try {
                this.wait();
            } catch (InterruptedException ignored) {
            }
        }
    }

    class CalcPart implements Runnable {

        private int offset;

        public CalcPart(int offset) {
            this.offset = offset;
        }

        @Override
        public void run() {

            for (; ; ) {
                synchronized (this) {
                    try {
                        this.wait();
                    } catch (InterruptedException ignored) {
                    }
                }
                map.computeNextGen(offset, threadCount);

                synchronized (CalcRunnable.this) {
                    doneCheck++;
                    CalcRunnable.this.notifyAll();
                }
            }
        }
    }
}