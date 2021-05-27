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

import net.vplaygames.PM4J.caches.TrainerDataCache;
import net.vplaygames.PM4J.core.Util;
import net.vplaygames.vjson.JSONObject;
import net.vplaygames.vjson.JSONValue;
import net.vplaygames.vjson.JSONable;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a Trainer who has formed a usable Sync Pair in Pokemon Masters.
 * <br>All of this class's variables are {@code public final} i.e. available without the use of getters
 * but not assignable.
 *
 * @since 1.0.0
 * @author Vaibhav Nargwani
 */
public class Trainer implements JSONable {
    /** The name of this trainer. */
    public final String name;
    /** The URL which corresponds to the image of this trainer on PokemasDB. */
    public final String img;
    /** The URL which corresponds to the data of this trainer on PokemasDB. */
    public final String data;
    /** The array of the names of all the Pokemon this trainer has formed a Sync Pair with. */
    public final String[] pokemon;
    /** The List of Pokemon this Trainer has formed a Sync Pair with */
    public final List<Pokemon> pokemonData;

    public Trainer(String name, String[] pokemon) {
        this.name = name;
        this.pokemon = pokemon;
        this.img = "https://pokemasdb.com/trainer/image/" + name + ".png";
        this.data = "https://pokemasdb.com/trainer/" + resolve(name);
        pokemonData = new ArrayList<>();
        if (name.equals("")) {
            return;
        }
        TrainerDataCache.getInstance().put(name, this);
    }

    /**
     * Adds a Pokemon to the list of Pokemon
     *
     * @param pokemon the Pokemon to be added
     * @return This instance. Useful for chaining.
     */
    public Trainer add(Pokemon pokemon) {
        pokemonData.add(pokemon);
        return this;
    }

    /**
     * Replaces non URL-friendly characters to URL-friendly characters
     *
     * @param name the <code>String</code> to resolve
     * @return a URL-friendly <code>String</code>
     */
    public static String resolve(String name) {
        return name.replaceAll("\\s", "%20");
    }

    @Override
    public String toString() {
        return toJSONString();
    }

    @Override
    public String toJSONString() {
        return "{" +
            "\"name\":\"" + name + "\"," +
            "\"pokemon\":" + Util.toString(pokemon) + "," +
            "\"image\":\"" + img + "\"," +
            "\"data\":\"" + data + "\"," +
            "\"pokemonData\":" + pokemonData +
            "}";
    }

    /**
     * Parses the given {@code String} to Trainer Data
     *
     * @param json The JSON String to be parsed
     * @return The Trainer Data object parsed from the JSON String
     * @throws ClassCastException   if the required value was unable to be cast into the desired type
     * @throws NullPointerException if the required values were not present in the String
     */
    public static Trainer parse(String json) {
        return parse(JSONValue.parse(json));
    }

    /**
     * Parses the given {@code JSONValue} to a Trainer Data
     *
     * @param val The {@code JSONValue} to be parsed
     * @return The Trainer Data object parsed from the JSON String
     * @throws ClassCastException   if the required value was unable to be cast into the desired type
     * @throws NullPointerException if the required values were not present in the {@code JSONValue}
     */
    public static Trainer parse(JSONValue val) {
        JSONObject jo = val.asObject();
        String name = jo.get("name").asString();
        String[] pokemon = jo.get("pokemon").asList(JSONValue::asString).toArray(new String[0]);
        Trainer trainer = new Trainer(name, pokemon);
        jo.get("pokemonData").asList(Pokemon::parse).forEach(trainer::add);
        return trainer;
    }
}
