/**
 * 
 */
package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.student.enrolment;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.curriculum.EnrollmentState;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class SetEnrolmentState extends FenixService {

    public void run(Enrolment enrolment, EnrollmentState state) {
	enrolment.setEnrollmentState(state);
    }

}
