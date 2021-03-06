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

/**
 * Represents a Cache of any type of Data in Pokemon Masters (Pokemon, Moves, Skills, Trainers).
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
public class PokemasDBCache extends Cache<Object> {
    private static volatile PokemasDBCache instance;

    protected PokemasDBCache() {}

    /**
     * Returns the Singleton Instance
     *
     * @return the Singleton Instance
     */
    public static PokemasDBCache getInstance() {
        return instance == null ? instance = new PokemasDBCache() : instance;
    }

    /**
     * Clears all the caches (Move, Pokemon, Skill, ThemeSkill, Trainer Data Caches).
     */
    public void clearCaches() {
        for (Type type : Type.values()) {
            type.getCache().clear();
        }
    }
}
