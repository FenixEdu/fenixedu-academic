package net.sourceforge.fenixedu.domain.teacherServiceDistribution;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.commons.CollectionUtils;
import net.sourceforge.fenixedu.domain.CompetenceCourse;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.domain.ShiftProfessorship;
import net.sourceforge.fenixedu.domain.ShiftType;
import net.sourceforge.fenixedu.domain.Teacher;

import org.apache.commons.collections.Predicate;

public class CopyLastYearRealDataService {

    private static CopyLastYearRealDataService instance = new CopyLastYearRealDataService();

    private CopyLastYearRealDataService() {

    }

    public static CopyLastYearRealDataService getInstance() {
	return instance;
    }

    public void copyLastYearRealDataToTSDProcessPhase(TSDProcessPhase tsdProcessPhase) {

	List<ExecutionSemester> executionSemesters = tsdProcessPhase.getTSDProcess().getExecutionPeriods();
	TeacherServiceDistribution rootTSD = tsdProcessPhase.getRootTSD();
	Set<ExecutionCourse> executionCourses = null;
	ExecutionSemester lastYearPeriod = null;
	TSDCurricularCourseGroup tsdCurricularCourseGroup = null;

	for (TSDCourse tsdCourse : rootTSD.getTSDCourses()) {
	    if (tsdCourse instanceof TSDCompetenceCourse) {
		tsdCourse.setIsActive(Boolean.FALSE);
	    } else {
		tsdCourse.delete();
	    }
	}

	for (ExecutionSemester executionSemester : executionSemesters) {
	    executionCourses = new HashSet<ExecutionCourse>();
	    lastYearPeriod = executionSemester.getPreviousExecutionPeriod().getPreviousExecutionPeriod();

	    for (CompetenceCourse competenceCourse : rootTSD.getCompetenceCoursesByExecutionPeriod(executionSemester)) {
		executionCourses.addAll(competenceCourse.getExecutionCoursesByExecutionPeriod(lastYearPeriod));
	    }

	    for (ExecutionCourse executionCourse : executionCourses) {
		tsdCurricularCourseGroup = createTSDCurricularCourseGroupByExecutionCourse(executionCourse, executionSemester,
			tsdProcessPhase);
		fillTSDProfessorships(tsdCurricularCourseGroup, executionCourse, tsdProcessPhase);
	    }
	}
    }

    @SuppressWarnings("unchecked")
    private TSDCurricularCourseGroup createTSDCurricularCourseGroupByExecutionCourse(ExecutionCourse executionCourse,
	    final ExecutionSemester executionSemester, TSDProcessPhase tsdProcessPhase) {

	final Department tsdDepartment = tsdProcessPhase.getTSDProcess().getDepartment();
	List<CurricularCourse> curricularCourseList = (List<CurricularCourse>) CollectionUtils.select(executionCourse
		.getAssociatedCurricularCourses(), new Predicate() {
	    public boolean evaluate(Object arg0) {
		CurricularCourse curricularCourse = (CurricularCourse) arg0;

		if (curricularCourse.getCompetenceCourse() == null
			|| !curricularCourse.getCompetenceCourse().hasDepartments(tsdDepartment)) {
		    return false;
		}
		if (!curricularCourse.hasActiveScopesInExecutionPeriod(executionSemester)) {
		    return false;
		}
		return true;
	    }
	});

	List<TSDCurricularCourse> tsdCurricularCourseList = createTSDCurricularCoursesByCurricularCourses(curricularCourseList,
		executionSemester, tsdProcessPhase);

	if (!tsdCurricularCourseList.isEmpty()) {
	    TeacherServiceDistribution rootTSD = tsdProcessPhase.getRootTSD();

	    TSDCurricularCourseGroup tsdCurricularCourseGroup = new TSDCurricularCourseGroup(rootTSD, tsdCurricularCourseList);

	    fillTSDCourseData(tsdCurricularCourseGroup, Boolean.TRUE);

	    return tsdCurricularCourseGroup;
	}

	return null;
    }

    private List<TSDCurricularCourse> createTSDCurricularCoursesByCurricularCourses(List<CurricularCourse> curricularCourseList,
	    ExecutionSemester executionSemester, TSDProcessPhase tsdProcessPhase) {
	List<TSDCurricularCourse> tsdCurricularCourseList = new ArrayList<TSDCurricularCourse>();

	for (CurricularCourse curricularCourse : curricularCourseList) {
	    TSDCurricularCourse tsdCurricularCourse = createTSDCurricularCourseByCurricularCourse(tsdProcessPhase,
		    curricularCourse, executionSemester);

	    if (tsdCurricularCourse != null)
		tsdCurricularCourseList.add(tsdCurricularCourse);
	}

	return tsdCurricularCourseList;
    }

    private TSDCurricularCourse createTSDCurricularCourseByCurricularCourse(TSDProcessPhase tsdProcessPhase,
	    CurricularCourse curricularCourse, ExecutionSemester executionSemester) {
	TeacherServiceDistribution rootTSD = tsdProcessPhase.getRootTSD();

	TSDCurricularCourse tsdCurricularCourse = new TSDCurricularCourse(rootTSD, curricularCourse, executionSemester);

	fillTSDCourseData(tsdCurricularCourse, Boolean.FALSE);

	return tsdCurricularCourse;
    }

    private void fillTSDCourseData(TSDCourse tsdCourse, Boolean isActive) {
	Integer zeroInteger = 0;
	TSDValueType valueType = TSDValueType.LAST_YEAR_REAL_VALUE;

	tsdCourse.setFirstTimeEnrolledStudentsManual(zeroInteger);
	tsdCourse.setSecondTimeEnrolledStudentsManual(zeroInteger);
	tsdCourse.setFirstTimeEnrolledStudentsType(valueType);
	tsdCourse.setSecondTimeEnrolledStudentsType(valueType);
	tsdCourse.setIsActive(isActive);

	for (TSDCurricularLoad load : tsdCourse.getTSDCurricularLoads()) {
	    load.setHoursType(valueType);
	    load.setStudentsPerShiftType(valueType);
	    load.setWeightFirstTimeEnrolledStudentsPerShiftType(valueType);
	    load.setWeightSecondTimeEnrolledStudentsPerShiftType(valueType);
	}
    }

    private void fillTSDProfessorships(TSDCurricularCourseGroup tsdCurricularCourseGroup, ExecutionCourse executionCourse,
	    TSDProcessPhase tsdProcessPhase) {

	if (tsdCurricularCourseGroup == null) {
	    return;
	}

	for (Professorship professorship : executionCourse.getProfessorships()) {
	    Teacher teacher = professorship.getTeacher();

	    TSDTeacher tsdTeacher = tsdProcessPhase.getRootTSD().getTSDTeacherByTeacher(teacher);

	    if (tsdTeacher != null) {
		for (ShiftType type : getDistinctShiftTypes(professorship)) {
		    TSDProfessorship tsdProfessorship = tsdCurricularCourseGroup.getTSDProfessorshipByTSDTeacherAndShiftType(
			    tsdTeacher, type);

		    if (tsdProfessorship == null) {
			tsdProfessorship = new TSDProfessorship(tsdCurricularCourseGroup, tsdTeacher, type);
		    }

		    tsdProfessorship.setHoursManual(0d);
		    tsdProfessorship.setHoursType(TSDValueType.LAST_YEAR_REAL_VALUE);
		}
	    }
	}
    }

    private Set<ShiftType> getDistinctShiftTypes(Professorship professorship) {
	HashSet<ShiftType> shiftTypeSet = new HashSet<ShiftType>();

	for (ShiftProfessorship shiftProfessorship : professorship.getAssociatedShiftProfessorship()) {
	    for (ShiftType type : shiftProfessorship.getShift().getTypes()) {
		shiftTypeSet.add(type);
	    }
	}

	return shiftTypeSet;
    }
}