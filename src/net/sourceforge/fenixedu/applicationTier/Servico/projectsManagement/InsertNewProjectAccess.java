/*
 * Created on Jan 11, 2005
 */

package net.sourceforge.fenixedu.applicationTier.Servico.projectsManagement;

import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.domain.IEmployee;
import net.sourceforge.fenixedu.domain.IPerson;
import net.sourceforge.fenixedu.domain.IPersonRole;
import net.sourceforge.fenixedu.domain.IRole;
import net.sourceforge.fenixedu.domain.ITeacher;
import net.sourceforge.fenixedu.domain.PersonRole;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.projectsManagement.IProject;
import net.sourceforge.fenixedu.domain.projectsManagement.IProjectAccess;
import net.sourceforge.fenixedu.domain.projectsManagement.ProjectAccess;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.persistenceTierOracle.IPersistentSuportOracle;
import net.sourceforge.fenixedu.persistenceTierOracle.Oracle.PersistentSuportOracle;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Susana Fernandes
 */
public class InsertNewProjectAccess implements IService {

    public InsertNewProjectAccess() {
    }

    public void run(String userView, String username, GregorianCalendar beginDate, GregorianCalendar endDate) throws ExcepcaoPersistencia {
        ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
        IPerson person = sp.getIPessoaPersistente().lerPessoaPorUsername(username);
        if (person == null)
            throw new IllegalArgumentException();
        sp.getIPersistentProjectAccess().deleteByPersonAndDate(person);
        IPersistentSuportOracle po = PersistentSuportOracle.getInstance();
        Integer coordinatorCode = po.getIPersistentProjectUser().getUserCoordId(getUserNumber(sp, userView));
        if (!hasProjectsManagerRole(person)) {
            IRole role = sp.getIPersistentRole().readByRoleType(RoleType.PROJECTS_MANAGER);
            role.setRoleType(RoleType.PROJECTS_MANAGER);
            IPersonRole personRole = new PersonRole();
            personRole.setPerson(person);
            personRole.setRole(role);
            sp.getIPersistentPersonRole().simpleLockWrite(personRole);
        }
        List projectCodes = sp.getIPersistentProjectAccess().readProjectCodesByPersonUsernameAndCoordinator(username, coordinatorCode, true);
        List projectList = po.getIPersistentProject().readByCoordinatorAndNotProjectsCodes(coordinatorCode, projectCodes);

        for (int i = 0; i < projectList.size(); i++) {
            IProject project = (IProject) projectList.get(i);
            if (sp.getIPersistentProjectAccess().countByPersonAndProject(person, new Integer(project.getProjectCode())) != 0)
                throw new IllegalArgumentException();
            IProjectAccess projectAccess = new ProjectAccess();
            sp.getIPersistentProjectAccess().simpleLockWrite(projectAccess);
            projectAccess.setPerson(person);
            projectAccess.setKeyPerson(person.getIdInternal());
            projectAccess.setKeyProjectCoordinator(coordinatorCode);
            projectAccess.setKeyProject(new Integer(project.getProjectCode()));
            projectAccess.setBeginDate(beginDate);
            projectAccess.setEndDate(endDate);
        }
    }

    public void run(String userView, String username, String[] projectCodes, GregorianCalendar beginDate, GregorianCalendar endDate)
            throws ExcepcaoPersistencia {
        ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
        IPerson person = sp.getIPessoaPersistente().lerPessoaPorUsername(username);
        if (person == null)
            throw new IllegalArgumentException();
        sp.getIPersistentProjectAccess().deleteByPersonAndDate(person);
        if (!hasProjectsManagerRole(person)) {
            IRole role = sp.getIPersistentRole().readByRoleType(RoleType.PROJECTS_MANAGER);
            role.setRoleType(RoleType.PROJECTS_MANAGER);
            IPersonRole personRole = new PersonRole();
            personRole.setPerson(person);
            personRole.setRole(role);
            sp.getIPersistentPersonRole().simpleLockWrite(personRole);
        }

        for (int i = 0; i < projectCodes.length; i++) {
            if (sp.getIPersistentProjectAccess().countByPersonAndProject(person, new Integer(projectCodes[i])) != 0)
                throw new IllegalArgumentException();

            IPersistentSuportOracle po = PersistentSuportOracle.getInstance();
            Integer coordinatorCode = po.getIPersistentProjectUser().getUserCoordId(getUserNumber(sp, userView));
            IProjectAccess projectAccess = new ProjectAccess();
            sp.getIPersistentProjectAccess().simpleLockWrite(projectAccess);
            projectAccess.setPerson(person);
            projectAccess.setKeyPerson(person.getIdInternal());
            projectAccess.setKeyProjectCoordinator(coordinatorCode);
            projectAccess.setKeyProject(new Integer(projectCodes[i]));
            projectAccess.setBeginDate(beginDate);
            projectAccess.setEndDate(endDate);
        }
    }

    private boolean hasProjectsManagerRole(IPerson person) {
        Iterator iterator = person.getPersonRoles().iterator();
        while (iterator.hasNext())
            if (((IRole) iterator.next()).getRoleType().equals(RoleType.PROJECTS_MANAGER))
                return true;
        return false;
    }

    private Integer getUserNumber(ISuportePersistente sp, String userView) throws ExcepcaoPersistencia {
        Integer userNumber = null;
        ITeacher teacher = sp.getIPersistentTeacher().readTeacherByUsername(userView);
        if (teacher != null)
            userNumber = teacher.getTeacherNumber();
        else {
            IPerson person = sp.getIPessoaPersistente().lerPessoaPorUsername(userView);
            IEmployee employee = sp.getIPersistentEmployee().readByPerson(person);
            if (employee != null)
                userNumber = employee.getEmployeeNumber();
        }
        return userNumber;
    }
}