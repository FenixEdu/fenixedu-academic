/*
 * Created on Dec 9, 2005
 */
package net.sourceforge.fenixedu.applicationTier.Servico.bolonhaManager;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.CompetenceCourse;
import net.sourceforge.fenixedu.domain.DomainFactory;
import net.sourceforge.fenixedu.domain.ICompetenceCourse;
import net.sourceforge.fenixedu.domain.ICurricularSemester;
import net.sourceforge.fenixedu.domain.IExecutionPeriod;
import net.sourceforge.fenixedu.domain.IExecutionYear;
import net.sourceforge.fenixedu.domain.degreeStructure.CourseGroup;
import net.sourceforge.fenixedu.domain.degreeStructure.CurricularStage;
import net.sourceforge.fenixedu.domain.degreeStructure.ICourseGroup;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

public class CreateCurricularCourse implements IService {

    public void run(Double weight, String prerequisites, String prerequisitesEn,
            Integer competenceCourseID, Integer courseGroupID, Integer year, Integer semester)
            throws ExcepcaoPersistencia, FenixServiceException {

        ISuportePersistente persistentSupport = PersistenceSupportFactory.getDefaultPersistenceSupport();
        ICompetenceCourse competenceCourse = (ICompetenceCourse) persistentSupport
                .getIPersistentCompetenceCourse().readByOID(CompetenceCourse.class, competenceCourseID);
        if (competenceCourse == null) {
            throw new FenixServiceException("error.noCompetenceCourse");
        }
        ICourseGroup courseGroup = (ICourseGroup) persistentSupport.getIPersistentObject().readByOID(
                CourseGroup.class, courseGroupID);
        if (courseGroup == null) {
            throw new FenixServiceException("error.noCourseGroup");
        }
        // TODO: check CurricularSemesterID for null value
        ICurricularSemester curricularSemester = persistentSupport.getIPersistentCurricularSemester()
                .readCurricularSemesterBySemesterAndCurricularYear(semester, year);
        if (curricularSemester == null) {
            throw new FenixServiceException("error.noCurricularSemesterGivenYearAndSemester");
        }
        // TODO: this should be modified to receive ExecutionYear, but for now
        // we just read the '2006/2007'
        IExecutionYear executionYear = persistentSupport.getIPersistentExecutionYear()
                .readExecutionYearByName("2006/2007");
        IExecutionPeriod executionPeriod = executionYear.getExecutionPeriodForSemester(Integer.valueOf(1));

        DomainFactory.makeCurricularCourse(weight, prerequisites, prerequisitesEn,
                CurricularStage.DRAFT, competenceCourse, courseGroup, curricularSemester,
                executionPeriod);
    }
}
