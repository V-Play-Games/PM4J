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

import net.vplaygames.PM4J.caches.MoveDataCache;
import net.vplaygames.vjson.JSONable;
import net.vplaygames.vjson.JSONObject;
import net.vplaygames.vjson.JSONValue;

/**
 * Represents a usable move in Pokemon Masters, which is one of the four (or three) moves
 * a moveset of Pokemon contains and not a Sync Move.
 * <br>All of this class's variables are {@code public final} i.e. available without the use of getters
 * but not assignable.
 *
 * @since 1.0.0
 * @author Vaibhav Nargwani
 *
 * @see SyncMove
 */
public class Move implements JSONable {
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
    public final String effect;

    public Move(String name, String type, String category,
                int minPower, int accuracy,
                String target,
                int cost, int uses, String effect) {
        this.name = name;
        this.type = type;
        this.category = category;
        this.minPower = minPower;
        this.target = target;
        this.accuracy = accuracy;
        this.cost = cost;
        this.uses = uses;
        this.effect = effect;
        if (!MoveDataCache.getInstance().containsKey(name)) {
            MoveDataCache.getInstance().put(name, new MoveDataCache.Node(this));
        }
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
            "\"target\":\"" + target + "\"," +
            "\"power\":{" +
            "\"min_power\":" + minPower + "," +
            "\"max_power\":" + Math.round(Math.floor(1.2 * minPower)) +
            "},\"accuracy\":" + accuracy + "," +
            "\"cost\":" + cost + "," +
            "\"uses\":" + uses + "," +
            "\"effect\":\"" + effect + "\"" +
            "}";
    }

    /**
     * Parses the given {@code String} to a Move
     *
     * @param json The JSON String to be parsed
     * @return The Move object parsed from the JSON String
     * @throws ClassCastException   if the required value was unable to be cast into the desired type
     * @throws NullPointerException if the required values were not present in the String
     */
    public static Move parse(String json) {
        return parse(JSONObject.parse(json));
    }

    /**
     * Parses the given {@code JSONValue} to a Move
     *
     * @param val The {@code JSONValue} to be parsed
     * @return The Move object parsed from the JSON String
     * @throws ClassCastException   if the required value was unable to be cast into the desired type
     * @throws NullPointerException if the required values were not present in the {@code JSONValue}
     */
    public static Move parse(JSONValue val) {
        JSONObject jo = val.asObject();
        String name     = jo.get("name").asString();
        String type     = jo.get("type").asString();
        String category = jo.get("category").asString();
        String target   = jo.get("target").asString();
        String effect   = jo.get("effect").asString();
        int minPower    = jo.get("power").asObject().get("min_power").asInt();
        int accuracy    = jo.get("accuracy").asInt();
        int cost        = jo.get("cost").asInt();
        int uses        = jo.get("uses").asInt();
        return new Move(name, type, category, minPower, accuracy, target, cost, uses, effect);
    }
}
