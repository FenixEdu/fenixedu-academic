package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import java.util.Date;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.EnrolmentPeriod;

public class ChangeEnrolmentPeriodValues extends Service {

    public void run(final Integer enrolmentPeriodID, final Date startDate, final Date endDate) {
	final EnrolmentPeriod enrolmentPeriod = rootDomainObject.readEnrolmentPeriodByOID(enrolmentPeriodID);
	enrolmentPeriod.setStartDate(startDate);
	enrolmentPeriod.setEndDate(endDate);
    }

}