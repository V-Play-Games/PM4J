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
package com.vplaygames.PM4J.jsonFramework;

import com.vplaygames.PM4J.exceptions.ParseException;

/**
 * This class represents JSON Object parsable from a String.
 * <br>All of this class's implementations can be parsed and stored into
 * a {@link JSONArray} and can be directly converted into a JSON String.
 *
 * @author Vaibhav Nargwani
 * @since 1.0.0
 * @see JSONArray
 */
public interface ParsableJSONObject<E extends ParsableJSONObject<E>> {
    /**
     * Converts the current object into a JSON String.
     *
     * @return The JSON String for the current object.
     */
    String getAsJSON();

    /**
     * Parses a JSON String to a Parsable JSON Object.
     *
     * @param JSON the JSON String to be parsed
     * @return the object parsed.
     * @throws ParseException if the JSON String was not a correctly formatted JSON String.
     */
    E parseFromJSON(String JSON) throws ParseException;
}