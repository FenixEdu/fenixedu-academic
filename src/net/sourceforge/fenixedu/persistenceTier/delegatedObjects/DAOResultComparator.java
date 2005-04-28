package net.sourceforge.fenixedu.persistenceTier.delegatedObjects;

import java.util.Collection;

public class DAOResultComparator {

    public static boolean compare(final int result1, final int result2) {
        return result1 == result2;
    }

    public static boolean compare(final boolean result1, final boolean result2) {
        return result1 == result2;
    }

    public static boolean compare(final Collection result1, final Collection result2) {
        return false;
    }

    public static boolean compare(final Object result1, final Object result2) {
        return false;
    }

}
