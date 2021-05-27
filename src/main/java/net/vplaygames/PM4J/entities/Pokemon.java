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

import net.vplaygames.PM4J.caches.MoveDataCache;
import net.vplaygames.PM4J.caches.PokemonDataCache;
import net.vplaygames.PM4J.caches.SkillDataCache;
import net.vplaygames.PM4J.caches.ThemeSkillDataCache;
import net.vplaygames.PM4J.core.Util;
import net.vplaygames.vjson.JSONable;
import net.vplaygames.vjson.JSONObject;
import net.vplaygames.vjson.JSONValue;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a Pokemon in Pokemon Masters, which has formed a Sync Pair with a trainer.
 * <br>All of this class's variables are {@code public final} i.e. available without the use of getters
 * but not assignable.
 *
 * @since 1.0.0
 * @author Vaibhav Nargwani
 */
public class Pokemon implements JSONable {
    /** The name of this Pokemon */
    public final String name;
    /** The name of the Trainer of this Pokemon */
    public final String trainer;
    /** The name of the Sync Pair formed by this Pokemon */
    public final String syncPair;
    /** The typing of this Pokemon */
    public final String[] typing;
    /** The weakness of this Pokemon */
    public final String weakness;
    /** The role of this Pokemon [Strike (Special/Physical)/Support/Tech */
    public final String role;
    /** The rarity of this Pokemon (in no. of Stars) */
    public final int rarity;
    /** The gender of this Pokemon */
    public final String gender;
    /** The name of other forms this Pokemon */
    public final String[] otherForms;
    /** If 6* EX has been unlocked for the Sync Pair formed by this Pokemon */
    public final boolean ex;
    /** The Moves of this Pokemon */
    public final List<Move> moves;
    /** The Sync Move of this Pokemon */
    public final SyncMove syncMove;
    /** The Passive Skills of this Pokemon */
    public final List<Passive> passives;
    /** The Theme Skills of this Pokemon */
    public final List<ThemeSkill> themeSkills;
    /** The Stats of this Pokemon */
    public final StatRange stats;
    /** The Sync Grid of this Pokemon */
    public final List<SyncTile> grid;

    public Pokemon(String name, String trainer, String syncPair,
                   String[] typing,
                   String weakness, String role, int rarity, String gender,
                   String[] otherForms, boolean ex,
                   StatRange stats, SyncMove syncMove,
                   List<Move> moves, List<Passive> passives, List<ThemeSkill> themeSkills,
                   List<SyncTile> grid) {
        this.name = name;
        this.trainer = trainer;
        this.syncPair = syncPair;
        this.typing = typing;
        this.weakness = weakness;
        this.role = role;
        this.rarity = rarity;
        this.gender = gender;
        this.otherForms = otherForms;
        this.ex = ex;
        this.moves = moves;
        this.syncMove = syncMove;
        this.passives = passives;
        this.stats = stats;
        this.themeSkills = themeSkills;
        this.grid = grid;
        if (name.equals("")) {
            return;
        }
        PokemonDataCache pdc = PokemonDataCache.getInstance();
        if (!pdc.containsKey(name)) {
            pdc.put(name, new ArrayList<>());
        }
        pdc.get(name).add(this);
        pdc.forEach((k, v) -> {
            if (name.contains(k) && !k.equals("Mewtwo"))
                v.add(this);
        });
        moves.forEach(m -> MoveDataCache.getInstance().get(m.name).users.add(this));
        themeSkills.forEach(t -> ThemeSkillDataCache.getInstance().get(t.name).pokemon.add(this));
        passives.forEach(p -> registerInCache(this, p.name, false));
        grid.forEach(sgn -> {
            if (sgn.title.equals(sgn.description)) return;
            if (sgn.title.contains(":"))
                registerInCache(this, sgn.title.split(":")[1], true);
            registerInCache(this, sgn.title.replace(":", ": "), true);
        });
    }

    // for lesser memory consumption, made this a separate static method
    static void registerInCache(Pokemon p, String passiveName, boolean isGrid) {
        SkillDataCache.getInstance().get(passiveName).add(p, isGrid);
        if (Character.isDigit(passiveName.charAt(passiveName.length() - 1)))
            SkillDataCache.getInstance().get(passiveName.substring(0, passiveName.length() - 2)).add(p, isGrid);
    }

    @Override
    public String toString() {
        return toJSONString();
    }

    @Override
    public String toJSONString() {
        return "{" +
            "\"name\":\"" + name + "\"," +
            "\"trainer\":\"" + trainer + "\"," +
            "\"syncPair\":\"" + syncPair + "\"," +
            "\"typing\":" + Util.toString(typing) + "," +
            "\"weakness\":\"" + weakness + "\"," +
            "\"role\":\"" + role + "\"," +
            "\"rarity\":" + rarity + "," +
            "\"ex\":" + ex + "," +
            "\"gender\":\"" + gender + "\"," +
            "\"otherForms\":" + Util.toString(otherForms) + "," +
            "\"moves\":" + moves + "," +
            "\"syncMove\":" + syncMove + "," +
            "\"passives\":" + passives + "," +
            "\"themeSkills\":" + themeSkills + "," +
            "\"stats\":" + stats + "," +
            "\"grid\":" + grid +
            "}";
    }

    /**
     * Parses the given {@code String} to a Pokemon
     *
     * @param json The JSON String to be parsed
     * @return The Pokemon object parsed from the JSON String
     * @throws ClassCastException   if the value was unable to be cast into the required type
     * @throws NullPointerException if the required values were not present in the String
     */
    public static Pokemon parse(String json) {
        return parse(JSONValue.parse(json));
    }

    /**
     * Parses the given {@code JSONValue} to a Pokemon
     *
     * @param val The {@code JSONValue} to be parsed
     * @return The Pokemon object parsed from the JSON String
     * @throws ClassCastException   if the required value was unable to be cast into the desired type
     * @throws NullPointerException if the required values were not present in the {@code JSONValue}
     */
    public static Pokemon parse(JSONValue val) {
        JSONObject jo = val.asObject();
        String name                  = jo.get("name").asString();
        String trainer               = jo.get("trainer").asString();
        String syncPair              = jo.get("syncPair").asString();
        String weakness              = jo.get("weakness").asString();
        String role                  = jo.get("role").asString();
        int rarity                   = jo.get("rarity").asInt();
        String gender                = jo.get("gender").asString();
        boolean ex                   = jo.get("ex").asBoolean();
        String[] typing              = jo.get("typing").asList(JSONValue::asString).toArray(new String[0]);
        String[] otherForms          = jo.get("otherForms").asList(JSONValue::asString).toArray(new String[0]);
        StatRange stats              = StatRange.parse(jo.get("stats"));
        SyncMove syncMove            = SyncMove.parse(jo.get("syncMove"));
        List<Move> moves        = jo.get("moves").asList(Move::parse);
        List<Passive> passives  = jo.get("passives").asList(Passive::parse);
        List<SyncTile> grid = jo.get("grid").asList(SyncTile::parse);
        List<ThemeSkill> themeSkills = jo.get("themeSkills").asList(ThemeSkill::parse);
        return new Pokemon(name, trainer, syncPair, typing, weakness, role, rarity, gender, otherForms, ex, stats, syncMove, moves, passives, themeSkills, grid);
    }
}
