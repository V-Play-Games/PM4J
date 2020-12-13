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
import com.vplaygames.PM4J.util.Strings;
import org.json.simple.JSONArray;

/**
 * Represents the Stats of a Sync Pair in Pokemon Masters.
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
 * @see StatRange
 */
public class Stats implements ParsableJSONObject<Stats>
{
    private final boolean hasBulk;
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
        this(Strings.toInt(hp),
                Strings.toInt(atk),
                Strings.toInt(def),
                Strings.toInt(spAtk),
                Strings.toInt(spDef),
                Strings.toInt(speed),
                Strings.toInt(bulk));
    }

    public Stats(int hp, int atk, int def, int spAtk, int spDef, int speed, int bulk) {
        this.hp = hp;
        this.atk = atk;
        this.def = def;
        this.spAtk = spAtk;
        this.spDef = spDef;
        this.speed = speed;
        this.bulk = bulk;
        this.hasBulk = bulk != 0;
    }

    @Override
    public String getAsJSON() {
        return "["+
                "[\"HP\",\""+hp+"\"],"+
                "[\"ATK\",\""+atk+"\"],"+
                "[\"DEF\",\""+def+"\"],"+
                "[\"Sp. ATK\",\""+spAtk+"\"],"+
                "[\"Sp. DEF\",\""+spDef+"\"],"+
                "[\"Speed\",\""+speed+"\"]"+
                (hasBulk?",[\"Bulk\",\""+bulk+"\"]":"")+
                "]";
    }

    @Override
    public String toString() {
        return getAsJSON();
    }

    @Override
    public Stats parseFromJSON(String JSON) {
        return parse(JSON);
    }

    /**
     * Parses the given <code>String</code> to Stats.
     *
     * @param json The JSON String to be parsed.
     *
     * @return The Stats object parsed from the JSON String.
     *
     * @throws com.vplaygames.PM4J.exceptions.ParseException If the JSON String was incorrectly formatted.
     * @throws ClassCastException if the required value was unable to be cast into the desired type.
     * @throws NullPointerException if the required values were not present in the String.
     */
    public static Stats parse(String json) {
        return parse(MiscUtil.parseJSONArray(json));
    }

    /**
     * Parses the given {@link JSONArray} to Stats.
     *
     * @param ja The {@link JSONArray} to be parsed.
     *
     * @return The Stats object parsed from the JSON String.
     *
     * @throws ClassCastException if the required value was unable to be cast into the desired type.
     * @throws NullPointerException if the required values were not present in the {@link JSONArray}.
     * @throws ArrayIndexOutOfBoundsException if enough values were not present.
     */
    @SuppressWarnings("unchecked")
    public static Stats parse(JSONArray ja) {
        final int[] i = {0};
        String[] stats=new String[ja.size()];
        ja.iterator().forEachRemaining(val -> stats[i[0]++] = (String) ((JSONArray) val).get(1));
        return new Stats(stats[0],stats[1],stats[2],stats[3],stats[4],stats[5],(stats.length==7?stats[6]:""));
    }

    static Stats emptyStats() {
        return new Stats(0,0,0,0,0,0,0);
    }
}
