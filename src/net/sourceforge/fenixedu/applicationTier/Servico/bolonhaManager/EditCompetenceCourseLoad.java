/*
 * Created on Dec 9, 2005
 */
package net.sourceforge.fenixedu.applicationTier.Servico.bolonhaManager;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.bolonhaManager.CourseLoad;
import net.sourceforge.fenixedu.domain.CompetenceCourse;
import net.sourceforge.fenixedu.domain.ICompetenceCourse;
import net.sourceforge.fenixedu.domain.degreeStructure.CompetenceCourseLoad;
import net.sourceforge.fenixedu.domain.degreeStructure.ICompetenceCourseLoad;
import net.sourceforge.fenixedu.domain.degreeStructure.RegimeType;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

public class EditCompetenceCourseLoad implements IService {
    
    public void run(Integer competenceCourseID, RegimeType regimeType, List<CourseLoad> courseLoads) 
        throws ExcepcaoPersistencia, FenixServiceException {

        final ISuportePersistente persistentSupport = PersistenceSupportFactory.getDefaultPersistenceSupport();
        final ICompetenceCourse competenceCourse = (ICompetenceCourse) persistentSupport
                .getIPersistentCompetenceCourse().readByOID(CompetenceCourse.class, competenceCourseID);
        if (competenceCourse == null) {
            throw new FenixServiceException("error.noCompetenceCourse");
        }
        competenceCourse.setRegime(regimeType);
        for (final CourseLoad courseLoad : courseLoads) {
            if (courseLoad.getAction().equals("create")) {
               competenceCourse.addCompetenceCourseLoad(courseLoad.getTheoreticalHours(), courseLoad
                        .getProblemsHours(), courseLoad.getLaboratorialHours(), courseLoad
                        .getSeminaryHours(), courseLoad.getFieldWorkHours(), courseLoad
                        .getTrainingPeriodHours(), courseLoad.getTutorialOrientationHours(), courseLoad
                        .getAutonomousWorkHours(), courseLoad.getEctsCredits(), courseLoad.getOrder());
            } else {
                final ICompetenceCourseLoad competenceCourseLoad = (ICompetenceCourseLoad) persistentSupport
                        .getIPersistentObject().readByOID(CompetenceCourseLoad.class,
                                courseLoad.getIdentification());
                if (competenceCourseLoad != null && courseLoad.getAction().equals("edit")) {
                    competenceCourseLoad.edit(courseLoad.getTheoreticalHours(), courseLoad
                            .getProblemsHours(), courseLoad.getLaboratorialHours(), courseLoad
                            .getSeminaryHours(), courseLoad.getFieldWorkHours(), courseLoad
                            .getTrainingPeriodHours(), courseLoad.getTutorialOrientationHours(), courseLoad
                            .getAutonomousWorkHours(), courseLoad.getEctsCredits(), Integer.valueOf(courseLoad.getOrder()));
                } else if (competenceCourseLoad != null && courseLoad.getAction().equals("delete")) {
                    competenceCourseLoad.delete();
                }
            }
        }
    }   
}
