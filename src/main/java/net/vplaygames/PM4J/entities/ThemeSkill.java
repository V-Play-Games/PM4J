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

import net.vplaygames.PM4J.caches.ThemeSkillDataCache;
import net.vplaygames.vjson.JSONObject;
import net.vplaygames.vjson.JSONValue;
import net.vplaygames.vjson.JSONable;

import java.util.Arrays;
import java.util.List;

/**
 * Represents a Theme Skill which almost all Pokemon have.
 * These theme skills provide buffs to the whole team when two or more sync pairs share the same theme skill.
 * Theme Skills do not work in any stage under Co-op mode.
 * <br>All of this class's variables are {@code public final} i.e. available without the use of getters
 * but not assignable.
 *
 * @since 1.1.0
 * @author Vaibhav Nargwani
 */
public class ThemeSkill implements JSONable {
    /** The name of this theme skill */
    public final String name;
    /**
     * The tag of this theme skill,
     * which is basically the name of this theme skill
     * but without the role
     */
    public final String tag;
    /** The category of this theme skill */
    public final String category;
    /** The condition for the activation of this theme skill */
    public final String condition;
    /** The effects of this theme skill after activation */
    public final List<Effect> effects;

    public ThemeSkill(String name, String tag, String category, String condition,
                      List<Effect> effects) {
        this.name = name;
        this.tag = tag;
        this.category = category;
        this.condition = condition;
        this.effects = effects;
        if (!ThemeSkillDataCache.getInstance().containsKey(name)) {
            ThemeSkillDataCache.getInstance().put(name, new ThemeSkillDataCache.Node(this));
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
            "\"tag\":\"" + tag + "\"," +
            "\"category\":\"" + category + "\"," +
            "\"condition\":\"" + condition + "\"," +
            "\"effects\":" + effects +
            "}";
    }

    /**
     * Parses the given {@code String} to Theme Skill
     *
     * @param json The JSON String to be parsed
     * @return The Theme Skill object parsed from the JSON String
     * @throws ClassCastException   if the required value was unable to be cast into the desired type
     * @throws NullPointerException if the required values were not present in the String
     */
    public static ThemeSkill parse(String json) {
        return parse(JSONValue.parse(json));
    }

    /**
     * Parses the given {@code JSONValue} to a Theme Skill
     *
     * @param val The {@code JSONValue} to be parsed
     * @return The Theme Skill object parsed from the JSON String
     * @throws ClassCastException   if the required value was unable to be cast into the desired type
     * @throws NullPointerException if the required values were not present in the {@code JSONValue}
     */
    public static ThemeSkill parse(JSONValue val) {
        JSONObject jo    = val.asObject();
        String name      = jo.get("name").asString();
        String tag       = jo.get("tag").asString();
        String category  = jo.get("category").asString();
        String condition = jo.get("condition").asString();
        List<Effect> effects = jo.get("effects").asList(Effect::parse);
        return new ThemeSkill(name, tag, category, condition, effects);
    }

    /**
     * Represents an Effect which a Theme Skill grants, if activated.
     * <br>All of this class's variables are {@code public final} i.e. available without the use of getters
     * but not assignable.
     *
     * @since 1.1.0
     * @author Vaibhav Nargwani
     */
    public static class Effect implements JSONable {
        /** The description of this theme skill effect */
        public final String description;
        /**
         * The amount of stats increased by this theme skill effect,
         *  which increase by the level of the theme skill
         */
        public final int[] values;

        public Effect(String description, int[] values) {
            this.description = description;
            this.values = values;
        }

        @Override
        public String toString() {
            return toJSONString();
        }

        @Override
        public String toJSONString() {
            return "{" +
                "\"description\":\"" + description + "\"," +
                "\"values\":" + Arrays.toString(values) +
                "}";
        }

        /**
         * Parses the given {@code String} to Theme Skill Effect
         *
         * @param json The JSON String to be parsed
         * @return The Theme Skill Effect object parsed from the JSON String
         * @throws ClassCastException   if the required value was unable to be cast into the desired type
         * @throws NullPointerException if the required values were not present in the String
         */
        public static ThemeSkill.Effect parse(String json) {
            return parse(JSONValue.parse(json));
        }

        /**
         * Parses the given {@code JSONValue} to a Theme Skill Effect
         *
         * @param val The {@code JSONValue} to be parsed
         * @return The Theme Skill Effect object parsed from the JSON String
         * @throws ClassCastException   if the required value was unable to be cast into the desired type
         * @throws NullPointerException if the required values were not present in the {@code JSONValue}
         */
        public static ThemeSkill.Effect parse(JSONValue val) {
            JSONObject jo = val.asObject();
            String description = jo.get("description").asString();
            int[] values = jo.get("values").asArray().stream().mapToInt(JSONValue::asInt).toArray();
            return new Effect(description, values);
        }
    }
}
