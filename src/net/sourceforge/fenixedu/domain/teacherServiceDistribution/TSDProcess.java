package net.sourceforge.fenixedu.domain.teacherServiceDistribution;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.commons.CollectionUtils;
import net.sourceforge.fenixedu.domain.CompetenceCourse;
import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
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

    public TSDProcess(Department department, List<ExecutionSemester> executionPeriodList, Person creator, String name,
	    String initialTSDProcessPhaseName) {
	this();

	if (department == null || executionPeriodList.isEmpty() || creator == null) {
	    throw new DomainException("Parameters.required");
	}

	for (ExecutionSemester executionSemester : executionPeriodList) {
	    if (executionSemester == null) {
		throw new DomainException("ExecutionPeriod.required");
	    } else {
		this.addExecutionPeriods(executionSemester);
	    }
	}

	this.setDepartment(department);
	this.setCreator(creator);
	this.setName(name);
	createInitialTSDProcessPhase(initialTSDProcessPhaseName);
    }

    private void createInitialTSDProcessPhase(String initialTSDProcessPhaseName) {
	new TSDProcessPhase(this, initialTSDProcessPhaseName, null, null, TSDProcessPhaseStatus.CURRENT);
    }

    public TSDProcessPhase createTSDProcessPhase(String tsdProcessPhaseName) {
	return new TSDProcessPhase(this, tsdProcessPhaseName, getLastTSDProcessPhase(), null, TSDProcessPhaseStatus.OPEN);
    }

    public Set<CompetenceCourse> getCompetenceCoursesByExecutionPeriodsAndDepartment(List<ExecutionSemester> executionSemesters,
	    Department department) {
	Set<CompetenceCourse> returnCompetenceCourseSet = new HashSet<CompetenceCourse>();
	Set<CompetenceCourse> competenceCourseSet = new HashSet<CompetenceCourse>(department.getCompetenceCourses());

	// add Bolonha Courses
	competenceCourseSet.addAll(department.getDepartmentUnit().getCompetenceCourses(CurricularStage.APPROVED));

	for (ExecutionSemester executionSemester : executionSemesters) {
	    for (CompetenceCourse competenceCourse : competenceCourseSet) {
		if (competenceCourse.getCurricularCoursesWithActiveScopesInExecutionPeriod(executionSemester).size() > 0) {
		    returnCompetenceCourseSet.add(competenceCourse);
		}
	    }
	}

	return returnCompetenceCourseSet;
    }

    public Set<CompetenceCourse> getCompetenceCoursesByExecutionPeriods(List<ExecutionSemester> executionSemesters) {
	return getCompetenceCoursesByExecutionPeriodsAndDepartment(executionSemesters, getDepartment());
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

	for (TSDProcessPhase firstTSDProcessPhase = getFirstTSDProcessPhase(); firstTSDProcessPhase != null; firstTSDProcessPhase = firstTSDProcessPhase
		.getNextTSDProcessPhase()) {
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

    public List<ExecutionSemester> getOrderedExecutionPeriods() {
	List<ExecutionSemester> orderedExecutionPeriods = new ArrayList<ExecutionSemester>(getExecutionPeriods());
	Collections.sort(orderedExecutionPeriods);

	return orderedExecutionPeriods;
    }

    public ExecutionSemester getFirstExecutionPeriod() {
	ExecutionSemester firstExecutionPeriod = getExecutionPeriods().get(0);

	for (ExecutionSemester executionSemester : getExecutionPeriods()) {
	    if (executionSemester.isBefore(firstExecutionPeriod)) {
		firstExecutionPeriod = executionSemester;
	    }
	}

	return firstExecutionPeriod;
    }

    public ExecutionSemester getLastExecutionPeriod() {
	ExecutionSemester lastExecutionPeriod = getExecutionPeriods().get(0);

	for (ExecutionSemester executionSemester : getExecutionPeriods()) {
	    if (executionSemester.isAfter(lastExecutionPeriod)) {
		lastExecutionPeriod = executionSemester;
	    }
	}

	return lastExecutionPeriod;
    }

    public ExecutionYear getExecutionYear() {
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
	return person.hasRole(RoleType.DEPARTMENT_ADMINISTRATIVE_OFFICE)
		&& person.getEmployee().getCurrentDepartmentWorkingPlace().equals(getDepartment());
    }

    public void addPhasesManagementPermission(Person person) {
	Group group = getPhasesManagementGroup();

	setPhasesManagementGroup(group != null ? new GroupUnion(group, new PersonGroup(person)) : new PersonGroup(person));
    }

    public void removePhasesManagementPermission(Person person) {
	Group group = getPhasesManagementGroup();

	if (group != null) {
	    setPhasesManagementGroup(new GroupDifference(group, new PersonGroup(person)));
	}

    }

    public void addAutomaticValuationPermission(Person person) {
	Group group = getAutomaticValuationGroup();

	setAutomaticValuationGroup(group != null ? new GroupUnion(group, new PersonGroup(person)) : new PersonGroup(person));
    }

    public void removeAutomaticValuationPermission(Person person) {
	Group group = getAutomaticValuationGroup();

	if (group != null) {
	    setAutomaticValuationGroup(new GroupDifference(group, new PersonGroup(person)));
	}

    }

    public void addOmissionConfigurationPermission(Person person) {
	Group group = getOmissionConfigurationGroup();

	setOmissionConfigurationGroup(group != null ? new GroupUnion(group, new PersonGroup(person)) : new PersonGroup(person));
    }

    public void removeOmissionConfigurationPermission(Person person) {
	Group group = getOmissionConfigurationGroup();

	if (group != null) {
	    setOmissionConfigurationGroup(new GroupDifference(group, new PersonGroup(person)));
	}
    }

    public void addCompetenceCoursesAndTeachersManagement(Person person) {
	Group group = getTsdCoursesAndTeachersManagementGroup();

	setTsdCoursesAndTeachersManagementGroup(group != null ? new GroupUnion(group, new PersonGroup(person)) : new PersonGroup(
		person));
    }

    public void removeCompetenceCoursesAndTeachersManagement(Person person) {
	Group group = getTsdCoursesAndTeachersManagementGroup();

	if (group != null) {
	    setTsdCoursesAndTeachersManagementGroup(new GroupDifference(group, new PersonGroup(person)));
	}
    }

    public Boolean hasAnyPermission(Person userViewPerson) {

	if (getHasSuperUserPermission(userViewPerson)) {
	    return Boolean.TRUE;
	}

	if (hasPermissionToCourseManagement(userViewPerson)) {
	    return Boolean.TRUE;
	}

	if (hasPermissionToTeacherManagement(userViewPerson)) {
	    return Boolean.TRUE;
	}
	
	if(hasPermissionToCoursesValuation(userViewPerson)) {
	    return Boolean.TRUE;
	}

	if(hasPermissionToTeachersValuation(userViewPerson)) {
	    return Boolean.TRUE;
	}
	
	if (getIsMemberOfPhasesManagementGroup(userViewPerson)) {
	    return Boolean.TRUE;
	}

	if (getIsMemberOfAutomaticValuationGroup(userViewPerson)) {
	    return Boolean.TRUE;
	}

	if (getIsMemberOfOmissionConfigurationGroup(userViewPerson)) {
	    return Boolean.TRUE;
	}

	if (getIsMemberOfCompetenceCoursesAndTeachersManagementGroup(userViewPerson)) {
	    return Boolean.TRUE;
	}

	return Boolean.FALSE;
    }

    public Boolean hasPermissionToTeachersValuation(Person person) {
	return hasPermissionToCoursesValuation(getCurrentTSDProcessPhase().getRootTSD(), person);
    }

    public boolean hasPermissionToTeachersValuation(TeacherServiceDistribution tsd, Person person) {
	if (tsd.isMemberOfTeachersValuationManagers(person)) {
	    return true;
	} else {
	    for (TeacherServiceDistribution son : tsd.getChilds()) {
		if (hasPermissionToTeachersValuation(son, person)) {
		    return true;
		}
	    }
	}

	return false;
    }

    public Boolean hasPermissionToCoursesValuation(Person person) {
	return hasPermissionToCoursesValuation(getCurrentTSDProcessPhase().getRootTSD(), person);
    }

    public boolean hasPermissionToCoursesValuation(TeacherServiceDistribution tsd, Person person) {
	if (tsd.isMemberOfCoursesValuationManagers(person)) {
	    return true;
	} else {
	    for (TeacherServiceDistribution son : tsd.getChilds()) {
		if (hasPermissionToCoursesValuation(son, person)) {
		    return true;
		}
	    }
	}

	return false;
    }

    public boolean hasPermissionToTeacherManagement(Person person) {
	return hasPermissionToTeacherManagement(getCurrentTSDProcessPhase().getRootTSD(), person);
    }

    public boolean hasPermissionToCourseManagement(Person person) {
	return hasPermissionToCourseManagement(getCurrentTSDProcessPhase().getRootTSD(), person);
    }

    public boolean hasPermissionToTeacherManagement(TeacherServiceDistribution tsd, Person person) {
	if (tsd.isMemberOfTeachersManagementGroup(person)) {
	    return true;
	} else {
	    for (TeacherServiceDistribution son : tsd.getChilds()) {
		if (hasPermissionToTeacherManagement(son, person)) {
		    return true;
		}
	    }
	}
	return false;
    }

    public boolean hasPermissionToCourseManagement(TeacherServiceDistribution tsd, Person person) {
	if (tsd.isMemberOfCoursesManagementGroup(person)) {
	    return true;
	} else {
	    for (TeacherServiceDistribution son : tsd.getChilds()) {
		if (hasPermissionToCourseManagement(son, person)) {
		    return true;
		}
	    }
	}
	return false;
    }

    public void delete() {
	for (TSDProcessPhase phase : getTSDProcessPhases()) {
	    phase.deleteDataAndPhase();
	}
	for (ExecutionSemester executionSemester : getExecutionPeriods()) {
	    removeExecutionPeriods(executionSemester);
	}

	removeCreator();
	removeDepartment();
	removeRootDomainObject();
	super.deleteDomainObject();
    }

}
