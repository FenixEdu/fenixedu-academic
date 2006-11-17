package net.sourceforge.fenixedu.domain.teacherServiceDistribution;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sourceforge.fenixedu.commons.CollectionUtils;
import net.sourceforge.fenixedu.domain.CompetenceCourse;
import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.accessControl.Group;
import net.sourceforge.fenixedu.domain.accessControl.GroupDifference;
import net.sourceforge.fenixedu.domain.accessControl.GroupUnion;
import net.sourceforge.fenixedu.domain.accessControl.PersonGroup;

import org.apache.commons.collections.Predicate;

public class ValuationGrouping extends ValuationGrouping_Base {

	private ValuationGrouping() {
		super();
		setRootDomainObject(RootDomainObject.getInstance());
	}

	public ValuationGrouping(
			ValuationPhase valuationPhase,
			String name,
			ValuationGrouping parent,
			List<ValuationTeacher> valuationTeacherList,
			List<ValuationCompetenceCourse> valuationCompetenceCourseList,
			Group coursesAndTeachersValuationGroup,
			Group coursesAndTeachersManagementGroup) {
		this();

		if (valuationPhase == null) {
			throw new NullPointerException();
		}

		this.setValuationPhase(valuationPhase);
		this.setName(name);
		this.setParent(parent);
		this.getValuationCompetenceCourses().addAll(valuationCompetenceCourseList);
		this.getValuationTeachers().addAll(valuationTeacherList);
		this.setCoursesAndTeachersValuationManagers(coursesAndTeachersValuationGroup);
		this.setCoursesAndTeachersManagementGroup(coursesAndTeachersManagementGroup);
	}

	public Boolean getIsRoot() {
		return getParent() == null;
	}

	public ValuationGrouping getRootValuationGrouping() {
		if (getIsRoot()) {
			return this;
		} else {
			return getParent().getRootValuationGrouping();
		}
	}

	public void delete() {
		for (ValuationCompetenceCourse valuationCompetenceCourse : getValuationCompetenceCourses()) {
			removeValuationCompetenceCourses(valuationCompetenceCourse);
		}
		for (ValuationTeacher teacher : getValuationTeachers()) {
			removeValuationTeachers(teacher);
		}

		removeParent();
		removeValuationPhase();
		removeRootDomainObject();
		super.deleteDomainObject();
	}

	public List<CompetenceCourse> getCompetenceCoursesByExecutionPeriods(ExecutionPeriod executionPeriod) {
		List<CompetenceCourse> competenceCourseList = new ArrayList<CompetenceCourse>();

		for (ValuationCompetenceCourse valuationCompetenceCourse : getValuationCompetenceCoursesByExecutionPeriod(executionPeriod)) {
			if (valuationCompetenceCourse.getIsRealCompetenceCourse()) {
				competenceCourseList.add(valuationCompetenceCourse.getCompetenceCourse());
			}
		}
		return competenceCourseList;
	}

	@SuppressWarnings("unchecked")
	public List<ValuationCompetenceCourse> getValuationCompetenceCoursesByExecutionPeriod(
			final ExecutionPeriod executionPeriod) {
		List<ValuationCompetenceCourse> valuationCompetenceCourseList = new ArrayList<ValuationCompetenceCourse>();

		valuationCompetenceCourseList.addAll(CollectionUtils.select(getValuationCompetenceCourses(), new Predicate() {
			public boolean evaluate(Object arg0) {
				ValuationCompetenceCourse valuationCompetenceCourse = (ValuationCompetenceCourse) arg0;

				return valuationCompetenceCourse.hasActiveValuationCurricularCourses(executionPeriod);
			}
		}));

		return valuationCompetenceCourseList;
	}

	public ValuationTeacher getValuationTeacherByTeacher(final Teacher teacher) {
		return (ValuationTeacher) CollectionUtils.find(getValuationTeachers(), new Predicate() {

			public boolean evaluate(Object arg0) {
				ValuationTeacher valuationTeacher = (ValuationTeacher) arg0;

				return valuationTeacher.getTeacher() == teacher;
			}
		});
	}

	public static void createAndCopyFromValuationGrouping(
			ValuationPhase valuationPhase,
			ValuationGrouping valuationGrouping,
			ValuationGrouping fatherGrouping,
			Map<ValuationTeacher, ValuationTeacher> oldAndNewValuationTeacherMap,
			Map<ValuationCompetenceCourse, ValuationCompetenceCourse> oldAndNewValuationCompetenceCourseMap) {
		List<ValuationTeacher> newValuationTeacherList = getNewValuationTeacherListFromMap(
				valuationGrouping.getValuationTeachers(),
				oldAndNewValuationTeacherMap);
		List<ValuationCompetenceCourse> newValuationCompetenceCourse = getNewValuationCompetenceCourseListFromMap(
				valuationGrouping.getValuationCompetenceCourses(),
				oldAndNewValuationCompetenceCourseMap);

		ValuationGrouping newValuationGrouping = new ValuationGrouping(
				valuationPhase,
				valuationGrouping.getName(),
				fatherGrouping,
				newValuationTeacherList,
				newValuationCompetenceCourse,
				valuationGrouping.getCoursesAndTeachersValuationManagers(),
				valuationGrouping.getCoursesAndTeachersManagementGroup());

		for (ValuationGrouping grouping : valuationGrouping.getChilds()) {
			createAndCopyFromValuationGrouping(
					valuationPhase,
					grouping,
					newValuationGrouping,
					oldAndNewValuationTeacherMap,
					oldAndNewValuationCompetenceCourseMap);
		}
	}

	private static List<ValuationCompetenceCourse> getNewValuationCompetenceCourseListFromMap(
			List<ValuationCompetenceCourse> valuationCompetenceCourses,
			Map<ValuationCompetenceCourse, ValuationCompetenceCourse> oldAndNewValuationCompetenceCourseMap) {
		List<ValuationCompetenceCourse> newValuationCompetenceCourseList = new ArrayList<ValuationCompetenceCourse>();

		for (ValuationCompetenceCourse valuationCompetenceCourse : valuationCompetenceCourses) {
			if (oldAndNewValuationCompetenceCourseMap.get(valuationCompetenceCourse) != null)
				newValuationCompetenceCourseList.add(oldAndNewValuationCompetenceCourseMap.get(valuationCompetenceCourse));
		}

		return newValuationCompetenceCourseList;
	}

	private static List<ValuationTeacher> getNewValuationTeacherListFromMap(
			List<ValuationTeacher> valuationTeachers,
			Map<ValuationTeacher, ValuationTeacher> oldAndNewValuationTeacherMap) {
		List<ValuationTeacher> newValuationTeacherList = new ArrayList<ValuationTeacher>();

		for (ValuationTeacher valuationTeacher : valuationTeachers) {
			if (oldAndNewValuationTeacherMap.get(valuationTeacher) != null)
				newValuationTeacherList.add(oldAndNewValuationTeacherMap.get(valuationTeacher));
		}

		return newValuationTeacherList;
	}

	public void removeValuationTeacherFromAllChilds(ValuationTeacher valuationTeacher) {
		removeValuationTeachers(valuationTeacher);
		for (ValuationGrouping childGrouping : getChilds()) {
			childGrouping.removeValuationTeacherFromAllChilds(valuationTeacher);
		}

	}

	public ValuationCompetenceCourse getValuationCompetenceCourseByCompetenceCourse(final CompetenceCourse course) {
		return (ValuationCompetenceCourse) CollectionUtils.find(getValuationCompetenceCourses(), new Predicate() {
			public boolean evaluate(Object arg0) {
				ValuationCompetenceCourse valuationCompetenceCourse = (ValuationCompetenceCourse) arg0;

				return course == valuationCompetenceCourse.getCompetenceCourse();
			}
		});
	}

	public void removeValuationCompetenceCourseFromAllChilds(ValuationCompetenceCourse course) {
		removeValuationCompetenceCourses(course);
		for (ValuationGrouping childGrouping : getChilds()) {
			childGrouping.removeValuationCompetenceCourseFromAllChilds(course);
		}
	}

	public List<ValuationCompetenceCourse> getGhostValuationCompetenceCourses() {
		return (List<ValuationCompetenceCourse>) CollectionUtils.select(
				getValuationCompetenceCourses(),
				new Predicate() {
					public boolean evaluate(Object arg0) {
						ValuationCompetenceCourse valuationCompetenceCourse = (ValuationCompetenceCourse) arg0;

						return !valuationCompetenceCourse.getIsRealCompetenceCourse();
					}

				});
	}

	public void mergeWithGrouping(ValuationGrouping peerGrouping) {

		if (peerGrouping == this) {
			return;
		}

		Set<ValuationTeacher> mergedValuationTeachers = new HashSet<ValuationTeacher>(this.getValuationTeachers());
		mergedValuationTeachers.addAll(peerGrouping.getValuationTeachers());

		Set<ValuationCompetenceCourse> mergedValuationCourses = new HashSet<ValuationCompetenceCourse>(
				this.getValuationCompetenceCourses());
		mergedValuationCourses.addAll(peerGrouping.getValuationCompetenceCourses());

		this.getValuationCompetenceCoursesSet().addAll(mergedValuationCourses);
		this.getValuationTeachersSet().addAll(mergedValuationTeachers);

		if(!(getParent().getValuationTeachers().containsAll(this.getValuationTeachers()) && 
				getParent().getValuationCompetenceCourses().containsAll(this.getValuationCompetenceCourses()))){
			this.setParent(getRootValuationGrouping());
		}
	}

	public List<Teacher> getDepartmentTeachersNotInGrouping(Department department) {
		TeacherServiceDistribution distribution = this.getValuationPhase().getTeacherServiceDistribution();
		List<Teacher> departmentTeachers = department.getAllTeachers(
				distribution.getFirstExecutionPeriod().getBeginDateYearMonthDay(),
				distribution.getLastExecutionPeriod().getEndDateYearMonthDay());

		List<Teacher> teachersList = new ArrayList<Teacher>();
		for (Teacher departmentTeacher : departmentTeachers) {
			if (this.getValuationTeacherByTeacher(departmentTeacher) == null) {
				teachersList.add(departmentTeacher);
			}
		}

		return teachersList;
	}

	public List<CompetenceCourse> getDepartmentCompetenceCoursesNotInGrouping(Department department) {
		TeacherServiceDistribution distribution = this.getValuationPhase().getTeacherServiceDistribution();
		Set<CompetenceCourse> departmentCourses = distribution.getCompetenceCoursesByDepartment(department);

		List<CompetenceCourse> coursesList = new ArrayList<CompetenceCourse>();
		for (CompetenceCourse course : departmentCourses) {
			if (this.getValuationCompetenceCourseByCompetenceCourse(course) == null) {
				coursesList.add(course);
			}
		}

		return coursesList;
	}

	public List<ValuationCompetenceCourse> getValuationCompetenceCourseWithoutCourseValuations(
			List<ExecutionPeriod> executionPeriodList) {
		List<ValuationCompetenceCourse> availableCompetenceCourseList = new ArrayList<ValuationCompetenceCourse>();
		List<ValuationCompetenceCourse> allCompetenceCoursesList = new ArrayList<ValuationCompetenceCourse>(
				getValuationCompetenceCoursesByExecutionPeriods(executionPeriodList));

		for (ValuationCompetenceCourse valuationCompetenceCourse : allCompetenceCoursesList) {
			for (ExecutionPeriod executionPeriod : executionPeriodList) {
				if (valuationCompetenceCourse.getCourseValuationType(getValuationPhase(), executionPeriod) != CourseValuationType.NOT_DETERMINED) {
					availableCompetenceCourseList.add(valuationCompetenceCourse);
					break;
				}
			}
		}

		return new ArrayList<ValuationCompetenceCourse>(CollectionUtils.subtract(
				allCompetenceCoursesList,
				availableCompetenceCourseList));
	}

	public List<ValuationCompetenceCourse> getValuationCompetenceCoursesByExecutionPeriods(
			List<ExecutionPeriod> executionPeriodList) {
		Set<ValuationCompetenceCourse> valuationCompetenceCourseSet = new HashSet<ValuationCompetenceCourse>();

		for (ExecutionPeriod executionPeriod : executionPeriodList) {
			valuationCompetenceCourseSet.addAll(getValuationCompetenceCoursesByExecutionPeriod(executionPeriod));
		}

		return new ArrayList<ValuationCompetenceCourse>(valuationCompetenceCourseSet);
	}

	public Boolean getIsMemberOfCoursesAndTeachersValuationManagers(Person person) {
		Group group = getCoursesAndTeachersValuationManagers();

		return group != null ? group.isMember(person) : false;
	}

	public Boolean getIsMemberOfCoursesAndTeachersManagementGroup(Person person) {
		Group group = getCoursesAndTeachersManagementGroup();

		return group != null ? group.isMember(person) : false;
	}

	public Boolean getHaveCoursesAndTeachersValuationPermission(Person person) {
		return getValuationPhase().getTeacherServiceDistribution().getHasSuperUserPermission(person)
				|| getIsMemberOfCoursesAndTeachersValuationManagers(person)
				|| ((getParent() != null) ? getParent().getHaveCoursesAndTeachersValuationPermission(person) : false);

	}

	public Boolean getHaveCoursesAndTeachersManagement(Person person) {
		return getValuationPhase().getTeacherServiceDistribution().getHasSuperUserPermission(person)
				|| getIsMemberOfCoursesAndTeachersManagementGroup(person)
				|| ((getParent() != null) ? getParent().getHaveCoursesAndTeachersManagement(person) : false);
	}

	public void addCoursesAndTeachersValuationPermission(Person person) {
		Group group = getCoursesAndTeachersValuationManagers();

		setCoursesAndTeachersValuationManagers((group != null) ? new GroupUnion(group, new PersonGroup(person))
				: new PersonGroup(person));
	}

	public void removeCoursesAndTeachersValuationPermission(Person person) {
		Group group = getCoursesAndTeachersValuationManagers();

		if (group != null) {
			setCoursesAndTeachersValuationManagers(new GroupDifference(group, new PersonGroup(person)));
		}
	}

	public void addCoursesAndTeachersManagement(Person person) {
		Group group = getCoursesAndTeachersManagementGroup();

		setCoursesAndTeachersManagementGroup((group != null) ? new GroupUnion(group, new PersonGroup(person))
				: new PersonGroup(person));
	}

	public void removeCoursesAndTeachersManagement(Person person) {
		Group group = getCoursesAndTeachersManagementGroup();

		if (group != null) {
			setCoursesAndTeachersManagementGroup(new GroupDifference(group, new PersonGroup(person)));
		}
	}

	public Double getAllActiveCourseValuationTotalHoursByExecutionPeriods(List<ExecutionPeriod> executionPeriodList) {
		double totalHours = 0d; 
		
		for(CourseValuation courseValuation : getActiveCourseValuationByExecutionPeriods(executionPeriodList)) {
			totalHours += courseValuation.getTotalHours();
		}
		
		return totalHours;
	}

	public Double getAllActiveCourseValuationTheoreticalHoursByExecutionPeriods(List<ExecutionPeriod> executionPeriodList) {
		double totalHours = 0d; 
		
		for(CourseValuation courseValuation : getActiveCourseValuationByExecutionPeriods(executionPeriodList)) {
			totalHours += courseValuation.getTheoreticalHours();
		}
		
		return totalHours;
	}

	public Double getAllActiveCourseValuationPraticalHoursByExecutionPeriods(List<ExecutionPeriod> executionPeriodList) {
		double totalHours = 0d; 
		
		for(CourseValuation courseValuation : getActiveCourseValuationByExecutionPeriods(executionPeriodList)) {
			totalHours += courseValuation.getPraticalHours();
		}
		
		return totalHours;
	}

	public Double getAllActiveCourseValuationTheoPratHoursByExecutionPeriods(List<ExecutionPeriod> executionPeriodList) {
		double totalHours = 0d; 
		
		for(CourseValuation courseValuation : getActiveCourseValuationByExecutionPeriods(executionPeriodList)) {
			totalHours += courseValuation.getTheoPratHours();
		}
		
		return totalHours;
	}

	public Double getAllActiveCourseValuationLaboratorialHoursByExecutionPeriods(List<ExecutionPeriod> executionPeriodList) {
		double totalHours = 0d; 
		
		for(CourseValuation courseValuation : getActiveCourseValuationByExecutionPeriods(executionPeriodList)) {
			totalHours += courseValuation.getLaboratorialHours();
		}
		
		return totalHours;
	}
	
	
	private List<CourseValuation> getActiveCourseValuationByExecutionPeriods(List<ExecutionPeriod> executionPeriodList) {
		List<CourseValuation> courseValuationList = new ArrayList<CourseValuation>();
		for(ExecutionPeriod executionPeriod : executionPeriodList) {
			for(ValuationCompetenceCourse valuationCompetenceCourse : getValuationCompetenceCoursesByExecutionPeriod(executionPeriod)) {
				courseValuationList.addAll(valuationCompetenceCourse.getActiveCourseValuations(getValuationPhase(), executionPeriod));
			}
		}
		return courseValuationList;
	}

	public Double getAllActiveCourseValuationTotalStudentsByExecutionPeriods(List<ExecutionPeriod> executionPeriodList) {
		double totalStudents = 0d; 
		
		for(CourseValuation courseValuation : getActiveCourseValuationByExecutionPeriods(executionPeriodList)) {
			totalStudents += courseValuation.getFirstTimeEnrolledStudents() + courseValuation.getSecondTimeEnrolledStudents();
		}
		
		return totalStudents;
	}

	public Integer getAllActiveCourseValuationFirstTimeEnrolledStudentsByExecutionPeriods(List<ExecutionPeriod> executionPeriodList) {
		int totalStudents = 0; 
		
		for(CourseValuation courseValuation : getActiveCourseValuationByExecutionPeriods(executionPeriodList)) {
			totalStudents += courseValuation.getFirstTimeEnrolledStudents();
		}
		
		return totalStudents;
	}

	public Integer getAllActiveCourseValuationSecondTimeEnrolledStudentsByExecutionPeriods(List<ExecutionPeriod> executionPeriodList) {
		int totalStudents = 0; 
		
		for(CourseValuation courseValuation : getActiveCourseValuationByExecutionPeriods(executionPeriodList)) {
			totalStudents += courseValuation.getSecondTimeEnrolledStudents();
		}
		
		return totalStudents;
	}
    
    @Override
    public void setCoursesAndTeachersValuationManagers(Group group) {
        super.setCoursesAndTeachersValuationManagers(group);

        if (group == null) {
            setCoursesAndTeachersValuationManagersExpression(null);
        } else {
            setCoursesAndTeachersValuationManagersExpression(group.getExpression());
        }
    }
    
    @Override
    public void setCoursesAndTeachersManagementGroup(Group group) {
        super.setCoursesAndTeachersManagementGroup(group);

        if (group == null) {
            setCoursesAndTeachersManagementGroupExpression(null);
        } else {
            setCoursesAndTeachersManagementGroupExpression(group.getExpression());
        }
    }
}