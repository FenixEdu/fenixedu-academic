package net.sourceforge.fenixedu.applicationTier.Servico.teacherServiceDistribution;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.CurricularCourseValuation;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.CurricularCourseValuationGroup;

public class DeleteCurricularCourseValuationGroup extends Service {
	public void run(Integer curricularCourseValuationGroupId) {
		CurricularCourseValuationGroup curricularCourseValuationGroup = (CurricularCourseValuationGroup) rootDomainObject.readCourseValuationByOID(curricularCourseValuationGroupId);
		
		for (CurricularCourseValuation curricularCourseValuation : curricularCourseValuationGroup.getCurricularCourseValuations()) {
			curricularCourseValuation.setCurricularCourseValuationGroup(null);
		}
			
		curricularCourseValuationGroup.delete();
	}
}
