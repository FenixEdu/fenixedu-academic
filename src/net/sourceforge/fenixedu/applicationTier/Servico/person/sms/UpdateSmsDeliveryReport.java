/*
 * Created on 11/Jun/2004
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.person.sms;

import java.util.Calendar;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.sms.ISentSms;
import net.sourceforge.fenixedu.domain.sms.SentSms;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.persistenceTier.sms.IPersistentSentSms;
import net.sourceforge.fenixedu.util.SmsDeliveryType;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author <a href="mailto:sana@ist.utl.pt">Shezad Anavarali </a>
 * @author <a href="mailto:naat@ist.utl.pt">Nadir Tarmahomed </a>
 *  
 */
public class UpdateSmsDeliveryReport implements IService {

    /**
     *  
     */
    public UpdateSmsDeliveryReport() {
    }

    public void run(Integer smsId, SmsDeliveryType smsDeliveryType) throws FenixServiceException {

        try {
            ISuportePersistente ps = PersistenceSupportFactory.getDefaultPersistenceSupport();
            IPersistentSentSms persistentSentSms = ps.getIPersistentSentSms();

            //read sentSms Object
            ISentSms sentSms = (ISentSms) persistentSentSms.readByOID(SentSms.class, smsId, true);

            if (sentSms == null) {
                throw new FenixServiceException();
            }
            //update sentSms Object
            sentSms.setDeliveryDate(Calendar.getInstance().getTime());
            sentSms.setDeliveryType(smsDeliveryType);

        } catch (ExcepcaoPersistencia e) {
            throw new FenixServiceException(e);
        }

    }

}