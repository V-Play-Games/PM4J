/*
 * Copyright 2020 Vaibhav Nargwani
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.vplaygames.PM4J.util;

import com.vplaygames.PM4J.caches.DataCache;

import java.util.Objects;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public class Queueable<T extends DataCache<?,?>> {
    protected static final ScheduledThreadPoolExecutor executor;
    static {
        executor = new ScheduledThreadPoolExecutor(1, r -> new Thread(r, "PM4J Cache"));
        executor.setKeepAliveTime(1, TimeUnit.SECONDS);
        executor.allowCoreThreadTimeOut(true);
    }
    protected boolean isQueued = false;
    protected boolean isDone = false;
    protected final Runnable task;
    protected final T toReturn;

    public Queueable(Runnable task, T toReturn) {
        this.task = Objects.requireNonNull(task);
        this.toReturn = Objects.requireNonNull(toReturn);
    }

    public void queue() {
        queue(null);
    }

    public void queue(Consumer<T> success) {
        queue(success, null);
    }

    public void queue(Consumer<T> success, Consumer<Throwable> failure) {
        isQueued = true;
        executor.execute(() -> {
            try {
                task.run();
                if (success != null) {
                    success.accept(toReturn);
                }
            } catch (Throwable t) {
                if (failure != null) {
                    failure.accept(t);
                } else {
                    System.err.println("PN4J encountered an error while performing an action: " + t.toString());
                    t.printStackTrace();
                }
            }
            isDone = true;
        });
    }

    public T complete() {
        queue();
        awaitCompletion();
        return toReturn;
    }

    public void awaitCompletion() {
        if (!isQueued)
            throw new IllegalStateException();
        while (isDone) {
            try {
                Thread.sleep(50);
            } catch (InterruptedException ignored) {
            }
        }
    }

    public Runnable getTask() {
        return task;
    }
}
