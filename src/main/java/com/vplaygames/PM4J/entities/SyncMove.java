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

public class SyncMove extends AbstractMove implements ParsableJSONObject<SyncMove>
{
    public final String effectTag; //a public copy of effectAtForce of AbstractMove
    public final String description;

    public SyncMove(String name, String type, String category,
                    int minPower,
                    String target, String effectTag, String description) {
        super(name,type,category,minPower,target,effectTag);
        this.description=description;
        this.effectTag=this.effectAtForce;
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
                "\"target\":\""+target+"\","+
                "\"effect_tag\":\""+effectTag+"\","+
                "\"description\":\""+description+"\""+
                "}";
    }

    @Override
    public String toString() {
        return getAsJSON();
    }

    @Override
    public SyncMove parseFromJSON(JSONObject JSON) {
        return parse(JSON);
    }

    public static SyncMove parse(String json) {
        return parse(MiscUtil.parseJSONObject(json));
    }

    @SuppressWarnings("rawtypes")
    public static SyncMove parse(JSONObject jo) {
        String name        = (String) jo.get("name");
        String type        = (String) jo.get("type");
        String category    = (String) jo.get("category");
        int minPower       = MiscUtil.objectToInt(((HashMap) jo.get("power")).get("min_power"));
        String target      = (String) jo.get("target");
        String effectTag   = (String) jo.get("effect_tag");
        String description = (String) jo.get("description");
        return new SyncMove(name, type, category, minPower, target, effectTag, description);
    }

    static SyncMove emptySyncMove() {
        return new SyncMove("","","",0,"","","");
    }
}