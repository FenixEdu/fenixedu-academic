package net.sourceforge.fenixedu.applicationTier.Servico.commons.student;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoEnrolment;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.Enrolment;

import org.apache.commons.beanutils.BeanComparator;

public class ReadStudentListByCurricularCourse extends Service {

    public List run(IUserView userView, Integer curricularCourseID, String executionYear)
	    throws FenixServiceException {

	final CurricularCourse curricularCourse = (CurricularCourse) rootDomainObject
		.readDegreeModuleByOID(curricularCourseID);

	if (executionYear != null) {
	    return cleanList(curricularCourse.getEnrolmentsByYear(executionYear));
	} else {
	    return cleanList(curricularCourse.getCurriculumModules());
	}
    }

    private List cleanList(final List enrolmentList) throws FenixServiceException {

	if (enrolmentList.isEmpty()) {
	    throw new NonExistingServiceException();
	}

	Integer studentNumber = null;
	final List<InfoEnrolment> result = new ArrayList<InfoEnrolment>();
	for (final Enrolment enrolment : (List<Enrolment>) enrolmentList) {

	    if (studentNumber == null
		    || studentNumber.intValue() != enrolment.getStudentCurricularPlan().getRegistration()
			    .getNumber().intValue()) {

		studentNumber = enrolment.getStudentCurricularPlan().getRegistration().getNumber();
		result.add(InfoEnrolment.newInfoFromDomain(enrolment));
	    }
	}
	Collections.sort(result, new BeanComparator("infoStudentCurricularPlan.infoStudent.number"));
	return result;
    }

}