/**
 * 
 */
package net.sourceforge.fenixedu.applicationTier.Servico.gep.inquiries;

import java.util.Collection;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.oldInquiries.InquiriesRegistry;
import net.sourceforge.fenixedu.domain.student.Student;
import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixWebFramework.services.Service;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class RetrieveOrCreateStudentInquiriesRegistries extends FenixService {

    @Checked("RolePredicates.STUDENT_PREDICATE")
    @Service
    public static Collection<InquiriesRegistry> run(Student student, ExecutionSemester executionSemester) {
	return student.getOrCreateInquiriesRegistriesForPeriod(executionSemester);
    }

}