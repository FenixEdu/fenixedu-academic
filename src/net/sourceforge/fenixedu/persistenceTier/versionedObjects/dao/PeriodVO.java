package net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao;

import java.util.Date;
import java.util.List;

import net.sourceforge.fenixedu.domain.IPeriod;
import net.sourceforge.fenixedu.domain.Period;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentPeriod;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.VersionedObjectsBase;

public class PeriodVO extends VersionedObjectsBase implements IPersistentPeriod {

    public Object readByCalendarAndNextPeriod(Date start, Date end, Integer keyNextPeriod)
            throws ExcepcaoPersistencia {

        List<IPeriod> periods = (List<IPeriod>) readAll(Period.class);
        for (IPeriod period : periods) {
            if (keyNextPeriod == null) {
                if (period.getStartDate().equals(start) && period.getEndDate().equals(end)) {
                    return period;
                }
            } else {
                if (period.getStartDate().equals(start) && period.getEndDate().equals(end)
                        && period.getNextPeriod().getIdInternal().equals(keyNextPeriod)) {
                    return period;
                }
            }
        }
        return null;
    }

}