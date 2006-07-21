package net.sourceforge.fenixedu.domain.assiduousness.util;

import org.joda.time.DateMidnight;
import org.joda.time.Duration;
import org.joda.time.Period;

public class TimeUtils {

    public static Duration subtractDurationsWithoutSeconds(Duration firstDuration,
            Duration secondDuration) {
        Period normalWorkedPeriod = firstDuration.toPeriod();
        normalWorkedPeriod = normalWorkedPeriod.minusSeconds(normalWorkedPeriod.getSeconds());
        return normalWorkedPeriod.toDurationFrom(new DateMidnight()).minus(secondDuration);
    }

}
