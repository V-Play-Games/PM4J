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

import net.vplaygames.vjson.JSONable;
import net.vplaygames.vjson.JSONObject;
import net.vplaygames.vjson.JSONValue;

/**
 * Represents a usable Sync Move in Pokemon Masters, which is a unique move executable by a Sync Pair
 * due to the bond between the Trainer and the Pokemon.
 * <br>All of this class's variables are {@code public final} i.e. available without the use of getters
 * but not assignable.
 *
 * @since 1.0.0
 * @author Vaibhav Nargwani
 */
public class SyncMove implements JSONable {
    /** The name of this move. */
    public final String name;
    /** The type of this move. */
    public final String type;
    /** The Category of this move */
    public final String category;
    /* The maximum power is not included as a field in this object
       because it is calculable by using Math.round(Math.floor(1.2 * minPower)) */
    /** The minimum power of this move */
    public final int minPower;
    /** The target(s) of this move */
    public final String target;
    /** The additional effect(s) of this move apart from dealing damage. */
    public final String description;

    public SyncMove(String name, String type, String category,
                    int minPower,
                    String target, String description) {
        this.name = name;
        this.type = type;
        this.category = category;
        this.minPower = minPower;
        this.target = target;
        this.description = description;
    }

    @Override
    public String toString() {
        return toJSONString();
    }

    @Override
    public String toJSONString() {
        return "{" +
            "\"name\":\"" + name + "\"," +
            "\"type\":\"" + type + "\"," +
            "\"category\":\"" + category + "\"," +
            "\"power\":" +
            "{" +
            "\"min_power\":" + minPower + "," +
            "\"max_power\":" + Math.round(Math.floor(1.2 * minPower)) +
            "}," +
            "\"target\":\"" + target + "\"," +
            "\"description\":\"" + description + "\"" +
            "}";
    }

    /**
     * Parses the given {@code String} to a Sync Move
     *
     * @param json The JSON String to be parsed
     * @return The Sync Move object parsed from the JSON String
     * @throws net.vplaygames.PM4J.exceptions.ParseException if the JSON String was incorrectly formatted
     * @throws ClassCastException                            if the required value was unable to be cast into the desired type
     * @throws NullPointerException                          if the required values were not present in the String
     */
    public static SyncMove parse(String json) {
        return parse(JSONValue.parse(json));
    }

    /**
     * Parses the given {@code JSONValue} to a Sync Move
     *
     * @param val The {@code JSONValue} to be parsed
     * @return The Sync Move object parsed from the JSON String
     * @throws ClassCastException   if the required value was unable to be cast into the desired type
     * @throws NullPointerException if the required values were not present in the {@code JSONValue}
     */
    public static SyncMove parse(JSONValue val) {
        JSONObject jo = val.asObject();
        String name        = jo.get("name").asString();
        String type        = jo.get("type").asString();
        String category    = jo.get("category").asString();
        String target      = jo.get("target").asString();
        String description = jo.get("description").asString();
        int minPower       = jo.get("power").asObject().get("min_power").asInt();
        return new SyncMove(name, type, category, minPower, target, description);
    }
}
