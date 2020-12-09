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
import org.json.simple.JSONArray;

/**
 * Contains all the non-<code>null</code> empty values representing an entity.
 * These are useful for parsing a JSON String which is supposed to
 * result in a {@link com.vplaygames.PM4J.jsonFramework.JSONArray JSONArray}.
 * All the variables in these constant objects are non-<code>null</code> values, but not logically not assigned.
 * For example, for a <code>String</code> variable, it will be assigned <code>""</code>,
 * for an integer, it will be assigned <code>0</code>
 *
 * @since 1.0
 * @author Vaibhav Nargwani
 *
 * @see com.vplaygames.PM4J.jsonFramework.JSONArray#parse(JSONArray, ParsableJSONObject)
 */
public class Constants {
    /** Represents an empty valued {@link Move Move} object */
    public static final Move EMPTY_MOVE = Move.emptyMove();
    /** Represents an empty valued {@link Passive Passive Skill} object */
    public static final Passive EMPTY_PASSIVE = Passive.emptyPassive();
    /** Represents an empty valued {@link Pokemon Pokemon} object */
    public static final Pokemon EMPTY_POKEMON = Pokemon.emptyPokemon();
    /** Represents an empty valued {@link StatRange StatRange} object */
    public static final StatRange EMPTY_STAT_RANGE = StatRange.emptyStatRange();
    /** Represents an empty valued {@link Stats Stats} object */
    public static final Stats EMPTY_STATS = Stats.emptyStats();
    /** Represents an empty valued {@link SyncGridNode Sync Grid Node} object */
    public static final SyncGridNode EMPTY_SYNC_GRID_NODE = SyncGridNode.emptySyncGridNode();
    /** Represents an empty valued {@link SyncMove Sync Move} object */
    public static final SyncMove EMPTY_SYNC_MOVE = SyncMove.emptySyncMove();
    /** Represents an empty valued {@link Trainer Trainer} object */
    public static final Trainer EMPTY_TRAINER = Trainer.emptyTrainer();
    /** The URL of the <a href="https://www.pokemasdb.com/">PokemasDB Homepage</a> */
    public static final String POKEMASDB_URL = "https://www.pokemasdb.com/";
    /** The URL of the <a href="https://www.pokemasdb.com/trainer/">Trainer Endpoint</a> */
    public static final String TRAINER_ENDPOINT_URL = POKEMASDB_URL+"trainer/";

    private Constants() {}
}
