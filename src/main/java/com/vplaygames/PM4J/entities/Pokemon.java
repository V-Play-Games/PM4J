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

/**
 * Represents a Pokemon in Pokemon Masters, which has formed a Sync Pair with a trainer.
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
 * @see com.vplaygames.PM4J.entities.AbstractMove
 * @see SyncMove
 */
public class Pokemon implements ParsableJSONObject<Pokemon>
{
    /** The name of this Pokemon */
    public final String name;
    /** The name of the Trainer of this Pokemon */
    public final String trainer;
    /** The typing of this Pokemon */
    protected final String[] typing;
    /** The weakness of this Pokemon */
    public final String weakness;
    /** The role of this Pokemon [Strike (Special/Physical)/Support/Tech */
    public final String role;
    /** The rarity of this Pokemon (in no. of Stars) */
    public final int rarity;
    /** The gender of this Pokemon */
    public final String gender;
    /** The name of other forms this Pokemon */
    protected final String[] otherForms;
    /** The moves of this Pokemon */
    public final JSONArray<Move> moves;
    /** The Sync Move of this Pokemon */
    public final SyncMove syncMove;
    /** The Passive Skills of this Pokemon */
    public final JSONArray<Passive> passives;
    /** The Stats of this Pokemon */
    public final StatRange stats;
    /** The Sync Grid of this Pokemon */
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

    /**
     * The Array of the names of Other forms of this Pokemon
     *
     * @return a deep copy of The Array of the names of Other forms of this Pokemon
     */
    public String[] getOtherForms() {
        return otherForms;
    }

    /**
     * The Array of the typing of this Pokemon
     *
     * @return a deep copy of The Array of the typing of this Pokemon
     */
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
    public Pokemon parseFromJSON(String JSON) {
        return parse(JSON);
    }

    /**
     * Parses the given <code>String</code> to a Pokemon.
     *
     * @param json The JSON String to be parsed.
     *
     * @return The Pokemon object parsed from the JSON String.
     *
     * @throws com.vplaygames.PM4J.exceptions.ParseException If the JSON String was incorrectly formatted.
     * @throws ClassCastException if the required value was unable to be cast into the desired type.
     * @throws NullPointerException if the required values were not present in the String.
     */
    public static Pokemon parse(String json) {
        return parse(MiscUtil.parseJSONObject(json));
    }

    /**
     * Parses the given {@link JSONObject} to a Pokemon.
     *
     * @param jo The {@link JSONObject} to be parsed.
     *
     * @return The Pokemon object parsed from the JSON String.
     *
     * @throws ClassCastException if the required value was unable to be cast into the desired type.
     * @throws NullPointerException if the required values were not present in the {@link JSONObject}.
     */
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