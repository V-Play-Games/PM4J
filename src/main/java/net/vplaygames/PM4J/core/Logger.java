/*
 * Copyright 2020-2021 Vaibhav Nargwani
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
package net.vplaygames.PM4J.core;

/**
 * The Logger used in {@link Connection} and the {@link net.vplaygames.PM4J.caches.TrainerDataCache}.
 * This logger's syntax imitates SL4J's Logger's Syntax and is inspired by it.
 * The Syntax of this Logger is:-
 * <pre><code>CURRENT_TIME [THREAD_NAME] {@link Mode LOG_MODE} CLASS_NAME - MESSAGE</code></pre>
 *
 * For example:-
 * <pre><code>
 *     13:25:43.527 [PM4J-Cache] INFO net.vplaygames.PM4J.core.Connection - Downloaded data for all the trainers.
 * </code></pre>
 *
 * @author Vaibhav Nargwani
 * @since 1.0.0
 */
public class Logger {
    private final Class<?> clazz;

    public Logger(Class<?> clazz) {
        if (clazz == null) throw new NullPointerException();
        this.clazz = clazz;
    }

    /**
     * Prepares the Logging Syntax and prints it
     *
     * @param s     the message to print
     * @param mode  the Logger mode or Level
     * @return the Logging message printed
     */
    public String log(String s, Mode mode) {
        return log(s, mode, true);
    }

    /**
     * Prepares the Logging Syntax and prints if allowed doing so
     *
     * @param s     the message to print
     * @param mode  the Logger mode or Level
     * @param print Whether to print the Log or not
     * @return the Logging message obtained from the parameters
     */
    public String log(String s, Mode mode, boolean print) {
        String top = java.time.LocalTime.now()
            + " [" + Thread.currentThread().getName() + "] " + mode + " " + clazz.getName() + " - " + s;
        if (print) System.out.print(top);
        return top;
    }

    /**
     * Represents a Log Mode or Level.
     *
     * @author Vaibhav Nargwani
     * @since 1.0.0
     */
    public enum Mode {
        INFO, DEBUG, WARN, ERROR
    }
}
