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
import com.vplaygames.PM4J.jsonFramework.JSONArray;
import com.vplaygames.PM4J.util.Array;
import com.vplaygames.PM4J.util.BranchedPrintStream;
import com.vplaygames.PM4J.util.MiscUtil;

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
public class PokemonDataCache extends DataCache<PokemonDataCache, JSONArray<Pokemon>> {
    private static volatile PokemonDataCache instance;

    private PokemonDataCache() {}

    protected void process0() {
        boolean log = settings.getLogPolicy();
        BranchedPrintStream bps = settings.getLogOutputStream();
        String[] toPrint = {""};
        TrainerDataCache tdc = TrainerDataCache.getInstance();
        tdc.forEach((trainerName, trainer) -> trainer.pokemonData.forEach(pokemon -> {
            computeIfAbsent(pokemon.name, name -> new JSONArray<>(0, Constants.EMPTY_POKEMON));
            get(pokemon.name).add(pokemon);
            if (log) {
                bps.print(MiscUtil.backspace(toPrint[0].length()) + (toPrint[0] = log("Categorized " + trainerName + "'s " + pokemon.name + "'s data.", DEBUG, getClass(), false)));
            }
        }));
        keySet().forEach(name1 -> keySet().forEach(name2 -> {
            if (name2.contains(name1)                                     // check if it's a form of the given pokemon
                && !name2.equals(name1)                                   // check if it's not the same pokemon; saves from duplicates
                // other exceptions
                && !Array.contains(name2, tdc.get("Player").getPokemon()) // the player has absurd amounts of duplicates
                && !name1.equals("Mew"))                                  // saves Mew from being included under Mewtwo's Category
            {
                JSONArray<Pokemon> result = get(name2);
                result.addAll(get(name1));
                put(name1, result);
                put(name2, result);
                if (log) {
                    bps.print(MiscUtil.backspace(toPrint[0].length()) + (toPrint[0] = log("Categorized " + name1 + " into " + name2 + "'s data.", DEBUG, getClass(), false)));
                }
            }
        }));
        values().forEach(JSONArray::initialized);
        if (log)
            bps.println(MiscUtil.backspace(toPrint[0].length()) + log("Categorized data for all Pokemon.", DEBUG, getClass(), false));
        processed(tdc.getTotalProcessed());
        initialized();
    }

    /**
     * Returns the Singleton Instance and logs any processes
     *
     * @return the Singleton Instance and logs any processes
     */
    public static PokemonDataCache getInstance() {
        return instance == null ? instance = new PokemonDataCache() : instance;
    }

    /**
     * In version 1.0.0, this method was used to construct the Singleton Instance
     * and turned on/off logging depending on the {@code log} parameter.
     *
     * This method is now {@link Deprecated} because whether logging should be done
     * or not can be set in the {@link com.vplaygames.PM4J.Settings} and the Singleton Instance
     * is now constructed by the {@link #getInstance()} and initialized by
     * {@link #process()} method.
     * @param log whether turn on logging or not.
     * @return the Singleton Instance.
     * @deprecated
     */
    @Deprecated
    public static PokemonDataCache getInstance(boolean log) {
        return getInstance().useSettings(instance.settings.setLogPolicy(log));
    }
}