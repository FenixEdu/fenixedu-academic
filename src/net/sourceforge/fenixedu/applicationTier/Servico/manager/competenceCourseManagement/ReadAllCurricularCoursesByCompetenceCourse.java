package net.sourceforge.fenixedu.applicationTier.Servico.manager.competenceCourseManagement;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.domain.CompetenceCourse;
import net.sourceforge.fenixedu.domain.CurricularCourse;

public class ReadAllCurricularCoursesByCompetenceCourse extends Service {

	public List<CurricularCourse> run(Integer competenceID) throws Exception {
		CompetenceCourse competenceCourse = rootDomainObject.readCompetenceCourseByOID(competenceID);
		if(competenceCourse == null) {
			throw new NonExistingServiceException("error.manager.noCompetenceCourse");
		}
		
        List<CurricularCourse> result = new ArrayList<CurricularCourse>();
        for (CurricularCourse curricularCourse : competenceCourse.getAssociatedCurricularCourses()) {
            if (!curricularCourse.isBolonha()) {
                result.add(curricularCourse);
            }
        }
        
        return result;
	}

}
