/*
 * Created on 7/Jun/2004
 *  
 */
package net.sourceforge.fenixedu.persistenceTier.OJB.sms;

import java.util.Date;
import java.util.List;

import net.sourceforge.fenixedu.domain.sms.SentSms;
import net.sourceforge.fenixedu.domain.sms.SmsDeliveryType;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.OJB.PersistentObjectOJB;
import net.sourceforge.fenixedu.persistenceTier.sms.IPersistentSentSms;

import org.apache.ojb.broker.query.Criteria;

/**
 * @author <a href="mailto:sana@ist.utl.pt">Shezad Anavarali </a>
 * @author <a href="mailto:naat@ist.utl.pt">Nadir Tarmahomed </a>
 *  
 */
public class SentSmsOJB extends PersistentObjectOJB implements IPersistentSentSms {

    public List readByPerson(Integer personID, Integer interval) throws ExcepcaoPersistencia {
        Criteria crit = new Criteria();
        crit.addEqualTo("person.idInternal", personID);
        return readInterval(SentSms.class, crit, interval, new Integer(0), "sendDate", false);
    }

    public Integer countByPersonAndDatePeriod(Integer personId, Date startDate, Date endDate) {
        Criteria crit = new Criteria();
        crit.addEqualTo("person.idInternal", personId);
        crit.addGreaterOrEqualThan("sendDate", startDate);
        crit.addLessThan("sendDate", endDate);
        crit.addNotEqualTo("deliveryType", SmsDeliveryType.NOT_SENT_TYPE);

        return new Integer(count(SentSms.class, crit));
    }

}