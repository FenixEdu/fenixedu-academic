/*
 * Created on Dec 9, 2005
 */
package net.sourceforge.fenixedu.applicationTier.Servico.bolonhaManager;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.CompetenceCourse;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class DeleteCompetenceCourse extends Service {

    public void run(Integer competenceCourseID) throws ExcepcaoPersistencia {
        CompetenceCourse competenceCourse = (CompetenceCourse) persistentObject.readByOID(CompetenceCourse.class, competenceCourseID);
        if (competenceCourse != null) {
            competenceCourse.delete();
        }
    }
}
