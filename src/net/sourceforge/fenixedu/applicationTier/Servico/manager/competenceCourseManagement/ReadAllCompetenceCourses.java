package net.sourceforge.fenixedu.applicationTier.Servico.manager.competenceCourseManagement;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.InfoCompetenceCourse;
import net.sourceforge.fenixedu.domain.CompetenceCourse;
import net.sourceforge.fenixedu.domain.ICompetenceCourse;
import net.sourceforge.fenixedu.domain.degreeStructure.CurricularStage;
import net.sourceforge.fenixedu.persistenceTier.IPersistentCompetenceCourse;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

public class ReadAllCompetenceCourses implements IService{

	public List<InfoCompetenceCourse> run() throws Exception {
		ISuportePersistente suportePersistente = PersistenceSupportFactory.getDefaultPersistenceSupport();
		IPersistentCompetenceCourse persistentCompetenceCourse = suportePersistente.getIPersistentCompetenceCourse();
		List<ICompetenceCourse> competenceCourses = (List<ICompetenceCourse>) persistentCompetenceCourse.readByCurricularStage(CurricularStage.OLD);
		List<InfoCompetenceCourse> result = new ArrayList<InfoCompetenceCourse>();
		for (ICompetenceCourse course : competenceCourses) {
			result.add(InfoCompetenceCourse.newInfoFromDomain(course));
		}
		return result;
	}

}
