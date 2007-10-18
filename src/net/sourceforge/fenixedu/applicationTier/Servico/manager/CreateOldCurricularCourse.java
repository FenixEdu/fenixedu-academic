package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.curricularPeriod.CurricularPeriod;
import net.sourceforge.fenixedu.domain.curriculum.CurricularCourseType;
import net.sourceforge.fenixedu.domain.degreeStructure.CourseGroup;
import net.sourceforge.fenixedu.domain.degreeStructure.CurricularStage;

public class CreateOldCurricularCourse extends Service {

    public void run(final Integer dcpId, final Integer cgId, final String name, final String nameEn, final String code,
	    final String acronym, final Integer minimumValueForAcumulatedEnrollments, final Integer maximumValueForAcumulatedEnrollments,
	    final Double weigth, final Integer enrolmentWeigth, final Double credits, final Double ectsCredits,
	    final Integer year, final Integer semester, final Integer beginExecutionPeriodId,
	    final Integer endExecutionPeriodId) throws FenixServiceException {
	
	final DegreeCurricularPlan degreeCurricularPlan = rootDomainObject.readDegreeCurricularPlanByOID(dcpId);
	if (degreeCurricularPlan == null) {
	    throw new FenixServiceException("error.createOldCurricularCourse.no.degreeCurricularPlan");
	}
	
	final CourseGroup courseGroup = (CourseGroup) rootDomainObject.readDegreeModuleByOID(cgId);
	if (courseGroup == null) {
	    throw new FenixServiceException("error.createOldCurricularCourse.no.courseGroup");
	}
	
	final CurricularCourse curricularCourse = degreeCurricularPlan.createCurricularCourse(name, code, acronym, Boolean.TRUE,
		CurricularStage.APPROVED);
	// hack to use dcp method
	curricularCourse.removeDegreeCurricularPlan();	

	curricularCourse.setNameEn(nameEn);
	curricularCourse.setWeigth(weigth);
	curricularCourse.setEnrollmentWeigth(enrolmentWeigth);
	curricularCourse.setMinimumValueForAcumulatedEnrollments(minimumValueForAcumulatedEnrollments);
	curricularCourse.setMaximumValueForAcumulatedEnrollments(maximumValueForAcumulatedEnrollments);
	curricularCourse.setCredits(credits);
	curricularCourse.setEctsCredits(ectsCredits);
	curricularCourse.setType(CurricularCourseType.NORMAL_COURSE);
	
	final CurricularPeriod curricularPeriod = getCurricularPeriod(degreeCurricularPlan, year, semester);
	final ExecutionPeriod beginExecutionPeriod = rootDomainObject.readExecutionPeriodByOID(beginExecutionPeriodId);
	final ExecutionPeriod endExecutionPeriod = rootDomainObject.readExecutionPeriodByOID(endExecutionPeriodId);
	
	curricularCourse.addContext(courseGroup, curricularPeriod, beginExecutionPeriod, endExecutionPeriod);
    }
    
    private CurricularPeriod getCurricularPeriod(final DegreeCurricularPlan degreeCurricularPlan, final Integer year, final Integer semester) {
	CurricularPeriod curricularPeriod = degreeCurricularPlan.getCurricularPeriodFor(year, semester);
	if (curricularPeriod == null) {
	    curricularPeriod = degreeCurricularPlan.createCurricularPeriodFor(year, semester);
	}
	return curricularPeriod;
    }
}
