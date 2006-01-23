/*
 * Created on Dec 9, 2005
 */
package net.sourceforge.fenixedu.applicationTier.Servico.bolonhaManager;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.bolonhaManager.CourseLoad;
import net.sourceforge.fenixedu.domain.CompetenceCourse;
import net.sourceforge.fenixedu.domain.curricularPeriod.CurricularPeriodType;
import net.sourceforge.fenixedu.domain.degreeStructure.CompetenceCourseLoad;
import net.sourceforge.fenixedu.domain.degreeStructure.RegimeType;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class EditCompetenceCourseLoad extends Service {

    public void run(Integer competenceCourseID, RegimeType regimeType, List<CourseLoad> courseLoads)
            throws ExcepcaoPersistencia, FenixServiceException {
        final CompetenceCourse competenceCourse = (CompetenceCourse) persistentObject.readByOID(CompetenceCourse.class, competenceCourseID);
        if (competenceCourse == null) {
            throw new FenixServiceException("error.noCompetenceCourse");
        }
        competenceCourse.setRegime(regimeType);
        final CurricularPeriodType curricularPeriodType = getCurricularPeriodType(regimeType);
        for (final CourseLoad courseLoad : courseLoads) {
            if (courseLoad.getAction().equals("create")) {
                competenceCourse.addCompetenceCourseLoad(courseLoad.getTheoreticalHours(), courseLoad
                        .getProblemsHours(), courseLoad.getLaboratorialHours(), courseLoad
                        .getSeminaryHours(), courseLoad.getFieldWorkHours(), courseLoad
                        .getTrainingPeriodHours(), courseLoad.getTutorialOrientationHours(), courseLoad
                        .getAutonomousWorkHours(), courseLoad.getEctsCredits(), courseLoad.getOrder(),
                        curricularPeriodType);
                
            } else {
                final CompetenceCourseLoad competenceCourseLoad = (CompetenceCourseLoad) persistentObject
                		.readByOID(CompetenceCourseLoad.class, courseLoad.getIdentification());
                
                if (competenceCourseLoad != null && courseLoad.getAction().equals("edit")) {
                    competenceCourseLoad.edit(courseLoad.getTheoreticalHours(), courseLoad
                            .getProblemsHours(), courseLoad.getLaboratorialHours(), courseLoad
                            .getSeminaryHours(), courseLoad.getFieldWorkHours(), courseLoad
                            .getTrainingPeriodHours(), courseLoad.getTutorialOrientationHours(),
                            courseLoad.getAutonomousWorkHours(), courseLoad.getEctsCredits(), Integer
                                    .valueOf(courseLoad.getOrder()), curricularPeriodType);
                    
                } else if (competenceCourseLoad != null && courseLoad.getAction().equals("delete")) {
                    competenceCourseLoad.delete();
                }
            }
        }
    }

    private CurricularPeriodType getCurricularPeriodType(final RegimeType regimeType) {
        if (regimeType.getName().equals("SEMESTRIAL")) {
            return CurricularPeriodType.SEMESTER;
        } else if (regimeType.getName().equals("ANUAL")) {
            return CurricularPeriodType.YEAR;
        }
        return null;
    }
}
