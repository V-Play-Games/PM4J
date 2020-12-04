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

import com.vplaygames.PM4J.caches.framework.DownloadedCache;

import java.util.ArrayList;
import java.util.HashMap;

import static com.vplaygames.PM4J.Logger.Mode.INFO;
import static com.vplaygames.PM4J.Logger.log;
import static com.vplaygames.PM4J.util.MiscUtil.*;

public class PokemasDBCache extends DownloadedCache<PokemasDBCache.Node> {
    private static volatile PokemasDBCache instance;
    private static volatile boolean initializing = false;
    private static volatile boolean initialized = false;

    private PokemasDBCache(boolean log, boolean isForced) {
        initializing = true;
        TrainerDataCache tdc;
        PokemonDataCache pdc;
        MoveDataCache mdc;
        SkillDataCache sdc;
        if (isForced) {
            tdc = TrainerDataCache.forceReinitialize(log);
            pdc = PokemonDataCache.forceReinitialize(log);
            mdc = MoveDataCache.forceReinitialize(log);
            sdc = SkillDataCache.forceReinitialize(log);
        } else {
            tdc = TrainerDataCache.getInstance(log);
            pdc = PokemonDataCache.getInstance(log);
            mdc = MoveDataCache.getInstance(log);
            sdc = SkillDataCache.getInstance(log);
        }
        process(tdc);
        process(pdc);
        process(mdc);
        process(sdc);
        totalDownloaded = tdc.getTotalDownloaded();
        downloadingTime = tdc.getDownloadingTime();
        totalProcessed = tdc.getTotalProcessed();
        processingTime = tdc.getProcessingTime() + pdc.getProcessingTime() + mdc.getProcessingTime() + sdc.getProcessingTime();
        Class<?> clazz = getClass();
        log("Successfully finished processing of all the data.\n", INFO, clazz);
        if (log) {
            String pSpeed = bytesToString(getProcessingSpeed()) + "/sec";
            String dSpeed = bytesToString(getDownloadingSpeed()) + "/sec";
            pSpeed += space(dSpeed.length() - pSpeed.length());
            dSpeed += space(pSpeed.length() - dSpeed.length());
            log("Took " + msToString(downloadingTime + processingTime) + ".\n", INFO, clazz);
            log("Process  - Speed: " + pSpeed + " Time: " + msToString(processingTime) + ".\n", INFO, clazz);
            log("Download - Speed: " + dSpeed + " Time: " + msToString(downloadingTime) + ".\n", INFO, clazz);
            log("Downloaded " + bytesToString(totalDownloaded * 2) + " of data.\n", INFO, clazz);
        }
        initializing = false;
        initialized = true;
    }

    public static PokemasDBCache getInstance() {
        return instance;
    }

    public static void initialize() {
        if (!initialized)
            initialize(true);
    }

    public static void initialize(boolean log) {
        if (!initialized)
            initialize(log, false);
    }

    public static void initialize(boolean log, boolean runInParallel) {
        if (!initialized)
        initialize(log, runInParallel, false);
    }

    private static void initialize(boolean log, boolean runInParallel, boolean isForced) {
        if (initialized || initializing) return;
        try {
            if (runInParallel)
                new Thread(() -> PokemasDBCache.initialize0(log, isForced),"PM4J-Cache").start();
            else
                initialize0(log, isForced);
        } catch (Throwable t) {
            initializing = false;
            initialized = false;
            throw t;
        }
    }

    public static void forceReinitialize(boolean log, boolean runInParallel) {
        initializing = false;
        initialized = false;
        initialize(log, runInParallel, true);
    }

    private static void initialize0(boolean log, boolean isForced) {
        if (instance == null)
            instance = new PokemasDBCache(log, isForced);
    }

    private <V> void process(HashMap<String,V> map) {
        map.forEach((k, v) -> {
            Node node;
            if (containsKey(k))
                node = get(k);
            else
                node = new Node();
            put(k,node.add(v));
        });
    }

    public static class Node {
        ArrayList<Object> data = new ArrayList<>();

        Node add(Object o) {
            data.add(o);
            return this;
        }

        public Object[] getData() {
            return data.toArray();
        }
    }
}