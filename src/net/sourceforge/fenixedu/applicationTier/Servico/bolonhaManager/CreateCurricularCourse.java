/*
 * Created on Dec 9, 2005
 */
package net.sourceforge.fenixedu.applicationTier.Servico.bolonhaManager;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.CurricularPeriodInfoDTO;
import net.sourceforge.fenixedu.domain.CompetenceCourse;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.curricularPeriod.CurricularPeriod;
import net.sourceforge.fenixedu.domain.curricularPeriod.CurricularPeriodType;
import net.sourceforge.fenixedu.domain.degreeStructure.CourseGroup;
import net.sourceforge.fenixedu.domain.degreeStructure.CurricularStage;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class CreateCurricularCourse extends Service {

    public void run(Double weight, String prerequisites, String prerequisitesEn,
            Integer competenceCourseID, Integer parentCourseGroupID, Integer year, Integer semester,
            Integer degreeCurricularPlanID) throws ExcepcaoPersistencia, FenixServiceException {

        initArguments(degreeCurricularPlanID, parentCourseGroupID, year, semester);
        
        final CompetenceCourse competenceCourse = (CompetenceCourse) persistentObject.readByOID(
                CompetenceCourse.class, competenceCourseID);
        if (competenceCourse == null) {
            throw new FenixServiceException("error.noCompetenceCourse");
        }
        degreeCurricularPlan.createCurricularCourse(weight, prerequisites, prerequisitesEn,
                CurricularStage.DRAFT, competenceCourse, parentCourseGroup, curricularPeriod,
                beginExecutionPeriod);
    }

    public void run(Integer degreeCurricularPlanID, Integer parentCourseGroupID, String name, String nameEn,
            Integer year, Integer semester) throws ExcepcaoPersistencia, FenixServiceException {

        initArguments(degreeCurricularPlanID, parentCourseGroupID, year, semester);
        degreeCurricularPlan.createCurricularCourse(parentCourseGroup, name, nameEn,
                CurricularStage.DRAFT, curricularPeriod, beginExecutionPeriod);
    }
    
    private DegreeCurricularPlan degreeCurricularPlan = null;
    private CourseGroup parentCourseGroup = null;
    private CurricularPeriod curricularPeriod = null;
    private ExecutionPeriod beginExecutionPeriod = null; 
    
    private void initArguments(Integer degreeCurricularPlanID, Integer parentCourseGroupID,
            Integer year, Integer semester) throws ExcepcaoPersistencia, FenixServiceException {
        
        degreeCurricularPlan = (DegreeCurricularPlan) persistentObject.readByOID(DegreeCurricularPlan.class, degreeCurricularPlanID);
        if (degreeCurricularPlan == null) {
            throw new FenixServiceException("error.noDegreeCurricularPlan");
        }
        
        parentCourseGroup = (CourseGroup) persistentObject.readByOID(CourseGroup.class, parentCourseGroupID);
        if (parentCourseGroup == null) {
            throw new FenixServiceException("error.noCourseGroup");
        }

        final CurricularPeriod degreeCurricularPeriod = (CurricularPeriod) degreeCurricularPlan.getDegreeStructure();
        curricularPeriod = degreeCurricularPeriod.getCurricularPeriod(
                new CurricularPeriodInfoDTO(year, CurricularPeriodType.YEAR),
                new CurricularPeriodInfoDTO(semester, CurricularPeriodType.SEMESTER));
        if (curricularPeriod == null) {
            curricularPeriod = degreeCurricularPeriod.addCurricularPeriod(new CurricularPeriodInfoDTO(
                    year, CurricularPeriodType.YEAR), new CurricularPeriodInfoDTO(semester,
                    CurricularPeriodType.SEMESTER));
        }
        // TODO: this should be modified to receive ExecutionYear, but for now
        // we just read the '2006/2007'
        final ExecutionYear executionYear = ExecutionYear.readExecutionYearByName("2006/2007");
        beginExecutionPeriod = executionYear.getExecutionPeriodForSemester(Integer.valueOf(1));
    }
}
