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

import org.joda.time.DateTime;

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

    public void delete() {
        removePerson();
        removeRootDomainObject();
        deleteDomainObject();
    }

    private static List<ProjectAccess> getAllByPersonUsernameAndCoordinatorOrCostCenterOrProject(
            String username, Integer coordinatorCode, Integer costCenter, Integer projectCode,
            boolean all) {
        List<ProjectAccess> result = new ArrayList<ProjectAccess>();

        Date currentDate = Calendar.getInstance().getTime();

        outter: for (ProjectAccess projectAccess : RootDomainObject.getInstance().getProjectAccesss()) {

            if (projectCode != null && !projectCode.equals(projectAccess.getKeyProject())) {
                continue;
            }

            if (coordinatorCode != null
                    && !coordinatorCode.equals(projectAccess.getKeyProjectCoordinator())) {
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

            for (Identification identification : projectAccess.getPerson().getUser()
                    .getIdentifications()) {
                if (!(identification instanceof Login)) {
                    continue outter;
                }

                Login login = (Login) identification;

                if (!login.hasUsername(username)) {
                    continue outter;
                }
            }

            result.add(projectAccess);
        }

        return result;
    }

    public static List<ProjectAccess> getAllByPersonUsernameAndCoordinator(String username,
            Integer coordinatorCode, boolean all) {
        return getAllByPersonUsernameAndCoordinatorOrCostCenterOrProject(username, coordinatorCode,
                null, null, all);
    }

    public static List<ProjectAccess> getAllByPersonUsernameAndDatesAndCostCenter(String username,
            String costCenter) {
        Integer costCenterCode = costCenter == null || costCenter.length() == 0 ? null : new Integer(
                costCenter);

        return getAllByPersonUsernameAndCoordinatorOrCostCenterOrProject(username, null, costCenterCode,
                null, false);
    }

    public static ProjectAccess getByPersonAndProject(Person person, Integer projectCode) {
        List<ProjectAccess> projectAccesses = person.getProjectAccesses();

        for (ProjectAccess access : projectAccesses) {
            if (projectCode.equals(access.getKeyProject())) {
                return access;
            }
        }

        return null;
    }

    public static ProjectAccess getByUsernameAndProjectCode(String username, Integer projectCode) {
        List<ProjectAccess> result = getAllByPersonUsernameAndCoordinatorOrCostCenterOrProject(username,
                null, null, projectCode, false);

        if (result.isEmpty()) {
            return null;
        }

        return result.get(0);
    }

    public static List<ProjectAccess> getAllByCoordinator(Integer coordinatorCode) {
        List<ProjectAccess> result = new ArrayList<ProjectAccess>();

        Date currentDate = Calendar.getInstance().getTime();

        for (ProjectAccess access : RootDomainObject.getInstance().getProjectAccesss()) {
            if (access.getEnd().before(currentDate)) {
                continue;
            }

            if (!access.getKeyProjectCoordinator().equals(coordinatorCode)) {
                continue;
            }

            result.add(access);
        }

        return result;
    }

    public static List<ProjectAccess> getAllByPersonAndCostCenter(Person person, boolean isCostCenter,
            boolean limitDates) {
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

            if (!new Boolean(isCostCenter).equals(access.getCostCenter())) {
                continue;
            }

            result.add(access);
        }

        return result;
    }

    public static List<ProjectAccess> getAllByPerson(Person person) {
        List<ProjectAccess> result = new ArrayList<ProjectAccess>();

        DateTime currentDate = new DateTime();

        for (ProjectAccess projectAccess : RootDomainObject.getInstance().getProjectAccesss()) {
            if (projectAccess.getEndDateTime().isAfter(currentDate)
                    && projectAccess.getBeginDateTime().isBefore(currentDate)) {

                if (projectAccess.getPerson().equals(person)) {
                    result.add(projectAccess);
                }
            }
        }
        return result;
    }

}
