/*
 * Created on 21/Out/2003
 *
 */
package net.sourceforge.fenixedu.persistenceTier.OJB;

import java.util.Date;

import net.sourceforge.fenixedu.domain.OccupationPeriod;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentPeriod;

import org.apache.ojb.broker.query.Criteria;

/**
 * @author Ana e Ricardo
 * 
 */
public class PeriodOJB extends ObjectFenixOJB implements IPersistentPeriod {

    public Object readByCalendarAndNextPeriod(Date startDate, Date endDate, Integer keyNextPeriod)
            throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("start", startDate);
        criteria.addEqualTo("end", endDate);
        if (keyNextPeriod != null) {
            criteria.addEqualTo("nextPeriod.idInternal", keyNextPeriod);
        }
        return queryObject(OccupationPeriod.class, criteria);
    }

}