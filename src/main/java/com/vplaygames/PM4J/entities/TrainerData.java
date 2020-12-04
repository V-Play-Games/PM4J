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
import com.vplaygames.PM4J.util.MiscUtil;
import org.json.simple.JSONObject;

public class TrainerData extends AbstractTrainer implements ParsableJSONObject<TrainerData>
{
    public final JSONArray<Pokemon> pokemonData;

    public TrainerData(Trainer trainer) {
        super(trainer.name,trainer.rarity,trainer.img,trainer.data,trainer.pokemon);
        pokemonData = new JSONArray<>(0, Constants.EMPTY_POKEMON);
    }

    public TrainerData add(Pokemon pokemon) {
        pokemonData.add(pokemon);
        return this;
    }

    public TrainerData initialized() {
        pokemonData.initialized();
        return this;
    }

    @Override
    public String getAsJSON() {
        return super.getAsJSON().substring(0,super.getAsJSON().length()-1)+",\"pokemonData\":"+pokemonData.getAsJSON()+"}";
    }

    @Override
    public String toString() {
        return getAsJSON();
    }

    @Override
    public TrainerData parseFromJSON(JSONObject jo) {
        return parse(jo);
    }

    public static TrainerData parse(String json) {
        return parse(MiscUtil.parseJSONObject(json));
    }

    public static TrainerData parse(JSONObject jo) {
        TrainerData trainerData = new TrainerData(Trainer.parse(jo));
        JSONArray.parse((org.json.simple.JSONArray) jo.get("pokemonData"), Constants.EMPTY_POKEMON)
                .forEach(trainerData::add);
        return trainerData;
    }

    static TrainerData emptyTrainerData() {
        return new TrainerData(Constants.EMPTY_TRAINER).add(Constants.EMPTY_POKEMON).initialized();
    }
}
