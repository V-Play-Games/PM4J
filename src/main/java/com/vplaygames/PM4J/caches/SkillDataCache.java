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
import com.vplaygames.PM4J.entities.Passive;
import com.vplaygames.PM4J.entities.Pokemon;
import com.vplaygames.PM4J.util.Array;
import com.vplaygames.PM4J.util.BranchedPrintStream;
import com.vplaygames.PM4J.util.MiscUtil;

import java.util.ArrayList;
import java.util.function.Consumer;

import static com.vplaygames.PM4J.Logger.log;

/**
 * Represents a Cache of all the Data of all the usable Passive Skills in Pokemon Masters,
 * present in either a Sync Pair's default Passives, or present in a Sync Pair's Sync Grid.
 * This class uses {@link com.vplaygames.PM4J.Logger} to log details of the processes.
 * Usage example:-
 * <pre><code>
 *     SkillDataCache sdc;
 *     try {
 *         sdc = SkillDataCache.getInstance();
 *     } catch (Exception e) {
 *         e.printStackTrace();
 *     }
 *     if (sdc != null) {
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
public class SkillDataCache extends DataCache<SkillDataCache, SkillDataCache.Node> {
    private static volatile SkillDataCache instance;

    private SkillDataCache() {}

    protected void process0() {
        boolean log = settings.getLogPolicy();
        BranchedPrintStream bps = settings.getLogOutputStream();
        String[] toPrint = {""};
        if (log) {
            bps.print(toPrint[0] = log("Processing Skill Data...", Logger.Mode.DEBUG, getClass(), false));
        }
        TrainerDataCache tdc = TrainerDataCache.getInstance();
        // Run through each Trainer's each Pokemon & scan & add Passives from passive skills & grid skills
        tdc.forEach((trainerName, trainer) -> trainer.pokemonData.forEach(pokemon -> {
            boolean[] isGrid = {false};
            Consumer<Passive> skillAcceptor = (pass) -> {
                Consumer<Passive> skillAdder = (pass2) -> {
                    computeIfAbsent(pass2.name, k -> new Node(pass2));
                    get(pass2.name).add(pokemon, isGrid[0]);
                    if (log) {
                        bps.print(MiscUtil.backspace(toPrint[0].length()) + (toPrint[0] = log("Processed " + trainerName + "'s " + pokemon.name + "'s " + pass2.name + "'s data.", Logger.Mode.DEBUG, getClass(), false)));
                    }
                };
                skillAdder.accept(pass);
                if (Character.isDigit(pass.name.charAt(pass.name.length() - 1))) {
                    String newName = pass.name.substring(0, pass.name.length() - 2);
                    skillAdder.accept(new Passive(newName, "This is a group of passive skills " + newName + " 1-9."));
                }
            };
            pokemon.passives.forEach(skillAcceptor);
            pokemon.grid.forEach((sgn) -> {
                if (sgn.title.equals(sgn.description)) { return; }
                isGrid[0] = true;
                if (sgn.title.contains(":")) {
                    skillAcceptor.accept(new Passive(sgn.title.split(":")[1], sgn.description));
                }
                skillAcceptor.accept(new Passive(sgn.title.replace(":", ": "), sgn.description));
            });
        }));
        if (log) bps.println(MiscUtil.backspace(toPrint[0].length()) + log("Processed data for all Skills.", Logger.Mode.DEBUG, getClass(), false));
        processed(tdc.getTotalProcessed());
        initialized();
    }

    /**
     * Returns the Singleton Instance
     *
     * @return the Singleton Instance
     */
    public static SkillDataCache getInstance() {
        return instance == null ? instance = new SkillDataCache() : instance;
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
    public static SkillDataCache getInstance(boolean log) {
        return getInstance().useSettings(instance.settings.setLogPolicy(log));
    }

    /** The Data Node for SkillDataCache */
    public static class Node {
        /** The Skill for which this Node contains data for. */
        public final Passive skill;
        private final ArrayList<String> inbuilt;
        private final ArrayList<String> inGrid;

        Node(Passive skill) {
            this.skill = skill;
            inbuilt = new ArrayList<>();
            inGrid = new ArrayList<>();
        }

        /**
         * Returns the names of Sync Pairs who have the corresponding skill in their default Passives
         *
         * @return the names of Sync Pairs who have the corresponding skill in their default Passives
         */
        public String[] getInbuilt() {
            return Array.toStringArray(inbuilt.toArray());
        }

        /**
         * Returns the names of Sync Pairs who have the corresponding skill in their Sync Grid
         *
         * @return the names of Sync Pairs who have the corresponding skill in their Sync Grid
         */
        public String[] getInGrid() {
            return Array.toStringArray(inGrid.toArray());
        }

        public Node add(Pokemon p, boolean isGrid) {
            String toPut = p.trainer + "'s " + p.name;
            ArrayList<String> toPutIn = isGrid ? inGrid : inbuilt;
            if (!toPutIn.contains(toPut)) {
                toPutIn.add(toPut);
            }
            return this;
        }
    }
}