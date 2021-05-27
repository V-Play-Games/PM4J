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

import net.vplaygames.PM4J.core.Util;
import net.vplaygames.vjson.JSONable;
import net.vplaygames.vjson.JSONValue;

/**
 * Represents the Stats of a Sync Pair in Pokemon Masters.
 * <br>All of this class's variables are {@code public final} i.e. available without the use of getters
 * but not assignable.
 *
 * @since 1.0.0
 * @author Vaibhav Nargwani
 *
 * @see StatRange
 */
public class Stats implements JSONable {
    /** The HP Stat */
    public final int hp;
    /** The Physical Attack Stat */
    public final int atk;
    /** The Physical Defense Stat */
    public final int def;
    /** The Special Attack Stat */
    public final int spAtk;
    /** The Special Defense Stat */
    public final int spDef;
    /** The Speed Stat */
    public final int speed;
    /**
     * The Bulk Stat. It is a hidden stat which was used
     * in past versions of Pokemon Masters to determine
     * which ally will be targeted by the opponents.
     */
    public final int bulk;

    public Stats(String hp, String atk, String def, String spAtk, String spDef, String speed, String bulk) {
        this(Util.toInt(hp),
            Util.toInt(atk),
            Util.toInt(def),
            Util.toInt(spAtk),
            Util.toInt(spDef),
            Util.toInt(speed),
            Util.toInt(bulk));
    }

    public Stats(int hp, int atk, int def, int spAtk, int spDef, int speed, int bulk) {
        this.hp = hp;
        this.atk = atk;
        this.def = def;
        this.spAtk = spAtk;
        this.spDef = spDef;
        this.speed = speed;
        this.bulk = bulk;
    }

    @Override
    public String toString() {
        return toJSONString();
    }

    @Override
    public String toJSONString() {
        return "[" +
            "[\"HP\",\"" + hp + "\"]," +
            "[\"ATK\",\"" + atk + "\"]," +
            "[\"DEF\",\"" + def + "\"]," +
            "[\"Sp. ATK\",\"" + spAtk + "\"]," +
            "[\"Sp. DEF\",\"" + spDef + "\"]," +
            "[\"Speed\",\"" + speed + "\"]" +
            ",[\"Bulk\",\"" + bulk + "\"]" +
            "]";
    }

    /**
     * Parses the given {@code String} to Stats
     *
     * @param json The JSON String to be parsed
     * @return The Stats object parsed from the JSON String
     * @throws ClassCastException   if the required value was unable to be cast into the desired type
     * @throws NullPointerException if the required values were not present in the String
     */
    public static Stats parse(String json) {
        return parse(JSONValue.parse(json));
    }

    /**
     * Parses the given {@code JSONValue} to Stats
     *
     * @param val The {@code JSONValue} to be parsed
     * @return The Stats object parsed from the JSON String
     * @throws ClassCastException             if the required value was unable to be cast into the desired type
     * @throws NullPointerException           if the required values were not present in the {@code JSONValue}
     * @throws ArrayIndexOutOfBoundsException if enough values were not present
     */
    public static Stats parse(JSONValue val) {
        int[] stats = val.asList(a -> a.asList(JSONValue::asString).get(1)).stream().mapToInt(Util::toInt).toArray();
        return new Stats(stats[0], stats[1], stats[2], stats[3], stats[4], stats[5], stats[6]);
    }
}
