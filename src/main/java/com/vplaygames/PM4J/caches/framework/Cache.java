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
package com.vplaygames.PM4J.caches.framework;

import com.vplaygames.PM4J.caches.*;
import com.vplaygames.PM4J.util.Strings;

import java.util.HashMap;

/**
 * Represents a Cache which stores data in the form of String-Object Mappings.
 * This cache has a unique method of finding data using
 * {@link #containsKey(Object)} and {@link #get(Object)} in this map.
 * It reduces the String to be searched to Alphanumeric Characters only.
 * and while searching it calls {@linkplain String#equalsIgnoreCase(String)} on the keys,
 * but it also reduces the keys to Alphanumeric Characters temporarily.
 * However, the default behaviour and the normal behaviour can be set using
 * {@link #reduceToAlphanumeric(boolean) reduceToAlphanumeric} method.
 * Default behaviour reduces the keys to Alphanumeric Characters while searching.
 *
 * @author Vaibhav Nargwani
 * @since 1.0.0
 * @see #reduceToAlphanumeric(boolean)
 */
public class Cache<T> extends HashMap<String, T> {
    boolean rta = true;

    @Override
    public T get(Object key) {
        if (rta) {
            if (!(key instanceof String)) {
                return null;
            }
            String temp = (String) key;
            for (String k : keySet()) {
                if (equals(k, temp)) {
                    return super.get(k);
                }
            }
            return null;
        }
        return super.get(key);
    }

    @Override
    public boolean containsKey(Object key) {
        if (!(key instanceof String)) {
            return false;
        }
        String temp = (String) key;
        for (String k : keySet()) {
            if (equals(k,temp)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Sets the searching behaviour.
     * {@code true} - Reduces the key to Alphanumeric temporarily while searching.
     * {@code false} - The normal way of searching.
     * @param rta The searching behaviour to use.
     */
    public void reduceToAlphanumeric(boolean rta) {
        this.rta = rta;
    }

    private boolean equals(String a, String b) {
        return rta ? Strings.reduceToAlphanumeric(a).equalsIgnoreCase(Strings.reduceToAlphanumeric(b)) : a.equals(b);
    }

    /**
     * Represents a type of Cache.
     */
    public enum Type {
        /** Refers to {@link TrainerDataCache} */
        TRAINER("Trainer", TrainerDataCache.getInstance()),
        /** Refers to {@link PokemonDataCache} */
        POKEMON("Pokemon", PokemonDataCache.getInstance()),
        /** Refers to {@link SkillDataCache} */
        SKILL("Passive Skill", SkillDataCache.getInstance()),
        /** Refers to {@link MoveDataCache} */
        MOVE("Move", MoveDataCache.getInstance()),
        /** Refers to {@link PokemasDBCache} */
        UNKNOWN("", PokemasDBCache.getInstance());

        private final String value;
        private final DataCache<?,?> cache;

        Type(String value, DataCache<?,?> cache) {
            this.value = value;
            this.cache = cache;
        }

        @Override
        public String toString() {
            return value;
        }

        /**
         * Returns the type of Cache being referred by this Type.
         *
         * @return the type of Cache being referred by this Type.
         */
        public DataCache<?,?> getCache() {
            return cache;
        }

        /**
         * Parses the Type of Cache from String, returns {@link Type#UNKNOWN} if none detected.
         *
         * @param toParse the String to Parse.
         * @return the Type of Cache detected, {@link Type#UNKNOWN} if none detected.
         */
        public static Type parseType(String toParse) {
            switch (toParse.toLowerCase()) {
                case "trainer": return TRAINER;
                case "pokemon": return POKEMON;
                case "move":    return MOVE;
                case "passive":
                case "skill":   return SKILL;
                default:        return UNKNOWN;
            }
        }

        /**
         * Returns true if the given String parses to any type except {@link Type#UNKNOWN}, false otherwise
         *
         * @param toParse the String to check
         * @return true if the given String parses to any type except {@link Type#UNKNOWN}, false otherwise
         */
        public static boolean isType(String toParse) {
            return parseType(toParse) != UNKNOWN;
        }
    }
}