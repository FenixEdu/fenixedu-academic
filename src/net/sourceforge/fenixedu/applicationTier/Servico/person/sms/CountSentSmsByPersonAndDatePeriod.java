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

/**
 * @author <a href="mailto:sana@ist.utl.pt">Shezad Anavarali </a>
 * @author <a href="mailto:naat@ist.utl.pt">Nadir Tarmahomed </a>
 * 
 * <strong>Description: </strong> <br/>
 * This service counts the number of sent Sms's by a person, in a date period
 * delimited by starDate(inclusive) and endDate(exclusive)
 * 
 */
public class CountSentSmsByPersonAndDatePeriod extends Service {

	public Integer run(IUserView userView, Date startDate, Date endDate) throws FenixServiceException {
		final Person person = Person.readPersonByUsername(userView.getUtilizador());
		int numberOfSms = person.countSentSmsBetween(startDate, endDate);
		if (numberOfSms >= SmsUtil.getInstance().getMonthlySmsLimit()) {
			throw new SmsLimitReachedServiceException("error.person.sendSmsLimitReached");
		}
		return Integer.valueOf(SmsUtil.getInstance().getMonthlySmsLimit() - numberOfSms);
	}
}