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
package com.vplaygames.PM4J.caches.framework;

public class ProcessedCache<T> extends Cache<T> {
    protected long totalProcessed = 0;
    protected long processingTime;

    public ProcessedCache() {
        processingTime = System.currentTimeMillis();
    }

    protected void processed(long amt) {
        totalProcessed += amt;
    }

    protected void initialized() {
        processingTime = System.currentTimeMillis() - processingTime;
    }

    public long getProcessingTime() {
        return processingTime;
    }

    public long getTotalProcessed() {
        return totalProcessed;
    }

    public long getProcessingSpeed() {
        return Math.round(totalProcessed * 2 * 1000.0 / processingTime);
    }
}
