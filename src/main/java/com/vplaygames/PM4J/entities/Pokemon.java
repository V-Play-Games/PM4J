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

import com.vplaygames.PM4J.jsonFramework.JSONArray;
import com.vplaygames.PM4J.jsonFramework.ParsableJSONObject;
import com.vplaygames.PM4J.util.Array;
import com.vplaygames.PM4J.util.MiscUtil;
import com.vplaygames.PM4J.util.Strings;
import org.json.simple.JSONObject;

import static com.vplaygames.PM4J.entities.Constants.*;

public class Pokemon implements ParsableJSONObject<Pokemon>
{
    public final String name;
    public final String trainer;
    protected final String[] typing;
    public final String weakness;
    public final String role;
    public final int rarity;
    public final String gender;
    protected final String[] otherForms;
    public final JSONArray<Move> moves;
    public final SyncMove syncMove;
    public final JSONArray<Passive> passives;
    public final StatRange stats;
    public final JSONArray<SyncGridNode> grid;

    public Pokemon(String name, String trainer,
                   String[] typing,
                   String weakness, String role, String rarity, String gender,
                   String[] otherForms,
                   JSONArray<Move> moves, SyncMove syncMove,
                   JSONArray<Passive> passives, StatRange stats, JSONArray<SyncGridNode> grid) {
        this.name = name;
        this.trainer = trainer;
        this.typing = typing;
        this.weakness = weakness;
        this.role = role;
        this.rarity = rarity.equals("") ? 0 : Strings.toInt(rarity);
        this.gender = gender;
        this.otherForms = otherForms;
        this.moves = moves.initialized();
        this.syncMove = syncMove;
        this.passives = passives.initialized();
        this.stats = stats;
        this.grid = grid.initialized();
    }

    public String[] getOtherForms() {
        return otherForms;
    }

    public String[] getTyping() {
        return typing;
    }

    @Override
    public String getAsJSON() {
        return "{"+
                "\"name\":\""+name+"\","+
                "\"trainer\":\""+trainer+"\","+
                "\"typing\":["+ Array.toString(",",typing,"")+"],"+
                "\"weakness\":\""+weakness+"\","+
                "\"role\":\""+role+"\","+
                "\"rarity\":\""+rarity+"\","+
                "\"gender\":\""+gender+"\","+
                "\"otherForms\":["+Array.toString(",",otherForms,"")+"],"+
                "\"moves\":"+moves.getAsJSON()+","+
                "\"syncMove\":"+syncMove.getAsJSON()+","+
                "\"passives\":"+passives.getAsJSON()+","+
                "\"stats\":"+stats.getAsJSON()+","+
                "\"grid\":"+grid.getAsJSON()+
                "}";
    }

    @Override
    public String toString() {
        return getAsJSON();
    }

    @Override
    public Pokemon parseFromJSON(JSONObject jo) {
        return parse(jo);
    }

    public static Pokemon parse(String json) {
        return parse(MiscUtil.parseJSONObject(json));
    }

    public static Pokemon parse(JSONObject jo) {
        String name                  = (String) jo.get("name");
        String trainer               = (String) jo.get("trainer");
        String[] typing              = Array.toStringArray(((org.json.simple.JSONArray) jo.get("typing")).toArray());
        String weakness              = (String) jo.get("weakness");
        String role                  = (String) jo.get("role");
        String rarity                = (String) jo.get("rarity");
        String gender                = (String) jo.get("gender");
        String[] otherForms          = Array.toStringArray(((org.json.simple.JSONArray) jo.get("otherForms")).toArray());
        JSONArray<Move> moves        = JSONArray.parse((org.json.simple.JSONArray) jo.get("moves"), EMPTY_MOVE);
        SyncMove syncMove            = SyncMove.parse((JSONObject) jo.get("syncMove"));
        JSONArray<Passive> passives  = JSONArray.parse((org.json.simple.JSONArray) jo.get("passives"), EMPTY_PASSIVE);
        StatRange stats              = StatRange.parse((JSONObject) jo.get("stats"));
        JSONArray<SyncGridNode> grid = JSONArray.parse((org.json.simple.JSONArray) jo.get("grid"), EMPTY_SYNC_GRID_NODE);
        return new Pokemon(name, trainer, typing, weakness, role, rarity, gender, otherForms, moves, syncMove, passives, stats, grid);
    }

    public static Pokemon emptyPokemon() {
        return new Pokemon("",
                "",
                new String[0],
                "",
                "",
                "",
                "",
                new String[0],
                new JSONArray<>(4, EMPTY_MOVE),
                EMPTY_SYNC_MOVE,
                new JSONArray<>(3, EMPTY_PASSIVE),
                EMPTY_STAT_RANGE,
                new JSONArray<>(7, EMPTY_SYNC_GRID_NODE));
    }
}