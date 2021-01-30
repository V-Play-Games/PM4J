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

import com.vplaygames.PM4J.Settings;
import com.vplaygames.PM4J.caches.framework.ProcessedCache;
import com.vplaygames.PM4J.util.Queueable;

@SuppressWarnings("unchecked")
public abstract class DataCache<T extends DataCache<T, C>, C> extends ProcessedCache<C> {
    protected Settings settings;
    protected boolean initialized = false;

    protected DataCache() {
        settings = new Settings();
    }

    public Queueable<T> invalidateCache() {
        return new Queueable<>(() -> {
            initialized = false;
            clear();
        }, (T) this);
    }

    public Queueable<T> process() {
        return new Queueable<T>(() -> {
            if (!initialized) {
                process0();
                initialized = true;
            }
        }, (T) this);
    }

    protected abstract void process0();

    public boolean isInitialized() {
        return initialized;
    }

    public T useSettings(Settings settings) {
        this.settings.copyFrom(settings);
        return (T) this;
    }

    public Settings getSettings() {
        return settings;
    }
}
