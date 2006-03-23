/*
 * Created on Dec 9, 2005
 */
package net.sourceforge.fenixedu.applicationTier.Servico.bolonhaManager;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.CurricularPeriodInfoDTO;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.curricularPeriod.CurricularPeriod;
import net.sourceforge.fenixedu.domain.curricularPeriod.CurricularPeriodType;
import net.sourceforge.fenixedu.domain.degreeStructure.CourseGroup;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class AddContextToDegreeModule extends Service {

    public void run(CurricularCourse curricularCourse, CourseGroup courseGroup, Integer year,
            Integer semester) throws ExcepcaoPersistencia, FenixServiceException {

        CurricularPeriod degreeCurricularPeriod = courseGroup.getParentDegreeCurricularPlan()
                .getDegreeStructure();
        CurricularPeriod curricularPeriod = degreeCurricularPeriod.getCurricularPeriod(
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
        final ExecutionPeriod beginExecutionPeriod = executionYear.getExecutionPeriodForSemester(Integer
                .valueOf(1));

        curricularCourse.addContext(courseGroup, curricularPeriod, beginExecutionPeriod, null);
    }
    
    public void run(CourseGroup courseGroup, CourseGroup parentCourseGroup) throws ExcepcaoPersistencia, FenixServiceException {
        if (courseGroup == null || parentCourseGroup == null) {
            throw new FenixServiceException("error.noCourseGroup");
        }
        
        // TODO: this should be modified to receive ExecutionYear, but for now
        // we just read the '2006/2007'
        final ExecutionYear executionYear = ExecutionYear.readExecutionYearByName("2006/2007");
        final ExecutionPeriod beginExecutionPeriod = executionYear.getExecutionPeriodForSemester(Integer
                .valueOf(1));

        courseGroup.addContext(parentCourseGroup, null, beginExecutionPeriod, null);
    }
    
}
