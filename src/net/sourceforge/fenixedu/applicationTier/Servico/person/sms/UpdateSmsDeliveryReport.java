/*
 * Created on 11/Jun/2004
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.person.sms;

import java.util.Calendar;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.sms.SentSms;
import net.sourceforge.fenixedu.domain.sms.SmsDeliveryType;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.persistenceTier.sms.IPersistentSentSms;
import net.sourceforge.fenixedu.applicationTier.IService;

/**
 * @author <a href="mailto:sana@ist.utl.pt">Shezad Anavarali </a>
 * @author <a href="mailto:naat@ist.utl.pt">Nadir Tarmahomed </a>
 * 
 */
public class UpdateSmsDeliveryReport implements IService {

	public void run(Integer smsId, SmsDeliveryType smsDeliveryType) throws FenixServiceException, ExcepcaoPersistencia {
		ISuportePersistente ps = PersistenceSupportFactory.getDefaultPersistenceSupport();
		IPersistentSentSms persistentSentSms = ps.getIPersistentSentSms();

		// read sentSms Object
		SentSms sentSms = (SentSms) persistentSentSms.readByOID(SentSms.class, smsId);

		if (sentSms == null) {
			throw new FenixServiceException();
		}
		// update sentSms Object
		sentSms.setDeliveryDate(Calendar.getInstance().getTime());
		sentSms.setDeliveryType(smsDeliveryType);
	}

}