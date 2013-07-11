/*
 * Created on Dec 9, 2005
 */
package net.sourceforge.fenixedu.applicationTier.Servico.bolonhaManager;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.bolonhaManager.CourseLoad;
import net.sourceforge.fenixedu.domain.CompetenceCourse;
import net.sourceforge.fenixedu.domain.degreeStructure.CompetenceCourseLoad;
import net.sourceforge.fenixedu.domain.degreeStructure.RegimeType;
import net.sourceforge.fenixedu.domain.time.calendarStructure.AcademicPeriod;
import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

public class EditCompetenceCourseLoad {

    @Checked("RolePredicates.BOLONHA_MANAGER_PREDICATE")
    @Atomic
    public static void run(String competenceCourseID, RegimeType regimeType, Integer numberOfPeriods, List<CourseLoad> courseLoads)
            throws FenixServiceException {
        final CompetenceCourse competenceCourse = FenixFramework.getDomainObject(competenceCourseID);
        if (competenceCourse == null) {
            throw new FenixServiceException("error.noCompetenceCourse");
        }
        competenceCourse.setRegime(regimeType);
        final AcademicPeriod academicPeriod = AcademicPeriod.SEMESTER;
        for (final CourseLoad courseLoad : courseLoads) {
            if (courseLoad.getAction().equals("create") && competenceCourse.getCompetenceCourseLoads().size() < numberOfPeriods) {
                competenceCourse.addCompetenceCourseLoad(courseLoad.getTheoreticalHours(), courseLoad.getProblemsHours(),
                        courseLoad.getLaboratorialHours(), courseLoad.getSeminaryHours(), courseLoad.getFieldWorkHours(),
                        courseLoad.getTrainingPeriodHours(), courseLoad.getTutorialOrientationHours(),
                        courseLoad.getAutonomousWorkHours(), courseLoad.getEctsCredits(), courseLoad.getOrder(), academicPeriod);
            } else {
                final CompetenceCourseLoad competenceCourseLoad =
                        FenixFramework.getDomainObject(courseLoad.getIdentification());

                if (competenceCourseLoad != null && courseLoad.getAction().equals("edit")) {
                    competenceCourseLoad.edit(courseLoad.getTheoreticalHours(), courseLoad.getProblemsHours(),
                            courseLoad.getLaboratorialHours(), courseLoad.getSeminaryHours(), courseLoad.getFieldWorkHours(),
                            courseLoad.getTrainingPeriodHours(), courseLoad.getTutorialOrientationHours(),
                            courseLoad.getAutonomousWorkHours(), courseLoad.getEctsCredits(),
                            Integer.valueOf(courseLoad.getOrder()), academicPeriod);

                } else if (competenceCourseLoad != null && courseLoad.getAction().equals("delete")) {
                    competenceCourseLoad.delete();
                }
            }
        }
    }
}