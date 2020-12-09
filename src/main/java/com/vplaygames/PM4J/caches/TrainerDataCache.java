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
package com.vplaygames.PM4J.caches;

import com.vplaygames.PM4J.Connection;
import com.vplaygames.PM4J.caches.framework.DownloadedCache;
import com.vplaygames.PM4J.entities.Constants;
import com.vplaygames.PM4J.entities.Trainer;
import com.vplaygames.PM4J.exceptions.CachingException;
import com.vplaygames.PM4J.jsonFramework.JSONArray;
import com.vplaygames.PM4J.util.Array;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;

import static com.vplaygames.PM4J.Logger.Mode.DEBUG;
import static com.vplaygames.PM4J.Logger.Mode.INFO;
import static com.vplaygames.PM4J.Logger.log;
import static com.vplaygames.PM4J.util.MiscUtil.*;
import static java.lang.System.currentTimeMillis;

/**
 * Represents a Cache of all the Data of all the Trainers and their respective Pokemon.
 * This class uses {@link com.vplaygames.PM4J.Logger} to log details of the
 * downloads and processes.
 * Usage example:-
 * <pre><code>
 *     TrainerDataCache tdc;
 *     try {
 *         tdc = TrainerDataCache.getInstance();
 *     } catch (Exception e) {
 *         e.printStackTrace();
 *     }
 *     if (tdc != null) {
 *         // use the object
 *     } else {
 *         System.out.println("Data not initialized yet");
 *     }
 * </code></pre>
 * This class is a Singleton Class, which means it can only be initialized once.
 * The instance is returned by the {@link #getInstance()} and {@link #getInstance(boolean)} methods.
 * This Cache caches the data in a {@link com.vplaygames.PM4J.caches.framework.Cache} which is an
 * inheritor of HashMap. This class also provides other details such as downloading and processing time.
 * See {@link DownloadedCache} for more information.
 *
 * @author Vaibhav Nargwani
 * @since 1.0.0
 * @see com.vplaygames.PM4J.caches.framework.Cache
 * @see com.vplaygames.PM4J.caches.framework.DownloadedCache
 * @see java.util.HashMap
 */
public class TrainerDataCache extends DownloadedCache<Trainer> {
    private static volatile TrainerDataCache instance;

    private TrainerDataCache(boolean log) throws CachingException {
        try {
            Class<?> clazz = getClass();
            long temp = currentTimeMillis();
            log("Commencing the download and parse of data from https://pokemasdb.com\n", INFO, clazz);
            Connection conn = new Connection();
            log("Downloading the list of trainers.\n", DEBUG, clazz, log);
            processingTime = currentTimeMillis();
            String tcache = conn.requestTrainers();
            downloadingTime = currentTimeMillis();
            log("Downloaded the list of all trainers.\n", INFO, clazz, log);
            String toPrint = log("Downloading Trainer Data.", DEBUG, clazz, log);
            JSONArray<Trainer> trainers = JSONArray.parse(
                    (org.json.simple.JSONArray) ((JSONObject) new JSONParser().parse(tcache)).get("trainers"),
                    Constants.EMPTY_TRAINER);
            temp = (currentTimeMillis() - downloadingTime) + (processingTime - temp);
            downloadingTime = downloadingTime - processingTime;
            int len = trainers.size();
            int maxNameLen = 12;
            int i = 0;
            downloaded(tcache.length());
            String[] localDataCache = new String[len];
            processingTime = temp;
            while (i < len) {
                temp = currentTimeMillis();
                String name = trainers.get(i).name;
                localDataCache[i] = conn.requestTrainer(name);
                processed(localDataCache[i].length());
                downloadingTime += (currentTimeMillis() - temp);
                if (log) {
                    System.out.print(backspace(toPrint.length()));
                    toPrint = log("Total Progress: " + doubleToString(5, ++i * 100.0 / len) + "% | Downloaded " + name + "'s Data" + space(maxNameLen - name.length()) + " | Download Speed: " + speed(downloadingTime), DEBUG, clazz);
                }
            }
            totalProcessed = 0;
            i = 0;
            trainers = new JSONArray<>(0, Constants.EMPTY_TRAINER);
            while (i < len) {
                temp = currentTimeMillis();
                String name = trainers.get(i).name;
                String json = localDataCache[i];
                Trainer datum;
                datum = Trainer.parse(json);
                trainers.add(datum);
                this.put(name, datum);
                processed(json.length());
                processingTime += currentTimeMillis() - temp;
                if (log) {
                    System.out.print(backspace(toPrint.length()));
                    toPrint = log("Total Progress: " + doubleToString(5, ++i * 100.0 / len) + "% | Parsed " + name + "'s Data" + space(maxNameLen - name.length()) + " | Processing Speed: " + speed(processingTime), DEBUG, clazz);
                }
            }
            if (log) {
                System.out.print(backspace(toPrint.length()));
                log("Parsed data for all the trainers.\n", INFO, clazz);
            }
            totalProcessed = totalDownloaded = tcache.length() + Array.toString("", localDataCache, "").length();
            conn.close();
        } catch (IOException | ParseException exc) {
            if (log) System.out.println();
            throw new CachingException(exc);
        }
    }

    /**
     * Returns the Singleton Instance and logs any processes
     *
     * @return the Singleton Instance and logs any processes
     */
    public static TrainerDataCache getInstance() {
        return getInstance(true);
    }

    /**
     * Returns the Singleton Instance and logs any processes if the parameter passed is true
     *
     * @param log to log processes or not
     *            Note:- if this is true, the method takes the monitor of {@code System.out}
     *            So, any other threads waiting on that monitor will be put to sleep
     * @return the Singleton Instance and logs any processes if the parameter passed is true
     */
    public static TrainerDataCache getInstance(boolean log) {
        if (log) {
            synchronized (System.out) {
                return instance != null ? instance : (instance = new TrainerDataCache(true));
            }
        } else {
            return instance != null ? instance : (instance = new TrainerDataCache(false));
        }
    }

    static TrainerDataCache forceReinitialize(boolean log) {
        if (log) {
            synchronized (System.out) {
                return instance = new TrainerDataCache(true);
            }
        } else {
            return instance = new TrainerDataCache(false);
        }
    }

    String speed(long time) {
        return bytesToString(Math.round(totalProcessed * 2 * 1000.0 / time)) + "/sec";
    }
}