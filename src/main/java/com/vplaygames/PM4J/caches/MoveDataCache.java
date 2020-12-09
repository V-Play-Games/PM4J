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

import com.vplaygames.PM4J.Logger;
import com.vplaygames.PM4J.caches.framework.ProcessedCache;
import com.vplaygames.PM4J.entities.Move;
import com.vplaygames.PM4J.entities.Pokemon;
import com.vplaygames.PM4J.entities.Trainer;
import com.vplaygames.PM4J.util.Array;
import com.vplaygames.PM4J.util.MiscUtil;

import java.util.ArrayList;

/**
 * Represents a Cache of all the Data of all the usable Moves in Pokemon Masters.
 * This class uses {@link com.vplaygames.PM4J.Logger} to log details of the processes.
 * Usage example:-
 * <pre><code>
 *     MoveDataCache mdc;
 *     try {
 *         mdc = MoveDataCache.getInstance();
 *     } catch (Exception e) {
 *         e.printStackTrace();
 *     }
 *     if (mdc != null) {
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
public class MoveDataCache extends ProcessedCache<MoveDataCache.Node> {
    private static volatile MoveDataCache instance;

    private MoveDataCache(boolean log) {
        super();
        String toPrint = "";
        TrainerDataCache tdc = TrainerDataCache.getInstance();
        for (Trainer t : tdc.values()) {
            for (Pokemon p : t.pokemonData) {
                for (Move m : p.moves) {
                    if (!this.containsKey(m.name)) {
                        this.put(m.name, new Node(m));
                    }
                    this.put(m.name, this.get(m.name).add(p));
                    if (log) {
                        System.out.print(MiscUtil.backspace(toPrint.length()) + (toPrint = Logger.log("Processed "+t.name + "'s " + p.name + "'s " + m.name + "'s data.", Logger.Mode.DEBUG, getClass(), false)));
                    }
                }
            }
        }
        if (log) System.out.println(MiscUtil.backspace(toPrint.length())+Logger.log("Processed "+"data for all Moves.", Logger.Mode.DEBUG, getClass(), false));
        processed(tdc.getTotalProcessed());
        initialized();
    }

    /**
     * Returns the Singleton Instance and logs any processes
     *
     * @return the Singleton Instance and logs any processes
     */
    public static MoveDataCache getInstance() {
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
    public static MoveDataCache getInstance(boolean log) {
        if (log) {
            synchronized (System.out) {
                return instance != null ? instance : (instance = new MoveDataCache(true));
            }
        } else {
            return instance != null ? instance : (instance = new MoveDataCache(false));
        }
    }

    static MoveDataCache forceReinitialize(boolean log) {
        if (log) {
            synchronized (System.out) {
                return instance = new MoveDataCache(true);
            }
        } else {
            return instance = new MoveDataCache(false);
        }
    }

    /** The Data Node for this Cache */
    public static class Node {
        /** The Move this Node contains data for. */
        public final Move move;
        private final ArrayList<String> users;
        private final ArrayList<String> userPokemonNames;
        private final ArrayList<String> userTrainerNames;

        Node(Move move) {
            this.move = move;
            users = new ArrayList<>();
            userPokemonNames = new ArrayList<>();
            userTrainerNames = new ArrayList<>();
        }

        /**
         * Returns the names of Sync Pairs who can use the corresponding move
         *
         * @return the names of Sync Pairs who can use the corresponding move
         */
        public String[] getUsers() {
            return Array.toStringArray(users.toArray());
        }

        /**
         * Returns the names of Pokemon who can use the corresponding move
         *
         * @return the names of Pokemon who can use the corresponding move
         */
        public String[] getUserPokemonNames() {
            return Array.toStringArray(userPokemonNames.toArray());
        }

        /**
         * Returns the names of Trainers who can use the corresponding move
         *
         * @return the names of Trainers who can use the corresponding move
         */
        public String[] getUserTrainerNames() {
            return Array.toStringArray(userTrainerNames.toArray());
        }

        /**
         * Adds the given Pokemon to the User list
         *
         * @return this instance. Useful for chaining.
         */
        Node add(Pokemon p) {
            int len = users.size();
            userTrainerNames.add(len, p.trainer);
            userPokemonNames.add(len, p.name);
            users.add(len, p.trainer + "'s " + p.name);
            return this;
        }
    }
}