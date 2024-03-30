/*
 *    hyper-event-impl
 *    Copyright [2024] [ideal-state]
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package team.idealstate.hyper.event.impl;

import team.idealstate.hyper.annotation.lang.NotNull;
import team.idealstate.hyper.core.common.AssertUtils;
import team.idealstate.hyper.core.common.object.ObjectUtils;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.*;

/**
 * <p>AsynchronousPublisher</p>
 *
 * <p>创建于 2024/3/30 12:17</p>
 *
 * @author ketikai
 * @version 1.0.0
 * @since 1.0.0
 */
final class AsynchronousPublisher implements ExecutorService {

    private final AsynchronousPublisherProvider provider;
    private volatile ExecutorService executorService;

    AsynchronousPublisher(@NotNull AsynchronousPublisherProvider provider) {
        AssertUtils.notNull(provider, "无效的异步发布器提供者");
        this.provider = provider;
    }

    @NotNull
    private ExecutorService getExecutorService() {
        if (ObjectUtils.isNull(executorService)) {
            executorService = provider.provide();
        }
        return executorService;
    }

    @Override
    public void execute(@NotNull Runnable command) {
        getExecutorService().execute(command);
    }

    @Override
    public void shutdown() {
        getExecutorService().shutdown();
        this.executorService = null;
    }

    @Override
    @NotNull
    public List<Runnable> shutdownNow() {
        List<Runnable> runnableList = getExecutorService().shutdownNow();
        this.executorService = null;
        return runnableList;
    }

    @Override
    public boolean isShutdown() {
        return getExecutorService().isShutdown();
    }

    @Override
    public boolean isTerminated() {
        return getExecutorService().isTerminated();
    }

    @Override
    public boolean awaitTermination(long timeout, @NotNull TimeUnit unit) throws InterruptedException {
        return getExecutorService().awaitTermination(timeout, unit);
    }

    @Override
    @NotNull
    public <T> Future<T> submit(@NotNull Callable<T> task) {
        return getExecutorService().submit(task);
    }

    @Override
    @NotNull
    public <T> Future<T> submit(@NotNull Runnable task, T result) {
        return getExecutorService().submit(task, result);
    }

    @Override
    @NotNull
    public Future<?> submit(@NotNull Runnable task) {
        return getExecutorService().submit(task);
    }

    @Override
    @NotNull
    public <T> List<Future<T>> invokeAll(@NotNull Collection<? extends Callable<T>> tasks) throws InterruptedException {
        return getExecutorService().invokeAll(tasks);
    }

    @Override
    @NotNull
    public <T> List<Future<T>> invokeAll(@NotNull Collection<? extends Callable<T>> tasks, long timeout, @NotNull TimeUnit unit) throws InterruptedException {
        return getExecutorService().invokeAll(tasks, timeout, unit);
    }

    @Override
    @NotNull
    public <T> T invokeAny(@NotNull Collection<? extends Callable<T>> tasks) throws InterruptedException, ExecutionException {
        return getExecutorService().invokeAny(tasks);
    }

    @Override
    public <T> T invokeAny(@NotNull Collection<? extends Callable<T>> tasks, long timeout, @NotNull TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        return getExecutorService().invokeAny(tasks, timeout, unit);
    }
}
