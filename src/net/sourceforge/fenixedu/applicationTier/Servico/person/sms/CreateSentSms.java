/*
 * Created on 11/Jun/2004
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.person.sms;

import java.util.Date;

import net.sourceforge.fenixedu.applicationTier.Servico.UserView;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.sms.SmsNotSentServiceException;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author <a href="mailto:sana@ist.utl.pt">Shezad Anavarali </a>
 * @author <a href="mailto:naat@ist.utl.pt">Nadir Tarmahomed </a>
 *  
 */
public class CreateSentSms implements IService {

    /**
     *  
     */
    public CreateSentSms() {
    }

    public void run(UserView userView, Date startDate, Date endDate, Integer destinationNumber,
            String message) throws FenixServiceException {

        throw new SmsNotSentServiceException("error.person.sendSms");
        //    	
        //        try {
        //            ISuportePersistente ps = PersistenceSupportFactory.getDefaultPersistenceSupport();
        //            IPersistentSentSms persistentSentSms = ps.getIPersistentSentSms();
        //
        //            IPerson person =
        // ps.getIPessoaPersistente().lerPessoaPorUsername(userView.getUtilizador());
        //
        //            //count number of sent SMS's by this person
        //            Integer numberOfSms =
        // persistentSentSms.countByPersonAndDatePeriod(person.getIdInternal(),
        //                    startDate, endDate);
        //
        //            if (numberOfSms.intValue() >=
        // SmsUtil.getInstance().getMonthlySmsLimit()) {
        //                throw new
        // SmsLimitReachedServiceException("error.person.sendSmsLimitReached");
        //            }
        //
        //            //create sentSms Object
        //            ISentSms sentSms = new SentSms();
        //            persistentSentSms.simpleLockWrite(sentSms);
        //
        //            sentSms.setDestinationNumber(destinationNumber);
        //            sentSms.setPerson(person);
        //            sentSms.setSendDate(Calendar.getInstance().getTime());
        //            sentSms.setDeliveryDate(Calendar.getInstance().getTime());
        //            sentSms.setDeliveryType(SmsDeliveryType.NOT_SENT);
        //
        //            //send SMS
        //            try {
        //                SmsUtil.getInstance().sendSms(destinationNumber, message,
        // sentSms.getIdInternal());
        //
        //            } catch (FenixUtilException e1) {
        //                throw new SmsNotSentServiceException("error.person.sendSms");
        //            }
        //
        //            sentSms.setDeliveryType(SmsDeliveryType.SMSC_SUBMIT);
        //
        //        } catch (ExcepcaoPersistencia e) {
        //            throw new FenixServiceException(e);
        //        }

    }

}