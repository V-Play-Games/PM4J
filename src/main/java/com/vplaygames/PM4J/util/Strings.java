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

/**
 * Utility methods related to Strings
 *
 * @author Vaibhav Nargwani
 * @since 1.0.0
 */
public class Strings {
    /**
     * Converts the given String to an integer, ignores any non-numeric characters
     *
     * @param a The String to be converted
     * @return The integer result
     */
    public static int toInt(String a) {
        int tor = 0, i = 0;
        while (i < a.length()) {
            char c = a.charAt(i++);
            tor = Character.isDigit(c) ? tor * 10 + MiscUtil.charToInt(c) : tor;
        }
        return a.startsWith("-") ? -tor : tor;
    }

    /**
     * Removes any non-alphanumeric characters from the given String
     *
     * @param s the String to reduce
     * @return The String, reduced to alphanumeric characters.
     */
    public static String reduceToAlphanumeric(String s) {
        StringBuilder tor = new StringBuilder();
        for (char c : s.toCharArray())
            if (Character.isLetterOrDigit(c))
                tor.append(c);
        return tor.toString();
    }
}