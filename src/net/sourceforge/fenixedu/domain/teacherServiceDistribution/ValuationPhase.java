package net.sourceforge.fenixedu.domain.teacherServiceDistribution;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sourceforge.fenixedu.accessControl.Checked;
import net.sourceforge.fenixedu.domain.CompetenceCourse;
import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;

import org.apache.commons.beanutils.BeanComparator;

public class ValuationPhase extends ValuationPhase_Base {

    private ValuationPhase() {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
    }

    public ValuationPhase(TeacherServiceDistribution teacherServiceDistribution, String name,
	    ValuationPhase previousValuationPhase, ValuationPhase nextValuationPhase,
	    ValuationPhaseStatus status, Map<String, Object> omissionValues) {
	this();

	this.setTeacherServiceDistribution(teacherServiceDistribution);
	this.setName(name);
	this.setPreviousValuationPhase(previousValuationPhase);
	this.setNextValuationPhase(nextValuationPhase);
	this.setStatus(status);
	this.setIsPublished(false);

	this.setStudentsPerTheoreticalShift((Integer) omissionValues.get("studentsPerTheoreticalShift"));
	this.setStudentsPerPraticalShift((Integer) omissionValues.get("studentsPerPraticalShift"));
	this.setStudentsPerTheoPratShift((Integer) omissionValues.get("studentsPerTheoPratShift"));
	this.setStudentsPerLaboratorialShift((Integer) omissionValues
		.get("studentsPerLaboratorialShift"));
	this.setWeightFirstTimeEnrolledStudentsPerTheoShift((Double) omissionValues
		.get("weightFirstTimeEnrolledStudentsPerTheoShift"));
	this.setWeightFirstTimeEnrolledStudentsPerPratShift((Double) omissionValues
		.get("weightFirstTimeEnrolledStudentsPerPratShift"));
	this.setWeightFirstTimeEnrolledStudentsPerTheoPratShift((Double) omissionValues
		.get("weightFirstTimeEnrolledStudentsPerTheoPratShift"));
	this.setWeightFirstTimeEnrolledStudentsPerLabShift((Double) omissionValues
		.get("weightFirstTimeEnrolledStudentsPerLabShift"));
	this.setWeightSecondTimeEnrolledStudentsPerTheoShift((Double) omissionValues
		.get("weightSecondTimeEnrolledStudentsPerTheoShift"));
	this.setWeightSecondTimeEnrolledStudentsPerPratShift((Double) omissionValues
		.get("weightSecondTimeEnrolledStudentsPerPratShift"));
	this.setWeightSecondTimeEnrolledStudentsPerTheoPratShift((Double) omissionValues
		.get("weightSecondTimeEnrolledStudentsPerTheoPratShift"));
	this.setWeightSecondTimeEnrolledStudentsPerLabShift((Double) omissionValues
		.get("weightSecondTimeEnrolledStudentsPerLabShift"));

	createRootValuationGroupingWithTeachersAndCourses(teacherServiceDistribution);
	createSubGroupings();
    }

    private void createRootValuationGroupingWithTeachersAndCourses(
	    TeacherServiceDistribution teacherServiceDistribution) {
	List<ValuationTeacher> valuationTeacherList = createValuationTeacher(teacherServiceDistribution
		.getDepartment(), teacherServiceDistribution.getExecutionPeriods());

	new ValuationGrouping(this, teacherServiceDistribution.getDepartment().getAcronym(), null,
		valuationTeacherList, createValuationCompetenceCourses(teacherServiceDistribution),
		null, null);
    }

    private void createSubGroupings() {
	HashMap<Unit, ValuationGrouping> unitTeachersNewGrouping = new HashMap<Unit, ValuationGrouping>();
	ValuationGrouping rootGrouping = getRootValuationGrouping();

	for (ValuationTeacher valuationTeacher : rootGrouping.getValuationTeachers()) {
	    Teacher teacher = valuationTeacher.getTeacher();
	    if (teacher.getCurrentWorkingUnit() != null) {
		createChildUnitsGroupingsForTeachers(valuationTeacher, teacher.getCurrentWorkingUnit(),
			null, rootGrouping, unitTeachersNewGrouping);
	    }
	}

	HashMap<Unit, ValuationGrouping> unitCoursesNewGrouping = new HashMap<Unit, ValuationGrouping>();

	for (ValuationCompetenceCourse valuationCompetenceCourse : rootGrouping
		.getValuationCompetenceCourses()) {
	    CompetenceCourse competenceCourse = valuationCompetenceCourse.getCompetenceCourse();
	    if (competenceCourse != null && competenceCourse.getCompetenceCourseGroupUnit() != null) {
		createChildCompetenceCourseGroupUnits(valuationCompetenceCourse, rootGrouping,
			unitCoursesNewGrouping);
	    }
	}

	HashMap<Unit, ValuationGrouping> unitScientificAreaGrouping = new HashMap<Unit, ValuationGrouping>();

	for (Unit courseUnit : unitCoursesNewGrouping.keySet()) {
	    Iterator<Unit> iter = courseUnit.getParentUnits().iterator();
	    Unit scientificAreaUnit = (!iter.hasNext()) ? null : iter.next();
	    ValuationGrouping scientificAreaGrouping = unitScientificAreaGrouping
		    .get(scientificAreaUnit);
	    ValuationGrouping teachersNewGrouping = unitTeachersNewGrouping.get(scientificAreaUnit);
	    ValuationGrouping coursesNewGrouping = unitCoursesNewGrouping.get(courseUnit);

	    if (scientificAreaGrouping == null) {
		if (teachersNewGrouping == null) {
		    scientificAreaGrouping = new ValuationGrouping(this, scientificAreaUnit.getName(),
			    rootGrouping, new ArrayList<ValuationTeacher>(),
			    new ArrayList<ValuationCompetenceCourse>(), rootGrouping
				    .getCoursesAndTeachersValuationManagers(), rootGrouping
				    .getCoursesAndTeachersManagementGroup());
		} else {
		    scientificAreaGrouping = teachersNewGrouping;
		}

		unitScientificAreaGrouping.put(scientificAreaUnit, scientificAreaGrouping);
	    }

	    scientificAreaGrouping.getValuationCompetenceCoursesSet().addAll(
		    coursesNewGrouping.getValuationCompetenceCourses());
	    coursesNewGrouping.setParent(scientificAreaGrouping);
	}
    }

    private void createChildUnitsGroupingsForTeachers(ValuationTeacher valuationTeacher, Unit unit,
	    ValuationGrouping sonGrouping, ValuationGrouping rootGrouping,
	    HashMap<Unit, ValuationGrouping> unitNewGrouping) {

	if (unit.isDepartmentUnit()) {
	    if (sonGrouping != null) {
		sonGrouping.setParent(rootGrouping);
	    }
	    return;
	}

	ValuationGrouping newGrouping = unitNewGrouping.get(unit);

	if (newGrouping == null) {
	    List<ValuationTeacher> teachersList = new ArrayList<ValuationTeacher>();
	    teachersList.add(valuationTeacher);
	    List<ValuationCompetenceCourse> emptyValuationCompetenceCourseList = new ArrayList<ValuationCompetenceCourse>();

	    newGrouping = new ValuationGrouping(this, unit.getName(), null, teachersList,
		    emptyValuationCompetenceCourseList, null, null);
	    unitNewGrouping.put(unit, newGrouping);

	    if (sonGrouping != null) {
		sonGrouping.setParent(newGrouping);
	    }

	    for (Unit parentUnit : unit.getParentByOrganizationalStructureAccountabilityType()) {
		for (Unit grandParentUnit : parentUnit
			.getParentByOrganizationalStructureAccountabilityType()) {
		    if (!grandParentUnit.getParentByOrganizationalStructureAccountabilityType()
			    .isEmpty()) {
			createChildUnitsGroupingsForTeachers(valuationTeacher, parentUnit, newGrouping,
				rootGrouping, unitNewGrouping);
		    }
		}
	    }
	} else {
	    newGrouping.addValuationTeachers(valuationTeacher);

	    for (ValuationGrouping parentGrouping = newGrouping.getParent(); parentGrouping != null
		    && parentGrouping != rootGrouping; parentGrouping = parentGrouping.getParent()) {
		parentGrouping.addValuationTeachers(valuationTeacher);
	    }
	}
    }

    private void createChildCompetenceCourseGroupUnits(
	    ValuationCompetenceCourse valuationCompetenceCourse, ValuationGrouping rootGrouping,
	    HashMap<Unit, ValuationGrouping> unitNewGrouping) {

	CompetenceCourse competenceCourse = valuationCompetenceCourse.getCompetenceCourse();
	ValuationGrouping newGrouping = unitNewGrouping.get(competenceCourse
		.getCompetenceCourseGroupUnit());

	if (newGrouping == null) {
	    List<ValuationCompetenceCourse> valuationCompetenceCourseList = new ArrayList<ValuationCompetenceCourse>();
	    valuationCompetenceCourseList.add(valuationCompetenceCourse);
	    List<ValuationTeacher> emptyValuationTeacherList = new ArrayList<ValuationTeacher>();

	    newGrouping = new ValuationGrouping(this, competenceCourse.getCompetenceCourseGroupUnit()
		    .getName(), rootGrouping, emptyValuationTeacherList, valuationCompetenceCourseList,
		    null, null);
	    unitNewGrouping.put(competenceCourse.getCompetenceCourseGroupUnit(), newGrouping);

	} else {
	    newGrouping.addValuationCompetenceCourses(valuationCompetenceCourse);
	}
    }

    private List<ValuationCompetenceCourse> createValuationCompetenceCourses(
	    TeacherServiceDistribution teacherServiceDistribution) {
	List<ValuationCompetenceCourse> valuationCompetenceCourseList = new ArrayList<ValuationCompetenceCourse>();
	for (CompetenceCourse competenceCourse : teacherServiceDistribution.getAllCompetenceCourses()) {
	    ValuationCompetenceCourse valuationCompetenceCourse = competenceCourse
		    .getValuationCompetenceCourse();
	    if (valuationCompetenceCourse == null) {
		valuationCompetenceCourse = new ValuationCompetenceCourse(competenceCourse);
	    }

	    valuationCompetenceCourseList.add(valuationCompetenceCourse);
	}

	return valuationCompetenceCourseList;
    }

    private List<ValuationTeacher> createValuationTeacher(Department department,
	    List<ExecutionPeriod> executionPeriodList) {
	List<ValuationTeacher> valuationTeacherList = new ArrayList<ValuationTeacher>();
	Set<Teacher> teacherSet = new HashSet<Teacher>();

	for (ExecutionPeriod executionPeriod : executionPeriodList) {
	    teacherSet.addAll(department.getAllTeachers(executionPeriod.getBeginDateYearMonthDay(),
		    executionPeriod.getEndDateYearMonthDay()));
	}

	for (Teacher teacher : teacherSet) {
	    valuationTeacherList.add(new ValuationTeacher(teacher));
	}

	return valuationTeacherList;
    }

    public ValuationPhase getCurrentValuationPhase() {
	for (ValuationPhase valuationPhase = getFirstValuationPhase(); valuationPhase != null; valuationPhase = valuationPhase
		.getNextValuationPhase()) {
	    if (valuationPhase.getStatus() == ValuationPhaseStatus.CURRENT) {
		return valuationPhase;
	    }
	}

	return null;
    }

    public ValuationPhase getFirstValuationPhase() {
	ValuationPhase valuationPhase = this;

	while (valuationPhase.getPreviousValuationPhase() != null) {
	    valuationPhase = valuationPhase.getPreviousValuationPhase();
	}

	return valuationPhase;
    }

    public ValuationPhase getLastValuationPhase() {
	ValuationPhase valuationPhase = this;

	while (valuationPhase.getNextValuationPhase() != null) {
	    valuationPhase = valuationPhase.getNextValuationPhase();
	}

	return valuationPhase;
    }

    public Integer getNumberOfValuationGroupings() {
	return getGroupings().size();
    }

    public Boolean getIsPrevious(ValuationPhase valuationPhase) {
	if (valuationPhase == this) {
	    return false;
	}

	for (; (valuationPhase != null); valuationPhase = valuationPhase.getNextValuationPhase()) {
	    if (valuationPhase.getNextValuationPhase() == this) {
		return false;
	    }
	}

	return true;
    }

    @Checked("ValuationPhasePredicates.writePredicate")
    public void setCurrent() {
	if (getStatus() == ValuationPhaseStatus.OPEN) {
	    getCurrentValuationPhase().setStatus(ValuationPhaseStatus.OPEN);
	    setStatus(ValuationPhaseStatus.CURRENT);
	}
    }

    @Checked("ValuationPhasePredicates.writePredicate")
    public void setClosed() {
	if (getStatus() == ValuationPhaseStatus.OPEN) {
	    setStatus(ValuationPhaseStatus.CLOSED);
	}
    }

    @Checked("ValuationPhasePredicates.writePredicate")
    public void setOpen() {
	if (getStatus() == ValuationPhaseStatus.CLOSED) {
	    setStatus(ValuationPhaseStatus.OPEN);
	}
    }

    public ValuationGrouping getRootValuationGrouping() {
	return getGroupings().get(0).getRootValuationGrouping();
    }

    @Checked("ValuationPhasePredicates.writePredicate")
    public void delete() {
	for (ValuationGrouping valuationGrouping : getGroupings()) {
	    removeGroupings(valuationGrouping);
	}
	for (CourseValuation courseValuation : getCourseValuations()) {
	    removeCourseValuations(courseValuation);
	}
	removeTeacherServiceDistribution();
	super.deleteDomainObject();
    }

    @Checked("ValuationPhasePredicates.writePredicate")
    public void deleteValuationPhaseData() {
	deleteCourseValuations();
	deleteValuationGroupings();
    }

    private void deleteValuationGroupings() {
	for (ValuationGrouping valuationGrouping : getGroupings()) {
	    for (ValuationTeacher valuationTeacher : valuationGrouping.getValuationTeachers()) {
		valuationTeacher.delete();
	    }
	    for (ValuationCompetenceCourse valuationCompetenceCourse : valuationGrouping
		    .getValuationCompetenceCourses()) {
		if (!valuationCompetenceCourse.getIsRealCompetenceCourse()) {
		    valuationCompetenceCourse.delete();
		}
	    }

	    valuationGrouping.delete();
	}
    }

    private void deleteCourseValuations() {
	for (CourseValuation courseValuation : getCourseValuations()) {
	    for (ProfessorshipValuation professorshipValuation : courseValuation
		    .getProfessorshipValuations()) {
		professorshipValuation.delete();
	    }
	    courseValuation.delete();
	}
    }

    @Checked("ValuationPhasePredicates.writePredicate")
    public void deleteDataAndPhase() {
	deleteValuationPhaseData();
	delete();
    }

    private void copyDataFromValuationPhase(ValuationPhase oldValuationPhase,
	    Map<ExecutionPeriod, ExecutionPeriod> oldAndNewExecutionPeriodMap) {
	deleteValuationPhaseData();

	Map<ValuationCompetenceCourse, ValuationCompetenceCourse> oldAndNewValuationCompetenceCourseMap = createAndCopyFromValuationCompetenceCourseList(
		oldValuationPhase.getRootValuationGrouping().getValuationCompetenceCourses(),
		new ArrayList<ExecutionPeriod>(oldAndNewExecutionPeriodMap.values()));

	Map<ValuationCurricularCourse, ValuationCurricularCourse> oldAndNewValuationCurricularCourseMap = createAndCopyFromValuationCurricularCourses(oldAndNewValuationCompetenceCourseMap);

	Map<ValuationTeacher, ValuationTeacher> oldAndNewValuationTeacherMap = createAndCopyFromValuationTeacherList(oldValuationPhase
		.getRootValuationGrouping().getValuationTeachers());

	ValuationGrouping.createAndCopyFromValuationGrouping(this, oldValuationPhase
		.getRootValuationGrouping(), null, oldAndNewValuationTeacherMap,
		oldAndNewValuationCompetenceCourseMap);

	createAndCopyCompetenceCourseValuationsAndProfessorshipValuations(oldValuationPhase,
		oldAndNewValuationCompetenceCourseMap, oldAndNewValuationTeacherMap,
		oldAndNewExecutionPeriodMap);

	Map<CurricularCourseValuation, CurricularCourseValuation> oldAndNewCurricularCourseValuationMap = createAndCopyCurricularCourseValuationsAndProfessorshipValuations(
		oldValuationPhase, oldAndNewValuationCurricularCourseMap, oldAndNewValuationTeacherMap,
		oldAndNewExecutionPeriodMap);

	createAndCopyCurricularCourseValuationGroupsAndProfessorshipValuations(oldValuationPhase,
		oldAndNewValuationCompetenceCourseMap, oldAndNewValuationTeacherMap,
		oldAndNewExecutionPeriodMap, oldAndNewCurricularCourseValuationMap);

	this.setStudentsPerTheoreticalShift(oldValuationPhase.getStudentsPerTheoreticalShift());
	this.setStudentsPerPraticalShift(oldValuationPhase.getStudentsPerPraticalShift());
	this.setStudentsPerTheoPratShift(oldValuationPhase.getStudentsPerTheoPratShift());
	this.setStudentsPerLaboratorialShift(oldValuationPhase.getStudentsPerLaboratorialShift());
	this.setWeightFirstTimeEnrolledStudentsPerTheoShift(oldValuationPhase
		.getWeightFirstTimeEnrolledStudentsPerTheoShift());
	this.setWeightFirstTimeEnrolledStudentsPerPratShift(oldValuationPhase
		.getWeightFirstTimeEnrolledStudentsPerPratShift());
	this.setWeightFirstTimeEnrolledStudentsPerTheoPratShift(oldValuationPhase
		.getWeightFirstTimeEnrolledStudentsPerTheoPratShift());
	this.setWeightFirstTimeEnrolledStudentsPerLabShift(oldValuationPhase
		.getWeightFirstTimeEnrolledStudentsPerLabShift());
	this.setWeightSecondTimeEnrolledStudentsPerTheoShift(oldValuationPhase
		.getWeightSecondTimeEnrolledStudentsPerTheoShift());
	this.setWeightSecondTimeEnrolledStudentsPerPratShift(oldValuationPhase
		.getWeightSecondTimeEnrolledStudentsPerPratShift());
	this.setWeightSecondTimeEnrolledStudentsPerTheoPratShift(oldValuationPhase
		.getWeightSecondTimeEnrolledStudentsPerTheoPratShift());
	this.setWeightSecondTimeEnrolledStudentsPerLabShift(oldValuationPhase
		.getWeightSecondTimeEnrolledStudentsPerLabShift());

    }

    public void copyDataFromValuationPhase(ValuationPhase oldValuationPhase) {
	Map<ExecutionPeriod, ExecutionPeriod> oldAndNewExecutionPeriodMap = TeacherServiceDistribution
		.getExecutionPeriodTranslationMap(oldValuationPhase.getTeacherServiceDistribution()
			.getExecutionPeriods(), this.getTeacherServiceDistribution()
			.getExecutionPeriods());

	copyDataFromValuationPhase(oldValuationPhase, oldAndNewExecutionPeriodMap);
    }

    private Map<ValuationCurricularCourse, ValuationCurricularCourse> createAndCopyFromValuationCurricularCourses(
	    Map<ValuationCompetenceCourse, ValuationCompetenceCourse> oldAndNewValuationCompetenceCourseMap) {
	Map<ValuationCurricularCourse, ValuationCurricularCourse> oldAndNewValuationCurricularCourseMap = new HashMap<ValuationCurricularCourse, ValuationCurricularCourse>();

	for (ValuationCompetenceCourse valuationCompetenceCourse : oldAndNewValuationCompetenceCourseMap
		.keySet()) {
	    for (ValuationCurricularCourse valuationCurricularCourse : valuationCompetenceCourse
		    .getValuationCurricularCourses(getTeacherServiceDistribution().getExecutionPeriods())) {
		if (valuationCurricularCourse.getIsRealCurricularCourse()) {
		    oldAndNewValuationCurricularCourseMap.put(valuationCurricularCourse,
			    valuationCurricularCourse);
		} else {
		    ValuationCurricularCourse newValuationCurricularCourse = new ValuationCurricularCourse(
			    oldAndNewValuationCompetenceCourseMap.get(valuationCompetenceCourse),
			    valuationCurricularCourse.getCurricularYears(), valuationCurricularCourse
				    .getDegreeCurricularPlan(), valuationCurricularCourse
				    .getExecutionPeriod(), valuationCurricularCourse.getAcronym(),
			    valuationCurricularCourse.getTheoreticalHours(), valuationCurricularCourse
				    .getPraticalHours(), valuationCurricularCourse.getTheoPratHours(),
			    valuationCurricularCourse.getLaboratorialHours());
		}
	    }
	}

	return oldAndNewValuationCurricularCourseMap;
    }

    private Map<ExecutionPeriod, ExecutionPeriod> getSimpleExecutionPeriodsTranslationMap(
	    ValuationPhase oldValuationPhase) {
	Map<ExecutionPeriod, ExecutionPeriod> oldAndNewExecutionPeriodMap = new HashMap<ExecutionPeriod, ExecutionPeriod>();
	for (ExecutionPeriod executionPeriod : oldValuationPhase.getTeacherServiceDistribution()
		.getExecutionPeriods()) {
	    oldAndNewExecutionPeriodMap.put(executionPeriod, executionPeriod);
	}
	return oldAndNewExecutionPeriodMap;
    }

    private void createAndCopyCompetenceCourseValuationsAndProfessorshipValuations(
	    ValuationPhase oldValuationPhase,
	    Map<ValuationCompetenceCourse, ValuationCompetenceCourse> oldAndNewValuationCompetenceCourseMap,
	    Map<ValuationTeacher, ValuationTeacher> oldAndNewValuationTeacherMap,
	    Map<ExecutionPeriod, ExecutionPeriod> oldAndNewExecutionPeriodMap) {
	for (ValuationCompetenceCourse oldValuationCompetenceCourse : oldAndNewValuationCompetenceCourseMap
		.keySet()) {
	    for (ExecutionPeriod oldExecutionPeriod : oldAndNewExecutionPeriodMap.keySet()) {
		CompetenceCourseValuation oldCompetenceCourseValuation = oldValuationCompetenceCourse
			.getCompetenceCourseValuationByValuationPhaseAndExecutionPeriod(
				oldValuationPhase, oldExecutionPeriod);
		ExecutionPeriod newExecutionPeriod = oldAndNewExecutionPeriodMap.get(oldExecutionPeriod);
		ValuationCompetenceCourse newValuationCompetenceCourse = oldAndNewValuationCompetenceCourseMap
			.get(oldValuationCompetenceCourse);

		if (oldCompetenceCourseValuation != null && newExecutionPeriod != null
			&& newValuationCompetenceCourse != null) {
		    CompetenceCourseValuation newCompetenceCourseValuation = CompetenceCourseValuation
			    .createAndCopyFromCompetenceCourseValuation(this,
				    newValuationCompetenceCourse, newExecutionPeriod,
				    oldCompetenceCourseValuation);

		    createAndCopyProfessorshipValuations(oldAndNewValuationTeacherMap,
			    oldCompetenceCourseValuation, newCompetenceCourseValuation);
		}
	    }
	}
    }

    private Map<CurricularCourseValuation, CurricularCourseValuation> createAndCopyCurricularCourseValuationsAndProfessorshipValuations(
	    ValuationPhase oldValuationPhase,
	    Map<ValuationCurricularCourse, ValuationCurricularCourse> oldAndNewValuationCurricularCourseMap,
	    Map<ValuationTeacher, ValuationTeacher> oldAndNewValuationTeacherMap,
	    Map<ExecutionPeriod, ExecutionPeriod> oldAndNewExecutionPeriodMap) {

	Map<CurricularCourseValuation, CurricularCourseValuation> oldAndNewCurricularCourseValuationMap = new HashMap<CurricularCourseValuation, CurricularCourseValuation>();

	for (ValuationCurricularCourse oldValuationCurricularCourse : oldAndNewValuationCurricularCourseMap
		.keySet()) {
	    for (ExecutionPeriod oldExecutionPeriod : oldAndNewExecutionPeriodMap.keySet()) {
		CurricularCourseValuation oldCurricularCourseValuation = oldValuationCurricularCourse
			.getCurricularCourseValuationByValuationPhaseAndExecutionPeriod(
				oldValuationPhase, oldExecutionPeriod);
		ExecutionPeriod newExecutionPeriod = oldAndNewExecutionPeriodMap.get(oldExecutionPeriod);
		ValuationCurricularCourse newValuationCurricularCourse = oldAndNewValuationCurricularCourseMap
			.get(oldValuationCurricularCourse);

		if (oldCurricularCourseValuation != null) {
		    CurricularCourseValuation newCurricularCourseValuation = null;
		    if (newExecutionPeriod != null && newValuationCurricularCourse != null) {
			newCurricularCourseValuation = CurricularCourseValuation
				.createAndCopyFromCurricularCourseValuation(this,
					newValuationCurricularCourse, newExecutionPeriod,
					oldCurricularCourseValuation);
			oldAndNewCurricularCourseValuationMap.put(oldCurricularCourseValuation,
				newCurricularCourseValuation);
		    }
		    createAndCopyProfessorshipValuations(oldAndNewValuationTeacherMap,
			    oldCurricularCourseValuation, newCurricularCourseValuation);
		}
	    }
	}

	return oldAndNewCurricularCourseValuationMap;
    }

    private void createAndCopyCurricularCourseValuationGroupsAndProfessorshipValuations(
	    ValuationPhase oldValuationPhase,
	    Map<ValuationCompetenceCourse, ValuationCompetenceCourse> oldAndNewValuationCompetenceCourseMap,
	    Map<ValuationTeacher, ValuationTeacher> oldAndNewValuationTeacherMap,
	    Map<ExecutionPeriod, ExecutionPeriod> oldAndNewExecutionPeriodMap,
	    Map<CurricularCourseValuation, CurricularCourseValuation> oldAndNewCurricularCourseValuationMap) {

	for (ValuationCompetenceCourse oldValuationCompetenceCourse : oldAndNewValuationCompetenceCourseMap
		.keySet()) {
	    for (ExecutionPeriod oldExecutionPeriod : oldAndNewExecutionPeriodMap.keySet()) {
		for (CurricularCourseValuationGroup oldCurricularCourseValuationGroup : oldValuationCompetenceCourse
			.getCurricularCourseValuationGroupsByValuationPhaseAndExecutionPeriod(
				oldValuationPhase, oldExecutionPeriod)) {
		    List<CurricularCourseValuation> newAssociatedCurricularCourseValuations = getNewAssociatedCurricularCourseValuation(
			    oldCurricularCourseValuationGroup.getCurricularCourseValuations(),
			    oldAndNewCurricularCourseValuationMap);

		    if (!newAssociatedCurricularCourseValuations.isEmpty()
			    && oldAndNewExecutionPeriodMap.get(oldExecutionPeriod) != null) {
			CurricularCourseValuationGroup newCurricularCourseValuationGroup = CurricularCourseValuationGroup
				.createAndCopyFromCurricularCoursevValuationGroup(
					newAssociatedCurricularCourseValuations,
					oldCurricularCourseValuationGroup);
			createAndCopyProfessorshipValuations(oldAndNewValuationTeacherMap,
				oldCurricularCourseValuationGroup, newCurricularCourseValuationGroup);
		    }
		}
	    }
	}
    }

    private void createAndCopyProfessorshipValuations(
	    Map<ValuationTeacher, ValuationTeacher> oldAndNewValuationTeacherMap,
	    CourseValuation oldCourseValuation, CourseValuation newCourseValuation) {
	for (ProfessorshipValuation oldProfessorshipValuation : oldCourseValuation
		.getProfessorshipValuations()) {
	    ValuationTeacher newValuationTeacher = oldAndNewValuationTeacherMap
		    .get(oldProfessorshipValuation.getValuationTeacher());

	    if (newValuationTeacher != null) {
		ProfessorshipValuation.createAndCopy(newCourseValuation, newValuationTeacher,
			oldProfessorshipValuation);
	    }
	}
    }

    private List<CurricularCourseValuation> getNewAssociatedCurricularCourseValuation(
	    List<CurricularCourseValuation> oldCurricularCourseValuations,
	    Map<CurricularCourseValuation, CurricularCourseValuation> oldAndNewCurricularCourseValuationMap) {
	List<CurricularCourseValuation> newCurricularCourseValuationList = new ArrayList<CurricularCourseValuation>();

	for (CurricularCourseValuation oldCurricularCourseValuation : oldCurricularCourseValuations) {
	    CurricularCourseValuation newCurricularCourseValuation = oldAndNewCurricularCourseValuationMap
		    .get(oldCurricularCourseValuation);

	    if (newCurricularCourseValuation != null)
		newCurricularCourseValuationList.add(newCurricularCourseValuation);
	}

	return newCurricularCourseValuationList;
    }

    private Map<ValuationCompetenceCourse, ValuationCompetenceCourse> createAndCopyFromValuationCompetenceCourseList(
	    List<ValuationCompetenceCourse> valuationCompetenceCourseList,
	    List<ExecutionPeriod> executionPeriodList) {
	Map<ValuationCompetenceCourse, ValuationCompetenceCourse> oldAndNewValuationCompetenceCourseMap = new HashMap<ValuationCompetenceCourse, ValuationCompetenceCourse>();

	for (ValuationCompetenceCourse valuationCompetenceCourse : valuationCompetenceCourseList) {
	    if (valuationCompetenceCourse.getIsRealCompetenceCourse()) {
		if (valuationCompetenceCourse
			.hasActiveValuationCurricularCoursesInExecutionPeriods(executionPeriodList)) {
		    oldAndNewValuationCompetenceCourseMap.put(valuationCompetenceCourse,
			    valuationCompetenceCourse);
		}
	    } else {
		ValuationCompetenceCourse newValuationCompetenceCourse = new ValuationCompetenceCourse(
			valuationCompetenceCourse.getName());
		oldAndNewValuationCompetenceCourseMap.put(valuationCompetenceCourse,
			newValuationCompetenceCourse);
	    }
	}

	return oldAndNewValuationCompetenceCourseMap;
    }

    private Map<ValuationTeacher, ValuationTeacher> createAndCopyFromValuationTeacherList(
	    List<ValuationTeacher> valuationTeacherList) {
	Map<ValuationTeacher, ValuationTeacher> oldAndNewValuationTeacherMap = new HashMap<ValuationTeacher, ValuationTeacher>();

	Department thisDepartment = getTeacherServiceDistribution().getDepartment();

	for (ValuationTeacher valuationTeacher : valuationTeacherList) {
	    ValuationTeacher newValuationTeacher = null;
	    if (valuationTeacher.getIsRealTeacher()
		    && thisDepartment == valuationTeacher.getTeacher().getLastWorkingDepartment(
			    getTeacherServiceDistribution().getFirstExecutionPeriod()
				    .getBeginDateYearMonthDay(),
			    getTeacherServiceDistribution().getFirstExecutionPeriod()
				    .getEndDateYearMonthDay())) {
		newValuationTeacher = new ValuationTeacher(valuationTeacher.getTeacher());

	    } else if (!valuationTeacher.getIsRealTeacher()) {
		newValuationTeacher = new ValuationTeacher(valuationTeacher.getCategory(),
			valuationTeacher.getName(), valuationTeacher.getRequiredHours());
	    }
	    oldAndNewValuationTeacherMap.put(valuationTeacher, newValuationTeacher);
	}

	return oldAndNewValuationTeacherMap;
    }

    public static ValuationPhase createAndCopy(TeacherServiceDistribution newTeacherServiceDistribution,
	    ValuationPhase oldValuationPhase,
	    Map<ExecutionPeriod, ExecutionPeriod> oldAndNewExecutionPeriodMap,
	    ValuationPhase lastValuationPhase) {
	ValuationPhase newValuationPhase = new ValuationPhase();
	newValuationPhase.setTeacherServiceDistribution(newTeacherServiceDistribution);
	newValuationPhase.setPreviousValuationPhase(lastValuationPhase);
	if (lastValuationPhase != null)
	    lastValuationPhase.setNextValuationPhase(newValuationPhase);
	newValuationPhase.setNextValuationPhase(null);

	newValuationPhase.setStatus(oldValuationPhase.getStatus());
	newValuationPhase.setName(oldValuationPhase.getName());
	newValuationPhase.setTeacherServiceDistribution(newTeacherServiceDistribution);
	newValuationPhase.setIsPublished(false);

	newValuationPhase.copyDataFromValuationPhase(oldValuationPhase, oldAndNewExecutionPeriodMap);

	return newValuationPhase;
    }

    public List<ValuationGrouping> getValuationGroupingsOrderedByName() {
	List<ValuationGrouping> orderedGrouping = new ArrayList<ValuationGrouping>(getGroupings());

	Collections.sort(orderedGrouping, new BeanComparator("name"));

	return orderedGrouping;
    }

    @Override
    @Checked("ValuationPhasePredicates.writePredicate")
    public void setIsPublished(Boolean isPublished) {
	// TODO Auto-generated method stub
	super.setIsPublished(isPublished);
    }

    @Override
    @Checked("ValuationPhasePredicates.writePredicate")
    public void setKeyNextValuationPhase(Integer keyNextValuationPhase) {

	super.setKeyNextValuationPhase(keyNextValuationPhase);
    }

    @Override
    @Checked("ValuationPhasePredicates.writePredicate")
    public void setKeyPreviousValuationPhase(Integer keyPreviousValuationPhase) {

	super.setKeyPreviousValuationPhase(keyPreviousValuationPhase);
    }

    @Override
    @Checked("ValuationPhasePredicates.writePredicate")
    public void setKeyTeacherServiceDistribution(Integer keyTeacherServiceDistribution) {

	super.setKeyTeacherServiceDistribution(keyTeacherServiceDistribution);
    }

    @Override
    @Checked("ValuationPhasePredicates.writePredicate")
    public void setName(String name) {

	super.setName(name);
    }

    @Override
    @Checked("ValuationPhasePredicates.writePredicate")
    public void setNextValuationPhase(ValuationPhase nextValuationPhase) {

	super.setNextValuationPhase(nextValuationPhase);
    }

    @Override
    @Checked("ValuationPhasePredicates.writePredicate")
    public void setPreviousValuationPhase(ValuationPhase previousValuationPhase) {

	super.setPreviousValuationPhase(previousValuationPhase);
    }

    @Override
    @Checked("ValuationPhasePredicates.writePredicate")
    public void setStatus(ValuationPhaseStatus status) {

	super.setStatus(status);
    }

    @Override
    @Checked("ValuationPhasePredicates.writePredicate")
    public void setStudentsPerLaboratorialShift(Integer studentsPerLaboratorialShift) {

	super.setStudentsPerLaboratorialShift(studentsPerLaboratorialShift);
    }

    @Override
    @Checked("ValuationPhasePredicates.writePredicate")
    public void setStudentsPerPraticalShift(Integer studentsPerPraticalShift) {

	super.setStudentsPerPraticalShift(studentsPerPraticalShift);
    }

    @Override
    @Checked("ValuationPhasePredicates.writePredicate")
    public void setStudentsPerTheoPratShift(Integer studentsPerTheoPratShift) {

	super.setStudentsPerTheoPratShift(studentsPerTheoPratShift);
    }

    @Override
    @Checked("ValuationPhasePredicates.writePredicate")
    public void setStudentsPerTheoreticalShift(Integer studentsPerTheoreticalShift) {

	super.setStudentsPerTheoreticalShift(studentsPerTheoreticalShift);
    }

    @Override
    @Checked("ValuationPhasePredicates.writePredicate")
    public void setWeightFirstTimeEnrolledStudentsPerLabShift(
	    Double weightFirstTimeEnrolledStudentsPerLabShift) {

	super.setWeightFirstTimeEnrolledStudentsPerLabShift(weightFirstTimeEnrolledStudentsPerLabShift);
    }

    @Override
    @Checked("ValuationPhasePredicates.writePredicate")
    public void setWeightFirstTimeEnrolledStudentsPerPratShift(
	    Double weightFirstTimeEnrolledStudentsPerPratShift) {

	super
		.setWeightFirstTimeEnrolledStudentsPerPratShift(weightFirstTimeEnrolledStudentsPerPratShift);
    }

    @Override
    @Checked("ValuationPhasePredicates.writePredicate")
    public void setWeightFirstTimeEnrolledStudentsPerTheoPratShift(
	    Double weightFirstTimeEnrolledStudentsPerTheoPratShift) {

	super
		.setWeightFirstTimeEnrolledStudentsPerTheoPratShift(weightFirstTimeEnrolledStudentsPerTheoPratShift);
    }

    @Override
    @Checked("ValuationPhasePredicates.writePredicate")
    public void setWeightFirstTimeEnrolledStudentsPerTheoShift(
	    Double weightFirstTimeEnrolledStudentsPerTheoShift) {

	super
		.setWeightFirstTimeEnrolledStudentsPerTheoShift(weightFirstTimeEnrolledStudentsPerTheoShift);
    }

    @Override
    @Checked("ValuationPhasePredicates.writePredicate")
    public void setWeightSecondTimeEnrolledStudentsPerLabShift(
	    Double weightSecondTimeEnrolledStudentsPerLabShift) {

	super
		.setWeightSecondTimeEnrolledStudentsPerLabShift(weightSecondTimeEnrolledStudentsPerLabShift);
    }

    @Override
    @Checked("ValuationPhasePredicates.writePredicate")
    public void setWeightSecondTimeEnrolledStudentsPerPratShift(
	    Double weightSecondTimeEnrolledStudentsPerPratShift) {

	super
		.setWeightSecondTimeEnrolledStudentsPerPratShift(weightSecondTimeEnrolledStudentsPerPratShift);
    }

    @Override
    @Checked("ValuationPhasePredicates.writePredicate")
    public void setWeightSecondTimeEnrolledStudentsPerTheoPratShift(
	    Double weightSecondTimeEnrolledStudentsPerTheoPratShift) {

	super
		.setWeightSecondTimeEnrolledStudentsPerTheoPratShift(weightSecondTimeEnrolledStudentsPerTheoPratShift);
    }

    @Override
    @Checked("ValuationPhasePredicates.writePredicate")
    public void setWeightSecondTimeEnrolledStudentsPerTheoShift(
	    Double weightSecondTimeEnrolledStudentsPerTheoShift) {

	super
		.setWeightSecondTimeEnrolledStudentsPerTheoShift(weightSecondTimeEnrolledStudentsPerTheoShift);
    }
}
