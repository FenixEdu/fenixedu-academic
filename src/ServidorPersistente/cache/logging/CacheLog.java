/*
 * Created on Dec 30, 2003
 *
 */
package ServidorPersistente.cache.logging;

/**
 * @author Luis Cruz
 *  
 */
public class CacheLog {

    private static int numberPuts = 0;

    private static int numberSuccessfulLookUps = 0;

    private static int numberFailedLookUps = 0;

    private static int numberRemoves = 0;

    private static int numberClears = 0;

    /**
     * @return Returns the numberClears.
     */
    public static int getNumberClears() {
        return numberClears;
    }

    /**
     * @param numberClears
     *            The numberClears to set.
     */
    public static void incrementClears() {
        CacheLog.numberClears++;
    }

    /**
     * @return Returns the numberFailedLookUps.
     */
    public static int getNumberFailedLookUps() {
        return numberFailedLookUps;
    }

    /**
     * @param numberFailedLookUps
     *            The numberFailedLookUps to set.
     */
    public static void incrementFailedLookUps() {
        CacheLog.numberFailedLookUps++;
    }

    /**
     * @return Returns the numberPuts.
     */
    public static int getNumberPuts() {
        return numberPuts;
    }

    /**
     * @param numberPuts
     *            The numberPuts to set.
     */
    public static void incrementPuts() {
        CacheLog.numberPuts++;
    }

    /**
     * @return Returns the numberRemoves.
     */
    public static int getNumberRemoves() {
        return numberRemoves;
    }

    /**
     * @param numberRemoves
     *            The numberRemoves to set.
     */
    public static void incrementRemoves() {
        CacheLog.numberRemoves++;
    }

    /**
     * @return Returns the numberSuccessfulLookUps.
     */
    public static int getNumberSuccessfulLookUps() {
        return numberSuccessfulLookUps;
    }

    /**
     * @param numberSuccessfulLookUps
     *            The numberSuccessfulLookUps to set.
     */
    public static void incrementSuccessfulLookUps() {
        CacheLog.numberSuccessfulLookUps++;
    }

}