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

import com.vplaygames.PM4J.util.MiscUtil;
import com.vplaygames.PM4J.exceptions.ParseException;
import com.vplaygames.PM4J.exceptions.ValidationException;
import org.json.simple.JSONObject;

public interface ParsableJSONObject<E extends ParsableJSONObject<E>> {
    String getAsJSON();

    default E parseFromJSON(String JSON) throws ParseException {
        return parseFromJSON(MiscUtil.parseJSONObject(JSON));
    }

    default E parseFromJSON(JSONObject JSON) {return null;}

    default void validate() throws ParseException, ValidationException {
        if (!this.getAsJSON().equals(parseFromJSON(this.getAsJSON()).getAsJSON()))
            throw new ValidationException();
    }
}