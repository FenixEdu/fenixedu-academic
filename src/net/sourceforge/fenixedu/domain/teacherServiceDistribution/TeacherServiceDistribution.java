package net.sourceforge.fenixedu.domain.teacherServiceDistribution;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sourceforge.fenixedu._development.PropertiesManager;
import net.sourceforge.fenixedu.commons.CollectionUtils;
import net.sourceforge.fenixedu.domain.CompetenceCourse;
import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.accessControl.Group;
import net.sourceforge.fenixedu.domain.accessControl.GroupDifference;
import net.sourceforge.fenixedu.domain.accessControl.GroupUnion;
import net.sourceforge.fenixedu.domain.accessControl.PersonGroup;
import net.sourceforge.fenixedu.domain.degreeStructure.CurricularStage;
import net.sourceforge.fenixedu.domain.person.RoleType;

import org.apache.commons.collections.Predicate;

public class TeacherServiceDistribution extends TeacherServiceDistribution_Base {

	protected TeacherServiceDistribution() {
		super();
		setRootDomainObject(RootDomainObject.getInstance());
	}

	public TeacherServiceDistribution(
			Department department,
			List<ExecutionPeriod> executionPeriodList,
			Person creator,
			String name,
			String initialValuationPhaseName) {
		this();

		if (department == null || executionPeriodList.isEmpty() || creator == null) {
			throw new NullPointerException();
		}

		for (ExecutionPeriod executionPeriod : executionPeriodList) {
			if (executionPeriod == null) {
				throw new NullPointerException();
			} else {
				this.addExecutionPeriods(executionPeriod);
			}
		}

		this.setDepartment(department);
		this.setCreator(creator);
		this.setName(name);
		createInitialValuationPhase(initialValuationPhaseName);
	}

	private void createInitialValuationPhase(String initialValuationPhaseName) {
		new ValuationPhase(
				this,
				initialValuationPhaseName,
				null,
				null,
				ValuationPhaseStatus.CURRENT,
				getDefaultOmissionValues());
	}

	private Map<String, Object> getDefaultOmissionValues() {
		Map<String, Object> defaultOmissionValues = new HashMap<String, Object>();

		defaultOmissionValues.put(
				"studentsPerTheoreticalShift",
				Integer.parseInt(PropertiesManager.getProperty("studentsPerTheoreticalShift")));
		defaultOmissionValues.put("studentsPerPraticalShift", new Integer(
				PropertiesManager.getProperty("studentsPerPraticalShift")));
		defaultOmissionValues.put("studentsPerTheoPratShift", new Integer(
				PropertiesManager.getProperty("studentsPerTheoPratShift")));
		defaultOmissionValues.put("studentsPerLaboratorialShift", new Integer(
				PropertiesManager.getProperty("studentsPerLaboratorialShift")));
		defaultOmissionValues.put("weightFirstTimeEnrolledStudentsPerTheoShift", new Double(
				PropertiesManager.getProperty("weightFirstTimeEnrolledStudentsPerTheoShift")));
		defaultOmissionValues.put("weightFirstTimeEnrolledStudentsPerPratShift", new Double(
				PropertiesManager.getProperty("weightFirstTimeEnrolledStudentsPerPratShift")));
		defaultOmissionValues.put("weightFirstTimeEnrolledStudentsPerTheoPratShift", new Double(
				PropertiesManager.getProperty("weightFirstTimeEnrolledStudentsPerTheoPratShift")));
		defaultOmissionValues.put("weightFirstTimeEnrolledStudentsPerLabShift", new Double(
				PropertiesManager.getProperty("weightFirstTimeEnrolledStudentsPerLabShift")));
		defaultOmissionValues.put("weightSecondTimeEnrolledStudentsPerTheoShift", new Double(
				PropertiesManager.getProperty("weightSecondTimeEnrolledStudentsPerTheoShift")));
		defaultOmissionValues.put("weightSecondTimeEnrolledStudentsPerPratShift", new Double(
				PropertiesManager.getProperty("weightSecondTimeEnrolledStudentsPerPratShift")));
		defaultOmissionValues.put("weightSecondTimeEnrolledStudentsPerTheoPratShift", new Double(
				PropertiesManager.getProperty("weightSecondTimeEnrolledStudentsPerTheoPratShift")));
		defaultOmissionValues.put("weightSecondTimeEnrolledStudentsPerLabShift", new Double(
				PropertiesManager.getProperty("weightSecondTimeEnrolledStudentsPerLabShift")));
		return defaultOmissionValues;
	}

	public ValuationPhase createValuationPhase(String valuationPhaseName) {
		return new ValuationPhase(
				this,
				valuationPhaseName,
				getLastValuationPhase(),
				null,
				ValuationPhaseStatus.OPEN,
				getDefaultOmissionValues());

	}
	
	public Set<CompetenceCourse> getCompetenceCoursesByExecutionPeriodsAndDepartment(List<ExecutionPeriod> executionPeriods, Department department) {
		Set<CompetenceCourse> returnCompetenceCourseSet = new HashSet<CompetenceCourse>();
		Set<CompetenceCourse> competenceCourseSet = new HashSet<CompetenceCourse>(department.getCompetenceCourses());
		
		// add Bolonha Courses
		competenceCourseSet.addAll(department.getDepartmentUnit().getDepartmentUnitCompetenceCourses(CurricularStage.APPROVED));

		for (ExecutionPeriod executionPeriod : executionPeriods) {
			for(CompetenceCourse competenceCourse : competenceCourseSet){
				if(competenceCourse.getCurricularCoursesWithActiveScopesInExecutionPeriod(executionPeriod).size() > 0){
					returnCompetenceCourseSet.add(competenceCourse);
				}
			}
		}
		
		return returnCompetenceCourseSet;
	}
	
	public Set<CompetenceCourse> getCompetenceCoursesByExecutionPeriods(List<ExecutionPeriod> executionPeriods) {
		return getCompetenceCoursesByExecutionPeriodsAndDepartment(executionPeriods, getDepartment()); 
	}
	
	public Set<CompetenceCourse> getCompetenceCoursesByDepartment(Department department) {
		return getCompetenceCoursesByExecutionPeriodsAndDepartment(getExecutionPeriods(), department);
	}
	
	public Set<CompetenceCourse> getAllCompetenceCourses() {
		return getCompetenceCoursesByExecutionPeriods(getExecutionPeriods());
	}
		
	public ValuationPhase getCurrentValuationPhase() {
		if (getValuationPhases().size() > 0) {
			return getValuationPhases().get(0).getCurrentValuationPhase();
		} else {
			return null;
		}
	}

	public ValuationPhase getFirstValuationPhase() {
		if (getValuationPhases().size() > 0) {
			return getValuationPhases().get(0).getFirstValuationPhase();
		} else {
			return null;
		}
	}

	public ValuationPhase getLastValuationPhase() {
		if (getValuationPhases().size() > 0) {
			return getValuationPhases().get(0).getLastValuationPhase();
		} else {
			return null;
		}
	}

	public List<ValuationPhase> getOrderedValuationPhases() {
		List<ValuationPhase> orderedValuationPhaseList = new ArrayList<ValuationPhase>();

		for (ValuationPhase firstValuationPhase = getFirstValuationPhase(); firstValuationPhase != null; firstValuationPhase = firstValuationPhase.getNextValuationPhase()) {
			orderedValuationPhaseList.add(firstValuationPhase);
		}

		return orderedValuationPhaseList;
	}

	public List<ValuationPhase> getPreviousValuationPhases(ValuationPhase valuationPhase) {
		List<ValuationPhase> previousValuationPhaseList = new ArrayList<ValuationPhase>();

		if (valuationPhase.getTeacherServiceDistribution() == this) {
			for (ValuationPhase firstValuationPhase = getFirstValuationPhase(); (firstValuationPhase != null)
					&& (firstValuationPhase.getIsPrevious(valuationPhase)); firstValuationPhase = firstValuationPhase.getNextValuationPhase()) {
				previousValuationPhaseList.add(firstValuationPhase);
			}
		}

		return previousValuationPhaseList;
	}

	public ExecutionYear getPreviousExecutionYear() {
		return getExecutionPeriods().get(0).getExecutionYear().getPreviousExecutionYear();
	}

	public static TeacherServiceDistribution copyTeacherServiceDistribution(
			TeacherServiceDistribution teacherServiceDistributionCopied,
			List<ExecutionPeriod> executionPeriodList,
			String name,
			Person creator) {
		TeacherServiceDistribution teacherServiceDistribution = new TeacherServiceDistribution();

		teacherServiceDistribution.setDepartment(teacherServiceDistributionCopied.getDepartment());
		teacherServiceDistribution.setCreator(creator);
		teacherServiceDistribution.setName(name);
		teacherServiceDistribution.getExecutionPeriods().addAll(executionPeriodList);

		Map<ExecutionPeriod, ExecutionPeriod> oldAndNewExecutionPeriodMap = getExecutionPeriodTranslationMap(
				teacherServiceDistributionCopied.getExecutionPeriods(),
				executionPeriodList);

		for (ValuationPhase oldValuationPhase : teacherServiceDistributionCopied.getOrderedValuationPhases()) {
			ValuationPhase newValuationPhase = ValuationPhase.createAndCopy(
					teacherServiceDistribution,
					oldValuationPhase,
					oldAndNewExecutionPeriodMap,
					teacherServiceDistribution.getLastValuationPhase());
		}

		return teacherServiceDistribution;
	}

	public static Map<ExecutionPeriod, ExecutionPeriod> getExecutionPeriodTranslationMap(
			List<ExecutionPeriod> oldExecutionPeriodList,
			List<ExecutionPeriod> newExecutionPeriodList) {
		Map<ExecutionPeriod, ExecutionPeriod> translationMap = new HashMap<ExecutionPeriod, ExecutionPeriod>();

		for (ExecutionPeriod oldExecutionPeriod : oldExecutionPeriodList) {
			for (ExecutionPeriod newExecutionPeriod : newExecutionPeriodList) {
				if (oldExecutionPeriod.getSemester() == newExecutionPeriod.getSemester()) {
					translationMap.put(oldExecutionPeriod, newExecutionPeriod);
				}
			}
		}

		return translationMap;
	}
	
	public List<ValuationPhase> getOrderedPublishedValuationPhases() {
		return (List<ValuationPhase>) CollectionUtils.select(getOrderedValuationPhases(), new Predicate() {

			public boolean evaluate(Object arg0) {
				ValuationPhase valuationPhase = (ValuationPhase) arg0;
				return valuationPhase.getIsPublished();
			}
			
		});
	}
	
	public List<ExecutionPeriod> getOrderedExecutionPeriods() {
		List<ExecutionPeriod> orderedExecutionPeriods = new ArrayList<ExecutionPeriod>(getExecutionPeriods());
		Collections.sort(orderedExecutionPeriods);

		return orderedExecutionPeriods;
	}
	
	public ExecutionPeriod getFirstExecutionPeriod() {
		ExecutionPeriod firstExecutionPeriod = getExecutionPeriods().get(0);
	
		for (ExecutionPeriod executionPeriod : getExecutionPeriods()) {
			if(executionPeriod.isBefore(firstExecutionPeriod)){ 
				firstExecutionPeriod = executionPeriod;
			}
		}
		
		return firstExecutionPeriod;
	}
	
	
	public ExecutionPeriod getLastExecutionPeriod() {
		ExecutionPeriod lastExecutionPeriod = getExecutionPeriods().get(0);

		for (ExecutionPeriod executionPeriod : getExecutionPeriods()) {
			if(executionPeriod.isAfter(lastExecutionPeriod)){
				lastExecutionPeriod = executionPeriod;
			}
		}
		
		return lastExecutionPeriod;
	}
	
	public ExecutionYear getExecutionYear(){
		return getExecutionPeriods().get(0).getExecutionYear();
	}

	public Boolean getIsMemberOfPhasesManagementGroup(Person person) {
		return (getPhasesManagementGroup() != null ? getPhasesManagementGroup().isMember(person) : false)
				|| getHasSuperUserPermission(person);
	}

	public Boolean getIsMemberOfAutomaticValuationGroup(Person person) {
		return (getAutomaticValuationGroup() != null ? getAutomaticValuationGroup().isMember(person) : false)
				|| getHasSuperUserPermission(person);
	}

	public Boolean getIsMemberOfOmissionConfigurationGroup(Person person) {
		return (getOmissionConfigurationGroup() != null ? getOmissionConfigurationGroup().isMember(person) : false)
				|| getHasSuperUserPermission(person);
	}

	public Boolean getIsMemberOfValuationCompetenceCoursesAndTeachersManagementGroup(Person person) {
		Group group = getValuationCompetenceCoursesAndTeachersManagementGroup();
		return (group != null ? group.isMember(person) : false) || getHasSuperUserPermission(person);
	}
	
	public Boolean getHavePermissionSettings(Person person) {
		return getHasSuperUserPermission(person);
	}
	
	public Boolean getHasSuperUserPermission(Person person) {
		return person.hasRole(RoleType.DEPARTMENT_ADMINISTRATIVE_OFFICE) && person.getEmployee().getCurrentDepartmentWorkingPlace() == getDepartment();
	}

	public void addPhasesManagementPermission(Person person) {
		Group group = getPhasesManagementGroup();
		
		setPhasesManagementGroup(group != null ? new GroupUnion(group, new PersonGroup(person)) : new PersonGroup(person));
	}
	
	public void removePhasesManagementPermission(Person person) {
		Group group = getPhasesManagementGroup();
		
		if(group != null) {
			setPhasesManagementGroup(new GroupDifference(group, new PersonGroup(person)));
		}
		
	}
	
	public void addAutomaticValuationPermission(Person person) {
		Group group = getAutomaticValuationGroup();
		
		setAutomaticValuationGroup(group != null ? new GroupUnion(group, new PersonGroup(person)) : new PersonGroup(person));
	}
	
	public void removeAutomaticValuationPermission(Person person) {
		Group group = getAutomaticValuationGroup();
		
		if(group != null) {
			setAutomaticValuationGroup(new GroupDifference(group, new PersonGroup(person)));
		}
		
	}
	
	public void addOmissionConfigurationPermission(Person person) {
		Group group = getOmissionConfigurationGroup();
		
		setOmissionConfigurationGroup(group != null ? new GroupUnion(group, new PersonGroup(person)) : new PersonGroup(person));
	}
	
	public void removeOmissionConfigurationPermission(Person person) {
		Group group = getOmissionConfigurationGroup();
		
		if(group != null) {
			setOmissionConfigurationGroup(new GroupDifference(group, new PersonGroup(person)));
		}
	}
	
	public void addValuationCompetenceCoursesAndTeachersManagement(Person person) {
		Group group = getValuationCompetenceCoursesAndTeachersManagementGroup();
		
		setValuationCompetenceCoursesAndTeachersManagementGroup(group != null ? new GroupUnion(group, new PersonGroup(person)) : new PersonGroup(person));
	}
	
	public void removeValuationCompetenceCoursesAndTeachersManagement(Person person) {
		Group group = getValuationCompetenceCoursesAndTeachersManagementGroup();
		
		if(group != null) {
			setValuationCompetenceCoursesAndTeachersManagementGroup(new GroupDifference(group, new PersonGroup(person)));
		}
	}	
	
	
	public Boolean hasAnyPermission(Person userViewPerson) {
	
		if(getHasSuperUserPermission(userViewPerson)) {
			return Boolean.TRUE;
		}
		
		if(hasPermissionToCoursesAndTeachersValuation(userViewPerson)) {
			return Boolean.TRUE;
		}
		
		if(hasPermissionToCoursesAndTeachersManagement(userViewPerson)) {
			return Boolean.TRUE;
		}
		
		if(getIsMemberOfPhasesManagementGroup(userViewPerson)) {
			return Boolean.TRUE;
		}
		
		if(getIsMemberOfAutomaticValuationGroup(userViewPerson)) {
			return Boolean.TRUE;
		}
		
		if(getIsMemberOfOmissionConfigurationGroup(userViewPerson)) {
			return Boolean.TRUE;
		}
		
		if(getIsMemberOfValuationCompetenceCoursesAndTeachersManagementGroup(userViewPerson)) {
			return Boolean.TRUE;
		}
		
		return Boolean.FALSE;
	}
	
	public Boolean hasPermissionToCoursesAndTeachersValuation(Person person) {
		return hasPermissionToCoursesAndTeachersValuation(getCurrentValuationPhase().getRootValuationGrouping(), person);
	}

	public Boolean hasPermissionToCoursesAndTeachersValuation(ValuationGrouping valuationGrouping, Person person) {
		if (valuationGrouping.getIsMemberOfCoursesAndTeachersValuationManagers(person)) {
			return true;
		} else {
			for (ValuationGrouping son : valuationGrouping.getChilds()) {
				if (hasPermissionToCoursesAndTeachersValuation(son, person)) {
					return true;
				}
			}
		}

		return false;
	}	
	
	public Boolean hasPermissionToCoursesAndTeachersManagement(Person person) {
		return hasPermissionToCoursesAndTeachersManagement(
				getCurrentValuationPhase().getRootValuationGrouping(), person);
	}

	public Boolean hasPermissionToCoursesAndTeachersManagement(ValuationGrouping valuationGrouping, Person person) {
		if (valuationGrouping.getIsMemberOfCoursesAndTeachersManagementGroup(person)) {
			return true;
		} else {
			for (ValuationGrouping son : valuationGrouping.getChilds()) {
				if (hasPermissionToCoursesAndTeachersManagement(son, person)) {
					return true;
				}
			}
		}

		return false;
	}
    
    @Override
    public void setPhasesManagementGroup(Group group) {
        super.setPhasesManagementGroup(group);

        if (group == null) {
            setPhasesManagementGroupExpression(null);
        } else {
            setPhasesManagementGroupExpression(group.getExpression());
        }
    }
    
    @Override
    public void setAutomaticValuationGroup(Group group) {
        super.setAutomaticValuationGroup(group);

        if (group == null) {
            setAutomaticValuationGroupExpression(null);
        } else {
            setAutomaticValuationGroupExpression(group.getExpression());
        }
    }
    
    @Override
    public void setOmissionConfigurationGroup(Group group) {
        super.setOmissionConfigurationGroup(group);

        if (group == null) {
            setOmissionConfigurationGroupExpression(null);
        } else {
            setOmissionConfigurationGroupExpression(group.getExpression());
        }
    }
    
    @Override
    public void setValuationCompetenceCoursesAndTeachersManagementGroup(Group group) {
        super.setValuationCompetenceCoursesAndTeachersManagementGroup(group);

        if (group == null) {
            setValuationCompetenceCoursesAndTeachersManagementGroupExpr(null);
        } else {
            setValuationCompetenceCoursesAndTeachersManagementGroupExpr(group.getExpression());
        }
    }
}
