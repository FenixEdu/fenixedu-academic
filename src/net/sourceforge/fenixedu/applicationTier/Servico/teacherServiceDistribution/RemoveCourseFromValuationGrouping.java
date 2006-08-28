package net.sourceforge.fenixedu.applicationTier.Servico.teacherServiceDistribution;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.CourseValuation;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.ValuationCompetenceCourse;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.ValuationGrouping;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

/**
 * 
 * @author jpmsit, amak
 */
public class RemoveCourseFromValuationGrouping extends Service {

	public void run(Integer valuationGroupingId, Integer courseId) throws FenixServiceException, ExcepcaoPersistencia {

		ValuationGrouping valuationGrouping = rootDomainObject.readValuationGroupingByOID(valuationGroupingId);
		ValuationCompetenceCourse course = rootDomainObject.readValuationCompetenceCourseByOID(courseId);

		List<CourseValuation> courseValuationList = course.getCourseValuationsByValuationPhase(valuationGrouping.getValuationPhase());
		
		for(CourseValuation courseValuation : courseValuationList) {
			courseValuation.delete();
		}
		
		valuationGrouping.removeValuationCompetenceCourseFromAllChilds(course);
		if(valuationGrouping.getIsRoot() && !course.getIsRealCompetenceCourse()) {		
			course.delete();
		}
	}
}
