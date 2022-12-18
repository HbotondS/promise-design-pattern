package com.HbotondS.client.Promise;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class PromiseSupport<T> implements Future<T> {
    private volatile PromiseState state = PromiseState.RUNNING;

    private final Object lock;

    private T value;
    private Exception exception;

    PromiseSupport() {
        this.lock = new Object();
    }

    void fulfill(T value) {
        this.value = value;
        this.state = PromiseState.COMPLETED;
        synchronized (this.lock) {
            this.lock.notifyAll();
        }
    }

    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
        return false;
    }

    @Override
    public boolean isCancelled() {
        return false;
    }

    @Override
    public boolean isDone() {
        return this.state != PromiseState.RUNNING;
    }

    @Override
    public T get() throws InterruptedException, ExecutionException {
        synchronized (this.lock) {
            while (this.state == PromiseState.RUNNING) {
                this.lock.wait();
            }
        }
        if (this.state == PromiseState.COMPLETED) {
            return this.value;
        }

        throw new ExecutionException(this.exception);
    }

    @Override
    public T get(long timeout, TimeUnit timeUnit) throws InterruptedException, ExecutionException, TimeoutException {
        synchronized (this.lock) {
            while (this.state == PromiseState.RUNNING) {
                this.lock.wait(timeUnit.toMillis(timeout));
            }
        }

        if (this.state == PromiseState.COMPLETED) {
            return this.value;
        }

        throw new ExecutionException(this.exception);
    }
}
