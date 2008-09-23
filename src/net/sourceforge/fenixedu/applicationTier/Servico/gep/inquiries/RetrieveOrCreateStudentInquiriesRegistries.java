/**
 * 
 */
package net.sourceforge.fenixedu.applicationTier.Servico.gep.inquiries;

import java.util.Collection;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.inquiries.InquiriesRegistry;
import net.sourceforge.fenixedu.domain.student.Student;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class RetrieveOrCreateStudentInquiriesRegistries extends FenixService {

    public Collection<InquiriesRegistry> run(Student student, ExecutionSemester executionSemester) {
	return student.getOrCreateInquiriesRegistriesForPeriod(executionSemester);
    }

}
