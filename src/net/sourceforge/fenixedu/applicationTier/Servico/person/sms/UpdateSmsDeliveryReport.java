/*
 * Created on 11/Jun/2004
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.person.sms;

import java.util.Calendar;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.sms.SentSms;
import net.sourceforge.fenixedu.domain.sms.SmsDeliveryType;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

/**
 * @author <a href="mailto:sana@ist.utl.pt">Shezad Anavarali </a>
 * @author <a href="mailto:naat@ist.utl.pt">Nadir Tarmahomed </a>
 * 
 */
public class UpdateSmsDeliveryReport extends Service {

	public void run(Integer smsId, SmsDeliveryType smsDeliveryType) throws FenixServiceException, ExcepcaoPersistencia {
		// read sentSms Object
		SentSms sentSms = rootDomainObject.readSentSmsByOID(smsId);

		if (sentSms == null) {
			throw new FenixServiceException();
		}
		// update sentSms Object
		sentSms.setDeliveryDate(Calendar.getInstance().getTime());
		sentSms.setDeliveryType(smsDeliveryType);
	}

}
