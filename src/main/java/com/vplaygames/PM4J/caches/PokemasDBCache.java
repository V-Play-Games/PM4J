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
package com.vplaygames.PM4J.caches;

import com.vplaygames.PM4J.caches.framework.DownloadedCache;
import com.vplaygames.PM4J.caches.framework.ProcessedCache;

import static com.vplaygames.PM4J.Logger.Mode.INFO;
import static com.vplaygames.PM4J.Logger.log;
import static com.vplaygames.PM4J.util.MiscUtil.*;

/**
 * Represents a Cache of any type of Data in Pokemon Masters (Pokemon, Moves, Skills, Trainers).
 * This class uses {@link com.vplaygames.PM4J.Logger} to log details of the processes.
 * Usage example:-
 * <pre><code>
 *     try {
 *         PokemasDBCache.initialize();
 *     } catch (Exception e) {
 *         e.printStackTrace();
 *     }
 *     PokemasDBCache pmdc = PokemasDBCache.getInstance();
 *     if (pmdc != null) {
 *         // use the object
 *     } else {
 *         System.out.println("Data not initialized yet");
 *     }
 * </code></pre>
 * This class is a Singleton Class, which means it can only be initialized once.
 * The instance is returned by the {@link #getInstance()} method.
 * This Cache caches the data in a {@link com.vplaygames.PM4J.caches.framework.Cache} which is an
 * inheritor of HashMap. This class also provides other details such as downloading time and processing time.
 * See {@link ProcessedCache} for more information.
 *
 * To use this class's Singleton Instance, you have to first initialize it
 * using any of the following methods:
 * <ol>
 *     <li>{@link #initialize()}</li>
 *     <li>{@link #initialize(boolean)}</li>
 *     <li>{@link #initialize(boolean, boolean)}</li>
 * </ol>
 * Calling any of the methods will initialize the Singleton Instance.
 * Note: This Instance can be Re-Initialize to refresh data from <a href="https://www.pokemasdb.com/">PokemasDB</a>
 * which can be done by calling the {@link #forceReinitialize(boolean, boolean)}.
 * Calling the {@link #forceReinitialize(boolean, boolean)} method will result in re-initialization of all the other
 * caches ({@link TrainerDataCache}, {@link PokemonDataCache}, {@link MoveDataCache}, {@link SkillDataCache})
 *
 * @author Vaibhav Nargwani
 * @since 1.0.0
 * @see com.vplaygames.PM4J.caches.framework.Cache
 * @see com.vplaygames.PM4J.caches.framework.ProcessedCache
 * @see java.util.HashMap
 */
public class PokemasDBCache extends DownloadedCache<Object> {
    private static volatile PokemasDBCache instance;
    private static volatile boolean initializing = false;
    private static volatile boolean initialized = false;

    private PokemasDBCache(boolean log, boolean isForced) {
        initializing = true;
        TrainerDataCache tdc;
        PokemonDataCache pdc;
        MoveDataCache mdc;
        SkillDataCache sdc;
        if (isForced) {
            tdc = TrainerDataCache.forceReinitialize(log);
            pdc = PokemonDataCache.forceReinitialize(log);
            mdc = MoveDataCache.forceReinitialize(log);
            sdc = SkillDataCache.forceReinitialize(log);
        } else {
            tdc = TrainerDataCache.getInstance(log);
            pdc = PokemonDataCache.getInstance(log);
            mdc = MoveDataCache.getInstance(log);
            sdc = SkillDataCache.getInstance(log);
        }
        putAll(tdc);
        putAll(pdc);
        putAll(mdc);
        putAll(sdc);
        totalDownloaded = tdc.getTotalDownloaded();
        downloadingTime = tdc.getDownloadingTime();
        totalProcessed = tdc.getTotalProcessed();
        processingTime = tdc.getProcessingTime() + pdc.getProcessingTime() + mdc.getProcessingTime() + sdc.getProcessingTime();
        Class<?> clazz = getClass();
        log("Successfully finished processing of all the data.\n", INFO, clazz);
        if (log) {
            String pSpeed = bytesToString(getProcessingSpeed()) + "/sec";
            String dSpeed = bytesToString(getDownloadingSpeed()) + "/sec";
            pSpeed += space(dSpeed.length() - pSpeed.length());
            dSpeed += space(pSpeed.length() - dSpeed.length());
            log("Took " + msToString(downloadingTime + processingTime) + ".\n", INFO, clazz);
            log("Process  - Speed: " + pSpeed + " Time: " + msToString(processingTime) + ".\n", INFO, clazz);
            log("Download - Speed: " + dSpeed + " Time: " + msToString(downloadingTime) + ".\n", INFO, clazz);
            log("Downloaded " + bytesToString(totalDownloaded * 2) + " of data.\n", INFO, clazz);
        }
        initializing = false;
        initialized = true;
    }

    /**
     * Returns the Singleton Instance, which is null if not initialized
     *
     * @return the Singleton Instance, which is null if not initialized
     */
    public static PokemasDBCache getInstance() {
        return instance;
    }

    /**
     * Initializes the Singleton Instance, logs any processes
     * and does not create a new Thread for the process
     */
    public static void initialize() {
        if (!initialized)
            initialize(true);
    }

    /**
     * Initializes the Singleton Instance,
     * logs any processes if the {@code log} parameter is {@code true}
     * and does not create a new Thread for the process
     *
     * @param log to log processes or not
     */
    public static void initialize(boolean log) {
        if (!initialized)
            initialize(log, false);
    }

    /**
     * Initializes the Singleton Instance,
     * logs any processes if the {@code log} parameter is {@code true}
     * and creates a new Thread for the process if the {@code runInParallel} parameter is {@code true}
     *
     * @param log to log processes or not
     * @param runInParallel to create a new Thread for the process or not
     */
    public static void initialize(boolean log, boolean runInParallel) {
        if (!initialized)
            initialize(log, runInParallel, false);
    }

    private static void initialize(boolean log, boolean runInParallel, boolean isForced) {
        if (initialized || initializing) return;
        try {
            if (runInParallel)
                new Thread(() -> PokemasDBCache.initialize0(log, isForced), "PM4J-Cache").start();
            else
                initialize0(log, isForced);
        } catch (Throwable t) {
            initializing = false;
            initialized = false;
            throw t;
        }
    }

    /**
     * Re-Initializes the Singleton Instance,
     * logs any processes if the {@code log} parameter is {@code true}
     * and creates a new Thread for the process if the {@code runInParallel} parameter is {@code true}
     *
     * @param log to log processes or not
     * @param runInParallel to create a new Thread for the process or not
     */
    public static void forceReinitialize(boolean log, boolean runInParallel) {
        initializing = false;
        initialized = false;
        initialize(log, runInParallel, true);
    }

    private static void initialize0(boolean log, boolean isForced) {
        if (instance == null)
            instance = new PokemasDBCache(log, isForced);
    }
}