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
import com.vplaygames.PM4J.Settings;
import com.vplaygames.PM4J.entities.Constants;
import com.vplaygames.PM4J.entities.Trainer;
import com.vplaygames.PM4J.exceptions.CachingException;
import com.vplaygames.PM4J.jsonFramework.JSONArray;
import com.vplaygames.PM4J.util.BranchedPrintStream;
import com.vplaygames.PM4J.util.MiscUtil;
import com.vplaygames.PM4J.util.Queueable;

import java.util.function.Consumer;

import static com.vplaygames.PM4J.Logger.Mode.DEBUG;
import static com.vplaygames.PM4J.Logger.Mode.INFO;
import static com.vplaygames.PM4J.Logger.log;
import static com.vplaygames.PM4J.util.MiscUtil.*;

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
 * inheritor of HashMap. This class also provides other details such as processing time.
 * See {@link com.vplaygames.PM4J.caches.framework.ProcessedCache} for more information.
 *
 * @author Vaibhav Nargwani
 * @since 1.0.0
 * @see com.vplaygames.PM4J.caches.framework.Cache
 * @see com.vplaygames.PM4J.caches.framework.ProcessedCache
 * @see java.util.HashMap
 */
public class TrainerDataCache extends DataCache<TrainerDataCache, Trainer> {
    private static volatile TrainerDataCache instance;
    private boolean downloaded = false;
    private boolean interrupted = false;
    private long startTime = System.currentTimeMillis();
    private JSONArray<Trainer> trainers;
    private String[] localData;
    private int cursor = 0;
    private int maxNameLen = 12;

    private TrainerDataCache() {}

    @Override
    public Queueable<TrainerDataCache> process() {
        return process(null);
    }

    public Queueable<TrainerDataCache> process(Consumer<TrainerDataCache> action) {
        Settings snapshot = new Settings().copyFrom(settings);
        return new Queueable<>(() -> {
            if (initialized) return;
            try {
                if (snapshot.getLogPolicy()) {
                    lockAll(snapshot.getLogOutputStream(), this::download);
                } else {
                    download();
                }
                if (action!=null) {
                    action.accept(this);
                }
                snapshot.copyFrom(settings);
                if (snapshot.getLogPolicy()) {
                    lockAll(snapshot.getLogOutputStream(), this::process0);
                } else {
                    process0();
                }
                initialized = true;
            } catch (Exception exc) {
                if (snapshot.getLogPolicy()) snapshot.getLogOutputStream().println();
                interrupted = true;
                if (exc instanceof CachingException) {
                    throw (CachingException) exc;
                }
                throw new CachingException(exc);
            }
        }, this);
    }

    protected void download() {
        if (downloaded) return;
        boolean log = settings.getLogPolicy();
        BranchedPrintStream printer = settings.getLogOutputStream().clone();
        Connection conn = settings.getConnection();
        Class<?> clazz = getClass();
        String toPrint = "";
        totalProcessed = interrupted ? totalProcessed : 0;
        if (trainers == null) {
            if (log) printer.print(log("Downloading the list of trainers...\n", DEBUG, clazz, false));
            String rawTrainerList;
            try {
                rawTrainerList = conn.requestTrainers();
            } catch (Exception exc) {
                throw new CachingException(exc);
            }
            trainers = JSONArray.parse(
                (org.json.simple.JSONArray) MiscUtil.parseJSONObject(rawTrainerList).get("trainers"),
                Constants.EMPTY_TRAINER);
            if (log) printer.print(log("Downloaded the list of all trainers.\n", INFO, clazz, false));
            totalProcessed += rawTrainerList.length();
        }
        int len = trainers.size();
        localData = new String[len];
        if (log) printer.print(toPrint = log("Downloading Trainer Data...", DEBUG, clazz, false));
        while (cursor < len) {
            String name = trainers.get(cursor).name;
            try {
                localData[cursor] = conn.requestTrainer(name);
            } catch (Exception exc) {
                throw new CachingException(exc);
            }
            totalProcessed += localData[cursor].length();
            if (log) {
                printer.print(backspace(toPrint.length()) + (toPrint = log("Total Progress: " + doubleToString(5, ++cursor * 100.0 / len) + "% | Downloaded " + name + "'s Data" + space(maxNameLen - name.length()) + " | Download Speed: " + speed(), DEBUG, clazz, false)));
            }
        }
        if (log) {
            printer.print(backspace(toPrint.length()) + log("Downloaded data for all the trainers.\n", INFO, clazz, false));
        }
        cursor = 0;
        downloaded = true;
    }

    protected void process0() {
        boolean log = settings.getLogPolicy();
        BranchedPrintStream printer = settings.getLogOutputStream().clone();
        Class<?> clazz = getClass();
        totalProcessed = interrupted ? totalProcessed : 0;
        int len = localData.length;
        String toPrint = "";
        if (log) printer.print(toPrint = log("Parsing Trainer Data...", INFO, clazz, false));
        while (cursor < len) {
            String name = trainers.get(cursor).name;
            String json = localData[cursor];
            Trainer trainer = Trainer.parse(json);
            put(name, trainer);
            totalProcessed += json.length();
            if (log) {
                printer.print(backspace(toPrint.length()) + (toPrint = log("Total Progress: " + doubleToString(5, ++cursor * 100.0 / len) + "% | Parsed " + name + "'s Data" + space(maxNameLen - name.length()) + " | Processing Speed: " + speed(), DEBUG, clazz, false)));
            }
        }
        if (log) {
            printer.print(backspace(toPrint.length()) + log("Parsed data for all the trainers.\n", INFO, clazz, false));
        }
    }

    /**
     * Returns the Singleton Instance and logs any processes
     *
     * @return the Singleton Instance and logs any processes
     */
    public static TrainerDataCache getInstance() {
        return instance == null ? instance = new TrainerDataCache() : instance;
    }

    /**
     * In version 1.0.0, this method was used to construct the Singleton Instance
     * and turned on/off logging depending on the {@code log} parameter.
     *
     * This method is now {@link Deprecated} because whether logging should be done
     * or not can be set in the {@link com.vplaygames.PM4J.Settings} and the Singleton Instance
     * is now constructed by the {@link #getInstance()} and initialized by
     * {@link #process()} or {@link #process(Consumer)} methods.
     * @param log whether turn on logging or not.
     * @return the Singleton Instance.
     * @deprecated
     */
    @Deprecated
    public static TrainerDataCache getInstance(boolean log) {
        return getInstance().useSettings(instance.settings.setLogPolicy(log));
    }

    @Override
    public Queueable<TrainerDataCache> invalidateCache() {
        return new Queueable<>(() -> {
            downloaded = false;
            initialized = false;
            interrupted = false;
            trainers = null;
            localData = null;
            cursor = 0;
            clear();
        }, this);
    }

    private String speed() {
        return bytesToString(Math.round(totalProcessed * 2 * 1000.0 / (processingTime = System.currentTimeMillis() - startTime))) + "/sec";
    }
}
