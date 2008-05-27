/*
 * Created on Dec 9, 2005
 */
package net.sourceforge.fenixedu.applicationTier.Servico.bolonhaManager;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.CurricularPeriodInfoDTO;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.curricularPeriod.CurricularPeriod;
import net.sourceforge.fenixedu.domain.degreeStructure.CourseGroup;
import net.sourceforge.fenixedu.domain.time.calendarStructure.AcademicPeriod;
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
	    curricularPeriodInfoYear = new CurricularPeriodInfoDTO(year, AcademicPeriod.YEAR);
	}
	final CurricularPeriodInfoDTO curricularPeriodInfoSemester = new CurricularPeriodInfoDTO(semester,
		AcademicPeriod.SEMESTER);

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

	final ExecutionSemester beginExecutionPeriod = getBeginExecutionPeriod(beginExecutionPeriodID);
	final ExecutionSemester endExecutionPeriod = getEndExecutionPeriod(endExecutionPeriodID);

	courseGroup.addContext(curricularCourse, curricularPeriod, beginExecutionPeriod, endExecutionPeriod);
    }

    private ExecutionSemester getBeginExecutionPeriod(final Integer beginExecutionPeriodID) {
	if (beginExecutionPeriodID == null) {
	    return ExecutionSemester.readActualExecutionSemester();
	} else {
	    return rootDomainObject.readExecutionSemesterByOID(beginExecutionPeriodID);
	}
    }

    private ExecutionSemester getEndExecutionPeriod(Integer endExecutionPeriodID) {
	final ExecutionSemester endExecutionPeriod = (endExecutionPeriodID == null) ? null : rootDomainObject
		.readExecutionSemesterByOID(endExecutionPeriodID);
	return endExecutionPeriod;
    }
}