package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import java.util.Date;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.domain.EnrolmentPeriod;
import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixWebFramework.services.Service;

public class ChangeEnrolmentPeriodValues extends FenixService {

    @Checked("RolePredicates.MANAGER_OR_OPERATOR_PREDICATE")
    @Service
    public static void run(final Integer enrolmentPeriodID, final Date startDate, final Date endDate) {
	final EnrolmentPeriod enrolmentPeriod = rootDomainObject.readEnrolmentPeriodByOID(enrolmentPeriodID);
	enrolmentPeriod.setStartDate(startDate);
	enrolmentPeriod.setEndDate(endDate);
    }

}