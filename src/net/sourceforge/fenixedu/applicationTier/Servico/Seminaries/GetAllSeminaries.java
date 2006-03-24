package net.sourceforge.fenixedu.applicationTier.Servico.Seminaries;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.Seminaries.InfoSeminary;
import net.sourceforge.fenixedu.dataTransferObject.Seminaries.InfoSeminaryWithEquivalencies;
import net.sourceforge.fenixedu.domain.Seminaries.Seminary;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.presentationTier.Action.Seminaries.Exceptions.BDException;

public class GetAllSeminaries extends Service {

	public List run(Boolean inEnrollmentPeriod) throws BDException, ExcepcaoPersistencia {
		List<InfoSeminary> result = new LinkedList<InfoSeminary>();

		List<Seminary> seminaries = rootDomainObject.getSeminarys();
		for (Seminary seminary : seminaries) {
			Calendar now = new GregorianCalendar();
			
            Calendar beginDate = new GregorianCalendar();
            beginDate.setTimeInMillis(seminary.getEnrollmentBeginTime().getTimeInMillis()
                    + seminary.getEnrollmentBeginDate().getTimeInMillis());
			
            Calendar endDate = new GregorianCalendar();
            endDate.setTimeInMillis(seminary.getEnrollmentEndTime().getTimeInMillis()
					+ seminary.getEnrollmentEndDate().getTimeInMillis());
			
			if (!inEnrollmentPeriod || (endDate.after(now) && beginDate.before(now)))
                result.add(InfoSeminaryWithEquivalencies.newInfoFromDomain(seminary));
		}

		return result;
	}
    
}
