/*
 * Created on 7/Jun/2004
 *  
 */
package net.sourceforge.fenixedu.persistenceTier.OJB.sms;

import java.util.Date;
import java.util.List;

import net.sourceforge.fenixedu.domain.IPerson;
import net.sourceforge.fenixedu.domain.sms.SentSms;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.OJB.PersistentObjectOJB;
import net.sourceforge.fenixedu.persistenceTier.sms.IPersistentSentSms;
import net.sourceforge.fenixedu.util.SmsDeliveryType;

import org.apache.ojb.broker.query.Criteria;

/**
 * @author <a href="mailto:sana@ist.utl.pt">Shezad Anavarali </a>
 * @author <a href="mailto:naat@ist.utl.pt">Nadir Tarmahomed </a>
 *  
 */
public class SentSmsOJB extends PersistentObjectOJB implements IPersistentSentSms {

    public SentSmsOJB() {
        super();
    }

    public List readByPerson(IPerson person) throws ExcepcaoPersistencia {
        Criteria crit = new Criteria();
        crit.addEqualTo("person.idInternal", person.getIdInternal());
        return queryList(SentSms.class, crit, "sendDate", false);
    }

    public List readByPerson(IPerson person, Integer interval) throws ExcepcaoPersistencia {
        Criteria crit = new Criteria();
        crit.addEqualTo("person.idInternal", person.getIdInternal());
        return readInterval(SentSms.class, crit, interval, new Integer(0), "sendDate", false);
    }

    public Integer countByPersonAndDatePeriod(Integer personId, Date startDate, Date endDate) {
        Criteria crit = new Criteria();
        crit.addEqualTo("person.idInternal", personId);
        crit.addGreaterOrEqualThan("sendDate", startDate);
        crit.addLessThan("sendDate", endDate);
        crit.addNotEqualTo("deliveryType", SmsDeliveryType.NOT_SENT);

        return new Integer(count(SentSms.class, crit));
    }

}