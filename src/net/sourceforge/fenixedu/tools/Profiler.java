/*
 * Created on May 29, 2004
 *
 */
package net.sourceforge.fenixedu.tools;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @author Luis Cruz
 *  
 */
public class Profiler {

    private static Profiler profiler = null;

    private static Map startTimes = null;

    private static Map times = null;

    private static int keyIndex = 0;

    private static int[] objectSynchCreateInstance = new int[0];

    private static void init() {
        synchronized (objectSynchCreateInstance) {
            if (profiler == null) {
                profiler = new Profiler();
            }
            if (startTimes == null) {
                startTimes = new HashMap();
            }
            if (times == null) {
                times = new HashMap();
            }
        }
    }

    public static Profiler getInstance() {
        init();
        return profiler;
    }

    public static void resetInstance() {
        startTimes = null;
        times = null;
        init();
    }

    private Profiler() {
        super();
    }

    public static int getKeyIndex() {
        synchronized (objectSynchCreateInstance) {
            return keyIndex++;
        }
    }

    public static Object start(String key) {
        HashKey hashKey = profiler.getHashKey(key, getKeyIndex());
        startTimes.put(hashKey, Calendar.getInstance());
        return hashKey;
    }

    public static void stop(Object hashKey) {
        Calendar stopTime = Calendar.getInstance();
        Calendar startTime = (Calendar) startTimes.get(hashKey);
        Long executionTime = new Long(stopTime.getTimeInMillis() - startTime.getTimeInMillis());
        times.put(hashKey, executionTime);
    }

    public static void report() {
        Map resultTimes = new HashMap();

        long totalTime = 0;
        Iterator iterator = times.keySet().iterator();
        while (iterator.hasNext()) {
            HashKey hashKey = (HashKey) iterator.next();
            Long time = (Long) times.get(hashKey);
            totalTime += time.longValue();

            String originalKey = hashKey.getKey();
            Long resultTime = (Long) resultTimes.get(originalKey);
            if (resultTime == null) {
                resultTimes.put(originalKey, time);
            } else {
                resultTimes.remove(originalKey);
                resultTimes.put(originalKey, new Long(resultTime.longValue() + time.longValue()));
            }
        }

        iterator = resultTimes.keySet().iterator();
        while (iterator.hasNext()) {
            String originalKey = (String) iterator.next();
            System.out.println(originalKey + " " + resultTimes.get(originalKey));
        }
        System.out.println("Total time: " + totalTime + "ms");
    }

    public HashKey getHashKey(String key, int index) {
        return new HashKey(key, index);
    }

    public class HashKey {

        private String key = null;

        private int index = 0;

        public HashKey(String keyArg, int indexArg) {
            key = keyArg;
            index = indexArg;
        }

        public int getIndex() {
            return index;
        }

        public void setIndex(int index) {
            this.index = index;
        }

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }
    }

}