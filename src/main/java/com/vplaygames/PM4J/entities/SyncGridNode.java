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
import org.json.simple.JSONObject;

public class SyncGridNode implements ParsableJSONObject<SyncGridNode>
{
    public final String bonus;
    public final String title;
    public final String description;
    public final int syncOrbCost;
    public final int energyCost;
    public final int reqSyncLevel;
    public final String gridPos;
    public final int gridPosX;
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
        this.gridPosX = Integer.parseInt(tempGridPos[0]);
        this.gridPosY = Integer.parseInt(tempGridPos[1]);
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
    public SyncGridNode parseFromJSON(JSONObject JSON) {
        return parse(JSON);
    }

    public static SyncGridNode parse(String json) {
        return parse(MiscUtil.parseJSONObject(json));
    }

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
