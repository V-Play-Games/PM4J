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
import com.vplaygames.PM4J.entities.Pokemon;
import com.vplaygames.PM4J.entities.TrainerData;
import com.vplaygames.PM4J.util.Array;
import com.vplaygames.PM4J.util.MiscUtil;

import java.util.ArrayList;
import java.util.Set;

import static com.vplaygames.PM4J.Logger.Mode.DEBUG;
import static com.vplaygames.PM4J.Logger.log;

public class PokemonDataCache extends ProcessedCache<ArrayList<Pokemon>> {
    private static volatile PokemonDataCache instance;

    private PokemonDataCache(boolean log) {
        super();
        String toPrint = "";
        TrainerDataCache tdc = TrainerDataCache.getInstance();
        for (TrainerData td : tdc.DataCache) {
            for (Pokemon p : td.pokemonData) {
                if (!this.containsKey(p.name)) {
                    this.put(p.name, new ArrayList<>());
                }
                ArrayList<Pokemon> result = this.get(p.name);
                result.add(p);
                this.put(p.name, result);
                if (log) {
                    System.out.print(MiscUtil.backspace(toPrint.length()) + (toPrint = log("Categorized " + td.name + "'s " + p.name + "'s data.", DEBUG, getClass(), false)));
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
                    ArrayList<Pokemon> result = this.get(name2);
                    result.addAll(this.get(name1));
                    this.put(name1, result);
                    this.put(name2, result);
                    if (log) {
                        System.out.print(MiscUtil.backspace(toPrint.length()) + (toPrint = log("Categorized " + name1 + " into " + name2 + "'s data.", DEBUG, getClass(), false)));
                    }
                }
            }
        }
        if (log) System.out.println(MiscUtil.backspace(toPrint.length()) + log("Categorized data for all Pokemon.", DEBUG, getClass(), false));
        processed(tdc.getTotalProcessed());
        initialized();
    }

    public static PokemonDataCache getInstance() {
        return getInstance(true);
    }

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