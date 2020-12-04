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

import java.util.HashMap;

import static com.vplaygames.PM4J.util.MiscUtil.objectToInt;

public class Move extends AbstractMove implements ParsableJSONObject<Move>
{
    public final int accuracy;
    public final int cost;
    public final int uses;
    public final String effect; //a public copy of effectAtForce of AbstractMove

    public Move(String name, String type, String category,
                int minPower, int accuracy,
                String target,
                int cost, int uses, String effect) {
        super(name,type,category,minPower,target,effect);
        this.accuracy=accuracy;
        this.cost=cost;
        this.uses=uses;
        this.effect = this.effectAtForce;
    }

    @Override
    public String getAsJSON() {
        return "{"+
                "\"name\":\""+name+"\","+
                "\"type\":\""+type+"\","+
                "\"category\":\""+category+"\","+
                "\"power\":"+
                    "{"+
                    "\"min_power\":"+minPower+","+
                    "\"max_power\":"+Math.round(1.2*minPower)+
                    "},"+
                "\"accuracy\":"+accuracy+","+
                "\"target\":\""+target+"\","+
                "\"cost\":"+((cost==0)?"\"\"":cost)+","+
                "\"uses\":"+((uses==0)?"null":uses)+","+
                "\"effect\":\""+effect+"\","+
                "\"unlock_requirements\":[]"+
                "}";
    }

    @Override
    public String toString() {
        return getAsJSON();
    }

    @Override
    public Move parseFromJSON(JSONObject JSON) {
        return parse(JSON);
    }

    public static Move parse(String json) {
        return parse(MiscUtil.parseJSONObject(json));
    }

    @SuppressWarnings("rawtypes")
    public static Move parse(JSONObject jo) {
        String name     = (String) jo.get("name");
        String type     = (String) jo.get("type");
        String category = (String) jo.get("category");
        int minPower    = objectToInt(((HashMap) jo.get("power")).get("min_power"));
        int accuracy    = objectToInt(jo.get("accuracy"));
        String target   = (String) jo.get("target");
        int cost        = (jo.get("cost") instanceof String)?0:objectToInt(jo.get("cost"));
        int uses        = (jo.get("uses") == null)?0:objectToInt(jo.get("uses"));
        String effect   = (String) jo.get("effect");
        return new Move(name, type, category, minPower, accuracy, target, cost, uses, effect);
    }

    static Move emptyMove() {
        return new Move("","","",0,0,"",0,0,"");
    }
}
