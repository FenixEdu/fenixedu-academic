package net.sourceforge.fenixedu.applicationTier.Servico.teacherServiceDistribution;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.CurricularCourseValuation;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.CurricularCourseValuationGroup;

public class AddCurricularCourseValuationFromValuationGroup extends Service {
	public void run(Integer curricularCourseValuationGroupId, Integer curricularCourseValuationId) {
		CurricularCourseValuationGroup curricularCourseValuationGroup = (CurricularCourseValuationGroup) rootDomainObject.readCourseValuationByOID(curricularCourseValuationGroupId);
		
		CurricularCourseValuation curricularCourseValuation = (CurricularCourseValuation) rootDomainObject.readCourseValuationByOID(curricularCourseValuationId);
		
		if(!curricularCourseValuationGroup.hasCurricularCourseValuations(curricularCourseValuation) && curricularCourseValuation.getCurricularCourseValuationGroup() == null) {
			curricularCourseValuationGroup.addCurricularCourseValuations(curricularCourseValuation);
			curricularCourseValuation.setCurricularCourseValuationGroup(curricularCourseValuationGroup);
		}
	}
}
