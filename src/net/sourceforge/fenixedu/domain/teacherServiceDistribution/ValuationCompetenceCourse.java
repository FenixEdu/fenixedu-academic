package net.sourceforge.fenixedu.domain.teacherServiceDistribution;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.Predicate;

import net.sourceforge.fenixedu.commons.CollectionUtils;
import net.sourceforge.fenixedu.domain.CompetenceCourse;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;

public class ValuationCompetenceCourse extends ValuationCompetenceCourse_Base {

	public ValuationCompetenceCourse() {
		super();
		setRootDomainObject(RootDomainObject.getInstance());
	}

	public ValuationCompetenceCourse(CompetenceCourse competenceCourse) {
		this();

		if (competenceCourse == null) {
			throw new NullPointerException();
		}

		this.setCompetenceCourse(competenceCourse);

		for (CurricularCourse curricularCourse : competenceCourse.getAssociatedCurricularCourses()) {
			ValuationCurricularCourse valuationCurricularCourse = curricularCourse.getValuationCurricularCourse();

			if (valuationCurricularCourse == null) {
				valuationCurricularCourse = new ValuationCurricularCourse(curricularCourse, this);
			}
		}
	}
	
	public ValuationCompetenceCourse(String name) {
		this();
		
		if(name == null) {
			throw new NullPointerException();
		}
		
		this.setName(name);
	}

	public Boolean getIsRealCompetenceCourse() {
		return getCompetenceCourse() != null;
	}

	public String getName() {
		if (getIsRealCompetenceCourse()) {
			return getCompetenceCourse().getName();
		} else {
			return super.getName();
		}
	}
	
	
	public List<ValuationCurricularCourse> getValuationCurricularCourses(List<ExecutionPeriod> executionPeriodList) {
		Set<ValuationCurricularCourse> valuationCurricularCourseSet = new HashSet<ValuationCurricularCourse>();
		
		for(ExecutionPeriod executionPeriod : executionPeriodList) {
			valuationCurricularCourseSet.addAll(getValuationCurricularCourses(executionPeriod));
		}
		
		return new ArrayList<ValuationCurricularCourse>(valuationCurricularCourseSet);
	}
	
	public List<ValuationCurricularCourse> getValuationCurricularCourses(ExecutionPeriod executionPeriod) {
		List<ValuationCurricularCourse> valuationCurricularCourseList = new ArrayList<ValuationCurricularCourse>();

		if (getIsRealCompetenceCourse()) {
			for (CurricularCourse curricularCourse : getCompetenceCourse().getCurricularCoursesWithActiveScopesInExecutionPeriod(
					executionPeriod)) {
				ValuationCurricularCourse valuationCurricularCourse = curricularCourse.getValuationCurricularCourse();
				if (valuationCurricularCourse != null)
					valuationCurricularCourseList.add(valuationCurricularCourse);
			}
		} else {
			for (ValuationCurricularCourse valuationCurricularCourse : super.getAssociatedValuationCurricularCourses()) {
				if (valuationCurricularCourse.getExecutionPeriod() == executionPeriod) {
					valuationCurricularCourseList.add(valuationCurricularCourse);
				}
			}
		}

		return valuationCurricularCourseList;
	}

	public Boolean hasActiveValuationCurricularCourses(ExecutionPeriod executionPeriod) {
		return !getValuationCurricularCourses(executionPeriod).isEmpty();
	}

	public CourseValuationType getCourseValuationType(
			final ValuationPhase valuationPhase,
			final ExecutionPeriod executionPeriod) {
		
		List<CurricularCourseValuationGroup> curricularCourseValuationGroupList = getCurricularCourseValuationGroupsByValuationPhaseAndExecutionPeriod(
				valuationPhase,
				executionPeriod);

		if (!curricularCourseValuationGroupList.isEmpty() && curricularCourseValuationGroupList.get(0).getIsActive()) {
			return CourseValuationType.CURRICULAR_COURSE_VALUATION_GROUP;
		}		
		
		CompetenceCourseValuation competenceCourseValuation = getCompetenceCourseValuationByValuationPhaseAndExecutionPeriod(
				valuationPhase,
				executionPeriod);

		if ((competenceCourseValuation != null) && competenceCourseValuation.getIsActive()) {
			return CourseValuationType.COMPETENCE_COURSE_VALUATION;
		}
		
		List<CurricularCourseValuation> curricularCourseValuationList = getCurricularCourseValuationsByValuationPhaseAndExecutionPeriod(
				valuationPhase,
				executionPeriod);

		if (!curricularCourseValuationList.isEmpty() && curricularCourseValuationList.get(0).getIsActive()) {
			return CourseValuationType.CURRICULAR_COURSE_VALUATION;
		}

		return CourseValuationType.NOT_DETERMINED;
	}
	
	public List<CourseValuation> getCourseValuationsByValuationPhase(final ValuationPhase valuationPhase) {
		List<CourseValuation> courseValuationList = new ArrayList<CourseValuation>();
		
		courseValuationList.addAll(getCompetenceCourseValuationByValuationPhase(valuationPhase));
		courseValuationList.addAll(getCurricularCourseValuationsByValuationPhase(valuationPhase));
		courseValuationList.addAll(getCurricularCourseValuationGroupsByValuationPhase(valuationPhase));
		
		return courseValuationList;
	}
	
	public CompetenceCourseValuation getCompetenceCourseValuationByValuationPhaseAndExecutionPeriod(
			final ValuationPhase valuationPhase,
			final ExecutionPeriod executionPeriod) {
		return (CompetenceCourseValuation) CollectionUtils.find(getCompetenceCourseValuations(), new Predicate() {

			public boolean evaluate(Object arg0) {
				CompetenceCourseValuation competenceCourseValuation = (CompetenceCourseValuation) arg0;

				return (competenceCourseValuation.getValuationPhase() == valuationPhase && competenceCourseValuation.getExecutionPeriod() == executionPeriod);
			}
		});
	}

	public List<CurricularCourseValuation> getCurricularCourseValuationsByValuationPhaseAndExecutionPeriod(
			final ValuationPhase valuationPhase,
			final ExecutionPeriod executionPeriod) {

		List<CurricularCourseValuation> curricularCourseValuationList = new ArrayList<CurricularCourseValuation>();
		List<ValuationCurricularCourse> valuationCurricularCourseList = getValuationCurricularCourses(executionPeriod);

		for (ValuationCurricularCourse valuationCurricularCourse : valuationCurricularCourseList) {
			CurricularCourseValuation curricularCourseValuation = valuationCurricularCourse.getCurricularCourseValuationByValuationPhaseAndExecutionPeriod(
					valuationPhase,
					executionPeriod);
			if (curricularCourseValuation != null)
				curricularCourseValuationList.add(curricularCourseValuation);
		}

		return curricularCourseValuationList;
	}

	public List<CurricularCourseValuationGroup> getCurricularCourseValuationGroupsByValuationPhaseAndExecutionPeriod(
			final ValuationPhase valuationPhase,
			final ExecutionPeriod executionPeriod) {
		Set<CurricularCourseValuationGroup> curricularCourseValuationGroupSet = new HashSet<CurricularCourseValuationGroup>();

		List<ValuationCurricularCourse> valuationCurricularCourseList = getValuationCurricularCourses(executionPeriod);

		for (ValuationCurricularCourse valuationCurricularCourse : valuationCurricularCourseList) {
			CurricularCourseValuationGroup curricularCourseValuationGroup = valuationCurricularCourse.getCurricularCourseValuationGroupByValuationPhaseAndExecutionPeriod(
					valuationPhase,
					executionPeriod);

			if (curricularCourseValuationGroup != null) {
				curricularCourseValuationGroupSet.add(curricularCourseValuationGroup);
			}
		}

		List<CurricularCourseValuationGroup> curricularCourseValuationGroupList = new ArrayList<CurricularCourseValuationGroup>();
		curricularCourseValuationGroupList.addAll(curricularCourseValuationGroupSet);

		return curricularCourseValuationGroupList;
	}

	public List<CompetenceCourseValuation> getCompetenceCourseValuationByValuationPhase(
			final ValuationPhase valuationPhase) {
		List<CompetenceCourseValuation> competenceCourseValuationList = new ArrayList<CompetenceCourseValuation>();
		
		for(ExecutionPeriod executionPeriod : valuationPhase.getTeacherServiceDistribution().getExecutionPeriods()) {
			CompetenceCourseValuation competenceCourseValuation = getCompetenceCourseValuationByValuationPhaseAndExecutionPeriod(valuationPhase, executionPeriod); 
			if(competenceCourseValuation != null) {
				competenceCourseValuationList.add(competenceCourseValuation);
			}
		}
		
		return competenceCourseValuationList;
	}

	public List<CurricularCourseValuation> getCurricularCourseValuationsByValuationPhase(
			final ValuationPhase valuationPhase) {

		List<CurricularCourseValuation> curricularCourseValuationList = new ArrayList<CurricularCourseValuation>();
		
		for(ExecutionPeriod executionPeriod : valuationPhase.getTeacherServiceDistribution().getExecutionPeriods()) {
			curricularCourseValuationList.addAll(getCurricularCourseValuationsByValuationPhaseAndExecutionPeriod(valuationPhase, executionPeriod));
		}

		return curricularCourseValuationList;
	}

	public List<CurricularCourseValuationGroup> getCurricularCourseValuationGroupsByValuationPhase(
			final ValuationPhase valuationPhase) {
		
		List<CurricularCourseValuationGroup> curricularCourseValuationGroupList = new ArrayList<CurricularCourseValuationGroup>();
		
		for(ExecutionPeriod executionPeriod : valuationPhase.getTeacherServiceDistribution().getExecutionPeriods()) {
			curricularCourseValuationGroupList.addAll(getCurricularCourseValuationGroupsByValuationPhaseAndExecutionPeriod(valuationPhase, executionPeriod));
		}

		return curricularCourseValuationGroupList;
	}
	
	
	public void setCourseType(
			final CourseValuationType courseValuationType,
			final ValuationPhase valuationPhase,
			final ExecutionPeriod executionPeriod) {
		CompetenceCourseValuation competenceCourseValuation = getCompetenceCourseValuationByValuationPhaseAndExecutionPeriod(
				valuationPhase,
				executionPeriod);
		List<CurricularCourseValuation> curricularCourseValuationList = getCurricularCourseValuationsByValuationPhaseAndExecutionPeriod(
				valuationPhase,
				executionPeriod);
		List<CurricularCourseValuationGroup> curricularCourseValuationGroupList = getCurricularCourseValuationGroupsByValuationPhaseAndExecutionPeriod(
				valuationPhase,
				executionPeriod);

		if (courseValuationType == CourseValuationType.COMPETENCE_COURSE_VALUATION) {
			if (competenceCourseValuation != null) {
				competenceCourseValuation.setIsActive(true);
			}

			for (CurricularCourseValuation curricularCourseValuation : curricularCourseValuationList) {
				curricularCourseValuation.setIsActive(false);
			}

			for (CurricularCourseValuationGroup curricularCourseValuationGroup : curricularCourseValuationGroupList) {
				curricularCourseValuationGroup.setIsActive(false);
			}
		} else if (courseValuationType == CourseValuationType.CURRICULAR_COURSE_VALUATION) {
			if (competenceCourseValuation != null) {
				competenceCourseValuation.setIsActive(false);
			}

			for (CurricularCourseValuation curricularCourseValuation : curricularCourseValuationList) {
				curricularCourseValuation.setIsActive(true);
			}

			for (CurricularCourseValuationGroup curricularCourseValuationGroup : curricularCourseValuationGroupList) {
				curricularCourseValuationGroup.setIsActive(false);
			}
		} else if (courseValuationType == CourseValuationType.CURRICULAR_COURSE_VALUATION_GROUP) {
			if (competenceCourseValuation != null) {
				competenceCourseValuation.setIsActive(false);
			}

			for (CurricularCourseValuation curricularCourseValuation : curricularCourseValuationList) {
				curricularCourseValuation.setIsActive(false);
			}

			for (CurricularCourseValuationGroup curricularCourseValuationGroup : curricularCourseValuationGroupList) {
				curricularCourseValuationGroup.setIsActive(true);
			}
		}
	}

	public List<CurricularCourseValuation> getCurricularCourseValuationsNotBelongingToValuationGroup(
			final ValuationPhase valuationPhase,
			final ExecutionPeriod executionPeriod) {

		List<CurricularCourseValuation> curricularCourseValuationList = new ArrayList<CurricularCourseValuation>();
		List<ValuationCurricularCourse> valuationCurricularCourseList = getValuationCurricularCourses(executionPeriod);

		for (ValuationCurricularCourse valuationCurricularCourse : valuationCurricularCourseList) {
			CurricularCourseValuation curricularCourseValuation = valuationCurricularCourse.getCurricularCourseValuationByValuationPhaseAndExecutionPeriod(
					valuationPhase,
					executionPeriod);
			if (curricularCourseValuation != null
					&& curricularCourseValuation.getCurricularCourseValuationGroup() == null)
				curricularCourseValuationList.add(curricularCourseValuation);
		}

		return curricularCourseValuationList;
	}

	public List<CourseValuation> getActiveCourseValuations(
			final ValuationPhase valuationPhase,
			final ExecutionPeriod executionPeriod) {
		List<CourseValuation> courseValuationList = new ArrayList<CourseValuation>();
		
		List<CurricularCourseValuationGroup> curricularCourseValuationGroupList = getCurricularCourseValuationGroupsByValuationPhaseAndExecutionPeriod(
				valuationPhase,
				executionPeriod);

		if (!curricularCourseValuationGroupList.isEmpty() && curricularCourseValuationGroupList.get(0).getIsActive()) {
			courseValuationList.addAll(curricularCourseValuationGroupList);
			return courseValuationList;
		}
		
		CompetenceCourseValuation competenceCourseValuation = getCompetenceCourseValuationByValuationPhaseAndExecutionPeriod(
				valuationPhase,
				executionPeriod);

		if ((competenceCourseValuation != null) && competenceCourseValuation.getIsActive()) {
			courseValuationList.add(competenceCourseValuation);
			return courseValuationList;
		}
		
		List<CurricularCourseValuation> curricularCourseValuationList = getCurricularCourseValuationsByValuationPhaseAndExecutionPeriod(
				valuationPhase,
				executionPeriod);
		
		if (!curricularCourseValuationList.isEmpty() && curricularCourseValuationList.get(0).getIsActive()) {
			courseValuationList.addAll(curricularCourseValuationList);
		}

		return courseValuationList;
	}
	

	public void delete() {
		for (ValuationCurricularCourse valuationCurricularCourse : getAssociatedValuationCurricularCourses()) {
			removeAssociatedValuationCurricularCourses(valuationCurricularCourse);
		}
		for (CompetenceCourseValuation competenceCourseValuation : getCompetenceCourseValuations()) {
			removeCompetenceCourseValuations(competenceCourseValuation);
		}
		for (ValuationGrouping valuationGrouping : getValuationGroupings()) {
			removeValuationGroupings(valuationGrouping);
		}
		if(!this.getIsRealCompetenceCourse()){
			for (ValuationCurricularCourse valuationCurricularCourse : getAssociatedValuationCurricularCourses()) {
				valuationCurricularCourse.delete();
			}
		}
		removeCompetenceCourse();
		removeRootDomainObject();
		
		super.deleteDomainObject();
	}
		
	public Department getCompetenceCourseDepartment(){
		if(getIsRealCompetenceCourse()){
			if(getCompetenceCourse().isBolonha()){
				return getCompetenceCourse().getDepartmentUnit().getDepartment();
			} else {
				return getCompetenceCourse().getDepartments().get(0);
			}
		} 
		
		return getValuationGroupings().get(0).getValuationPhase().getTeacherServiceDistribution().getDepartment();
	}
	
	public Boolean hasActiveValuationCurricularCoursesInExecutionPeriods(List<ExecutionPeriod> executionPeriodList) {
		for(ExecutionPeriod executionPeriod : executionPeriodList) {
			if(!getValuationCurricularCourses(executionPeriod).isEmpty()) return true;
		}
		
		return false;
	}

	public boolean getHavePermissionToValuate(TeacherServiceDistribution teacherServiceDistribution, Person person) {
		List<ValuationGrouping> associatedValuationGroupingList = getAssociatedValuationGroupings(teacherServiceDistribution);
		
		for(ValuationGrouping valuationGrouping : associatedValuationGroupingList) {
			if(valuationGrouping.getHaveCoursesAndTeachersValuationPermission(person)) {
				return true;
			}
		}
		
		return false;
	}

	@SuppressWarnings("unchecked")
	private List<ValuationGrouping> getAssociatedValuationGroupings(final TeacherServiceDistribution teacherServiceDistribution) {
		return (List<ValuationGrouping>) CollectionUtils.select(getValuationGroupings(), new Predicate() {
			public boolean evaluate(Object arg0) {
				ValuationGrouping valuationGrouping = (ValuationGrouping) arg0;
				
				return valuationGrouping.getValuationPhase().getTeacherServiceDistribution() == teacherServiceDistribution;
			}
		});
	}	
}
