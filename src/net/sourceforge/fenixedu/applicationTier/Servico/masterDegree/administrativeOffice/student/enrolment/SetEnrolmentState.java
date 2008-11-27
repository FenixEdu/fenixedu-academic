/**
 * 
 */
package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.student.enrolment;

import pt.ist.fenixWebFramework.services.Service;

import pt.ist.fenixWebFramework.security.accessControl.Checked;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.curriculum.EnrollmentState;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class SetEnrolmentState extends FenixService {

    @Checked("RolePredicates.MASTER_DEGREE_ADMINISTRATIVE_OFFICE_PREDICATE")
    @Service
    public static void run(Enrolment enrolment, EnrollmentState state) {
	enrolment.setEnrollmentState(state);
    }

}