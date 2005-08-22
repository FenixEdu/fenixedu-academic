package net.sourceforge.fenixedu.applicationTier.Servico.manager.competenceCourseManagement;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.domain.CompetenceCourse;
import net.sourceforge.fenixedu.domain.ICompetenceCourse;
import net.sourceforge.fenixedu.domain.ICurricularCourse;
import net.sourceforge.fenixedu.persistenceTier.IPersistentCompetenceCourse;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

public class ReadAllCurricularCoursesByCompetenceCourse implements IService {

	public List<ICurricularCourse> run(Integer competenceID) throws Exception {
		ISuportePersistente suportePersistente = PersistenceSupportFactory.getDefaultPersistenceSupport();
		IPersistentCompetenceCourse persistentCompetenceCourse = suportePersistente.getIPersistentCompetenceCourse();
		ICompetenceCourse competenceCourse = (ICompetenceCourse) persistentCompetenceCourse.readByOID(CompetenceCourse.class, competenceID);
		if(competenceCourse == null) {
			throw new NonExistingServiceException("error.manager.noCompetenceCourse");
		}
		
		return competenceCourse.getAssociatedCurricularCourses();
	}

}
