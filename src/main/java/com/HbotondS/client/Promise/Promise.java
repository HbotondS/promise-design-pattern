package com.HbotondS.client.Promise;

import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.function.Consumer;
import java.util.function.Function;

public class Promise<T> extends PromiseSupport<T> {
    private Runnable fulfillmentAction;

    public Promise() {}

    @Override
    public void fulfill(T value) {
        super.fulfill(value);
        postFulfillment();
    }

    private void postFulfillment() {
        if (this.fulfillmentAction == null) {
            return;
        }
        fulfillmentAction.run();
    }

    public Promise<T> fulfillAsync(final Callable<T> task, Executor executor) {
        executor.execute(() -> {
            try {
                fulfill(task.call());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        return this;
    }

    public Promise<Void> thenAccept(Consumer<? super T> action) {
        var dest = new Promise<Void>();
        fulfillmentAction = new ConsumeAction(this, dest, action);
        return dest;
    }

    public <V> Promise<V> thenApply(Function<? super T, V> func) {
        var dest = new Promise<V>();
        fulfillmentAction = new TransformAction(this, dest, func);
        return dest;
    }


    private class ConsumeAction implements Runnable {
        private final Promise<T> src;
        private final Promise<Void> dest;
        private final Consumer<? super T> action;

        private ConsumeAction(Promise<T> src, Promise<Void> dest, Consumer<? super T> action) {
            this.src = src;
            this.dest = dest;
            this.action = action;
        }

        @Override
        public void run() {
            try {
                action.accept(src.get());
                dest.fulfill(null);
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        }
    }


    private class TransformAction<V> implements Runnable {
        private final Promise<T> src;
        private final Promise<V> dest;
        private final Function<? super T, V> func;

        private TransformAction(Promise<T> src, Promise<V> dest, Function<? super T, V> func) {
            this.src = src;
            this.dest = dest;
            this.func = func;
        }

        @Override
        public void run() {
            try {
                dest.fulfill(func.apply(src.get()));
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        }
    }
}
