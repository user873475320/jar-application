package ru;

import java.util.concurrent.*;
import java.util.*;

public class ThreadPool implements ExecutorService {
    private final BlockingQueue<Runnable> taskQueue = new LinkedBlockingQueue<>();
    private final List<WorkerThread> threads = new ArrayList<>();
    private volatile boolean isShutdown = false;

    public ThreadPool(int threadCount) {
        for (int i = 0; i < threadCount; i++) {
            WorkerThread worker = new WorkerThread(taskQueue);
            threads.add(worker);
            worker.start();
        }
    }

    @Override
    public void execute(Runnable command) {
        if (!isShutdown) {
            taskQueue.offer(command);
        }
    }

    @Override
    public void shutdown() {
        isShutdown = true;
        for (WorkerThread thread : threads) {
            thread.interrupt();
        }
    }

    @Override
    public List<Runnable> shutdownNow() {
        isShutdown = true;
        taskQueue.clear();
        for (WorkerThread thread : threads) {
            thread.interrupt();
        }
        return new ArrayList<>();
    }

    @Override
    public boolean isShutdown() {
        return isShutdown;
    }

    @Override
    public boolean isTerminated() {
        for (WorkerThread thread : threads) {
            if (thread.isAlive()) return false;
        }
        return true;
    }

    @Override
    public boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {
        long deadline = System.currentTimeMillis() + unit.toMillis(timeout);
        for (WorkerThread thread : threads) {
            long remaining = deadline - System.currentTimeMillis();
            if (remaining > 0) {
                thread.join(remaining);
            }
        }
        return isTerminated();
    }

    @Override
    public <T> Future<T> submit(Callable<T> task) {
        return null;
    }

    @Override
    public <T> Future<T> submit(Runnable task, T result) {
        return null;
    }

    @Override
    public Future<?> submit(Runnable task) {
        return null;
    }

    @Override
    public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks) throws InterruptedException {
        return null;
    }

    @Override
    public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit) throws InterruptedException {
        return null;
    }

    @Override
    public <T> T invokeAny(Collection<? extends Callable<T>> tasks) throws InterruptedException, ExecutionException {
        return null;
    }

    @Override
    public <T> T invokeAny(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        return null;
    }

    private static class WorkerThread extends Thread {
        private final BlockingQueue<Runnable> taskQueue;

        public WorkerThread(BlockingQueue<Runnable> taskQueue) {
            this.taskQueue = taskQueue;
        }

        @Override
        public void run() {
            while (!isInterrupted()) {
                try {
                    Runnable task = taskQueue.take();
                    task.run();
                } catch (InterruptedException e) {
                    interrupt();
                }
            }
        }
    }
}
