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
package com.vplaygames.PM4J.util;

public class Array {
    public static boolean contains(String a, String[] b) {
        return Strings.equalsAnyIgnoreCase(a, b);
    }

    public static String toString(String delimiter, String[] a, String def) {
        if (a == null) return def;
        if (a.length == 0) return def;
        StringBuilder b = new StringBuilder().append("\"").append(a[0]).append("\"");
        for (int i = 1; i < a.length; i++)
            b.append(delimiter).append("\"").append(a[i]).append("\"");
        return b.toString();
    }

    public static String toString(String delimiter, Object[] a, String def) {
        if (a == null) return def;
        if (a.length == 0) return def;
        StringBuilder b = new StringBuilder().append(a[0]);
        for (int i = 1; i < a.length; i++)
            b.append(delimiter).append(a[i]);
        return b.toString();
    }

    public static String[] toStringArray(Object[] a) {
        String[] b = new String[a.length];
        for(int i = 0; i<a.length; i++) {
            b[i] = a[i].toString();
        }
        return b;
    }

    public static int returnID(char[] a, char b) {
        for (int i = 0; i < a.length; i++)
            if (b == a[i])
                return i;
        return a.length - 1;
    }
}
