package net.sourceforge.fenixedu.persistenceTier.delegatedObjects;

import java.util.Collection;

public class DAOResultComparator {

    public static boolean compare(final int result1, final int result2) {
        return result1 == result2;
    }

    public static boolean compare(final boolean result1, final boolean result2) {
        return result1 == result2;
    }

    public static boolean compare(final Object result1, final Object result2) {
        return (result1 == result2) || (result1 != null && result1.equals(result2));
    }

    public static boolean compare(final Collection result1, final Collection result2) {
        return (result1 == result2)
                || (result1 != null && result2 != null && result1.size() == result2.size()
                        && result1.containsAll(result2) && result2.containsAll(result1));
    }

}
