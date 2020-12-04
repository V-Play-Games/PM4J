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

public class DownloadedCache<T> extends ProcessedCache<T> {
    protected long totalDownloaded = 0;
    protected long downloadingTime = 0;

    protected void downloaded(long amt) {
        totalDownloaded += amt;
    }

    public long getDownloadingTime() {
        return downloadingTime;
    }

    public long getTotalDownloaded() {
        return totalDownloaded;
    }

    public long getDownloadingSpeed() {
        return Math.round(totalDownloaded * 2 * 1000.0 / downloadingTime);
    }
}
