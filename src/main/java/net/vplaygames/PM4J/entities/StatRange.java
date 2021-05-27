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
 * Represents a range of {@link Stats} (from base to max) of a Sync Pair in Pokemon Masters.
 * <br>All of this class's variables are {@code public final} i.e. available without the use of getters
 * but not assignable.
 *
 * @since 1.0.0
 * @author Vaibhav Nargwani
 * @see Stats
 */
public class StatRange implements JSONable {
    /** The base {@link Stats} of this Stat Range */
    public final Stats base;
    /** The max {@link Stats} of this Stat Range */
    public final Stats max;

    public StatRange(Stats base, Stats max) {
        this.base = base;
        this.max = max;
    }

    @Override
    public String toString() {
        return toJSONString();
    }

    @Override
    public String toJSONString() {
        return "{\"base\":" + base.toJSONString() + ",\"max\":" + max.toJSONString() + "}";
    }

    /**
     * Parses the given {@code String} to a Stat Range
     *
     * @param json The JSON String to be parsed
     * @return The Stat Range object parsed from the JSON String
     * @throws ClassCastException   if the required value was unable to be cast into the desired type
     * @throws NullPointerException if the required values were not present in the String
     */
    public static StatRange parse(String json) {
        return parse(JSONValue.parse(json));
    }

    /**
     * Parses the given {@code JSONValue} to a Stat Range
     *
     * @param val The {@code JSONValue} to be parsed
     * @return The Stat Range object parsed from the JSON String
     * @throws ClassCastException   if the required value was unable to be cast into the desired type
     * @throws NullPointerException if the required values were not present in the {@code JSONValue}
     * @see Stats#parse(JSONValue)
     */
    public static StatRange parse(JSONValue val) {
        JSONObject jo = val.asObject();
        Stats base = Stats.parse(jo.get("base"));
        Stats max = Stats.parse(jo.get("max"));
        return new StatRange(base, max);
    }
}
