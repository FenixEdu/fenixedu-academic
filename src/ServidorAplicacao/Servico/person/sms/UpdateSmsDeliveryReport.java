/*
 * Created on 11/Jun/2004
 *  
 */
package ServidorAplicacao.Servico.person.sms;

import java.util.Calendar;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import Dominio.sms.ISentSms;
import Dominio.sms.SentSms;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import ServidorPersistente.sms.IPersistentSentSms;
import Util.SmsDeliveryType;

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

    public void run(Integer smsId, SmsDeliveryType smsDeliveryType)
            throws FenixServiceException {

        try {
            ISuportePersistente ps = SuportePersistenteOJB.getInstance();
            IPersistentSentSms persistentSentSms = ps.getIPersistentSentSms();

            //read sentSms Object
            ISentSms sentSms = (ISentSms) persistentSentSms.readByOID(
                    SentSms.class, smsId, true);

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