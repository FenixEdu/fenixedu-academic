/**
 * 
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.teacher.advise;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.teacher.Advise;
import net.sourceforge.fenixedu.domain.teacher.AdviseType;

/**
 * @author naat
 * 
 */
public class ReadTeacherAdvisesByTeacherIDAndAdviseTypeAndExecutionYearID extends FenixService {

	public List<Advise> run(AdviseType adviseType, Integer teacherID, Integer executionYearID) throws FenixServiceException,
			DomainException {
		Teacher teacher = rootDomainObject.readTeacherByOID(teacherID);
		List<Advise> result;

		if (executionYearID != null) {
			ExecutionYear executionYear = rootDomainObject.readExecutionYearByOID(executionYearID);

			result = teacher.getAdvisesByAdviseTypeAndExecutionYear(adviseType, executionYear);
		} else {
			result = teacher.getAdvisesByAdviseType(adviseType);
		}

		return result;

	}
}