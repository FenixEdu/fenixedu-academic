/*
 * Created on Jan 11, 2005
 */

package net.sourceforge.fenixedu.applicationTier.Servico.projectsManagement;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.dataTransferObject.projectsManagement.InfoProject;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Role;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.projectsManagement.ProjectAccess;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTierOracle.BackendInstance;
import net.sourceforge.fenixedu.persistenceTierOracle.Oracle.PersistentProject;

/**
 * @author Susana Fernandes
 */
public class InsertNewProjectAccess extends FenixService {

    public void run(String userView, String costCenter, String username, GregorianCalendar beginDate, GregorianCalendar endDate,
            final BackendInstance instance, String userNumber) throws ExcepcaoPersistencia {
        Person person = Person.readPersonByUsername(username);
        if (person == null) {
            throw new IllegalArgumentException();
        }

        // deletePastProjectAccesses(person);

        Integer coordinatorCode = new Integer(userNumber);
        Boolean isCostCenter = setProjectsRoles(person, costCenter, instance);

        List<String> projectCodes = new ArrayList<String>();

        for (ProjectAccess projectAccess : person.readProjectAccessesByCoordinator(coordinatorCode, instance)) {
            projectCodes.add(projectAccess.getKeyProject());
        }

        PersistentProject persistentProject = new PersistentProject();
        List<InfoProject> projectList =
                persistentProject.readByCoordinatorAndNotProjectsCodes(coordinatorCode, projectCodes, instance);

        for (InfoProject project : projectList) {
            if (ProjectAccess.getByPersonAndProject(person, project.getProjectCode(), instance) != null) {
                throw new IllegalArgumentException();
            }
            ProjectAccess projectAccess = new ProjectAccess();
            projectAccess.setPerson(person);
            projectAccess.setKeyProjectCoordinator(coordinatorCode);
            projectAccess.setKeyProject(project.getProjectCode());
            projectAccess.setBeginDate(beginDate);
            projectAccess.setEndDate(endDate);
            projectAccess.setCostCenter(isCostCenter);
            projectAccess.setInstance(instance);
            if (instance == BackendInstance.IT) {
                projectAccess.setItProject(true);
            } else if (instance == BackendInstance.IST) {
                projectAccess.setItProject(false);
            }
        }

    }

    public void run(String userView, String costCenter, String username, String[] projectCodes, GregorianCalendar beginDate,
            GregorianCalendar endDate, BackendInstance instance, String userNumber) {
        Person person = Person.readPersonByUsername(username);
        if (person == null) {
            throw new IllegalArgumentException();
        }

        Boolean isCostCenter = setProjectsRoles(person, costCenter, instance);

        for (String projectCode : projectCodes) {
            ProjectAccess projectAccess = getPersonOldProjectAccess(person, projectCode, instance);
            if (projectAccess == null) {
                projectAccess = new ProjectAccess();
                projectAccess.setPerson(person);
                projectAccess.setKeyProjectCoordinator(new Integer(userNumber));
                projectAccess.setKeyProject(projectCode);
                projectAccess.setCostCenter(isCostCenter);
                projectAccess.setInstance(instance);
                if (instance == BackendInstance.IT) {
                    projectAccess.setItProject(true);
                } else if (instance == BackendInstance.IST) {
                    projectAccess.setItProject(false);
                }
            }
            projectAccess.setBeginDate(beginDate);
            projectAccess.setEndDate(endDate);
        }

        // deletePastProjectAccesses(person);
    }

    private ProjectAccess getPersonOldProjectAccess(Person person, String projectCode, final BackendInstance instance) {
        for (ProjectAccess projectAccess : person.getProjectAccesses()) {
            if (projectAccess.getKeyProject().equals(projectCode) && projectAccess.getInstance() == instance) {
                return projectAccess;
            }
        }
        return null;
    }

    private Boolean setProjectsRoles(Person person, String costCenter, final BackendInstance instance) {
        final boolean isCostCenter = costCenter != null && !costCenter.equals("");
        final RoleType roleType = isCostCenter ? instance.institutionalRoleType : instance.roleType;

        if (!hasProjectsManagerRole(person, roleType)) {
            person.getPersonRoles().add(Role.getRoleByRoleType(roleType));
        }
        return isCostCenter;
    }

    // private void deletePastProjectAccesses(Person person) {
    // List<ProjectAccess> projectAccessesToRemove = new
    // ArrayList<ProjectAccess>();
    // Date currentDate = Calendar.getInstance().getTime();
    //
    // for (ProjectAccess projectAccess : person.getProjectAccesses()) {
    // if (projectAccess.getEnd().before(currentDate)) {
    // projectAccessesToRemove.add(projectAccess);
    // }
    // }
    //
    // for (ProjectAccess projectAccess : projectAccessesToRemove) {
    // projectAccess.delete();
    // }
    // }

    private boolean hasProjectsManagerRole(Person person, RoleType roleType) {
        Iterator iterator = person.getPersonRoles().iterator();
        while (iterator.hasNext()) {
            if (((Role) iterator.next()).getRoleType().equals(roleType)) {
                return true;
            }
        }
        return false;
    }

}