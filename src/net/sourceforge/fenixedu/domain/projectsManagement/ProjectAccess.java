/*
 * Created on Jan 12, 2005
 *
 */
package net.sourceforge.fenixedu.domain.projectsManagement;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import net.sourceforge.fenixedu.domain.Identification;
import net.sourceforge.fenixedu.domain.Login;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.persistenceTierOracle.BackendInstance;

import org.joda.time.DateTime;
import org.joda.time.Interval;

/**
 * @author Susana Fernandes
 * 
 */
public class ProjectAccess extends ProjectAccess_Base {

    public ProjectAccess() {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
    }

    /**
     * @return Returns the beginDate.
     */
    public Calendar getBeginDate() {
	if (this.getBegin() != null) {
	    Calendar result = Calendar.getInstance();
	    result.setTime(this.getBegin());
	    return result;
	}
	return null;
    }

    /**
     * @param beginDate
     *            The beginDate to set.
     */
    public void setBeginDate(Calendar beginDate) {
	if (beginDate != null) {
	    this.setBegin(beginDate.getTime());
	} else {
	    this.setBegin(null);
	}
    }

    /**
     * @return Returns the endDate.
     */
    public Calendar getEndDate() {
	if (this.getEnd() != null) {
	    Calendar result = Calendar.getInstance();
	    result.setTime(this.getEnd());
	    return result;
	}
	return null;
    }

    /**
     * @param endDate
     *            The endDate to set.
     */
    public void setEndDate(Calendar endDate) {
	if (endDate != null) {
	    this.setEnd(endDate.getTime());
	} else {
	    this.setEnd(null);
	}
    }

    public Interval getProjectAccessInterval() {
	return new Interval(getBeginDateTime(), getEndDateTime());
    }

    public void delete() {
	removePerson();
	removeRootDomainObject();
	deleteDomainObject();
    }

    private static List<ProjectAccess> getAllByPersonUsernameAndCoordinatorOrCostCenterOrProject(String username,
	    Integer coordinatorCode, Integer costCenter, String projectCode, boolean all, BackendInstance instance) {
	List<ProjectAccess> result = new ArrayList<ProjectAccess>();

	Date currentDate = Calendar.getInstance().getTime();

	outter: for (ProjectAccess projectAccess : RootDomainObject.getInstance().getProjectAccesss()) {

	    if (instance != projectAccess.getInstance()) {
		continue;
	    }

	    if (projectCode != null && !projectCode.equals(projectAccess.getKeyProject())) {
		continue;
	    }

	    if (coordinatorCode != null && !coordinatorCode.equals(projectAccess.getKeyProjectCoordinator())) {
		continue;
	    }

	    if (costCenter != null) {
		if (!costCenter.equals(projectAccess.getKeyProjectCoordinator())) {
		    continue;
		}

		if (!projectAccess.getCostCenter()) {
		    continue;
		}
	    } else {
		if (coordinatorCode == null && projectCode == null && projectAccess.getCostCenter()) {
		    continue;
		}
	    }

	    if (projectAccess.getEnd().before(currentDate)) {
		continue;
	    }

	    // skip projects accesses that begin in the future
	    if (!all && projectAccess.getBegin().after(currentDate)) {
		continue;
	    }

	    for (Identification identification : projectAccess.getPerson().getUser().getIdentifications()) {
		if (identification instanceof Login) {
		    Login login = (Login) identification;
		    if (!login.hasUsername(username)) {
			continue outter;
		    }
		}
	    }
	    result.add(projectAccess);
	}

	return result;
    }

    public static List<ProjectAccess> getAllByPersonUsernameAndCoordinator(String username, Integer coordinatorCode, boolean all,
	    BackendInstance instance) {
	return getAllByPersonUsernameAndCoordinatorOrCostCenterOrProject(username, coordinatorCode, null, null, all, instance);
    }

    public static List<ProjectAccess> getAllByPersonUsernameAndDatesAndCostCenter(String username, String costCenter, BackendInstance instance) {
	Integer costCenterCode = costCenter == null || costCenter.length() == 0 ? null : new Integer(costCenter);

	return getAllByPersonUsernameAndCoordinatorOrCostCenterOrProject(username, null, costCenterCode, null, false, instance);
    }

    public static ProjectAccess getByPersonAndProject(Person person, String projectCode, BackendInstance instance) {
	List<ProjectAccess> projectAccesses = person.getProjectAccesses();

	for (ProjectAccess access : projectAccesses) {
	    if (projectCode.equals(access.getKeyProject()) && access.getInstance() == instance) {
		return access;
	    }
	}

	return null;
    }

    public static ProjectAccess getByUsernameAndProjectCode(String username, String projectCode, BackendInstance instance) {
	List<ProjectAccess> result = getAllByPersonUsernameAndCoordinatorOrCostCenterOrProject(username, null, null, projectCode,
		false, instance);

	if (result.isEmpty()) {
	    return null;
	}

	return result.get(0);
    }

    public static List<ProjectAccess> getAllByCoordinator(Integer coordinatorCode, boolean isCostCenter, BackendInstance instance) {
	List<ProjectAccess> result = new ArrayList<ProjectAccess>();

	Date currentDate = Calendar.getInstance().getTime();

	for (ProjectAccess access : RootDomainObject.getInstance().getProjectAccesss()) {
	    if (access.getInstance() != instance) {
		continue;
	    }
	    if (access.getEnd().before(currentDate)) {
		continue;
	    }
	    if (!access.getKeyProjectCoordinator().equals(coordinatorCode)) {
		continue;
	    }
	    if (!new Boolean(isCostCenter).equals(access.getCostCenter())) {
		continue;
	    }

	    result.add(access);
	}

	return result;
    }

    public static List<ProjectAccess> getAllByPersonAndCostCenter(Person person, boolean isCostCenter, boolean limitDates,
	    BackendInstance instance) {
	List<ProjectAccess> result = new ArrayList<ProjectAccess>();

	Date currentDate = Calendar.getInstance().getTime();

	for (ProjectAccess access : person.getProjectAccesses()) {
	    if (limitDates) {
		if (access.getBegin().after(currentDate)) {
		    continue;
		}

		if (access.getEnd().before(currentDate)) {
		    continue;
		}
	    }

	    if (access.getInstance() != instance) {
		continue;
	    }

	    if (!new Boolean(isCostCenter).equals(access.getCostCenter())) {
		continue;
	    }

	    result.add(access);
	}

	return result;
    }

    public static List<ProjectAccess> getAllByPerson(Person person, BackendInstance instance) {
	List<ProjectAccess> result = new ArrayList<ProjectAccess>();

	DateTime currentDate = new DateTime();

	for (ProjectAccess projectAccess : RootDomainObject.getInstance().getProjectAccesss()) {
	    if (projectAccess.getEndDateTime().isAfter(currentDate) && projectAccess.getBeginDateTime().isBefore(currentDate)) {

		if (projectAccess.getPerson().equals(person) && projectAccess.getInstance() == instance) {
		    result.add(projectAccess);
		}
	    }
	}
	return result;
    }

    public boolean isActive(DateTime date) {
	return ((!getBeginDateTime().isAfter(date)) && (!getEndDateTime().isBefore(date)));
    }

}
