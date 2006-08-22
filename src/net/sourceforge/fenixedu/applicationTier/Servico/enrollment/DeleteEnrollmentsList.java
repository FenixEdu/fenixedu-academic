package net.sourceforge.fenixedu.applicationTier.Servico.enrollment;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.student.Registration;

public class DeleteEnrollmentsList extends Service {

	// degreeType used by filter
    public void run(final Registration registration, final DegreeType degreeType, final List<Integer> enrolmentIDList) throws FenixServiceException {
    	
        if (registration != null && enrolmentIDList != null) {
            for (final Integer enrolmentID : enrolmentIDList) {
            	
                final Enrolment enrolment = registration.findEnrolmentByEnrolmentID(enrolmentID);
                if (enrolment != null) {
                    enrolment.unEnroll();
                }                
            }
        }
    }
}
