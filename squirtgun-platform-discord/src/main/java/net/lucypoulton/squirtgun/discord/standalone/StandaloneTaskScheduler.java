/*
 * Copyright © 2021 Lucy Poulton
 *
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the
 * "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to
 * the following conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 * WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package net.lucypoulton.squirtgun.discord.standalone;

import net.lucypoulton.squirtgun.minecraft.platform.Platform;
import net.lucypoulton.squirtgun.minecraft.platform.scheduler.Task;
import net.lucypoulton.squirtgun.minecraft.platform.scheduler.TaskScheduler;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static java.util.concurrent.Executors.newSingleThreadScheduledExecutor;

public class StandaloneTaskScheduler implements TaskScheduler {
    private final ScheduledExecutorService executor = newSingleThreadScheduledExecutor();
    private final Platform platform;

    private final Map<Task, Future<?>> tasks = new HashMap<>();

    public StandaloneTaskScheduler(Platform platform) {
        this.platform = platform;
    }

    private Future<?> startTask(Task task) {
        Runnable runnable = () -> task.execute(platform);
        if (task.isRepeating()) {
            return executor.scheduleAtFixedRate(runnable,
                    task.getDelay() * 50L,
                    task.getInterval() * 50L,
                    TimeUnit.MILLISECONDS);
        }
        return executor.schedule(runnable,
                task.getDelay() * 50L,
                TimeUnit.MILLISECONDS);
    }

    @Override
    public void start(Task task) {
        tasks.put(task, startTask(task));
    }

    @Override
    public void cancel(Task task) {
        Future<?> future = tasks.get(task);
        if (future != null) {
            future.cancel(true);
        }
    }
}
