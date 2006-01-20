package net.sourceforge.fenixedu.applicationTier.Servico.manager.competenceCourseManagement;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.domain.CompetenceCourse;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.degreeStructure.CurricularStage;
import net.sourceforge.fenixedu.persistenceTier.IPersistentCompetenceCourse;

public class ReadAllCurricularCoursesByCompetenceCourse extends Service {

	public List<CurricularCourse> run(Integer competenceID) throws Exception {
		IPersistentCompetenceCourse persistentCompetenceCourse = persistentSupport.getIPersistentCompetenceCourse();
		CompetenceCourse competenceCourse = (CompetenceCourse) persistentCompetenceCourse.readByOID(CompetenceCourse.class, competenceID);
		if(competenceCourse == null) {
			throw new NonExistingServiceException("error.manager.noCompetenceCourse");
		}
		
        List<CurricularCourse> result = new ArrayList<CurricularCourse>();
        for (CurricularCourse curricularCourse : competenceCourse.getAssociatedCurricularCourses()) {
            if (curricularCourse.getCurricularStage().equals(CurricularStage.OLD)) {
                result.add(curricularCourse);
            }
        }
        
        return result;
	}

}
