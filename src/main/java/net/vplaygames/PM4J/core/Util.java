package net.vplaygames.PM4J.core;

import java.util.StringJoiner;

/**
 * Utility methods related to Arrays
 *
 * @author Vaibhav Nargwani
 * @since 1.1.0
 */
public class Util {
    // private constructor to disable instances.
    private Util() {}

    /**
     * Formats the given {@code String} Array into a String with the given delimiter
     *
     * @param a the String Array to be formatted
     * @return The resultant String with the values from the Array
     * @throws NullPointerException if the <code>delimiter</code> was null
     */
    public static String toString(String[] a) {
        if (a == null || a.length == 0) return "";
        StringJoiner b = new StringJoiner("\",\"", "[\"", "\"]");
        for (String s : a) b.add(s);
        return b.toString();
    }

    /**
     * Converts a {@code double} to a {@code String} of the given length
     *
     * @param len the length of the resulting String
     * @param num the <code>Double</code> to convert
     * @return The resultant String of the given length.
     */
    public static String toString(int len, double num) {
        String tor = Double.toString(num);
        tor = tor.substring(0, Math.min(tor.length(), len));
        while (tor.length() < len)
            tor = tor.concat("0");
        return tor;
    }

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
            tor += Character.isDigit(c) ? tor * 9 + c - 48 : 0; // '0' = 48
        }
        return a.startsWith("-") ? -tor : tor;
    }

    /**
     * Removes any non-alphanumeric characters from the given {@code String}
     *
     * @param s the {@code String} to reduce
     * @return The {@code String}, reduced to only alphanumeric characters.
     */
    public static String reduceToAlphanumeric(String s) {
        if (s == null) return null;
        StringBuilder tor = new StringBuilder();
        for (int i = 0; i < s.length(); i++)
            if (Character.isLetterOrDigit(s.charAt(i)))
                tor.append(s.charAt(i));
        return tor.toString();
    }
}
