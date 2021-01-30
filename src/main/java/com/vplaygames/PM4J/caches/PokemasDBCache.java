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

import com.vplaygames.PM4J.caches.framework.Cache;
import com.vplaygames.PM4J.caches.framework.ProcessedCache;
import com.vplaygames.PM4J.util.Queueable;

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
public class PokemasDBCache extends DataCache<PokemasDBCache, Object> {
    private static volatile PokemasDBCache instance;

    protected PokemasDBCache() {}

    public Queueable<PokemasDBCache> downloadData() {
        return queueProcess(TrainerDataCache.getInstance());
    }

    public Queueable<PokemasDBCache> processPokemon() {
        return queueProcess(PokemonDataCache.getInstance());
    }

    public Queueable<PokemasDBCache> processMoves() {
        return queueProcess(MoveDataCache.getInstance());
    }

    public Queueable<PokemasDBCache> processSkills() {
        return queueProcess(SkillDataCache.getInstance());
    }

    private Queueable<PokemasDBCache> queueProcess(DataCache<?,?> cache) {
        return new Queueable<>(() -> cache.useSettings(settings).process().getTask().run(), this);
    }

    protected void process0() {
        downloadData().getTask().run();
        processPokemon().getTask().run();
        processMoves().getTask().run();
        processSkills().getTask().run();
    }

    /**
     * Returns the Singleton Instance
     *
     * @return the Singleton Instance
     */
    public static PokemasDBCache getInstance() {
        return instance == null ? instance = new PokemasDBCache() : instance;
    }

    /** @deprecated */
    @Deprecated
    public static void initialize() {}

    /** @deprecated */
    @Deprecated
    public static void initialize(boolean log) {}

    /** @deprecated */
    @Deprecated
    public static void initialize(boolean log, boolean runInParallel) {}

    /** @deprecated */
    @Deprecated
    public static void forceReinitialize(boolean log, boolean runInParallel) {}

    public Queueable<PokemasDBCache> invalidateCaches() {
        return new Queueable<>(() -> {
            for (Cache.Type type : Cache.Type.values()) {
                type.getCache().invalidateCache();
            }
        }, this);
    }
}
