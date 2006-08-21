/*
 * Created on Feb 18, 2005
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.student.Registration;

/**
 * @author Luis Cruz
 * 
 */
public class DeleteEnrollment extends Service {

    public void run(final Integer studentNumber, final DegreeType degreeType, final Integer enrollmentId) {
        final Registration registration = Registration.readStudentByNumberAndDegreeType(studentNumber, degreeType);
        final Enrolment enrollment = registration.findEnrolmentByEnrolmentID(enrollmentId);
        enrollment.delete();
    }

}
