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

import com.vplaygames.PM4J.caches.framework.ProcessedCache;
import com.vplaygames.PM4J.entities.Constants;
import com.vplaygames.PM4J.entities.Pokemon;
import com.vplaygames.PM4J.entities.Trainer;
import com.vplaygames.PM4J.jsonFramework.JSONArray;
import com.vplaygames.PM4J.util.Array;
import com.vplaygames.PM4J.util.MiscUtil;

import java.util.Set;

import static com.vplaygames.PM4J.Logger.Mode.DEBUG;
import static com.vplaygames.PM4J.Logger.log;

/**
 * Represents a Cache of all the Data of all the usable Pokemon in Pokemon Masters
 * who have formed a Sync Pair with a scoutable or usable Trainer.
 * This class uses {@link com.vplaygames.PM4J.Logger} to log details of the processes.
 * Usage example:-
 * <pre><code>
 *     PokemonDataCache pdc;
 *     try {
 *         pdc = PokemonDataCache.getInstance();
 *     } catch (Exception e) {
 *         e.printStackTrace();
 *     }
 *     if (pdc != null) {
 *         // use the object
 *     } else {
 *         System.out.println("Data not initialized yet");
 *     }
 * </code></pre>
 * This class is a Singleton Class, which means it can only be initialized once.
 * The instance is returned by the {@link #getInstance()} and {@link #getInstance(boolean)} methods.
 * This Cache caches the data in a {@link com.vplaygames.PM4J.caches.framework.Cache} which is an
 * inheritor of HashMap. This class also provides other details such as processing time.
 * See {@link ProcessedCache} for more information.
 *
 * @author Vaibhav Nargwani
 * @since 1.0.0
 * @see com.vplaygames.PM4J.caches.framework.Cache
 * @see com.vplaygames.PM4J.caches.framework.ProcessedCache
 * @see java.util.HashMap
 */
public class PokemonDataCache extends ProcessedCache<JSONArray<Pokemon>> {
    private static volatile PokemonDataCache instance;

    private PokemonDataCache(boolean log) {
        super();
        String toPrint = "";
        TrainerDataCache tdc = TrainerDataCache.getInstance();
        for (Trainer t : tdc.values()) {
            for (Pokemon p : t.pokemonData) {
                if (!this.containsKey(p.name)) {
                    this.put(p.name, new JSONArray<>(0, Constants.EMPTY_POKEMON));
                }
                JSONArray<Pokemon> result = this.get(p.name);
                result.add(p);
                this.put(p.name, result);
                if (log) {
                    System.out.print(MiscUtil.backspace(toPrint.length()) + (toPrint = log("Categorized " + t.name + "'s " + p.name + "'s data.", DEBUG, getClass(), false)));
                }
            }
        }

        Set<String> keys = this.keySet();

        for (String name1 : keys) {
            for (String name2 : keys) {
                if (name2.contains(name1)                                         // check if it's a form of the given pokemon
                        && !name2.equals(name1)                                   // check if it's not the same pokemon; saves from duplicates
                        // other exceptions
                        && !Array.contains(name2, tdc.get("Player").getPokemon()) // the player has absurd amounts of duplicates
                        && !name1.equals("Mew")) {                                // saves Mew from being included under Mewtwo's Category
                    JSONArray<Pokemon> result = this.get(name2);
                    result.addAll(this.get(name1));
                    this.put(name1, result);
                    this.put(name2, result);
                    if (log) {
                        System.out.print(MiscUtil.backspace(toPrint.length()) + (toPrint = log("Categorized " + name1 + " into " + name2 + "'s data.", DEBUG, getClass(), false)));
                    }
                }
            }
        }

        for (String key : keys) {
            get(key).initialized();
        }
        if (log) System.out.println(MiscUtil.backspace(toPrint.length()) + log("Categorized data for all Pokemon.", DEBUG, getClass(), false));
        processed(tdc.getTotalProcessed());
        initialized();
    }

    /**
     * Returns the Singleton Instance and logs any processes
     *
     * @return the Singleton Instance and logs any processes
     */
    public static PokemonDataCache getInstance() {
        return getInstance(true);
    }

    /**
     * Returns the Singleton Instance and logs any processes if the parameter passed is true
     *
     * @param log to log processes or not
     *            Note:- if this is true, the method takes the monitor of {@code System.out}
     *            So, any other threads waiting on that monitor will be put to sleep
     * @return the Singleton Instance and logs any processes if the parameter passed is true
     */
    public static PokemonDataCache getInstance(boolean log) {
        if (log) {
            synchronized (System.out) {
                return instance != null ? instance : (instance = new PokemonDataCache(true));
            }
        } else {
            return instance != null ? instance : (instance = new PokemonDataCache(false));
        }
    }

    static PokemonDataCache forceReinitialize(boolean log) {
        if (log) {
            synchronized (System.out) {
                return instance = new PokemonDataCache(true);
            }
        } else {
            return instance = new PokemonDataCache(false);
        }
    }
}