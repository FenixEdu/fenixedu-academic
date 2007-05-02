package net.sourceforge.fenixedu.applicationTier.Servico.administrativeOffice.notNeedToEnrol;

import java.util.Collection;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.degree.enrollment.NotNeedToEnrollInCurricularCourse;
import net.sourceforge.fenixedu.domain.student.Student;

public class AssociateEnrolmentsToNotNeedToEnrol extends Service {
    
    public void run(Student student, NotNeedToEnrollInCurricularCourse notNeedToEnrollInCurricularCourse, Collection<Enrolment> selectedEnrolments) throws FenixServiceException {
	for (Enrolment enrolment : notNeedToEnrollInCurricularCourse.getEnrolmentsSet()) {
	    if(!selectedEnrolments.contains(enrolment)) {
		notNeedToEnrollInCurricularCourse.removeEnrolments(enrolment);
	    }
	}
	
	if(selectedEnrolments != null && !selectedEnrolments.isEmpty()) {
	    Collection<Enrolment> aprovedEnrolments = student.getApprovedEnrolments();
	    for (Enrolment selectedEnrolment : selectedEnrolments) {
		Enrolment enrolment = getAprovedEnrolment(aprovedEnrolments, selectedEnrolment);
		if(enrolment == null) {
		    throw new FenixServiceException();
		}
		
		notNeedToEnrollInCurricularCourse.addEnrolments(enrolment);
	    }
	}
    }
    
    private Enrolment getAprovedEnrolment(Collection<Enrolment> aprovedEnrolments, Enrolment selectedEnrolment) {
	for (Enrolment enrolment : aprovedEnrolments) {
	    if(enrolment.equals(selectedEnrolment)) {
		return enrolment;
	    }
	}
	return null;
    }

}
