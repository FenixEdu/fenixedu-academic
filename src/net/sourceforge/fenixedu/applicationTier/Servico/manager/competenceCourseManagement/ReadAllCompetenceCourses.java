package net.sourceforge.fenixedu.applicationTier.Servico.manager.competenceCourseManagement;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.InfoCompetenceCourse;
import net.sourceforge.fenixedu.domain.CompetenceCourse;
import net.sourceforge.fenixedu.domain.degreeStructure.CurricularStage;
import net.sourceforge.fenixedu.persistenceTier.IPersistentCompetenceCourse;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.applicationTier.Service;

public class ReadAllCompetenceCourses extends Service{

	public List<InfoCompetenceCourse> run() throws Exception {
		ISuportePersistente suportePersistente = PersistenceSupportFactory.getDefaultPersistenceSupport();
		IPersistentCompetenceCourse persistentCompetenceCourse = suportePersistente.getIPersistentCompetenceCourse();
		List<CompetenceCourse> competenceCourses = (List<CompetenceCourse>) persistentCompetenceCourse.readByCurricularStage(CurricularStage.OLD);
		List<InfoCompetenceCourse> result = new ArrayList<InfoCompetenceCourse>();
		for (CompetenceCourse course : competenceCourses) {
			result.add(InfoCompetenceCourse.newInfoFromDomain(course));
		}
		return result;
	}

}
