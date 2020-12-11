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

/**
 * Represents a usable Sync Move in Pokemon Masters, which is a unique move executable by a Sync Pair
 * due to the bond between the Trainer and the Pokemon.
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
public class SyncMove implements ParsableJSONObject<SyncMove>
{
    /** The name of this move. */
    public final String name;
    /** The type of this move. */
    public final String type;
    /** The Category of this move */
    public final String category;
    /* The maximum power is not included as a field in this object
     * because it is calculable by using Math.round(minPower*1.2) */
    /** The minimum power of this move */
    public final int minPower;
    /** The target(s) of this move */
    public final String target;
    /** The effect tag of this move. */
    public final String effectTag; //a public copy of effectAtForce of AbstractMove
    /** The additional effect(s) of this move apart from dealing damage. */
    public final String description;

    public SyncMove(String name, String type, String category,
                    int minPower,
                    String target, String effectTag, String description) {
        this.name = name;
        this.type = type;
        this.category = category;
        this.minPower = minPower;
        this.target = target;
        this.description=description;
        this.effectTag=effectTag;
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
                "\"target\":\""+target+"\","+
                "\"effect_tag\":\""+effectTag+"\","+
                "\"description\":\""+description+"\""+
                "}";
    }

    @Override
    public String toString() {
        return getAsJSON();
    }

    @Override
    public SyncMove parseFromJSON(String JSON) {
        return parse(JSON);
    }

    /**
     * Parses the given <code>String</code> to a Sync Move.
     *
     * @param json The JSON String to be parsed.
     *
     * @return The Sync Move object parsed from the JSON String.
     *
     * @throws com.vplaygames.PM4J.exceptions.ParseException If the JSON String was incorrectly formatted.
     * @throws ClassCastException if the required value was unable to be cast into the desired type.
     * @throws NullPointerException if the required values were not present in the String.
     */
    public static SyncMove parse(String json) {
        return parse(MiscUtil.parseJSONObject(json));
    }

    /**
     * Parses the given {@link JSONObject} to a Sync Move.
     *
     * @param jo The {@link JSONObject} to be parsed.
     *
     * @return The Sync Move object parsed from the JSON String.
     *
     * @throws ClassCastException if the required value was unable to be cast into the desired type.
     * @throws NullPointerException if the required values were not present in the {@link JSONObject}.
     */
    @SuppressWarnings("rawtypes")
    public static SyncMove parse(JSONObject jo) {
        String name        = (String) jo.get("name");
        String type        = (String) jo.get("type");
        String category    = (String) jo.get("category");
        int minPower       = MiscUtil.objectToInt(((HashMap) jo.get("power")).get("min_power"));
        String target      = (String) jo.get("target");
        String effectTag   = (String) jo.get("effect_tag");
        String description = (String) jo.get("description");
        return new SyncMove(name, type, category, minPower, target, effectTag, description);
    }

    static SyncMove emptySyncMove() {
        return new SyncMove("","","",0,"","","");
    }
}