/*
 * Created on 7/Jun/2004
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.person.sms;

import java.util.Date;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.sms.SmsLimitReachedServiceException;
import net.sourceforge.fenixedu.applicationTier.utils.SmsUtil;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.sms.IPersistentSentSms;

/**
 * @author <a href="mailto:sana@ist.utl.pt">Shezad Anavarali </a>
 * @author <a href="mailto:naat@ist.utl.pt">Nadir Tarmahomed </a>
 * 
 * <strong>Description: </strong> <br>
 * This service counts the number of sent Sms's by a person, in a date period
 * delimited by starDate(inclusive) and endDate(exclusive)
 * 
 */
public class CountSentSmsByPersonAndDatePeriod extends Service {

	public Integer run(IUserView userView, Date startDate, Date endDate) throws FenixServiceException,
			ExcepcaoPersistencia {
		IPersistentSentSms persistentSentSms = persistentSupport.getIPersistentSentSms();

		Person person = Person.readPersonByUsername(userView.getUtilizador());

		Integer numberOfSms = persistentSentSms.countByPersonAndDatePeriod(person.getIdInternal(),
				startDate, endDate);

		if (numberOfSms.intValue() >= SmsUtil.getInstance().getMonthlySmsLimit()) {
			throw new SmsLimitReachedServiceException("error.person.sendSmsLimitReached");
		}

		return new Integer(SmsUtil.getInstance().getMonthlySmsLimit() - numberOfSms.intValue());
	}

}