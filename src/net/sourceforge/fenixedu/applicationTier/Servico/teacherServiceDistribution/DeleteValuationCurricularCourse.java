package net.sourceforge.fenixedu.applicationTier.Servico.teacherServiceDistribution;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.ValuationCurricularCourse;

public class DeleteValuationCurricularCourse extends Service {
	public void run(Integer valuationCurricularCourseId) {
						
		ValuationCurricularCourse valuationCurricularCourse= rootDomainObject.readValuationCurricularCourseByOID(valuationCurricularCourseId);
		
		valuationCurricularCourse.delete();
	}
}
