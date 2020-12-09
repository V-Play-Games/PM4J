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
package com.vplaygames.PM4J.exceptions;

import com.vplaygames.PM4J.entities.Constants;

/**
 * This Exception is thrown while getting a trainer's data
 * indicating that either the data was not available,
 * or a problem occurred while getting it from the Internet.
 *
 * @since 1.0.0
 * @author Vaibhav Nargwani
 */
public class TrainerNotFoundException extends RuntimeException {
    public TrainerNotFoundException(int code, String trainer) {
        super("Error Code " + code + " was returned from " + Constants.TRAINER_ENDPOINT_URL + trainer);
    }
}
