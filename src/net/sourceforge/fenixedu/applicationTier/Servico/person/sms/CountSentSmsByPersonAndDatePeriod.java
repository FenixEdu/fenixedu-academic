/*
 * Created on 7/Jun/2004
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.person.sms;

import java.util.Date;

import net.sourceforge.fenixedu.applicationTier.Servico.UserView;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.sms.SmsLimitReachedServiceException;
import net.sourceforge.fenixedu.applicationTier.utils.SmsUtil;
import net.sourceforge.fenixedu.domain.IPerson;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.PersistenceSupportFactory;
import net.sourceforge.fenixedu.persistenceTier.sms.IPersistentSentSms;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author <a href="mailto:sana@ist.utl.pt">Shezad Anavarali </a>
 * @author <a href="mailto:naat@ist.utl.pt">Nadir Tarmahomed </a>
 * 
 * <strong>Description: </strong> <br>
 * This service counts the number of sent Sms's by a person, in a date period
 * delimited by starDate(inclusive) and endDate(exclusive)
 *  
 */
public class CountSentSmsByPersonAndDatePeriod implements IService {

    /**
     *  
     */
    public CountSentSmsByPersonAndDatePeriod() {
    }

    public Integer run(UserView userView, Date startDate, Date endDate) throws FenixServiceException {

        try {
            ISuportePersistente ps = PersistenceSupportFactory.getDefaultPersistenceSupport();
            IPersistentSentSms persistentSentSms = ps.getIPersistentSentSms();

            IPerson person = ps.getIPessoaPersistente().lerPessoaPorUsername(userView.getUtilizador());

            Integer numberOfSms = persistentSentSms.countByPersonAndDatePeriod(person.getIdInternal(),
                    startDate, endDate);

            if (numberOfSms.intValue() >= SmsUtil.getInstance().getMonthlySmsLimit()) {
                throw new SmsLimitReachedServiceException("error.person.sendSmsLimitReached");
            }

            return new Integer(SmsUtil.getInstance().getMonthlySmsLimit() - numberOfSms.intValue());

        } catch (ExcepcaoPersistencia e) {
            throw new FenixServiceException(e);
        }

    }

}