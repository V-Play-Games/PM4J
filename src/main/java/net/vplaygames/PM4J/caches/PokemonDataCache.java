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
package net.vplaygames.PM4J.caches;

import net.vplaygames.PM4J.entities.Pokemon;

import java.util.ArrayList;

/**
 * Represents a Cache of all the Data of all the usable Pokemon in Pokemon Masters
 * who have formed a Sync Pair with a usable Trainer.
 *
 * This class is a Singleton Class, which means it can only be initialized once.
 * The instance is returned by the {@link #getInstance()} method.
 * This Cache caches the data in a {@link Cache} which is an inheritor of {@link java.util.HashMap}.
 *
 * @author Vaibhav Nargwani
 * @since 1.0.0
 * @see Cache
 * @see java.util.HashMap
 */
public class PokemonDataCache extends Cache<ArrayList<Pokemon>> {
    private static volatile PokemonDataCache instance;

    private PokemonDataCache() {}

    /**
     * Returns the Singleton Instance and logs any processes
     *
     * @return the Singleton Instance and logs any processes
     */
    public static PokemonDataCache getInstance() {
        return instance == null ? instance = new PokemonDataCache() : instance;
    }
}
