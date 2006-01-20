/*
 * Created on Dec 9, 2005
 */
package net.sourceforge.fenixedu.applicationTier.Servico.bolonhaManager;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.CompetenceCourse;
import net.sourceforge.fenixedu.domain.degreeStructure.CurricularStage;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class EditCompetenceCourse extends Service {
    
    public void run(Integer competenceCourseID, String objectives, String program, String evaluationMethod,
            String objectivesEn, String programEn, String evaluationMethodEn) throws ExcepcaoPersistencia, FenixServiceException {
        final CompetenceCourse competenceCourse = (CompetenceCourse) persistentSupport
                .getIPersistentCompetenceCourse().readByOID(CompetenceCourse.class, competenceCourseID);
        if (competenceCourse == null) {
            throw new FenixServiceException("error.noCompetenceCourse");
        }
        competenceCourse.edit(objectives, program, evaluationMethod, objectivesEn, programEn, evaluationMethodEn);        
    }

    public void run(Integer competenceCourseID, String name, String nameEn, String acronym,
            Boolean basic, CurricularStage curricularStage) throws ExcepcaoPersistencia, FenixServiceException {
        final CompetenceCourse competenceCourse = (CompetenceCourse) persistentSupport
                .getIPersistentCompetenceCourse().readByOID(CompetenceCourse.class, competenceCourseID);
        if (competenceCourse == null) {
            throw new FenixServiceException("error.noCompetenceCourse");
        }
        competenceCourse.edit(name, nameEn, acronym, basic, curricularStage);
    }
}
