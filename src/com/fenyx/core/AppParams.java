package com.fenyx.core;

import com.fenyx.utils.ResourceUtils;
import com.fenyx.utils.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author DarkPartizaN
 */
public class AppParams {

    private static final Map<String, String> params = new HashMap<>();

    public static void addParam(String name, String value) {
        params.put(name, value);
    }

    public static void editParam(String name, String newValue) {
        params.replace(name, newValue);
    }

    public static Map<String, String> getAllParams() {
        return params;
    }

    public static String getString(String name) {
        return params.get(name);
    }

    public static int getInt(String name) {
        return Integer.parseInt(params.get(name));
    }

    public static long getLong(String name) {
        return Long.parseLong(params.get(name));
    }

    public static float getFloat(String name) {
        return Float.parseFloat(params.get(name));
    }

    public static double getDouble(String name) {
        return Double.parseDouble(params.get(name));
    }

    public static boolean getBool(String name) {
        return Boolean.parseBoolean(params.get(name));
    }

    public static boolean isEmpty() {
        return params.isEmpty();
    }

    public static void loadFromFile(String path) {
        setDefaults();

        for (String s : ResourceUtils.loadLinesArray(path)) {
            if (s.isEmpty() || s.startsWith("//")) continue;

            String[] tokens = StringUtils.splitString(s, "=");
            tokens[0] = StringUtils.trim(tokens[0]);
            tokens[0] = StringUtils.replace(tokens[0], " ", "");
            tokens[1] = StringUtils.crop(tokens[1], "//");
            tokens[1] = StringUtils.trim(tokens[1]);

            AppParams.addParam(tokens[0], tokens[1]);
        }
    }

    public static void setDefaults() {
        AppParams.addParam("com.fenyx.AppName", "Default Fenyx App");
        AppParams.addParam("com.fenyx.Width", "800");
        AppParams.addParam("com.fenyx.Height", "600");
        AppParams.addParam("com.fenyx.Fullscreen", "false");
    }
}