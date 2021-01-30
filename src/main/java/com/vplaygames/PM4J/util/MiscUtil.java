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

import com.vplaygames.PM4J.exceptions.ParseException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Objects;

/**
 * Other Utility methods
 *
 * @author Vaibhav Nargwani
 * @since 1.0.0
 */
public class MiscUtil {
    /**
     * Formats the given amount of milliseconds to String.
     * Sample Input: 145679
     * Sample Output: 2m 15s 679ms
     *
     * @param ms The amount of milliseconds
     * @return the formatted String
     */
    public static String msToString(long ms) {
        if (ms < 0)
            throw new IllegalArgumentException("ms cannot be in negative!");
        if (ms < 1000)
            return ms + "ms";
        String tor = ms % 1000 + "ms";
        ms /= 1000;
        if (ms < 60)
            return ms + "s " + tor;
        tor = ms % 60 + "s " + tor;
        ms /= 60;
        if (ms < 60)
            return ms + "m " + tor;
        tor = ms % 60 + "m " + tor;
        ms /= 60;
        if (ms < 24)
            return ms + "h " + tor;
        tor = ms % 24 + "h " + tor;
        ms /= 24;
        if (ms < 7)
            return ms + "d " + tor;
        return ms / 7 + "w " + ms % 7 + "d " + tor;
    }

    /**
     * Formats the given amount of bytes to String
     * Sample Input: 1048576
     * Sample Output: 2.0 MB
     *
     * @param bytes The amount of milliseconds
     * @return the formatted String
     */
    public static String bytesToString(long bytes) {
        String[] units = {"bytes", "KB", "MB", "GB", "TB", "PB", "EB", "ZB", "YB"};
        int i = 0;
        double b = bytes;
        while (b > 1024) {
            i++;
            b /= 1024;
        }
        return Math.round(b * 100) / 100.0 + " " + units[i];
    }

    /**
     * Returns a sequence of spaces for the given no. of times
     *
     * @param count The no. of times to repeat a space
     * @return a sequence of spaces for the given no. of times
     */
    public static String space(int count) {
        return repeatCharacter(' ', count);
    }

    /**
     * Returns a sequence of backspaces (<code>'\b'</code>) for the given no. of times
     *
     * @param count The no. of times to repeat a backspace (<code>'\b'</code>)
     * @return a sequence of backspaces (<code>'\b'</code>) for the given no. of times
     */
    public static String backspace(int count) {
        return repeatCharacter('\b', count);
    }

    /**
     * Returns a sequence of the given character for the given no. of times
     *
     * @param c the character to repeat
     * @param count The no. of times to repeat the character
     * @return a sequence of the given character for the given no. of times
     */
    public static String repeatCharacter(char c, int count) {
        String tor = "";
        while (count-- > 0)
            tor = tor.concat(String.valueOf(c));
        return tor;
    }

    /**
     * Converts an object to an integer
     *
     * @param a the Object to convert
     * @return The resultant integer
     */
    public static int objectToInt(Object a) {
        return Strings.toInt(a.toString());
    }

    /**
     * Converts a <code>char</code> to an integer
     *
     * @param a the <code>char</code> to convert
     * @return The resultant integer, returns 10 if the character is not a digit.
     */
    public static int charToInt(char a) {
        return Array.returnID(new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '-'}, a);
    }

    /**
     * Converts a <code>Double</code> to a String of the given length
     *
     * @param len the length of the resulting String
     * @param num the <code>Double</code> to convert
     * @return The resultant String of the given length.
     */
    public static String doubleToString(int len, Double num) {
        String tor = Objects.requireNonNull(num).toString();
        tor=tor.substring(0,Math.min(tor.length(),len));
        while (tor.length()<len)
            tor=tor.concat("0");
        return tor;
    }

    /**
     * Parses the given JSON String to a {@link JSONObject}
     *
     * @param json The JSON String to parse
     * @return The resultant {@link JSONObject}
     * @throws ParseException if the Parsing fails
     */
    public static JSONObject parseJSONObject(String json) throws ParseException {
        JSONObject jo;
        try {
            jo = (JSONObject) new JSONParser().parse(json);
        } catch (org.json.simple.parser.ParseException e) {
            throw new ParseException();
        }
        return jo;
    }

    /**
     * Parses the given JSON String to a {@link JSONArray}
     *
     * @param json The JSON String to parse
     * @return The resultant {@link JSONArray}
     * @throws ParseException if the Parsing fails
     */
    public static JSONArray parseJSONArray(String json) throws ParseException {
        JSONArray ja;
        try {
            ja = (JSONArray) new JSONParser().parse(json);
        } catch (org.json.simple.parser.ParseException e) {
            throw new ParseException();
        }
        return ja;
    }

    public static void lockAll(BranchedPrintStream stream, Runnable action) {
        lock(new ArrayList<>(stream.getPrintStreams()), action, 0);
    }

    private static void lock(ArrayList<PrintStream> streams, Runnable action, int index) {
        if (index<streams.size()) {
            synchronized (streams.get(index)) {
                lock(streams, action, ++index);
                if (index==streams.size()) {
                    action.run();
                }
            }
        }
    }
}
