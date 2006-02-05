package net.sourceforge.fenixedu.applicationTier.Servico.manager;
import java.util.Date;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.EnrolmentPeriod;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class ChangeEnrolmentPeriodValues extends Service {

    public void run(final Integer enrolmentPeriodID, final Date startDate, final Date endDate) throws ExcepcaoPersistencia {
    	final EnrolmentPeriod enrolmentPeriod = (EnrolmentPeriod) persistentObject.readByOID(EnrolmentPeriod.class, enrolmentPeriodID);
    	enrolmentPeriod.setStartDate(startDate);
    	enrolmentPeriod.setEndDate(endDate);
    }

}