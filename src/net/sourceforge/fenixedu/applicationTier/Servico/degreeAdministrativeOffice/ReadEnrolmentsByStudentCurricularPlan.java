package net.sourceforge.fenixedu.applicationTier.Servico.degreeAdministrativeOffice;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.InfoEnrolment;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;

public class ReadEnrolmentsByStudentCurricularPlan extends Service {

    public List<InfoEnrolment> run(Integer studentCurricularPlanId) {

	final StudentCurricularPlan studentCurricularPlan = rootDomainObject
		.readStudentCurricularPlanByOID(studentCurricularPlanId);

	if (studentCurricularPlan == null) {
	    return Collections.EMPTY_LIST;

	} else {

	    final List<InfoEnrolment> result = new ArrayList<InfoEnrolment>(studentCurricularPlan.getEnrolmentsCount());
	    for (final Enrolment enrolment : studentCurricularPlan.getEnrolmentsSet()) {
		result.add(InfoEnrolment.newInfoFromDomain(enrolment));
	    }
	    return result;
	}
    }

}