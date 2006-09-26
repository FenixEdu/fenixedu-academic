package net.sourceforge.fenixedu.applicationTier.Servico.commons.student;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoEnrolment;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;

public class ReadStudentCurriculum extends Service {

    public List run(Integer executionDegreeCode, Integer studentCurricularPlanID)
	    throws FenixServiceException {

	final StudentCurricularPlan studentCurricularPlan = rootDomainObject
		.readStudentCurricularPlanByOID(studentCurricularPlanID);
	if (studentCurricularPlan == null) {
	    throw new NonExistingServiceException("error.readStudentCurriculum.noStudentCurricularPlan");
	}

	final List<InfoEnrolment> result = new ArrayList<InfoEnrolment>(studentCurricularPlan
		.getEnrolmentsCount());
	for (final Enrolment enrolment : studentCurricularPlan.getEnrolmentsSet()) {
	    result.add(InfoEnrolment.newInfoFromDomain(enrolment));
	}
	return result;
    }
}