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

public class Cache<T> extends HashMap<String, T> {
    boolean rta = true;

    @Override
    public T get(Object key) {
        if (!(key instanceof String)) {
            return null;
        }
        String temp = (String) key;
        for (String k : this.keySet()) {
            if (equals(k,temp)) {
                return super.get(k);
            }
        }
        return null;
    }

    @Override
    public boolean containsKey(Object key) {
        if (!(key instanceof String)) {
            return false;
        }
        String temp = (String) key;
        for (String k : this.keySet()) {
            if (equals(k,temp)) {
                return true;
            }
        }
        return false;
    }

    public void reduceToAlphanumeric(boolean bool) {
        rta = bool;
    }

    private boolean equals(String a, String b) {
        return rta ? Strings.reduceToAlphanumeric(a).equalsIgnoreCase(Strings.reduceToAlphanumeric(b)) : a.equals(b);
    }

    public enum Type {
        TRAINER("Trainer", TrainerDataCache.getInstance()),
        POKEMON("Pokemon", PokemonDataCache.getInstance()),
        SKILL("Passive Skill", SkillDataCache.getInstance()),
        MOVE("Move", MoveDataCache.getInstance()),
        UNKNOWN("", PokemasDBCache.getInstance());

        private final String value;
        private final Cache<?> cache;

        Type(String value, Cache<?> cache) {
            this.value = value;
            this.cache = cache;
        }

        @Override
        public String toString() {
            return value;
        }

        public Cache<?> getCache() {
            return cache;
        }

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

        public static boolean isType(String toParse) {
            return parseType(toParse) != UNKNOWN;
        }
    }
}