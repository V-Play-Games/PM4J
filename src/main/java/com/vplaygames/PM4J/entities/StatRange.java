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

public class StatRange implements ParsableJSONObject<StatRange> {
    public final Stats base;
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
    public StatRange parseFromJSON(JSONObject JSON) {
        return parse(JSON);
    }

    public static StatRange parse(String json) {
        return parse(MiscUtil.parseJSONObject(json));
    }

    public static StatRange parse(JSONObject jo) {
        Stats base = Stats.parse((JSONArray) jo.get("base"));
        Stats max = Stats.parse((JSONArray) jo.get("max"));
        return new StatRange(base, max);
    }

    static StatRange emptyStatRange() {
        return new StatRange(Constants.EMPTY_STATS, Constants.EMPTY_STATS);
    }
}