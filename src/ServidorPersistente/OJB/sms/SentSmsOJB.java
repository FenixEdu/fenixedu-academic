/*
 * Created on 7/Jun/2004
 *  
 */
package ServidorPersistente.OJB.sms;

import java.util.Date;
import java.util.List;

import org.apache.ojb.broker.query.Criteria;

import Dominio.IPessoa;
import Dominio.sms.SentSms;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.OJB.PersistentObjectOJB;
import ServidorPersistente.sms.IPersistentSentSms;
import Util.SmsDeliveryType;

/**
 * @author <a href="mailto:sana@ist.utl.pt">Shezad Anavarali </a>
 * @author <a href="mailto:naat@ist.utl.pt">Nadir Tarmahomed </a>
 *  
 */
public class SentSmsOJB extends PersistentObjectOJB implements IPersistentSentSms {

    public SentSmsOJB() {
        super();
    }

    public List readByPerson(IPessoa person) throws ExcepcaoPersistencia {
        Criteria crit = new Criteria();
        crit.addEqualTo("person.idInternal", person.getIdInternal());
        return queryList(SentSms.class, crit, "sendDate", false);
    }

    public List readByPerson(IPessoa person, Integer interval) throws ExcepcaoPersistencia {
        Criteria crit = new Criteria();
        crit.addEqualTo("person.idInternal", person.getIdInternal());
        return readInterval(SentSms.class, crit, interval, new Integer(0), "sendDate", false);
    }

    public Integer countByPersonAndDatePeriod(Integer personId, Date startDate, Date endDate)
            throws ExcepcaoPersistencia {
        Criteria crit = new Criteria();
        crit.addEqualTo("person.idInternal", personId);
        crit.addGreaterOrEqualThan("sendDate", startDate);
        crit.addLessThan("sendDate", endDate);
        crit.addNotEqualTo("deliveryType", SmsDeliveryType.NOT_SENT);

        return new Integer(count(SentSms.class, crit));
    }

}