package net.sourceforge.fenixedu.domain.teacherServiceDistribution;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.commons.CollectionUtils;
import net.sourceforge.fenixedu.domain.CompetenceCourse;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.ShiftType;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.Predicate;

public abstract class TSDCourse extends TSDCourse_Base {

    protected TSDCourse() {
	super();
	setIsActive(false);
	setRootDomainObject(RootDomainObject.getInstance());

	this.setFirstTimeEnrolledStudentsManual(0);
	this.setFirstTimeEnrolledStudentsType(TSDValueType.MANUAL_VALUE);
	this.setSecondTimeEnrolledStudentsManual(0);
	this.setSecondTimeEnrolledStudentsType(TSDValueType.MANUAL_VALUE);
    }

    public abstract List<CurricularCourse> getAssociatedCurricularCourses();

    public abstract String getName();

    public Set<TSDTeacher> getAssociatedTSDTeachers() {
	Set<TSDTeacher> teachersSet = new HashSet<TSDTeacher>();

	for (TSDProfessorship professorship : getTSDProfessorships()) {
	    teachersSet.add(professorship.getTSDTeacher());
	}

	return teachersSet;
    }

    public TSDCurricularLoad getTSDCurricularLoadByShiftType(ShiftType type) {
	for (TSDCurricularLoad curricularLoad : super.getTSDCurricularLoads()) {
	    if (curricularLoad.getType().equals(type)) {
		return curricularLoad;
	    }
	}

	return null;
    }

    public Double getHoursManual(ShiftType type) {
	TSDCurricularLoad curricularLoad = getTSDCurricularLoadByShiftType(type);
	return curricularLoad == null ? 0d : curricularLoad.getHoursManual();
    }

    public TSDValueType getHoursType(ShiftType type) {
	TSDCurricularLoad curricularLoad = getTSDCurricularLoadByShiftType(type);
	return curricularLoad == null ? TSDValueType.MANUAL_VALUE : curricularLoad.getHoursType();
    }

    public Double getShiftFrequency(ShiftType type) {
	TSDCurricularLoad curricularLoad = getTSDCurricularLoadByShiftType(type);
	return curricularLoad == null ? 0d : curricularLoad.getFrequency();
    }

    public Integer getStudentsPerShiftManual(ShiftType type) {
	TSDCurricularLoad curricularLoad = getTSDCurricularLoadByShiftType(type);
	return curricularLoad == null ? 0 : curricularLoad.getStudentsPerShiftManual();
    }

    public TSDValueType getStudentsPerShiftType(ShiftType type) {
	TSDCurricularLoad curricularLoad = getTSDCurricularLoadByShiftType(type);
	return curricularLoad == null ? TSDValueType.MANUAL_VALUE : curricularLoad.getStudentsPerShiftType();
    }

    public Double getWeightFirstTimeEnrolledStudentsPerShiftManual(ShiftType type) {
	TSDCurricularLoad curricularLoad = getTSDCurricularLoadByShiftType(type);
	return curricularLoad == null ? 0d : curricularLoad.getWeightFirstTimeEnrolledStudentsPerShiftManual();
    }

    public TSDValueType getWeightFirstTimeEnrolledStudentsPerShiftType(ShiftType type) {
	TSDCurricularLoad curricularLoad = getTSDCurricularLoadByShiftType(type);
	return curricularLoad == null ? TSDValueType.MANUAL_VALUE : curricularLoad
		.getWeightFirstTimeEnrolledStudentsPerShiftType();
    }

    public Double getWeightSecondTimeEnrolledStudentsPerShiftManual(ShiftType type) {
	TSDCurricularLoad curricularLoad = getTSDCurricularLoadByShiftType(type);
	return curricularLoad == null ? 0d : curricularLoad.getWeightSecondTimeEnrolledStudentsPerShiftManual();
    }

    public TSDValueType getWeightSecondTimeEnrolledStudentsPerShiftType(ShiftType type) {
	TSDCurricularLoad curricularLoad = getTSDCurricularLoadByShiftType(type);
	return curricularLoad == null ? TSDValueType.MANUAL_VALUE : curricularLoad
		.getWeightSecondTimeEnrolledStudentsPerShiftType();
    }

    public Integer getRealStudentsPerShiftLastYear(ShiftType type) {
	return getRealStudentsByShiftAndExecutionPeriod(type, getLastYearExecutionPeriod());
    }

    public Integer getRealStudentsPerShift(ShiftType type) {
	return getRealStudentsByShiftAndExecutionPeriod(type, getExecutionPeriod());
    }

    public Double getHoursPerShift(ShiftType type) {
	TSDCurricularLoad curricularLoad = getTSDCurricularLoadByShiftType(type);
	return curricularLoad == null ? 0d : curricularLoad.getHoursPerShift();
    }

    public Double getRealHoursLastYear(ShiftType type) {
	return getRealHoursByShiftAndExecutionPeriod(type, getLastYearExecutionPeriod());
    }

    public Double getRealHours(ShiftType type) {
	return getRealHoursByShiftAndExecutionPeriod(type, getExecutionPeriod());
    }

    public Double getHoursCalculated(ShiftType type) {
	if (getStudentsPerShiftType(type) == TSDValueType.CALCULATED_VALUE) {
	    return 0d;
	}

	if (getStudentsPerShift(type).equals(0)) {
	    return 0d;
	}

	Double numberOfShifts = null;

	try {
	    numberOfShifts = StrictMath.ceil(((new Double(getTotalNumberOfStudents(type)) / getStudentsPerShift(type))));
	} catch (ArithmeticException e) {
	    numberOfShifts = 0d;
	}

	return numberOfShifts * getHoursPerShift(type) * getShiftFrequency(type);
    }

    public TSDProcessPhase getTSDProcessPhase() {
	return getTeacherServiceDistributions().get(0).getTSDProcessPhase();
    }

    public Double getHours(ShiftType type) {
	if (getHoursType(type) == TSDValueType.MANUAL_VALUE) {
	    return getHoursManual(type);
	} else if (getHoursType(type) == TSDValueType.LAST_YEAR_REAL_VALUE) {
	    return getRealHoursLastYear(type);
	} else if (getHoursType(type) == TSDValueType.REAL_VALUE) {
	    return getRealHours(type);
	} else if (getHoursType(type) == TSDValueType.CALCULATED_VALUE) {
	    return getHoursCalculated(type);
	}

	return 0d;
    }

    public Integer getStudentsPerShift(ShiftType type) {
	if (getStudentsPerShiftType(type) == TSDValueType.MANUAL_VALUE) {
	    return getStudentsPerShiftManual(type);
	} else if (getStudentsPerShiftType(type) == TSDValueType.LAST_YEAR_REAL_VALUE) {
	    return getRealStudentsPerShiftLastYear(type);
	} else if (getStudentsPerShiftType(type) == TSDValueType.REAL_VALUE) {
	    return getRealStudentsPerShift(type);
	} else if (getStudentsPerShiftType(type) == TSDValueType.CALCULATED_VALUE) {
	    return getStudentsPerShiftCalculated(type);
	} else if (getStudentsPerShiftType(type) == TSDValueType.OMISSION_VALUE) {
	    return getTSDProcessPhase().getStudentsPerShift(type);
	}

	return 0;
    }

    public Double getWeightFirstTimeEnrolledStudentsPerShift(ShiftType type) {
	if (getWeightFirstTimeEnrolledStudentsPerShiftType(type) == TSDValueType.MANUAL_VALUE) {
	    return getWeightFirstTimeEnrolledStudentsPerShiftManual(type);
	} else if (getWeightFirstTimeEnrolledStudentsPerShiftType(type) == TSDValueType.OMISSION_VALUE) {
	    return getTSDProcessPhase().getWeightFirstTimeEnrolledStudentsPerShift(type);
	}

	return 0d;
    }

    public Double getWeightSecondTimeEnrolledStudentsPerShift(ShiftType type) {
	if (getWeightSecondTimeEnrolledStudentsPerShiftType(type) == TSDValueType.MANUAL_VALUE) {
	    return getWeightSecondTimeEnrolledStudentsPerShiftManual(type);
	} else if (getWeightSecondTimeEnrolledStudentsPerShiftType(type) == TSDValueType.OMISSION_VALUE) {
	    return getTSDProcessPhase().getWeightSecondTimeEnrolledStudentsPerShift(type);
	}

	return 0d;
    }

    public Integer getTotalNumberOfStudents(ShiftType type) {
	Double totalNumberOfStudents = (getFirstTimeEnrolledStudents() * getWeightFirstTimeEnrolledStudentsPerShift(type))
		+ (getSecondTimeEnrolledStudents() * getWeightSecondTimeEnrolledStudentsPerShift(type));

	totalNumberOfStudents = StrictMath.ceil(totalNumberOfStudents);
	return totalNumberOfStudents.intValue();
    }

    public Integer getNumberOfShifts(ShiftType type) {
	double ratio = new Double(getTotalNumberOfStudents(type)) / getStudentsPerShift(type);
	return Double.valueOf((Math.ceil(ratio))).intValue();
    }

    public Integer getNumberOfSchoolClasses(ShiftType type) {
	TSDCurricularLoad load = getTSDCurricularLoadByShiftType(type);
	return load == null ? 0 : load.getNumberOfSchoolClasses();
    }

    public double getNumberOfHoursForStudents(ShiftType type) {
	return getHoursPerShift(type) * getShiftFrequency(type);
    }

    public double getNumberOfHoursForTeachers(ShiftType type) {
	return getHoursPerShift(type) * getShiftFrequency(type) * getNumberOfShifts(type);
    }

    public Integer getStudentsPerShiftCalculated(ShiftType type) {
	if (getHoursType(type) == TSDValueType.CALCULATED_VALUE) {
	    return 0;
	} else {
	    if (getHoursPerShift(type).equals(0d)) {
		return 0;
	    }

	    Double numberOfShifts = null;

	    try {
		numberOfShifts = StrictMath.ceil(new Double((getHours(type) / getHoursPerShift(type))));
	    } catch (ArithmeticException e) {
		numberOfShifts = 0d;
	    }

	    if (numberOfShifts.equals(0d)) {
		return 0;
	    }

	    Double studentsPerShift = StrictMath.ceil(new Double(getTotalNumberOfStudents(type) / numberOfShifts));

	    return studentsPerShift.intValue();
	}
    }

    public Double getTotalHoursLecturedByShiftType(ShiftType shiftType) {
	Double totalHours = 0d;

	for (TSDProfessorship tsdProfessorship : getTSDProfessorships()) {
	    if (shiftType == null || tsdProfessorship.getType().equals(shiftType)) {
		totalHours += tsdProfessorship.getHours();
	    }
	}

	return totalHours;
    }

    public Double getTotalHours() {
	Double hours = 0d;

	for (TSDCurricularLoad tsdLoad : getTSDCurricularLoads()) {
	    hours += tsdLoad.getHours();
	}

	return hours;
    }

    public Double getTotalHoursLectured() {
	return getTotalHoursLecturedByShiftType(null);
    }

    private Integer getRealFirstTimeEnrolledStudentsNumberByExecutionPeriod(ExecutionSemester executionSemester) {
	int firstTimeEnrolledStudents = 0;

	for (CurricularCourse curricularCourse : getAssociatedCurricularCourses()) {
	    firstTimeEnrolledStudents += curricularCourse.getFirstTimeEnrolmentStudentNumber(executionSemester);
	}

	return firstTimeEnrolledStudents;
    }

    private Integer getRealSecondTimeEnrolledStudentsNumberByExecutionPeriod(ExecutionSemester executionSemester) {
	Integer secondTimeEnrolledStudentsNumber = 0;

	if (executionSemester != null) {
	    for (CurricularCourse curricularCourse : getAssociatedCurricularCourses()) {
		secondTimeEnrolledStudentsNumber += curricularCourse.getSecondTimeEnrolmentStudentNumber(executionSemester);
	    }
	}

	return secondTimeEnrolledStudentsNumber;
    }

    public Integer getRealFirstTimeEnrolledStudentsNumberLastYear() {
	return getRealFirstTimeEnrolledStudentsNumberByExecutionPeriod(getLastYearExecutionPeriod());
    }

    public Integer getRealSecondTimeEnrolledStudentsNumberLastYear() {
	return getRealSecondTimeEnrolledStudentsNumberByExecutionPeriod(getLastYearExecutionPeriod());
    }

    public Integer getRealFirstTimeEnrolledStudentsNumber() {
	return getRealFirstTimeEnrolledStudentsNumberByExecutionPeriod(getExecutionPeriod());
    }

    public Integer getRealSecondTimeEnrolledStudentsNumber() {
	return getRealSecondTimeEnrolledStudentsNumberByExecutionPeriod(getExecutionPeriod());
    }

    public Integer getFirstTimeEnrolledStudents() {
	if (getFirstTimeEnrolledStudentsType() == TSDValueType.MANUAL_VALUE) {
	    return super.getFirstTimeEnrolledStudentsManual();
	} else if (getFirstTimeEnrolledStudentsType() == TSDValueType.LAST_YEAR_REAL_VALUE) {
	    return getRealFirstTimeEnrolledStudentsNumberLastYear();
	} else if (getFirstTimeEnrolledStudentsType() == TSDValueType.REAL_VALUE) {
	    return getRealFirstTimeEnrolledStudentsNumber();
	}

	return 0;
    }

    public Integer getSecondTimeEnrolledStudents() {
	if (getSecondTimeEnrolledStudentsType() == TSDValueType.MANUAL_VALUE) {
	    return super.getSecondTimeEnrolledStudentsManual();
	} else if (getSecondTimeEnrolledStudentsType() == TSDValueType.LAST_YEAR_REAL_VALUE) {
	    return getRealSecondTimeEnrolledStudentsNumberLastYear();
	} else if (getSecondTimeEnrolledStudentsType() == TSDValueType.REAL_VALUE) {
	    return getRealSecondTimeEnrolledStudentsNumber();
	}

	return 0;
    }

    public Integer getTotalEnrolledStudents() {
	TSDValueType firstTimeType = getFirstTimeEnrolledStudentsType();

	if (firstTimeType == getSecondTimeEnrolledStudentsType()) {
	    if (firstTimeType == TSDValueType.REAL_VALUE) {
		return getTotalEnrolledStudentsNumberByExecutionPeriod(getExecutionPeriod());
	    }
	    if (firstTimeType == TSDValueType.LAST_YEAR_REAL_VALUE) {
		return getTotalEnrolledStudentsNumberByExecutionPeriod(getLastYearExecutionPeriod());
	    }
	}

	return getFirstTimeEnrolledStudents() + getSecondTimeEnrolledStudents();
    }

    private Double getRealHoursByShiftAndExecutionPeriod(ShiftType shiftType, ExecutionSemester executionSemester) {
	Double hours = 0d;
	for (ExecutionCourse executionCourse : getAssociatedExecutionCoursesByExecutionPeriod(executionSemester)) {
	    hours += executionCourse.getAllShiftUnitHours(shiftType).doubleValue();
	}
	return hours;
    }

    private Integer getRealStudentsByShiftAndExecutionPeriod(ShiftType shiftType, ExecutionSemester executionSemester) {
	Double numberOfShifts = 0.0;
	Double totalNumberOfStudents = 0d;

	for (CurricularCourse curricularCourse : getAssociatedCurricularCourses()) {
	    List<ExecutionCourse> executionCourseList = curricularCourse.getExecutionCoursesByExecutionPeriod(executionSemester);

	    for (ExecutionCourse executionCourse : executionCourseList) {
		numberOfShifts += executionCourse.getNumberOfShifts(shiftType)
			* executionCourse.getCurricularCourseEnrolmentsWeight(curricularCourse);
	    }

	    totalNumberOfStudents += curricularCourse.getTotalEnrolmentStudentNumber(executionSemester);
	}

	numberOfShifts = StrictMath.ceil(numberOfShifts);

	if (numberOfShifts > 0d) {
	    return ((Double) StrictMath.ceil((totalNumberOfStudents / numberOfShifts))).intValue();
	} else {
	    return 0;
	}
    }

    public TSDProfessorship getTSDProfessorshipByTSDTeacherAndShiftType(final TSDTeacher tsdTeacher, final ShiftType type) {
	return (TSDProfessorship) CollectionUtils.find(getTSDProfessorships(), new Predicate() {

	    public boolean evaluate(Object arg0) {
		TSDProfessorship tsdProfessorship = (TSDProfessorship) arg0;

		return tsdProfessorship.getType().equals(type) && tsdProfessorship.getTSDTeacher() == tsdTeacher;
	    }
	});
    }

    public List<TSDProfessorship> getTSDProfessorshipByTSDTeacher(final TSDTeacher tsdTeacher) {
	return (List<TSDProfessorship>) CollectionUtils.select(getTSDProfessorships(), new Predicate() {

	    public boolean evaluate(Object arg0) {
		TSDProfessorship tsdProfessorship = (TSDProfessorship) arg0;

		return tsdProfessorship.getTSDTeacher() == tsdTeacher;
	    }
	});
    }

    public List<ExecutionCourse> getAssociatedExecutionCoursesByExecutionPeriod(ExecutionSemester executionSemester) {
	Set<ExecutionCourse> executionCourseSet = new HashSet<ExecutionCourse>();

	for (CurricularCourse valuationCurricularcourse : getAssociatedCurricularCourses()) {
	    executionCourseSet.addAll(valuationCurricularcourse.getExecutionCoursesByExecutionPeriod(executionSemester));
	}

	List<ExecutionCourse> executionCourseList = new ArrayList<ExecutionCourse>();
	executionCourseList.addAll(executionCourseSet);

	return executionCourseList;
    }

    public List<ExecutionCourse> getAssociatedExecutionCoursesLastYear() {
	return getAssociatedExecutionCoursesByExecutionPeriod(getLastYearExecutionPeriod());
    }

    public List<ExecutionCourse> getAssociatedExecutionCourses() {
	return getAssociatedExecutionCoursesByExecutionPeriod(getExecutionPeriod());
    }

    public ExecutionSemester getLastYearExecutionPeriod() {
	ExecutionYear executionYear = getExecutionPeriod().getExecutionYear();

	ExecutionYear lastExecutionYear = executionYear.getPreviousExecutionYear();

	if (lastExecutionYear != null) {
	    for (ExecutionSemester executionSemester : lastExecutionYear.getExecutionPeriods()) {
		if (executionSemester.getSemester().equals(getExecutionPeriod().getSemester())) {
		    return executionSemester;
		}
	    }
	}

	return null;
    }

    public Double getHoursNotLectured(ShiftType type) {
	return getHours(type) - getTotalHoursLecturedByShiftType(type);
    }

    public Double getTotalHoursNotLectured() {
	return getTotalHours() - getTotalHoursLectured();
    }

    public List<String> getCampus() {
	Set<String> campusSet = new LinkedHashSet<String>();
	ExecutionSemester executionPeriod = getExecutionPeriod();

	for (CurricularCourse curricularCourse : getAssociatedCurricularCourses()) {
	    // if(!curricularCourse.getIsRealCurricularCourse()) continue;
	    for (ExecutionDegree executionDegree : curricularCourse.getDegreeCurricularPlan().getExecutionDegrees()) {
		if (executionDegree.getExecutionYear() == executionPeriod.getExecutionYear()) {
		    campusSet.add(executionDegree.getCampus().getName());
		    break;
		}
	    }
	}

	List<String> campusList = new ArrayList<String>();
	campusList.addAll(campusSet);

	return campusList;
    }

    public CompetenceCourse getCompetenceCourse() {
	if (getAssociatedCurricularCourses().size() > 0) {
	    return getAssociatedCurricularCourses().get(0).getCompetenceCourse();
	} else {
	    return null;
	}
    }

    public String getCompetenceName() {
	return getCompetenceCourse() == null ? getName() : getCompetenceCourse().getName();
    }

    public void delete() {
	for (TSDProfessorship tsdProfessorship : getTSDProfessorships()) {
	    tsdProfessorship.delete();
	}

	for (TeacherServiceDistribution teacherServiceDistribution : getTeacherServiceDistributions()) {
	    removeTeacherServiceDistributions(teacherServiceDistribution);
	}

	for (TSDCurricularLoad load : getTSDCurricularLoads()) {
	    load.delete();
	}

	removeExecutionPeriod();
	removeRootDomainObject();
	super.deleteDomainObject();
    }

    public String getAcronym() {

	CurricularCourse curricularCourse = null;
	if (getAssociatedCurricularCourses().size() > 0) {
	    curricularCourse = getAssociatedCurricularCourses().get(0);
	}

	if (curricularCourse != null) {
	    return curricularCourse.getAcronym();
	} else {
	    return getCompetenceName();
	}
    }

    private Integer getTotalEnrolledStudentsNumberByExecutionPeriod(ExecutionSemester executionSemester) {
	Integer totalEnrolledStudentsNumber = 0;

	if (executionSemester != null) {
	    for (CurricularCourse curricularCourse : getAssociatedCurricularCourses()) {
		totalEnrolledStudentsNumber += curricularCourse.getTotalEnrolmentStudentNumber(executionSemester);
	    }
	}

	return totalEnrolledStudentsNumber;
    }

    public List<TSDCurricularLoad> getSortedTSDCurricularLoads() {
	List<TSDCurricularLoad> loads = new ArrayList<TSDCurricularLoad>(super.getTSDCurricularLoads());
	Collections.sort(loads, new BeanComparator("type"));
	return loads;
    }

    public Set<Degree> getDegrees() {
	Set<Degree> degrees = new HashSet<Degree>();
	for (CurricularCourse course : getAssociatedCurricularCourses()) {
	    degrees.add(course.getDegree());
	}

	return degrees;
    }
}
