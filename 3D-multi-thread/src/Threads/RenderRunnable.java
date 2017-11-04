package Threads;

import Main.Map;

public class RenderRunnable implements Runnable {

    private final Map map;

    public RenderRunnable(Map map) {
        this.map = map;
    }

    @Override
    public void run() {

        for (; ; ) {
            synchronized (this) {
                try {
                    this.wait();
                } catch (InterruptedException e) {
                    return;
                }
            }

            map.render();
            synchronized (map) {
                map.doneCheck++;
                map.notifyAll();
            }
        }
    }
}