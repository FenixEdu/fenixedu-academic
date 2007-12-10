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
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.person.RoleType;

import org.apache.commons.collections.Predicate;

public class TSDProcess extends TSDProcess_Base {

	protected TSDProcess() {
		super();
		setRootDomainObject(RootDomainObject.getInstance());
	}

	public TSDProcess(
			Department department,
			List<ExecutionPeriod> executionPeriodList,
			Person creator,
			String name,
			String initialTSDProcessPhaseName) {
		this();

		if (department == null || executionPeriodList.isEmpty() || creator == null) {
			throw new DomainException("Parameters.required");
		}

		for (ExecutionPeriod executionPeriod : executionPeriodList) {
			if (executionPeriod == null) {
				throw new DomainException("ExecutionPeriod.required");
			} else {
				this.addExecutionPeriods(executionPeriod);
			}
		}

		this.setDepartment(department);
		this.setCreator(creator);
		this.setName(name);
		createInitialTSDProcessPhase(initialTSDProcessPhaseName);
	}

	private void createInitialTSDProcessPhase(String initialTSDProcessPhaseName) {
		new TSDProcessPhase(
				this,
				initialTSDProcessPhaseName,
				null,
				null,
				TSDProcessPhaseStatus.CURRENT,
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

	public TSDProcessPhase createTSDProcessPhase(String tsdProcessPhaseName) {
		return new TSDProcessPhase(
				this,
				tsdProcessPhaseName,
				getLastTSDProcessPhase(),
				null,
				TSDProcessPhaseStatus.OPEN,
				getDefaultOmissionValues());

	}
	
	public Set<CompetenceCourse> getCompetenceCoursesByExecutionPeriodsAndDepartment(List<ExecutionPeriod> executionPeriods, Department department) {
		Set<CompetenceCourse> returnCompetenceCourseSet = new HashSet<CompetenceCourse>();
		Set<CompetenceCourse> competenceCourseSet = new HashSet<CompetenceCourse>(department.getCompetenceCourses());
		
		// add Bolonha Courses
		competenceCourseSet.addAll(department.getDepartmentUnit().getCompetenceCourses(CurricularStage.APPROVED));

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
		
	public TSDProcessPhase getCurrentTSDProcessPhase() {
		if (getTSDProcessPhases().size() > 0) {
			return getTSDProcessPhases().get(0).getCurrentTSDProcessPhase();
		} else {
			return null;
		}
	}

	public TSDProcessPhase getFirstTSDProcessPhase() {
		if (getTSDProcessPhases().size() > 0) {
			return getTSDProcessPhases().get(0).getFirstTSDProcessPhase();
		} else {
			return null;
		}
	}

	public TSDProcessPhase getLastTSDProcessPhase() {
		if (getTSDProcessPhases().size() > 0) {
			return getTSDProcessPhases().get(0).getLastTSDProcessPhase();
		} else {
			return null;
		}
	}

	public List<TSDProcessPhase> getOrderedTSDProcessPhases() {
		List<TSDProcessPhase> orderedTSDProcessPhaseList = new ArrayList<TSDProcessPhase>();

		for (TSDProcessPhase firstTSDProcessPhase = getFirstTSDProcessPhase(); firstTSDProcessPhase != null; firstTSDProcessPhase = firstTSDProcessPhase.getNextTSDProcessPhase()) {
			orderedTSDProcessPhaseList.add(firstTSDProcessPhase);
		}

		return orderedTSDProcessPhaseList;
	}

	public ExecutionYear getPreviousExecutionYear() {
		return getExecutionPeriods().get(0).getExecutionYear().getPreviousExecutionYear();
	}
	
	public List<TSDProcessPhase> getOrderedPublishedTSDProcessPhases() {
		return (List<TSDProcessPhase>) CollectionUtils.select(getOrderedTSDProcessPhases(), new Predicate() {

			public boolean evaluate(Object arg0) {
				TSDProcessPhase tsdProcessPhase = (TSDProcessPhase) arg0;
				return tsdProcessPhase.getIsPublished();
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

	public Boolean getIsMemberOfCompetenceCoursesAndTeachersManagementGroup(Person person) {
		Group group = getTsdCoursesAndTeachersManagementGroup();
		return (group != null ? group.isMember(person) : false) || getHasSuperUserPermission(person);
	}
	
	public Boolean getHavePermissionSettings(Person person) {
		return getHasSuperUserPermission(person);
	}
	
	public Boolean getHasSuperUserPermission(Person person) {
		return person.hasRole(RoleType.DEPARTMENT_ADMINISTRATIVE_OFFICE) && person.getEmployee().getCurrentDepartmentWorkingPlace().equals(getDepartment());
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
	
	public void addCompetenceCoursesAndTeachersManagement(Person person) {
		Group group = getTsdCoursesAndTeachersManagementGroup();
		
		setTsdCoursesAndTeachersManagementGroup(group != null ? new GroupUnion(group, new PersonGroup(person)) : new PersonGroup(person));
	}
	
	public void removeCompetenceCoursesAndTeachersManagement(Person person) {
		Group group = getTsdCoursesAndTeachersManagementGroup();
		
		if(group != null) {
			setTsdCoursesAndTeachersManagementGroup(new GroupDifference(group, new PersonGroup(person)));
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
		
		if(getIsMemberOfCompetenceCoursesAndTeachersManagementGroup(userViewPerson)) {
			return Boolean.TRUE;
		}
		
		return Boolean.FALSE;
	}
	
	public Boolean hasPermissionToCoursesAndTeachersValuation(Person person) {
		return hasPermissionToCoursesAndTeachersValuation(getCurrentTSDProcessPhase().getRootTSD(), person);
	}

	public Boolean hasPermissionToCoursesAndTeachersValuation(TeacherServiceDistribution tsd, Person person) {
		if (tsd.getIsMemberOfCoursesAndTeachersValuationManagers(person)) {
			return true;
		} else {
			for (TeacherServiceDistribution son : tsd.getChilds()) {
				if (hasPermissionToCoursesAndTeachersValuation(son, person)) {
					return true;
				}
			}
		}

		return false;
	}	
	
	public Boolean hasPermissionToCoursesAndTeachersManagement(Person person) {
		return hasPermissionToCoursesAndTeachersManagement(
				getCurrentTSDProcessPhase().getRootTSD(), person);
	}

	public Boolean hasPermissionToCoursesAndTeachersManagement(TeacherServiceDistribution tsd, Person person) {
		if (tsd.getIsMemberOfCoursesAndTeachersManagementGroup(person)) {
			return true;
		} else {
			for (TeacherServiceDistribution son : tsd.getChilds()) {
				if (hasPermissionToCoursesAndTeachersManagement(son, person)) {
					return true;
				}
			}
		}

		return false;
	}

	public void delete() {
		for(TSDProcessPhase phase : getTSDProcessPhases()){
			phase.deleteDataAndPhase();
		}
		for (ExecutionPeriod executionPeriod : getExecutionPeriods()) {
			removeExecutionPeriods(executionPeriod);
		}		
		
		removeCreator();
		removeDepartment();
		removeRootDomainObject();
		super.deleteDomainObject();
	}
    
}
