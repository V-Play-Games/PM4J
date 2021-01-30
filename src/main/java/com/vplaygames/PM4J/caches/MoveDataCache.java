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
import com.vplaygames.PM4J.util.BranchedPrintStream;
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
public class MoveDataCache extends DataCache<MoveDataCache, MoveDataCache.Node> {
    private static volatile MoveDataCache instance;

    private MoveDataCache() {}

    protected void process0() {
        boolean log = settings.getLogPolicy();
        BranchedPrintStream bps = settings.getLogOutputStream();
        String[] toPrint = {""};
        TrainerDataCache tdc = TrainerDataCache.getInstance();
        tdc.forEach((trainerName, trainer) -> trainer.pokemonData.forEach(pokemon -> pokemon.moves.forEach(move -> {
            computeIfAbsent(move.name, name -> new Node(move));
            get(move.name).add(pokemon);
            if (log) {
                bps.print(MiscUtil.backspace(toPrint[0].length()) + (toPrint[0] = Logger.log("Processed "+trainerName + "'s " + pokemon.name + "'s " + move.name + "'s data.", Logger.Mode.DEBUG, getClass(), false)));
            }
        })));
        if (log) bps.println(MiscUtil.backspace(toPrint[0].length())+Logger.log("Processed data for all Moves.", Logger.Mode.DEBUG, getClass(), false));
        processed(tdc.getTotalProcessed());
        initialized();
    }

    /**
     * Returns the Singleton Instance
     *
     * @return the Singleton Instance
     */
    public static MoveDataCache getInstance() {
        return instance == null ? instance = new MoveDataCache() : instance;
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
    public static MoveDataCache getInstance(boolean log) {
        return getInstance().useSettings(instance.settings.setLogPolicy(log));
    }

    /** The Data Node for MoveDataCache */
    public static class Node {
        /** The Move this Node contains data for. */
        public final Move move;
        private final ArrayList<String> users            = new ArrayList<>();
        private final ArrayList<String> userPokemonNames = new ArrayList<>();
        private final ArrayList<String> userTrainerNames = new ArrayList<>();

        Node(Move move) {
            this.move = move;
        }

        /**
         * Returns the names of Sync Pairs who can use the corresponding move
         *
         * @return the names of Sync Pairs who can use the corresponding move
         */
        public String[] getUsers() {
            return users.toArray(new String[0]);
        }

        /**
         * Returns the names of Pokemon who can use the corresponding move
         *
         * @return the names of Pokemon who can use the corresponding move
         */
        public String[] getUserPokemonNames() {
            return userPokemonNames.toArray(new String[0]);
        }

        /**
         * Returns the names of Trainers who can use the corresponding move
         *
         * @return the names of Trainers who can use the corresponding move
         */
        public String[] getUserTrainerNames() {
            return userTrainerNames.toArray(new String[0]);
        }

        /**
         * Adds the given Pokemon to the User list
         *
         * @return this instance. Useful for chaining.
         */
        public Node add(Pokemon p) {
            int len = users.size();
            userTrainerNames.add(len, p.trainer);
            userPokemonNames.add(len, p.name);
            users.add(len, p.trainer + "'s " + p.name);
            return this;
        }
    }
}