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
import com.vplaygames.PM4J.util.Array;
import com.vplaygames.PM4J.util.MiscUtil;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import static com.vplaygames.PM4J.util.MiscUtil.objectToInt;

public class Trainer extends AbstractTrainer implements ParsableJSONObject<Trainer> {
    public Trainer(String name, int rarity, String... pokemon) {
        super(name,
                rarity,
                "https://pokemasdb.com/trainer/image/" + resolve(name) + ".png",
                "https://pokemasdb.com/trainer/" + resolve(name),
                pokemon);
    }

    public static String resolve(String name) {
        return name.contains(" ") ? Array.toString("%20", (Object[]) name.split(" "), "") : name;
    }

    @Override
    public Trainer parseFromJSON(JSONObject jo) {
        return parse(jo);
    }

    public static Trainer parse(String json) {
        return parse(MiscUtil.parseJSONObject(json));
    }

    public static Trainer parse(JSONObject jo) {
        String name = (String) jo.get("name");
        int rarity = objectToInt(jo.get("rarity"));
        String[] pokemon = Array.toStringArray(((JSONArray) jo.get("pokemon")).toArray());
        return new Trainer(name, rarity, pokemon);
    }

    static Trainer emptyTrainer() {
        return new Trainer("", 0, "");
    }
}