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

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class MiscUtil {
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

    public static String space(int count) {
        return repeatCharacter(' ', count);
    }

    public static String backspace(int count) {
        return repeatCharacter('\b', count);
    }

    public static String repeatCharacter(char c, int count) {
        String tor = "";
        while (count-- > 0)
            tor = tor.concat(String.valueOf(c));
        return tor;
    }

    public static int objectToInt(Object a) {
        return Integer.parseInt(a.toString());
    }

    public static int charToInt(char a) {
        return Array.returnID(new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '-'}, a);
    }

    public static String doubleToString(int len, Double num) {
        String tor = num.toString();
        tor=tor.substring(0,Math.min(tor.length(),len));
        while (tor.length()<len) {
            tor=tor.concat("0");
        }
        return tor;
    }

    public static JSONObject parseJSONObject(String json) throws com.vplaygames.PM4J.exceptions.ParseException {
        JSONObject jo;
        try {
            jo = (JSONObject) new JSONParser().parse(json);
        } catch (ParseException e) {
            throw new com.vplaygames.PM4J.exceptions.ParseException();
        }
        return jo;
    }

    public static JSONArray parseJSONArray(String json) throws com.vplaygames.PM4J.exceptions.ParseException {
        JSONArray ja;
        try {
            ja = (JSONArray) new JSONParser().parse(json);
        } catch (ParseException e) {
            throw new com.vplaygames.PM4J.exceptions.ParseException();
        }
        return ja;
    }
}