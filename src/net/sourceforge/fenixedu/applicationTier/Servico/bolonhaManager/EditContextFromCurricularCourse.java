package net.sourceforge.fenixedu.applicationTier.Servico.bolonhaManager;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.CurricularPeriodInfoDTO;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.curricularPeriod.CurricularPeriod;
import net.sourceforge.fenixedu.domain.curricularPeriod.CurricularPeriodType;
import net.sourceforge.fenixedu.domain.degreeStructure.Context;
import net.sourceforge.fenixedu.domain.degreeStructure.CourseGroup;

public class EditContextFromCurricularCourse extends Service {

    public void run(CurricularCourse curricularCourse, Context context, CourseGroup courseGroup, Integer year, Integer semester,
	    Integer beginExecutionPeriodID, Integer endExecutionPeriodID) {

	final CurricularPeriod degreeCurricularPeriod = context.getParentCourseGroup().getParentDegreeCurricularPlan()
		.getDegreeStructure();

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

	context.edit(courseGroup, curricularPeriod, getBeginExecutionPeriod(beginExecutionPeriodID),
		getEndExecutionPeriod(endExecutionPeriodID));
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
