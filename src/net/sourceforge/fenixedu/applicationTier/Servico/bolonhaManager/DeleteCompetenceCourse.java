/*
 * Created on Dec 9, 2005
 */
package net.sourceforge.fenixedu.applicationTier.Servico.bolonhaManager;

import net.sourceforge.fenixedu.domain.CompetenceCourse;
import net.sourceforge.fenixedu.domain.ICompetenceCourse;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

public class DeleteCompetenceCourse implements IService {

    public void run(Integer competenceCourseID) throws ExcepcaoPersistencia {
        ISuportePersistente persistentSupport = PersistenceSupportFactory.getDefaultPersistenceSupport();
        ICompetenceCourse competenceCourse = (ICompetenceCourse) persistentSupport
                .getIPersistentCompetenceCourse().readByOID(CompetenceCourse.class, competenceCourseID);
        if (competenceCourse != null) {
            competenceCourse.delete();
        }
    }
}
