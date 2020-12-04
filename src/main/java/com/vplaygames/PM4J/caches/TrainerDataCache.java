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
import com.vplaygames.PM4J.entities.TrainerData;
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

public class TrainerDataCache extends DownloadedCache<TrainerData> {
    private static volatile TrainerDataCache instance;

    public final JSONArray<Trainer> TrainerCache;
    public final JSONArray<TrainerData> DataCache;

    private TrainerDataCache(boolean log) throws CachingException {
        try {
            Class<?> clazz = getClass();
            long temp = currentTimeMillis();
            log("Commencing the download & parse of data from https://pokemasdb.com\n", INFO, clazz);
            Connection conn = new Connection();
            log("Downloading the list of trainers.\n", DEBUG, clazz, log);
            processingTime = currentTimeMillis();
            String tcache = conn.requestTrainers();
            downloadingTime = currentTimeMillis();
            log("Downloaded the list of all trainers.\n", INFO, clazz, log);
            String toPrint = log("Downloading Trainer Data.", DEBUG, clazz, log);
            TrainerCache = JSONArray.parse(
                    (org.json.simple.JSONArray) ((JSONObject) new JSONParser().parse(tcache)).get("trainers"),
                    Constants.EMPTY_TRAINER);
            temp = (currentTimeMillis() - downloadingTime) + (processingTime - temp);
            downloadingTime = downloadingTime - processingTime;
            int len = TrainerCache.size();
            int maxNameLen = 12;
            int i = 0;
            downloaded(tcache.length());
            String[] localDataCache = new String[len];
            processingTime = temp;
            while (i < len) {
                temp = currentTimeMillis();
                String name = TrainerCache.get(i).name;
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
            DataCache = new JSONArray<>(0, Constants.EMPTY_TRAINER_DATA);
            while (i < len) {
                temp = currentTimeMillis();
                String name = TrainerCache.get(i).name;
                String json = localDataCache[i];
                TrainerData datum;
                datum = TrainerData.parse(json);
                DataCache.add(datum);
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
            TrainerCache.initialized();
            DataCache.initialized();
            conn.close();
        } catch (IOException | ParseException exc) {
            if (log) System.out.println();
            throw new CachingException(exc);
        }
    }

    public static TrainerDataCache getInstance() {
        return getInstance(true);
    }

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