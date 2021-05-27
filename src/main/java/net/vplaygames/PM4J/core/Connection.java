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

import net.vplaygames.PM4J.entities.Trainer;
import net.vplaygames.PM4J.exceptions.ConnectionClosedException;
import net.vplaygames.PM4J.exceptions.TrainerNotFoundException;
import net.vplaygames.vjson.JSONObject;
import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.Response;

import java.io.Closeable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static net.vplaygames.PM4J.core.Logger.Mode.DEBUG;
import static net.vplaygames.PM4J.core.Logger.Mode.INFO;

/**
 * A connection object which allows the user to connect to PokemasDB and fetch:
 * <ul>
 *     <li>data of any specific trainer and their Pokemon.
 *         For example:-
 *         <pre><code>
 *             try (Connection conn = new Connection()) {
 *                 String data = conn.requestTrainer("Red");
 *                 Trainer t = Trainer.parse(data);
 *                 // rest of the code
 *             } catch (Exception e) {
 *                 e.printStackTrace();
 *             }
 *         </code></pre>
 *     </li>
 *     <li>a list of all trainers.
 *         For example:-
 *         <pre><code>
 *             try (Connection conn = new Connection()) {
 *                 String data = conn.requestTrainerList();
 *                 JSONArray&lt;Trainer&gt; array = JSONArray.parse(data, Constants.EMPTY_TRAINER);
 *                 // rest of the code
 *             } catch (Exception e) {
 *                 e.printStackTrace();
 *             }
 *         </code></pre>
 *     </li>
 *     <li>a list of all trainers with their Pokemon.
 *         For example:-
 *         <pre><code>
 *             try (Connection conn = new Connection()) {
 *                 JSONArray&lt;Trainer&gt; array = conn.requestAllTrainers(true);
 *                 // rest of the code
 *             } catch (Exception e) {
 *                 e.printStackTrace();
 *             }
 *         </code></pre>
 *     </li>
 * </ul>
 * This class implements {@link Closeable}, so it can be used with
 * {@code try}-with-resources block, as shown in the examples given above.
 *
 * @author Vaibhav Nargwani
 * @since 1.0.0
 */
public class Connection implements Closeable {
    static Logger logger = new Logger(Connection.class);
    OkHttpClient client;
    String baseURL;

    /**
     * Constructs a new Connection object and uses HTTPS by default.
     */
    public Connection() {
        this(new OkHttpClient.Builder().protocols(Collections.singletonList(Protocol.HTTP_1_1)).build());
    }

    /**
     * Constructs a new Connection object with the given <code>OkHttpClient</code>
     * which will be used to communicate with the Internet and uses HTTPS by default.
     *
     * @param okHttpClient the <code>OkHttpClient</code>
     *                     which will be used to communicate with the Internet.
     */
    public Connection(OkHttpClient okHttpClient) {
        this(okHttpClient, true);
    }

    /**
     * Constructs a new Connection object with the given <code>OkHttpClient</code>
     * which will be used to communicate with the Internet and uses HTTPS by default.
     *
     * @param okHttpClient the <code>OkHttpClient</code> which will be used to communicate with the Internet.
     * @param useHTTPS     if true, HTTPS is used to get data, HTTP is used otherwise.
     */
    public Connection(OkHttpClient okHttpClient, boolean useHTTPS) {
        client = Objects.requireNonNull(okHttpClient, "OkHttpClient cannot be null!");
        baseURL = Routes.TRAINER_ENDPOINT_URL.replaceFirst(useHTTPS ? "" : "s", "");
    }

    /**
     * Requests and returns the trainer list at the <a href="https://www.pokemasdb.com/trainer/">Trainer Endpoint</a>.
     * Usage example:-
     * <pre><code>
     *     try (Connection conn = new Connection()) {
     *         String data = conn.requestTrainerList();
     *         JSONArray&lt;Trainer&gt; array = JSONArray.parse(data, Constants.EMPTY_TRAINER);
     *         // rest of the code
     *     } catch (Exception e) {
     *         e.printStackTrace();
     *     }
     * </code></pre>
     *
     * @return the list of trainers obtained from the <a href="https://www.pokemasdb.com/trainer/">Trainer Endpoint</a>.
     * @throws IOException               if the request could not be executed due to cancellation, a connectivity
     *                                   problem or timeout. Because networks can fail during an exchange, it is possible that the
     *                                   remote server accepted the request before the failure.
     * @throws ConnectionClosedException if this method was called after calling the {@link #close() close} method.
     */
    @SuppressWarnings("ConstantConditions")
    public String requestTrainerList() throws IOException {
        checkClosed();
        try (Response response = requestData("")) {
            if (response.code() >= 400)
                throw new IOException("An unexpected error has occurred! " + baseURL + " returned HTTP Code " + response.code());
            return response.body().string();
        }
    }

    /**
     * Requests and returns the data of a particular trainer at
     * <a href="https://www.pokemasdb.com/trainer/">https://www.pokemasdb.com/trainer/</a>
     * in {@code String} form.
     * Usage Example:-
     * <pre><code>
     *     try (Connection conn = new Connection()) {
     *         String data = conn.requestTrainer("Serena");
     *         Trainer t = Trainer.parse(data);
     *         // rest of the code
     *     } catch (Exception e) {
     *         e.printStackTrace();
     *     }
     * </code></pre>
     *
     * @param trainer the trainer to be searched for.
     * @return the data of trainer requested in {@code String} form.
     * @throws IOException               if the request could not be executed due to cancellation, a connectivity
     *                                   problem or timeout. Because networks can fail during an exchange, it is possible that the
     *                                   remote server accepted the request before the failure.
     * @throws TrainerNotFoundException  if HTTP code received is 400 or greater.
     * @throws ConnectionClosedException if this method was called after calling the {@link #close() close} method.
     */
    @SuppressWarnings("ConstantConditions")
    public String requestTrainer(String trainer) throws IOException {
        checkClosed();
        try (Response response = requestData(Trainer.resolve(trainer))) {
            if (response.code() >= 400)
                throw new TrainerNotFoundException(response.code(), baseURL + Trainer.resolve(trainer));
            return response.body().string();
        }
    }

    /**
     * Requests and returns the data of all trainers available at
     * <a href="https://www.pokemasdb.com/trainer/">https://www.pokemasdb.com/trainer/</a>.
     * For example:-
     * <pre><code>
     *     try (Connection conn = new Connection()) {
     *         JSONArray&lt;Trainer&gt; array = conn.requestAllTrainers(true);
     *         // rest of the code
     *     } catch (Exception e) {
     *         e.printStackTrace();
     *     }
     * </code></pre>
     *
     * @param log to log the details or not.
     * @return the data of all trainers obtained available from the
     * <a href="https://www.pokemasdb.com/trainer/">Trainer Endpoint</a>.
     * @throws IOException               if the request could not be executed due to cancellation, a connectivity
     *                                   problem or timeout. Because networks can fail during an exchange, it is possible that the
     *                                   remote server accepted the request before the failure.
     * @throws TrainerNotFoundException  if HTTP code received is 400 or greater.
     * @throws ConnectionClosedException if this method was called after calling the {@link #close() close} method.
     */
    public List<Trainer> requestAllTrainers(boolean log) throws IOException, TrainerNotFoundException {
        checkClosed();
        if (log) logger.log("Downloading the list of trainers.", DEBUG);
        String trainerList = requestTrainerList();
        if (log) {
            logger.log("Downloaded the list of all trainers.", INFO);
            logger.log("Downloading Trainer Data.", DEBUG);
        }
        List<Trainer> trainers = JSONObject.parse(trainerList)
            .asObject()
            .get("trainers")
            .asList(Trainer::parse)
            .stream()
            .map(trainer -> {
                Trainer tor;
                try {
                    tor = Trainer.parse(requestTrainer(trainer.name));
                    if (log)
                        logger.log("Downloaded " + trainer.name + "'s Data", DEBUG);
                } catch (IOException exc) {
                    throw new TrainerNotFoundException(trainer.name, exc);
                }
                return tor;
            })
            .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
        if (log) logger.log("Downloaded data for all the trainers.", INFO);
        return trainers;
    }

    /**
     * Requests data from the <a href="https://www.pokemasdb.com/trainer/">Trainer Endpoint</a>
     *
     * @param restOfTheURL the endpoint to hit
     * @return the {@code Response} received
     * @throws IOException if the request could not be executed due to cancellation, a connectivity
     *                     problem or timeout. Because networks can fail during an exchange, it is possible that the
     *                     remote server accepted the request before the failure.
     */
    private Response requestData(String restOfTheURL) throws IOException {
        checkClosed();
        return client.newCall(new Request.Builder().url(baseURL + restOfTheURL).build()).execute();
    }

    /**
     * Throws {@link ConnectionClosedException ConnectionClosedException} if this Connection object
     * is closed using the {@link #close() close} method
     *
     * @throws ConnectionClosedException if the {@link #close() close} method has been called
     */
    private void checkClosed() {
        if (client == null) throw new ConnectionClosedException();
    }

    @Override
    public void close() {
        client = null;
        baseURL = null;
    }
}
