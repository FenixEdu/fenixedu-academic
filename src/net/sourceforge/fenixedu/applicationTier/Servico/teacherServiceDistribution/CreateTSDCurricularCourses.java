package net.sourceforge.fenixedu.applicationTier.Servico.teacherServiceDistribution;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.CompetenceCourse;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.TSDCurricularCourse;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.TSDProcessPhase;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.TeacherServiceDistribution;

public class CreateTSDCurricularCourses extends Service {
    public void run(Integer tsdId, Integer competenceCourseId, Integer tsdProcessPhaseId, Integer executionPeriodId,
	    Boolean activateCourses) {

	TeacherServiceDistribution tsd = rootDomainObject.readTeacherServiceDistributionByOID(tsdId);
	CompetenceCourse competenceCourse = rootDomainObject.readCompetenceCourseByOID(competenceCourseId);
	TSDProcessPhase tsdProcessPhase = rootDomainObject.readTSDProcessPhaseByOID(tsdProcessPhaseId);
	ExecutionSemester executionSemester = rootDomainObject.readExecutionSemesterByOID(executionPeriodId);

	List<CurricularCourse> curricularCourseList = competenceCourse
		.getCurricularCoursesWithActiveScopesInExecutionPeriod(executionSemester);

	Set<CurricularCourse> existingCurricularCourses = new HashSet<CurricularCourse>();
	for (TSDCurricularCourse tsdCourse : tsdProcessPhase.getRootTSD().getTSDCurricularCourses(competenceCourse,
		executionSemester)) {
	    existingCurricularCourses.add(((TSDCurricularCourse) tsdCourse).getCurricularCourse());
	}

	for (CurricularCourse curricularCourse : curricularCourseList) {
	    if (!existingCurricularCourses.contains(curricularCourse)) {
		TSDCurricularCourse tsdCurricularCourse = new TSDCurricularCourse(tsd, curricularCourse, executionSemester);
		tsdCurricularCourse.setIsActive(activateCourses);
	    }
	}
    }
}
