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

import java.util.StringJoiner;

/**
 * Utility methods related to Arrays
 *
 * @author Vaibhav Nargwani
 * @since 1.0.0
 */
public class Array {
    /**
     * Checks if the given <code>String</code> is present
     * in the given <code>String</code> Array
     *
     * @param a The String to search
     * @param b The String Array to search in
     * @return <code>true</code> if the <code>a</code> contains <code>b</code>, false otherwise
     */
    public static boolean contains(String a, String[] b) {
        for (String s : b)
            if (a.equalsIgnoreCase(s))
                return true;
        return false;
    }

    /**
     * Formats the given <code>String</code> Array into a String with the given delimiter
     *
     * @param a         the String Array to be formatted
     * @param delimiter the delimiter to be used between the values
     * @param def       the String to be returned if <code>a</code> was null or empty.
     * @return The resultant String with the values from the Array.
     * @throws NullPointerException if the <code>delimiter</code> was null.
     */
    public static String toString(String delimiter, String[] a, String def) {
        if (a == null) return def;
        if (a.length == 0) return def;
        StringJoiner b = new StringJoiner(delimiter);
        for (String s : a) b.add("\"" + s + "\"");
        return b.toString();
    }

    /**
     * Formats the given <code>Object</code> Array into a String with the given delimiter
     *
     * @param a         the Object Array to be formatted
     * @param delimiter the delimiter to be used between the values
     * @param def       the String to be returned if <code>a</code> was null or empty.
     * @return The resultant String with the values from the Array
     * @throws NullPointerException if the <code>delimiter</code> was null.
     */
    public static String toString(String delimiter, Object[] a, String def) {
        if (a == null) return def;
        if (a.length == 0) return def;
        StringJoiner b = new StringJoiner(delimiter);
        for (Object o : a) b.add(o.toString());
        return b.toString();
    }

    /**
     * Converts the given Object Array to a String Array
     *
     * @param a the Object Array to be converted
     * @return The converted String Array.
     */
    public static String[] toStringArray(Object[] a) {
        String[] b = new String[a.length];
        for (int i = 0; i < a.length; i++) {
            b[i] = String.valueOf(a[i]);
        }
        return b;
    }

    /**
     * Returns the index of the given element in the given Array,
     * returns the last index if the Array does not contain the given element
     *
     * @param a the Array to Search in
     * @param b the <code>char</code> to search for
     * @return if the Array contains the <code>char</code>,
     * returns the index of the given element in the given Array,
     * returns the last index otherwise.
     */
    public static int returnID(char[] a, char b) {
        for (int i = 0; i < a.length; i++)
            if (b == a[i])
                return i;
        return a.length - 1;
    }
}