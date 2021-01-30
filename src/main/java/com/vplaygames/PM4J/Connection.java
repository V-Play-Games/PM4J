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
package com.vplaygames.PM4J;

import com.vplaygames.PM4J.entities.Trainer;
import com.vplaygames.PM4J.exceptions.ConnectionClosedException;
import com.vplaygames.PM4J.exceptions.TrainerNotFoundException;
import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.Response;

import java.io.Closeable;
import java.io.IOException;
import java.util.Collections;
import java.util.Objects;

import static com.vplaygames.PM4J.entities.Constants.TRAINER_ENDPOINT_URL;

/**
 * A connection object which allows the user to connect to PokemasDB and
 * fetch data of any specific trainer and their Pokemon.
 * For example:-
 * <pre><code>
 *     try (Connection conn = new Connection()) {
 *         String data = conn.requestTrainer("Red");
 *         Trainer t = Trainer.parse(data);
 *         // rest of the code
 *     } catch (Exception e) {
 *         e.printStackTrace();
 *     }
 * </code></pre>
 * It can also be used to fetch data for all trainers excluding their
 * Pokemon.
 * For example:-
 * <pre><code>
 *     try (Connection conn = new Connection()) {
 *         String data = conn.requestTrainers();
 *         JSONArray&lt;Trainer&gt; array = JSONArray.parse(data, Constants.EMPTY_TRAINER);
 *         // rest of the code
 *     } catch (Exception e) {
 *         e.printStackTrace();
 *     }
 * </code></pre>
 * This class implements <code>Closeable</code>, so you can use it with
 * {@code try}-with-resources block, as shown in the examples given above.
 *
 * @author Vaibhav Nargwani
 * @since 1.0.0
 */
public class Connection implements Closeable {
    OkHttpClient client;
    boolean useHTTPS;

    /**
     * Constructs a new Connection object.
     */
    public Connection() {
        this(new OkHttpClient.Builder().protocols(Collections.singletonList(Protocol.HTTP_1_1)).build());
    }

    /**
     * Constructs a new Connection object with the given <code>OkHttpClient</code>
     * which will be used to communicate with the Internet.
     *
     * @param okHttpClient the <code>OkHttpClient</code>
     *                    which will be used to communicate with the Internet.
     */
    public Connection(OkHttpClient okHttpClient) {
        this(okHttpClient, true);
    }

    public Connection(OkHttpClient okHttpClient, boolean useHTTPS) {
        this.useHTTPS = useHTTPS;
        client = Objects.requireNonNull(okHttpClient, "OkHttpClient cannot be null!");
    }

    /**
     * Requests and returns the trainer list at the <a href="https://www.pokemasdb.com/trainer/">Trainer Endpoint</a>.
     *
     * @return the list of trainers obtained from the <a href="https://www.pokemasdb.com/trainer/">Trainer Endpoint</a>.
     * @throws IOException if HTTP code received is 400 or greater.
     * @throws ConnectionClosedException if this method was called after calling the {@link #close() close} method.
     */
    public String requestTrainers() throws IOException {
        Response response = requestData("");
        if (response.code() >= 400) {
            throw new IOException("An unexpected error has occurred! "+ TRAINER_ENDPOINT_URL+" returned HTTP Code " + response.code());
        }
        String tor = response.body() == null ? "" : response.body().string();
        response.close();
        return tor;
    }

    /**
     * Requests and returns the data of a particular trainer at
     * <a href="https://www.pokemasdb.com/trainer/">https://www.pokemasdb.com/trainer/</a>{trainer}.
     *
     * @param trainer the trainer to be searched for.
     * @return the list of trainers obtained from the <a href="https://www.pokemasdb.com/trainer/">Trainer Endpoint</a>.
     * @throws IOException if the request could not be executed due to cancellation, a connectivity
     * problem or timeout. Because networks can fail during an exchange, it is possible that the
     * remote server accepted the request before the failure.
     * @throws TrainerNotFoundException if HTTP code received is 400 or greater.
     * @throws ConnectionClosedException if this method was called after calling the {@link #close() close} method.
     */
    public String requestTrainer(String trainer) throws IOException {
        Response response = requestData(Trainer.resolve(trainer));
        if (response.code() >= 400) {
            throw new TrainerNotFoundException(response.code(), trainer);
        }
        String tor = response.body() == null ? "" : response.body().string();
        response.close();
        return tor;
    }

    private Response requestData(String restOfTheURL) throws IOException {
        if (client == null)
            throw new ConnectionClosedException();
        return client.newCall(new Request.Builder().url("http"+TRAINER_ENDPOINT_URL.substring(4+(useHTTPS?0:1))+restOfTheURL).build()).execute();
    }

    @Override
    public void close() {
        client = null;
    }
}