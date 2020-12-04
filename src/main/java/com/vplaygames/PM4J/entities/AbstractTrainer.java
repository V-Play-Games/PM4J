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

import com.vplaygames.PM4J.util.Array;

abstract class AbstractTrainer
{
    public final String name;
    public final int rarity;
    public final String img;
    public final String data;
    protected final String[] pokemon;

    protected AbstractTrainer(String name, int rarity, String img, String data, String[] pokemon) {
        this.name=name;
        this.rarity=rarity;
        this.img=img;
        this.data=data;
        this.pokemon=pokemon;
    }

    public String[] getPokemon() {
        return pokemon;
    }

    public String getAsJSON() {
        return "{"+
                "\"name\":\""+name+"\","+
                "\"rarity\":"+rarity+","+
                "\"pokemon\":["+Array.toString(",",pokemon,"")+"],"+
                "\"image\":\""+img+"\","+
                "\"data\":\""+data+"\""+
                "}";
    }

    @Override
    public String toString() {
        return getAsJSON();
    }
}
