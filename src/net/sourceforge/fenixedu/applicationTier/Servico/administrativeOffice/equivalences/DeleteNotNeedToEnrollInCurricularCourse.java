/**
 * Jul 29, 2005
 */
package net.sourceforge.fenixedu.applicationTier.Servico.administrativeOffice.equivalences;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.domain.degree.enrollment.NotNeedToEnrollInCurricularCourse;

/**
 * @author Ricardo Rodrigues
 * 
 */

public class DeleteNotNeedToEnrollInCurricularCourse extends FenixService {

	public void run(Integer notNeedToEnrollInCurricularCourseID) {
		NotNeedToEnrollInCurricularCourse notNeedToEnrollInCurricularCourse =
				rootDomainObject.readNotNeedToEnrollInCurricularCourseByOID(notNeedToEnrollInCurricularCourseID);
		notNeedToEnrollInCurricularCourse.delete();
	}

}
