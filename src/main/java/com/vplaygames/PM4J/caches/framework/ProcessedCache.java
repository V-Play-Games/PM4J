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

/**
 * Represents a Processed Cache which stores data in the form of String-Object Mappings.
 * It also tracks the amount of data processed, amount of time taken to process
 * and the processing speed.
 *
 * @author Vaibhav Nargwani
 * @since 1.0.0
 * @see Cache
 * @see ProcessedCache
 * @see java.util.HashMap
 */
public class ProcessedCache<T> extends Cache<T> {
    protected long totalProcessed = 0;
    protected long processingTime;

    public ProcessedCache() {
        processingTime = System.currentTimeMillis();
    }

    /**
     * Adds the given amount of data processed
     * to the {@link ProcessedCache#totalProcessed}
     *
     * @param amt Amount of Data just processed
     */
    protected void processed(long amt) {
        totalProcessed += amt;
    }

    /**
     * Indicates that the object is initialized and the data is processed
     * Also sets the processing time. It is recommended to call this method
     * as the last line of the constructor.
     */
    protected void initialized() {
        processingTime = System.currentTimeMillis() - processingTime;
    }

    /**
     * Returns the amount of time taken to process
     *
     * @return The amount of time taken to process
     */
    public long getProcessingTime() {
        return processingTime;
    }

    /**
     * Returns the amount of data processed
     *
     * @return the amount of data processed
     */
    public long getTotalProcessed() {
        return totalProcessed;
    }

    /**
     * Returns the approximate processing speed
     * Note:- This may not be correct to the actual
     * downloading speed of this device
     *
     * @return the approximate processing speed
     */
    public long getProcessingSpeed() {
        return Math.round(totalProcessed * 2 * 1000.0 / processingTime);
    }
}
