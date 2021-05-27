/*
 * Copyright 2020-2021 Vaibhav Nargwani
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
package net.vplaygames.PM4J.entities;

import net.vplaygames.PM4J.core.Util;
import net.vplaygames.vjson.JSONable;
import net.vplaygames.vjson.JSONObject;
import net.vplaygames.vjson.JSONValue;

/**
 * Represents a Sync Grid Node of a Sync Grid of a Sync Pair in Pokemon Masters.
 * <br>All of this class's variables are {@code public final} i.e. available without the use of getters
 * but not assignable.
 *
 * @since 1.0.0
 * @author Vaibhav Nargwani
 */
public class SyncTile implements JSONable {
    /** The bonus this Sync Grid Node activates */
    public final String bonus;
    /**
     * If this node represents a Passive Skill,
     * the title of the Passive Skill this Sync Grid Node activates,
     * else the bonus
     */
    public final String title;
    /**
     * If this node represents a Passive Skill,
     * the description of the Passive Skill this Sync Grid Node activates,
     * else the bonus
     */
    public final String description;
    /** The amount of Sync Pair Specific Sync Orbs this Sync Grid Node requires to be activated */
    public final int syncOrbCost;
    /** The amount of energy this Sync Grid Node requires to be activated */
    public final int energyCost;
    /** The Sync Move Level this Sync Grid Node requires for the Sync Pair to be at to be activated */
    public final int reqSyncLevel;
    /** The position in the Sync Grid of this Sync Grid Node */
    public final String gridPos;
    /** The X position in the Sync Grid of this Sync Grid Node */
    public final int gridPosX;
    /** The Y position in the Sync Grid of this Sync Grid Node */
    public final int gridPosY;

    public SyncTile(String bonus, String syncOrbCost, String energyCost, String reqSyncLevel, String gridPos) {
        this.bonus = bonus;
        if (bonus.contains("- ")) {
            String[] tBonus = bonus.split("- ");
            this.title = tBonus[0];
            this.description = tBonus[1];
        } else {
            this.title = bonus;
            this.description = bonus;
        }
        this.syncOrbCost = Util.toInt(syncOrbCost);
        this.energyCost = Util.toInt(energyCost);
        this.reqSyncLevel = Util.toInt(reqSyncLevel);
        if ("".equals(gridPos)) {
            this.gridPosX = 0;
            this.gridPosY = 0;
        } else {
            String[] tempGridPos = gridPos.substring(1, gridPos.length() - 1).split(",");
            this.gridPosX = Util.toInt(tempGridPos[0]);
            this.gridPosY = Util.toInt(tempGridPos[1]);
        }
        this.gridPos = "[" + gridPosX + "," + gridPosY + "]";
        if (!title.equals(description)) {
            if (title.contains(":"))
                new Passive(title.split(":")[1], description);
            new Passive(title.replace(":", ": "), description);
        }
    }

    @Override
    public String toString() {
        return toJSONString();
    }

    @Override
    public String toJSONString() {
        return "{" +
            "\"bonus\":\"" + bonus + "\"," +
            "\"syncOrbCost\":\"" + syncOrbCost + "\"," +
            "\"energyCost\":\"" + energyCost + "\"," +
            "\"reqSyncLevel\":\"" + reqSyncLevel + "\"," +
            "\"gridPos\":\"" + gridPos + "\"" +
            "}";
    }

    /**
     * Parses the given <code>String</code> to a Sync Grid Node
     *
     * @param json The JSON String to be parsed
     * @return The Sync Grid Node object parsed from the JSON String
     * @throws ClassCastException   if the required value was unable to be cast into the desired type
     * @throws NullPointerException if the required values were not present in the String
     */
    public static SyncTile parse(String json) {
        return parse(JSONValue.parse(json));
    }

    /**
     * Parses the given {@code JSONValue} to a Sync Grid Node
     *
     * @param val The {@code JSONValue} to be parsed
     * @return The Sync Grid Node object parsed from the JSON String
     * @throws ClassCastException   if the required value was unable to be cast into the desired type
     * @throws NullPointerException if the required values were not present in the {@code JSONValue}
     */
    public static SyncTile parse(JSONValue val) {
        JSONObject jo = val.asObject();
        String bonus        = jo.get("bonus").asString();
        String syncOrbCost  = jo.get("syncOrbCost").asString();
        String energyCost   = jo.get("energyCost").asString();
        String reqSyncLevel = jo.get("reqSyncLevel").asString();
        String gridPos      = jo.get("gridPos").asString();
        return new SyncTile(bonus, syncOrbCost, energyCost, reqSyncLevel, gridPos);
    }
}
