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
import net.sourceforge.fenixedu.domain.degreeStructure.Context;
import net.sourceforge.fenixedu.domain.degreeStructure.CourseGroup;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class EditContextFromCurricularCourse extends Service {

    public void run(CurricularCourse curricularCourse, Context context, CourseGroup courseGroup,
            Integer year, Integer semester, Integer beginExecutionPeriodID,
            Integer endExecutionPeriodID) throws ExcepcaoPersistencia, FenixServiceException {

        CurricularPeriod degreeCurricularPeriod = context.getParentCourseGroup()
                .getParentDegreeCurricularPlan().getDegreeStructure();

        CurricularPeriod curricularPeriod = degreeCurricularPeriod.getCurricularPeriod(
                new CurricularPeriodInfoDTO(year, CurricularPeriodType.YEAR),
                new CurricularPeriodInfoDTO(semester, CurricularPeriodType.SEMESTER));

        if (curricularPeriod == null) {
            curricularPeriod = degreeCurricularPeriod.addCurricularPeriod(new CurricularPeriodInfoDTO(
                    year, CurricularPeriodType.YEAR), new CurricularPeriodInfoDTO(semester,
                    CurricularPeriodType.SEMESTER));
        }

        curricularCourse.editContext(context, courseGroup, curricularPeriod,
                getBeginExecutionPeriod(beginExecutionPeriodID),
                getEndExecutionPeriod(endExecutionPeriodID));
    }
    
    private ExecutionPeriod getBeginExecutionPeriod(Integer beginExecutionPeriodID) throws FenixServiceException {
        final ExecutionPeriod beginExecutionPeriod;
        if (beginExecutionPeriodID == null) {
            final ExecutionYear currentExecutionYear = ExecutionYear.readCurrentExecutionYear();
            final ExecutionYear nextExecutionYear = currentExecutionYear.getNextExecutionYear();
            if (nextExecutionYear == null) {
                throw new FenixServiceException("error.no.next.execution.year");
            }
            beginExecutionPeriod = nextExecutionYear.readExecutionPeriodForSemester(Integer.valueOf(1));
        } else {
            beginExecutionPeriod = rootDomainObject.readExecutionPeriodByOID(beginExecutionPeriodID);
        }
        return beginExecutionPeriod;
    }
    
    private ExecutionPeriod getEndExecutionPeriod(Integer endExecutionPeriodID) {
        final ExecutionPeriod endExecutionPeriod = (endExecutionPeriodID == null) ? null
                : rootDomainObject.readExecutionPeriodByOID(endExecutionPeriodID);
        return endExecutionPeriod;
    }
}
