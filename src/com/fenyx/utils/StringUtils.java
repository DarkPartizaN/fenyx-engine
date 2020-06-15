package com.fenyx.utils;

import java.util.ArrayList;

/**
 *
 * @author DarkPartizaN
 */
public class StringUtils {

    private static final StringBuilder sb = new StringBuilder(64);

    public static String replace(String s, String from, String to) {
        while (s.contains(from))
            s = s.replace(from, to);

        return s;
    }

    public static String concat(String... strings) {
        sb.delete(0, sb.length());

        for (String s : strings) sb.append(s);

        return sb.toString();
    }

    public static String concat(Object... strings) {
        sb.delete(0, sb.length());

        for (Object s : strings) sb.append(String.valueOf(s));

        return sb.toString();
    }

    public static String[] splitString(String str, String separator) {
        ArrayList<String> strings = new ArrayList<>();

        int start = 0;
        int skip = separator.length();

        str = concat(str, separator);

        while (start < str.length()) {
            int end = str.indexOf(separator, start);
            strings.add(str.substring(start, end));
            start = end + skip;
        }

        return (String[]) strings.toArray(new String[strings.size()]);
    }

    public static final String arrayToString(String[] strings) {
        sb.delete(0, sb.length());

        for (String s : strings) sb.append(s);

        return sb.toString();
    }

    public static final String arrayToString(String[] strings, String separator) {
        sb.delete(0, sb.length());

        for (String s : strings) {
            sb.append(s);
            sb.append(separator);
        }

        return sb.toString();
    }

    public static final String crop(String src, String pattern) {
        int pos = src.lastIndexOf(pattern);
        return (pos <= 0) ? src : src.substring(0, pos - 1);
    }

    public static final String trim(String string) {
        return string.trim();
    }
}