/*
 * Created on 21/Out/2003
 *
 */
package net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao;

import java.util.Calendar;
import java.util.List;

import net.sourceforge.fenixedu.domain.IPeriod;
import net.sourceforge.fenixedu.domain.Period;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentPeriod;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.VersionedObjectsBase;

/**
 * @author Ana e Ricardo
 * 
 */
public class PeriodVO extends VersionedObjectsBase implements IPersistentPeriod {

    public Object readByCalendarAndNextPeriod(Calendar startDate, Calendar endDate, Integer keyNextPeriod)
            throws ExcepcaoPersistencia {

        List<IPeriod> periods = (List<IPeriod>) readAll(Period.class);
        for (IPeriod period : periods) {
            if (keyNextPeriod == null) {
                if (period.getStartDate().equals(startDate) && period.getEndDate().equals(endDate)) {
                    return period;
                }
            } else {
                if (period.getStartDate().equals(startDate) && period.getEndDate().equals(endDate)
                        && period.getNextPeriod().getIdInternal().equals(keyNextPeriod)) {
                    return period;
                }
            }
        }
        return null;

        /*
         * Criteria criteria = new Criteria(); criteria.addEqualTo("startDate",
         * startDate); criteria.addEqualTo("endDate", endDate); if
         * (keyNextPeriod != null) {
         * criteria.addEqualTo("nextPeriod.idInternal", keyNextPeriod); } return
         * queryObject(Period.class, criteria);
         */
    }

}