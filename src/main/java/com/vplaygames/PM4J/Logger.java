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
package com.vplaygames.PM4J;

import java.time.LocalTime;

/**
 * The Logger used in {@link com.vplaygames.PM4J.caches.PokemasDBCache} and all the other caches.
 * This logger's syntax imitates SL4J's Logger's Syntax and is greatly inspired by it.
 * The Syntax of this Logger is:-
 * <pre><code>CURRENT_TIME [THREAD_NAME] {@link Mode LOG_MODE} CLASS_NAME - MESSAGE</code></pre>
 *
 * For example:-
 * <pre><code>
 *     13:25:43.527 [PM4J-Cache] DEBUG com.vplaygames.PM4J.caches.PokemasDBCache - Completed processing data of all the trainers
 * </code></pre>
 *
 * @author Vaibhav Nargwani
 * @since 1.0.0
 */
public class Logger {
    // Private Constructor to avoid making Objects
    private Logger() {}

    /**
     * Prepares the Logging Syntax and prints it.
     *
     * @param s     the message to print.
     * @param mode  the Logger mode or Level.
     * @param clazz the caller class.
     * @return the Logging message printed.
     */
    public static String log(String s, Mode mode, Class<?> clazz) {
        return log(s, mode, clazz, true);
    }

    /**
     * Prepares the Logging Syntax and prints if allowed doing so.
     *
     * @param s     the message to print.
     * @param mode  the Logger mode or Level.
     * @param clazz the caller class.
     * @param print Whether to print the Log or not.
     * @return the Logging message obtained from the parameters.
     */
    public static String log(String s, Mode mode, Class<?> clazz, boolean print) {
        String top = LocalTime.now() + " [" + Thread.currentThread().getName() + "] " + mode + " " + clazz.getName() + " - " + s;
        if (print) System.out.print(top);
        return top;
    }

    /**
     * Represents a Log Mode or Level.
     */
    public enum Mode {
        INFO, DEBUG, WARN, ERROR
    }
}