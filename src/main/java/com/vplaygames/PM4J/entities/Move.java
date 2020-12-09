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

import java.util.HashMap;

import static com.vplaygames.PM4J.util.MiscUtil.objectToInt;

/**
 * Represents a usable move in Pokemon Masters, which is one of the four moves a moveset of Pokemon contains
 * and not a Sync Move.
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
public class Move extends AbstractMove implements ParsableJSONObject<Move>
{
    /** The accuracy of this move. */
    public final int accuracy;
    /**
     * The amount of gauges this move this moves consumes in order to be used.
     * It is 0 for Quick Moves, Trainer Moves, X Items and some other items (like Move Gauge Boost)
     */
    public final int cost;
    /** The amount of MP (Move Point) this move has. */
    public final int uses;
    /** The additional effect(s) of this move apart from dealing damage. */
    public final String effect; //a public copy of effectAtForce of AbstractMove

    public Move(String name, String type, String category,
                int minPower, int accuracy,
                String target,
                int cost, int uses, String effect) {
        super(name,type,category,minPower,target,effect);
        this.accuracy=accuracy;
        this.cost=cost;
        this.uses=uses;
        this.effect = this.effectAtForce;
    }

    @Override
    public String getAsJSON() {
        return "{"+
                "\"name\":\""+name+"\","+
                "\"type\":\""+type+"\","+
                "\"category\":\""+category+"\","+
                "\"power\":"+
                    "{"+
                    "\"min_power\":"+minPower+","+
                    "\"max_power\":"+Math.round(1.2*minPower)+
                    "},"+
                "\"accuracy\":"+accuracy+","+
                "\"target\":\""+target+"\","+
                "\"cost\":"+((cost==0)?"\"\"":cost)+","+
                "\"uses\":"+((uses==0)?"null":uses)+","+
                "\"effect\":\""+effect+"\","+
                "\"unlock_requirements\":[]"+
                "}";
    }

    @Override
    public String toString() {
        return getAsJSON();
    }

    @Override
    public Move parseFromJSON(String JSON) {
        return parse(JSON);
    }

    /**
     * Parses the given <code>String</code> to a Move.
     *
     * @param json The JSON String to be parsed.
     *
     * @return The Move object parsed from the JSON String.
     *
     * @throws com.vplaygames.PM4J.exceptions.ParseException If the JSON String was incorrectly formatted.
     * @throws ClassCastException if the required value was unable to be cast into the desired type.
     * @throws NullPointerException if the required values were not present in the String.
     */
    public static Move parse(String json) {
        return parse(MiscUtil.parseJSONObject(json));
    }

    /**
     * Parses the given {@link JSONObject} to a Move.
     *
     * @param jo The {@link JSONObject} to be parsed.
     *
     * @return The Move object parsed from the JSON String.
     *
     * @throws ClassCastException if the required value was unable to be cast into the desired type.
     * @throws NullPointerException if the required values were not present in the {@link JSONObject}.
     */
    @SuppressWarnings("rawtypes")
    public static Move parse(JSONObject jo) {
        String name     = (String) jo.get("name");
        String type     = (String) jo.get("type");
        String category = (String) jo.get("category");
        int minPower    = objectToInt(((HashMap) jo.get("power")).get("min_power"));
        int accuracy    = objectToInt(jo.get("accuracy"));
        String target   = (String) jo.get("target");
        int cost        = (jo.get("cost") instanceof String) ? 0 : objectToInt(jo.get("cost"));
        int uses        = (jo.get("uses") == null) ? 0 : objectToInt(jo.get("uses"));
        String effect   = (String) jo.get("effect");
        return new Move(name, type, category, minPower, accuracy, target, cost, uses, effect);
    }

    static Move emptyMove() {
        return new Move("","","",0,0,"",0,0,"");
    }
}
