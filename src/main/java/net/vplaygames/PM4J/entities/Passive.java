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

import net.vplaygames.PM4J.caches.SkillDataCache;
import net.vplaygames.vjson.JSONable;
import net.vplaygames.vjson.JSONObject;
import net.vplaygames.vjson.JSONValue;

/**
 * Represents a Passive Skill in Pokemon Masters.
 * <br>All of this class's variables are {@code public final} i.e. available without the use of getters
 * but not assignable.
 *
 * @since 1.0.0
 * @author Vaibhav Nargwani
 */
public class Passive implements JSONable {
    /** The name of this Passive Skill. */
    public final String name;
    /** The description of this Passive Skill. */
    public final String description;

    public Passive(String name, String description) {
        this.name = name;
        this.description = description;
        if (name.equals("")) return;
        if (!SkillDataCache.getInstance().containsKey(name)) {
            SkillDataCache.getInstance().put(name, new SkillDataCache.Node(this));
            if (Character.isDigit(name.charAt(name.length() - 1))) {
                String newName = name.substring(0, name.length() - 2);
                if (!SkillDataCache.getInstance().containsKey(newName)) {
                    new Passive(newName, "This is a group of " + newName + " 1-9.");
                }
            }
        }
    }

    @Override
    public String toString() {
        return toJSONString();
    }

    @Override
    public String toJSONString() {
        return "{\"name\":\"" + name + "\",\"description\":\"" + description + "\"}";
    }

    /**
     * Parses the given {@code String} to a Passive Skill
     *
     * @param json The JSON String to be parsed
     * @return The Passive Skill object parsed from the JSON String
     * @throws ClassCastException if the required value was unable to be cast into the desired type
     */
    public static Passive parse(String json) {
        return parse(JSONValue.parse(json));
    }

    /**
     * Parses the given {@code JSONValue} to a Passive Skill
     *
     * @param val The {@code JSONValue} to be parsed
     * @return The Passive Skill object parsed from the JSON String
     * @throws ClassCastException if the required value was unable to be cast into the desired type
     */
    public static Passive parse(JSONValue val) {
        JSONObject jo = val.asObject();
        String name = jo.get("name").asString();
        String description = jo.get("description").asString();
        return new Passive(name, description);
    }
}
