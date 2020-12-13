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
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 * Represents a range of {@link Stats} of a Sync Pair in Pokemon Masters.
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
 * @see Stats
 */
public class StatRange implements ParsableJSONObject<StatRange> {
    /** The base {@link Stats} of this Stat Range */
    public final Stats base;
    /** The max {@link Stats} of this Stat Range */
    public final Stats max;

    public StatRange(Stats base, Stats max) {
        this.base = base;
        this.max = max;
    }

    @Override
    public String getAsJSON() {
        return "{\"base\":" + base.getAsJSON() + ",\"max\":" + max.getAsJSON() + "}";
    }

    @Override
    public String toString() {
        return getAsJSON();
    }

    @Override
    public StatRange parseFromJSON(String JSON) {
        return parse(JSON);
    }

    /**
     * Parses the given <code>String</code> to a Stat Range.
     *
     * @param json The JSON String to be parsed.
     *
     * @return The Stat Range object parsed from the JSON String.
     *
     * @throws com.vplaygames.PM4J.exceptions.ParseException if the JSON String was incorrectly formatted.
     * @throws ClassCastException if the required value was unable to be cast into the desired type.
     * @throws NullPointerException if the required values were not present in the String.
     */
    public static StatRange parse(String json) {
        return parse(MiscUtil.parseJSONObject(json));
    }

    /**
     * Parses the given {@link JSONObject} to a Stat Range.
     *
     * @param jo The {@link JSONObject} to be parsed.
     *
     * @return The Stat Range object parsed from the JSON String.
     *
     * @throws com.vplaygames.PM4J.exceptions.ParseException if the JSON String was incorrectly formatted.
     * @throws ClassCastException if the required value was unable to be cast into the desired type.
     * @throws NullPointerException if the required values were not present in the {@link JSONObject}.
     *
     * @see Stats#parse(JSONArray)
     */
    public static StatRange parse(JSONObject jo) {
        Stats base = Stats.parse((JSONArray) jo.get("base"));
        Stats max = Stats.parse((JSONArray) jo.get("max"));
        return new StatRange(base, max);
    }

    static StatRange emptyStatRange() {
        return new StatRange(Constants.EMPTY_STATS, Constants.EMPTY_STATS);
    }
}