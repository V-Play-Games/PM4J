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
import com.vplaygames.PM4J.entities.SyncGridNode;
import com.vplaygames.PM4J.entities.Trainer;
import com.vplaygames.PM4J.util.Array;
import com.vplaygames.PM4J.util.MiscUtil;

import java.util.ArrayList;

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
public class SkillDataCache extends ProcessedCache<SkillDataCache.Node> {
    private static volatile SkillDataCache instance;
    private Pokemon p;
    private Passive pass;
    private String toPrint;
    private boolean log;

    private SkillDataCache(boolean log) {
        super();
        this.log = log;
        toPrint = log("Processing Skill Data.", Logger.Mode.DEBUG, getClass(), log);
        TrainerDataCache tdc = TrainerDataCache.getInstance();
        for (Trainer t : tdc.values()) {
            for (int i2 = 0; i2 < t.pokemonData.size(); i2++) {
                p = t.pokemonData.get(i2);
                for (int i3 = 0; i3 < p.passives.size(); i3++) {
                    pass = p.passives.get(i3);
                    process(false);
                }
                for (int i3 = 0; i3 < p.grid.size(); i3++) {
                    SyncGridNode sgn = p.grid.get(i3);
                    if (sgn.title.equals(sgn.description)) continue;
                    if (sgn.title.contains(":")) {
                        pass = new Passive(sgn.title.split(":")[1], sgn.description);
                        process(true);
                    }
                    pass = new Passive(sgn.title.replace(":", ": "), sgn.description);
                    process(true);
                }
            }
        }
        if (log)
            System.out.println(MiscUtil.backspace(toPrint.length()) + log("Processed data for all Skills.", Logger.Mode.DEBUG, getClass(), false));
        processed(tdc.getTotalProcessed());
        initialized();
    }

    private void process(boolean isGrid) {
        computeSkill(isGrid);
        computeSkillGroup(isGrid);
        if (log) {
            System.out.print(MiscUtil.backspace(toPrint.length()) + (toPrint = log("Processed " + p.trainer + "'s " + p.name + "'s " + pass.name + "'s data.", Logger.Mode.DEBUG, getClass(), false)));
        }
    }

    private void computeSkillGroup(boolean isGrid) {
        if (Character.isDigit(pass.name.charAt(pass.name.length() - 1))) {
            String newName = pass.name.substring(0, pass.name.length() - 2);
            pass = new Passive(newName, "This is a group of passive skills " + newName + " 1-9.");
            computeSkill(isGrid);
        }
    }

    private void computeSkill(boolean isGird) {
        if (!this.containsKey(pass.name)) {
            this.put(pass.name, new Node(pass));
        }
        this.put(pass.name, this.get(pass.name).add(p, isGird));
    }

    /**
     * Returns the Singleton Instance and logs any processes
     *
     * @return the Singleton Instance and logs any processes
     */
    public static SkillDataCache getInstance() {
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
    public static SkillDataCache getInstance(boolean log) {
        if (log) {
            synchronized (System.out) {
                return instance != null ? instance : (instance = new SkillDataCache(true));
            }
        } else {
            return instance != null ? instance : (instance = new SkillDataCache(false));
        }
    }

    static SkillDataCache forceReinitialize(boolean log) {
        if (log) {
            synchronized (System.out) {
                return instance = new SkillDataCache(true);
            }
        } else {
            return instance = new SkillDataCache(false);
        }
    }

    /** The Data Node for this Cache */
    public static class Node {
        /** The Skill this Node contains data for. */
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

        Node add(Pokemon p, boolean isGrid) {
            String toPut = p.trainer + "'s " + p.name;
            if (isGrid) {
                for (String ig : inGrid) {
                    if (ig.equals(toPut)) {
                        return this;
                    }
                }
                inGrid.add(toPut);
            } else {
                for (String ib : inbuilt) {
                    if (ib.equals(toPut)) {
                        return this;
                    }
                }
                inbuilt.add(toPut);
            }
            return this;
        }
    }
}