package org.fenixedu.academic.util;

import java.util.HashMap;
import java.util.Map;

public class ReportCache {

    private static InheritableThreadLocal<Map<String, Object>> cache = new InheritableThreadLocal<>();

    public static boolean isActive() {
        return cache.get() != null;
    }

    public static void activete() {
        cache.set(new HashMap<>());
    }

    public static void deactive() {
        cache.remove();
    }

    public static <T> T read(final String key) {
        return (T) cache.get().get(key);
    }

    public static <T> void cache(final String key, final T value) {
        cache.get().put(key, value);
    }

}
