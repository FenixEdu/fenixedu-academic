/*
 * Created on Dec 9, 2005
 */
package net.sourceforge.fenixedu.applicationTier.Servico.bolonhaManager;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.CurricularPeriodInfoDTO;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.curricularPeriod.CurricularPeriod;
import net.sourceforge.fenixedu.domain.curricularPeriod.CurricularPeriodType;
import net.sourceforge.fenixedu.domain.degreeStructure.CourseGroup;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class AddContextToCurricularCourse extends Service {

    public void run(CurricularCourse curricularCourse, CourseGroup courseGroup, Integer beginExecutionPeriodID,
	    Integer endExecutionPeriodID, Integer year, Integer semester) throws ExcepcaoPersistencia, FenixServiceException {

	CurricularPeriod degreeCurricularPeriod = courseGroup.getParentDegreeCurricularPlan().getDegreeStructure();

	// ********************************************************
	/*
         * TODO: Important - change this code (must be generic to support
         * several curricularPeriodInfoDTOs, instead of year and semester)
         * 
         */
	CurricularPeriod curricularPeriod = null;
	CurricularPeriodInfoDTO curricularPeriodInfoYear = null;
	if (courseGroup.getParentDegreeCurricularPlan().getDegree().getDegreeType().getYears() > 1) {
	    curricularPeriodInfoYear = new CurricularPeriodInfoDTO(year, CurricularPeriodType.YEAR);
	}
	final CurricularPeriodInfoDTO curricularPeriodInfoSemester = new CurricularPeriodInfoDTO(semester,
		CurricularPeriodType.SEMESTER);

	if (curricularPeriodInfoYear != null) {
	    curricularPeriod = degreeCurricularPeriod.getCurricularPeriod(curricularPeriodInfoYear, curricularPeriodInfoSemester);
	    if (curricularPeriod == null) {
		curricularPeriod = degreeCurricularPeriod.addCurricularPeriod(curricularPeriodInfoYear,
			curricularPeriodInfoSemester);
	    }
	} else {
	    curricularPeriod = degreeCurricularPeriod.getCurricularPeriod(curricularPeriodInfoSemester);
	    if (curricularPeriod == null) {
		curricularPeriod = degreeCurricularPeriod.addCurricularPeriod(curricularPeriodInfoSemester);
	    }
	}

	// ********************************************************

	final ExecutionPeriod beginExecutionPeriod = getBeginExecutionPeriod(beginExecutionPeriodID);
	final ExecutionPeriod endExecutionPeriod = getEndExecutionPeriod(endExecutionPeriodID);

	courseGroup.addContext(curricularCourse, curricularPeriod, beginExecutionPeriod, endExecutionPeriod);
    }

    private ExecutionPeriod getBeginExecutionPeriod(final Integer beginExecutionPeriodID) {
	if (beginExecutionPeriodID == null) {
	    return ExecutionPeriod.readActualExecutionPeriod();
	} else {
	    return rootDomainObject.readExecutionPeriodByOID(beginExecutionPeriodID);
	}
    }

    private ExecutionPeriod getEndExecutionPeriod(Integer endExecutionPeriodID) {
	final ExecutionPeriod endExecutionPeriod = (endExecutionPeriodID == null) ? null : rootDomainObject
		.readExecutionPeriodByOID(endExecutionPeriodID);
	return endExecutionPeriod;
    }
}