package Threads;


class RenderRunnable implements Runnable {

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

            render();
            synchronized (Map.this) {
                doneCheck++;
                Map.this.notifyAll();
            }
        }
    }
}
