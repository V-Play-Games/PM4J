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
import com.vplaygames.PM4J.entities.TrainerData;
import com.vplaygames.PM4J.util.MiscUtil;

import java.util.ArrayList;

import static com.vplaygames.PM4J.Logger.log;

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
        for (int i1 = 0; i1 < tdc.DataCache.size(); i1++) {
            TrainerData td = tdc.DataCache.get(i1);
            for (int i2 = 0; i2 < td.pokemonData.size(); i2++) {
                p = td.pokemonData.get(i2);
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

    public static SkillDataCache getInstance() {
        return getInstance(true);
    }

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

    public static class Node {
        public final Passive skill;
        private final ArrayList<String> inbuilt;
        private final ArrayList<String> inGrid;

        public Node(Passive skill) {
            this.skill = skill;
            inbuilt = new ArrayList<>();
            inGrid = new ArrayList<>();
        }

        public Object[] getInbuilt() {
            return inbuilt.toArray();
        }

        public Object[] getInGrid() {
            return inGrid.toArray();
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
                for (String ib : inGrid) {
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