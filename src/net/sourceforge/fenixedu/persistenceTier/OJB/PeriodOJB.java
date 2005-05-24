/*
 * Created on 21/Out/2003
 *
 */
package net.sourceforge.fenixedu.persistenceTier.OJB;

import java.util.Calendar;

import net.sourceforge.fenixedu.domain.Period;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentPeriod;

import org.apache.ojb.broker.query.Criteria;

/**
 * @author Ana e Ricardo
 * 
 */
public class PeriodOJB extends ObjectFenixOJB implements IPersistentPeriod {

    public Object readByCalendarAndNextPeriod(Calendar startDate, Calendar endDate, Integer keyNextPeriod)
            throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("startDate", startDate);
        criteria.addEqualTo("endDate", endDate);
        if (keyNextPeriod != null) {
            criteria.addEqualTo("nextPeriod.idInternal", keyNextPeriod);
        }
        return queryObject(Period.class, criteria);
    }

}