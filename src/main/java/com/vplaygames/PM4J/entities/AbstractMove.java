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

/**
 * Represents a usable move in Pokemon Masters, be it a {@link Move Move}
 * or a {@link SyncMove Sync Move}.
 * <br>This contains the minimum amount of information a move has.
 * <br>All of this class's variables are {@code public final} i.e. available without the use of getters
 * but not assignable.
 *
 * @since 1.0
 * @author Vaibhav Nargwani
 *
 * @see Move
 * @see SyncMove
 */
public abstract class AbstractMove
{
    /** The name of this move. */
    public final String name;
    /** The type of this move. */
    public final String type;
    /** The Category of this move */
    public final String category;
    /* The maximum power is not included as a field in this object
    * because it is calculable by using Math.round(minPower*1.2) */
    /** The minimum power of this move */
    public final int minPower;
    /** The target(s) of this move */
    public final String target;
    /** The additional effect(s) of this move apart from dealing damage. */
    final String effectAtForce; //to be replaced by child class with an appropriate name

    protected AbstractMove(String name, String type, String category,
                 int minPower,
                 String target, String effectAtForce)
    {
        this.name = name;
        this.type = type;
        this.category = category;
        this.minPower = minPower;
        this.target = target;
        this.effectAtForce = effectAtForce;
    }
}
