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
import net.vplaygames.PM4J.entities.ThemeSkill;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a Cache of all the Data of all the usable Theme Skills in Pokemon Masters EX.
 *
 * This class is a Singleton Class, which means it can only be initialized once.
 * The instance is returned by the {@link #getInstance()} method.
 * This Cache caches the data in a {@link Cache} which is an inheritor of {@link java.util.HashMap}.
 *
 * @author Vaibhav Nargwani
 * @since 1.1.0
 * @see Cache
 * @see java.util.HashMap
 */
public class ThemeSkillDataCache extends Cache<ThemeSkillDataCache.Node> {
    private static volatile ThemeSkillDataCache instance;

    private ThemeSkillDataCache() {}

    /**
     * Returns the Singleton Instance
     *
     * @return the Singleton Instance
     */
    public static ThemeSkillDataCache getInstance() {
        return instance == null ? instance = new ThemeSkillDataCache() : instance;
    }

    /**
     * The Data Node for ThemeSkillDataCache
     *
     * @author Vaibhav Nargwani
     * @since 1.0.0
     */
    public static class Node {
        /** The Theme Skill for which this Node contains data for. */
        public final ThemeSkill skill;
        /** The names of Sync Pairs who have the corresponding theme skill. */
        public final List<Pokemon> pokemon = new ArrayList<>();

        public Node(ThemeSkill skill) {
            this.skill = skill;
        }
    }
}
