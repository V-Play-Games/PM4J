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

public class Passive implements ParsableJSONObject<Passive> {
    public final String name;
    public final String description;

    public Passive(String name, String description) {
        this.name = name;
        this.description = description;
    }

    @Override
    public String getAsJSON() {
        return "{" + "\"name\":\"" + name + "\",\"description\":\"" + description + "\"}";
    }

    @Override
    public String toString() {
        return getAsJSON();
    }

    @Override
    public Passive parseFromJSON(JSONObject JSON) {
        return parse(JSON);
    }

    public static Passive parse(String json) {
        return parse(MiscUtil.parseJSONObject(json));
    }

    public static Passive parse(JSONObject jo) {
        String name = (String) jo.get("name");
        String description = (String) jo.get("description");
        return new Passive(name, description);
    }

    static Passive emptyPassive() {
        return new Passive("", "");
    }
}
