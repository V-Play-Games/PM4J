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

import net.vplaygames.PM4J.entities.Passive;
import net.vplaygames.PM4J.entities.Pokemon;

import java.util.HashSet;
import java.util.Set;

/**
 * Represents a Cache of all the Data of all the usable Passive Skills in Pokemon Masters,
 * present in either a Sync Pair's default Passives, or present in a Sync Pair's Sync Grid.
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
public class SkillDataCache extends Cache<SkillDataCache.Node> {
    private static volatile SkillDataCache instance;

    private SkillDataCache() {}

    /**
     * Returns the Singleton Instance
     *
     * @return the Singleton Instance
     */
    public static SkillDataCache getInstance() {
        return instance == null ? instance = new SkillDataCache() : instance;
    }

    /**
     * The Data Node for SkillDataCache
     *
     * @author Vaibhav Nargwani
     * @since 1.0.0
     */
    public static class Node {
        /** The Skill for which this Node contains data for. */
        public final Passive skill;
        /** The names of Sync Pairs who have the corresponding skill in their default Passives. */
        public final Set<Pokemon> inbuilt = new HashSet<>();
        /** The names of Sync Pairs who have the corresponding skill in their Sync Grid. */
        public final Set<Pokemon> inGrid = new HashSet<>();

        public Node(Passive skill) {
            this.skill = skill;
        }

        /**
         * Adds the {@link Pokemon} to the grid or inbuilt list, as specified
         *
         * @param p the {@link Pokemon} to be attached to the list
         * @param isGrid if true {@code p} is added in the Grid list, otherwise to the Inbuilt list
         * @return this instance, useful for chaining.
         */
        public Node add(Pokemon p, boolean isGrid) {
            (isGrid ? inGrid : inbuilt).add(p);
            return this;
        }
    }
}
