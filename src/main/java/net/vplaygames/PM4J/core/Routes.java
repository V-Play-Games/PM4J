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
package net.vplaygames.PM4J.core;

/**
 * Contains all the Routes to the endpoints that can be hit on PokeMasDB
 *
 * @since 1.1.0
 * @author Vaibhav Nargwani
 */
public class Routes {
    /** The URL of the <a href="https://www.pokemasdb.com/">PokemasDB Homepage</a> */
    public static final String POKEMASDB_URL = "https://www.pokemasdb.com/";
    /** The URL of the <a href="https://www.pokemasdb.com/trainer/">Trainer Endpoint</a> */
    public static final String TRAINER_ENDPOINT_URL = POKEMASDB_URL + "trainer/";

    private Routes() {}
}
