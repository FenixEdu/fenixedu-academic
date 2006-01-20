package net.sourceforge.fenixedu.applicationTier.Servico.manager.competenceCourseManagement;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.InfoCompetenceCourse;
import net.sourceforge.fenixedu.domain.CompetenceCourse;
import net.sourceforge.fenixedu.domain.degreeStructure.CurricularStage;
import net.sourceforge.fenixedu.persistenceTier.IPersistentCompetenceCourse;

public class ReadAllCompetenceCourses extends Service{

	public List<InfoCompetenceCourse> run() throws Exception {
		IPersistentCompetenceCourse persistentCompetenceCourse = persistentSupport.getIPersistentCompetenceCourse();
		List<CompetenceCourse> competenceCourses = (List<CompetenceCourse>) persistentCompetenceCourse.readByCurricularStage(CurricularStage.OLD);
		List<InfoCompetenceCourse> result = new ArrayList<InfoCompetenceCourse>();
		for (CompetenceCourse course : competenceCourses) {
			result.add(InfoCompetenceCourse.newInfoFromDomain(course));
		}
		return result;
	}

}
