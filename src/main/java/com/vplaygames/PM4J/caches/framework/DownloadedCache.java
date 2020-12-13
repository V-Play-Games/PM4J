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
 * Represents a Downloaded Cache which stores data in the form of String-Object Mappings.
 * It also tracks the amount of data downloaded, amount of time taken to download,
 * and the download speed. This class inherits all the details from {@link ProcessedCache}
 * like Processing Speed etc.
 *
 * @author Vaibhav Nargwani
 * @since 1.0.0
 * @see Cache
 * @see ProcessedCache
 * @see java.util.HashMap
 */
public class DownloadedCache<T> extends ProcessedCache<T> {
    protected long totalDownloaded = 0;
    protected long downloadingTime = 0;

    /**
     * Adds the given amount of data downloaded
     * to the {@link DownloadedCache#totalDownloaded}
     *
     * @param amt Amount of Data just downloaded
     */
    protected void downloaded(long amt) {
        totalDownloaded += amt;
    }

    /**
     * Returns the amount of time taken to download
     *
     * @return The amount of time taken to download
     */
    public long getDownloadingTime() {
        return downloadingTime;
    }

    /**
     * Returns the amount of data downloaded
     *
     * @return the amount of data downloaded
     */
    public long getTotalDownloaded() {
        return totalDownloaded;
    }

    /**
     * Returns the approximate downloading speed
     * Note:- This may not be correct to the actual
     * downloading speed of this device
     *
     * @return the approximate downloading speed
     */
    public long getDownloadingSpeed() {
        return Math.round(totalDownloaded * 2 * 1000.0 / downloadingTime);
    }
}
