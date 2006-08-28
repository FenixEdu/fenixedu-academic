package net.sourceforge.fenixedu.applicationTier.Servico.teacherServiceDistribution;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.CompetenceCourse;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.ValuationCompetenceCourse;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.ValuationGrouping;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

/**
 * 
 * @author jpmsit, amak
 */
public class AddCourseToValuationGrouping extends Service {

	public void run(Integer valuationGroupingId, final Integer courseId) throws FenixServiceException, ExcepcaoPersistencia {

		ValuationGrouping rootGrouping = rootDomainObject.readValuationGroupingByOID(valuationGroupingId).getRootValuationGrouping();
		CompetenceCourse course = rootDomainObject.readCompetenceCourseByOID(courseId);
		
		if(rootGrouping.getValuationCompetenceCourseByCompetenceCourse(course) == null){
			rootGrouping.addValuationCompetenceCourses(new ValuationCompetenceCourse(course));
		}
	}
}
