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

/**
 * Represents a Passive Skill in Pokemon Masters.
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
public class Passive implements ParsableJSONObject<Passive> {
    /** The name of this Passive Skill. */
    public final String name;
    /** The description of this Passive Skill. */
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
    public Passive parseFromJSON(String JSON) {
        return parse(JSON);
    }

    /**
     * Parses the given <code>String</code> to a Passive Skill.
     *
     * @param json The JSON String to be parsed.
     *
     * @return The Passive Skill object parsed from the JSON String.
     *
     * @throws com.vplaygames.PM4J.exceptions.ParseException if the JSON String was incorrectly formatted.
     * @throws ClassCastException if the required value was unable to be cast into the desired type.
     */
    public static Passive parse(String json) {
        return parse(MiscUtil.parseJSONObject(json));
    }

    /**
     * Parses the given {@link JSONObject} to a Passive Skill.
     *
     * @param jo The {@link JSONObject} to be parsed.
     *
     * @return The Passive Skill object parsed from the JSON String.
     *
     * @throws ClassCastException if the required value was unable to be cast into the desired type.
     */
    public static Passive parse(JSONObject jo) {
        String name = (String) jo.get("name");
        String description = (String) jo.get("description");
        return new Passive(name, description);
    }

    static Passive emptyPassive() {
        return new Passive("", "");
    }
}
