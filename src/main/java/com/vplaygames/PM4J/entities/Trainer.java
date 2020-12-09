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

import com.vplaygames.PM4J.jsonFramework.JSONArray;
import com.vplaygames.PM4J.jsonFramework.ParsableJSONObject;
import com.vplaygames.PM4J.util.Array;
import com.vplaygames.PM4J.util.MiscUtil;
import org.json.simple.JSONObject;

import static com.vplaygames.PM4J.entities.Constants.EMPTY_POKEMON;
import static com.vplaygames.PM4J.util.MiscUtil.objectToInt;

/**
 * Represents a Trainer who has formed a scoutable or usable Sync Pair in Pokemon Masters.
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
public class Trainer implements ParsableJSONObject<Trainer> {
    /** The name of this trainer. */
    public final String name;
    /** The rarity (no. of stars) of this trainer. */
    public final int rarity;
    /** The String url which corresponds to the image of this trainer on PokemasDB. */
    public final String img;
    /** The String url which corresponds to the data of this trainer. on PokemasDB. */
    public final String data;
    /** The Array of the names of all the Pokemon this trainer has formed a Sync Pair with. */
    protected final String[] pokemon;
    /** The List of Pokemon this Trainer has formed a Sync Pair with */
    public final JSONArray<Pokemon> pokemonData;

    public Trainer(String name, int rarity, String[] pokemon) {
        this.name = name;
        this.rarity = rarity;
        this.img = "https://pokemasdb.com/trainer/image/" + resolve(name) + ".png";
        this.data = "https://pokemasdb.com/trainer/" + resolve(name);
        this.pokemon = pokemon;
        pokemonData = new JSONArray<>(0, EMPTY_POKEMON);
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
     * Sets the initialized state on the Pokemon list,
     * which makes the list unmodifiable.
     *
     * @return This instance. Useful for chaining.
     */
    public Trainer initialized() {
        pokemonData.initialized();
        return this;
    }

    /**
     * Replaces non URL-friendly characters to URL-friendly characters
     *
     * @param name the <code>String</code> to resolve
     * @return a URL-friendly <code>String</code>
     */
    public static String resolve(String name) {
        return name.contains(" ") ? Array.toString("%20", (Object[]) name.split(" "), "") : name;
    }

    /**
     * Returns the Array of the names of all the Pokemon this trainer has formed a Sync Pair with.
     *
     * @return the Array of the names of all the Pokemon this trainer has formed a Sync Pair with.
     */
    public String[] getPokemon() {
        return pokemon;
    }

    @Override
    public String getAsJSON() {
        return "{" +
                "\"name\":\"" + name + "\"," +
                "\"rarity\":" + rarity + "," +
                "\"pokemon\":[" + Array.toString(",", pokemon, "") + "]," +
                "\"image\":\"" + img + "\"," +
                "\"data\":\"" + data + "\"," +
                "\"pokemonData\":" + pokemonData.getAsJSON() +
                "}";
    }

    @Override
    public String toString() {
        return getAsJSON();
    }

    @Override
    public Trainer parseFromJSON(String JSON) {
        return parse(JSON);
    }

    /**
     * Parses the given <code>String</code> to Trainer Data.
     *
     * @param json The JSON String to be parsed.
     * @return The Trainer Data object parsed from the JSON String.
     * @throws com.vplaygames.PM4J.exceptions.ParseException if the JSON String was incorrectly formatted.
     * @throws ClassCastException                            if the required value was unable to be cast into the desired type.
     * @throws NullPointerException                          if the required values were not present in the String.
     */
    public static Trainer parse(String json) {
        return parse(MiscUtil.parseJSONObject(json));
    }

    /**
     * Parses the given {@link JSONObject} to a Trainer Data.
     *
     * @param jo The {@link JSONObject} to be parsed.
     * @return The Trainer Data object parsed from the JSON String.
     * @throws ClassCastException   if the required value was unable to be cast into the desired type.
     * @throws NullPointerException if the required values were not present in the {@link JSONObject}.
     */
    public static Trainer parse(JSONObject jo) {
        String name = (String) jo.get("name");
        int rarity = objectToInt(jo.get("rarity"));
        String[] pokemon = Array.toStringArray(((org.json.simple.JSONArray) jo.get("pokemon")).toArray());
        Trainer trainer = new Trainer(name, rarity, pokemon);
        JSONArray.parse((org.json.simple.JSONArray) jo.get("pokemonData"), EMPTY_POKEMON)
                .forEach(trainer::add);
        return trainer;
    }

    static Trainer emptyTrainer() {
        return new Trainer("", 1, new String[0]).add(EMPTY_POKEMON).initialized();
    }
}