package net.sourceforge.fenixedu.applicationTier.Servico.manager.competenceCourseManagement;

import net.sourceforge.fenixedu.domain.CompetenceCourse;
import net.sourceforge.fenixedu.domain.ICompetenceCourse;
import net.sourceforge.fenixedu.persistenceTier.IPersistentCompetenceCourse;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

public class DeleteCompetenceCourses implements IService {
	public void run(Integer[] competenceCourseIDs) throws Exception {
		ISuportePersistente suportePersistente = PersistenceSupportFactory.getDefaultPersistenceSupport();
		IPersistentCompetenceCourse persistentCompetenceCourse = suportePersistente.getIPersistentCompetenceCourse();
		for (Integer competenceCourseID : competenceCourseIDs) {
			ICompetenceCourse competenceCourse = (ICompetenceCourse) persistentCompetenceCourse.readByOID(CompetenceCourse.class, competenceCourseID);
			if(competenceCourse != null) {
				competenceCourse.delete();
			}
		}
	}
}
