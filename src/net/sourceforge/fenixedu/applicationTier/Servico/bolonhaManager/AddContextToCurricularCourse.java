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

public class AddContextToCurricularCourse extends Service {

    public void run(CurricularCourse curricularCourse, CourseGroup courseGroup, Integer beginExecutionPeriodID,
            Integer endExecutionPeriodID, Integer year, Integer semester) throws ExcepcaoPersistencia, FenixServiceException {

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
        
        final ExecutionPeriod beginExecutionPeriod = getBeginExecutionPeriod(beginExecutionPeriodID);
        final ExecutionPeriod endExecutionPeriod = getEndExecutionPeriod(endExecutionPeriodID);

        curricularCourse.addContext(courseGroup, curricularPeriod, beginExecutionPeriod, endExecutionPeriod);
    }

    private ExecutionPeriod getBeginExecutionPeriod(Integer beginExecutionPeriodID) throws FenixServiceException {
        final ExecutionPeriod beginExecutionPeriod;
        if (beginExecutionPeriodID == null) {
            final ExecutionYear currentExecutionYear = ExecutionYear.readCurrentExecutionYear();
            final ExecutionYear nextExecutionYear = currentExecutionYear.getNextExecutionYear();
            if (nextExecutionYear == null) {
                throw new FenixServiceException("error.no.next.execution.year");
            }
            beginExecutionPeriod = nextExecutionYear.getExecutionPeriodForSemester(Integer.valueOf(1));
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