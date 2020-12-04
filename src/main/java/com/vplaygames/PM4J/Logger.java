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
package com.vplaygames.PM4J;

import java.time.LocalTime;

public class Logger {
    public static String log(String s, Mode mode, Class<?> clazz) {
        return log(s, mode, clazz, true);
    }

    public static String log(String s, Mode mode, Class<?> clazz, boolean print) {
        String top = LocalTime.now().toString() + " ["+Thread.currentThread().getName()+"] "+mode+" " + clazz.getName() + " - " + s;
        if (print) System.out.print(top);
        return top;
    }

    public enum Mode {
        INFO, DEBUG, WARN, ERROR
    }
}
