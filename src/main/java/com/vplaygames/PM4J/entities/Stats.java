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
import org.json.simple.JSONObject;

public class Stats implements ParsableJSONObject<Stats>
{
    private final boolean hasBulk;
    public final int hp;
    public final int atk;
    public final int def;
    public final int spAtk;
    public final int spDef;
    public final int speed;
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
    public Stats parseFromJSON(JSONObject JSON) {
        return parse(MiscUtil.parseJSONArray(JSON.toJSONString()));
    }

    public static Stats parse(String json) {
        return parse(MiscUtil.parseJSONArray(json));
    }

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
