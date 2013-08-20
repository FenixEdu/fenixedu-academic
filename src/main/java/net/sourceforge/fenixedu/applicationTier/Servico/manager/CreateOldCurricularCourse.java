package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.GradeScale;
import net.sourceforge.fenixedu.domain.curricularPeriod.CurricularPeriod;
import net.sourceforge.fenixedu.domain.curriculum.CurricularCourseType;
import net.sourceforge.fenixedu.domain.degreeStructure.CourseGroup;
import net.sourceforge.fenixedu.domain.degreeStructure.CurricularStage;
import pt.ist.fenixWebFramework.services.Service;
import pt.ist.fenixframework.pstm.AbstractDomainObject;

public class CreateOldCurricularCourse {

    @Service
    public static void run(final String dcpId, final String cgId, final String name, final String nameEn, final String code,
            final String acronym, final Integer minimumValueForAcumulatedEnrollments,
            final Integer maximumValueForAcumulatedEnrollments, final Double weigth, final Integer enrolmentWeigth,
            final Double credits, final Double ectsCredits, final Integer year, final Integer semester,
            final String beginExecutionPeriodId, final String endExecutionPeriodId, final GradeScale gradeScale)
            throws FenixServiceException {

        final DegreeCurricularPlan degreeCurricularPlan = AbstractDomainObject.fromExternalId(dcpId);
        if (degreeCurricularPlan == null) {
            throw new FenixServiceException("error.createOldCurricularCourse.no.degreeCurricularPlan");
        }

        final CourseGroup courseGroup = (CourseGroup) AbstractDomainObject.fromExternalId(cgId);
        if (courseGroup == null) {
            throw new FenixServiceException("error.createOldCurricularCourse.no.courseGroup");
        }

        final CurricularCourse curricularCourse =
                degreeCurricularPlan.createCurricularCourse(name, code, acronym, Boolean.TRUE, CurricularStage.APPROVED);
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
        curricularCourse.setGradeScale(gradeScale);

        final CurricularPeriod curricularPeriod = getCurricularPeriod(degreeCurricularPlan, year, semester);
        final ExecutionSemester beginExecutionPeriod = AbstractDomainObject.fromExternalId(beginExecutionPeriodId);
        final ExecutionSemester endExecutionPeriod = AbstractDomainObject.fromExternalId(endExecutionPeriodId);

        courseGroup.addContext(curricularCourse, curricularPeriod, beginExecutionPeriod, endExecutionPeriod);
    }

    private static CurricularPeriod getCurricularPeriod(final DegreeCurricularPlan degreeCurricularPlan, final Integer year,
            final Integer semester) {
        CurricularPeriod curricularPeriod = degreeCurricularPlan.getCurricularPeriodFor(year, semester);
        if (curricularPeriod == null) {
            curricularPeriod = degreeCurricularPlan.createCurricularPeriodFor(year, semester);
        }
        return curricularPeriod;
    }
}