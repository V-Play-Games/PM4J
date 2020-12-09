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
package com.vplaygames.PM4J.entities;

import com.vplaygames.PM4J.jsonFramework.ParsableJSONObject;
import com.vplaygames.PM4J.util.MiscUtil;
import com.vplaygames.PM4J.util.Strings;
import org.json.simple.JSONObject;

/**
 * Represents a Sync Grid Node of a Sync Grid of a Sync Pair in Pokemon Masters.
 * <br>All of this class's variables are {@code public final} i.e. available without the use of getters
 * but not assignable.
 * <br>This class implements the {@link ParsableJSONObject} interface, which means that it can be parsed
 * and stored into a {@link com.vplaygames.PM4J.jsonFramework.JSONArray} and can be converted into a JSON String.
 *
 * @since 1.0
 * @author Vaibhav Nargwani
 *
 * @see com.vplaygames.PM4J.jsonFramework.ParsableJSONObject
 * @see com.vplaygames.PM4J.jsonFramework.JSONArray
 */
public class SyncGridNode implements ParsableJSONObject<SyncGridNode>
{
    /** The bonus this Sync Grid Node activates */
    public final String bonus;
    /**
     * If the bonus represents a Passive Skill,
     * the title of the Passive Skill this Sync Grid Node activates,
     * else the bonus
     */
    public final String title;
    /**
     * If the bonus represents a Passive Skill,
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

    public SyncGridNode(String bonus, String syncOrbCost, String energyCost, String reqSyncLevel, String gridPos) {
        this.bonus = bonus;
        String[] tBonus = bonus.contains("- ") ? bonus.split("- ") : new String[]{bonus, bonus};
        this.title = tBonus[0];
        this.description = tBonus[1];
        this.syncOrbCost = "".equals(syncOrbCost) ? 0 : Integer.parseInt(syncOrbCost);
        this.energyCost = "".equals(energyCost) ? 0 : Integer.parseInt(energyCost);
        this.reqSyncLevel = "".equals(reqSyncLevel) ? 0 : reqSyncLevel.contains("1") ? 1 : reqSyncLevel.contains("2") ? 2 : 3;
        this.gridPos = gridPos;
        String[] tempGridPos = "".equals(gridPos) ? new String[]{"0","0"} : gridPos.substring(1, gridPos.length() - 1).split(",");
        this.gridPosX = Strings.toInt(tempGridPos[0]);
        this.gridPosY = Strings.toInt(tempGridPos[1]);
    }

    @Override
    public String getAsJSON() {
        return "{"+
                "\"bonus\":\""+bonus+"\","+
                "\"syncOrbCost\":\""+syncOrbCost+"\","+
                "\"energyCost\":\""+energyCost+"\","+
                "\"reqSyncLevel\":\""+reqSyncLevel+"\","+
                "\"gridPos\":\""+gridPos+"\""+
                "}";
    }

    @Override
    public String toString() {
        return getAsJSON();
    }

    @Override
    public SyncGridNode parseFromJSON(String JSON) {
        return parse(JSON);
    }

    /**
     * Parses the given <code>String</code> to a Sync Grid Node.
     *
     * @param json The JSON String to be parsed.
     *
     * @return The Sync Grid Node object parsed from the JSON String.
     *
     * @throws com.vplaygames.PM4J.exceptions.ParseException If the JSON String was incorrectly formatted.
     * @throws ClassCastException if the required value was unable to be cast into the desired type.
     * @throws NullPointerException if the required values were not present in the String.
     */
    public static SyncGridNode parse(String json) {
        return parse(MiscUtil.parseJSONObject(json));
    }

    /**
     * Parses the given {@link JSONObject} to a Sync Grid Node.
     *
     * @param jo The {@link JSONObject} to be parsed.
     *
     * @return The Sync Grid Node object parsed from the JSON String.
     *
     * @throws ClassCastException if the required value was unable to be cast into the desired type.
     * @throws NullPointerException if the required values were not present in the {@link JSONObject}.
     */
    public static SyncGridNode parse(JSONObject jo) {
        String bonus        = (String) jo.get("bonus");
        String syncOrbCost  = (String) jo.get("syncOrbCost");
        String energyCost   = (String) jo.get("energyCost");
        String reqSyncLevel = (String) jo.get("reqSyncLevel");
        String gridPos      = (String) jo.get("gridPos");
        return new SyncGridNode(bonus,syncOrbCost,energyCost,reqSyncLevel,gridPos);
    }

    static SyncGridNode emptySyncGridNode() {
        return new SyncGridNode("","","","","");
    }
}
