/*
 * Created on Jan 11, 2005
 */

package net.sourceforge.fenixedu.applicationTier.Servico.projectsManagement;

import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.domain.IPerson;
import net.sourceforge.fenixedu.domain.IRole;
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

    public void run(String userView, String costCenter, String username, GregorianCalendar beginDate, GregorianCalendar endDate, String userNumber)
            throws ExcepcaoPersistencia {
        ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
        IPerson person = sp.getIPessoaPersistente().lerPessoaPorUsername(username);
        if (person == null)
            throw new IllegalArgumentException();
        sp.getIPersistentProjectAccess().deleteByPersonAndDate(person);
        IPersistentSuportOracle po = PersistentSuportOracle.getInstance();
        Integer coordinatorCode = new Integer(userNumber);
        Boolean isCostCenter = false;
        RoleType roleType = RoleType.PROJECTS_MANAGER;
        if (costCenter != null && !costCenter.equals("")) {
            roleType = RoleType.INSTITUCIONAL_PROJECTS_MANAGER;
            isCostCenter = true;
        }
        if (!hasProjectsManagerRole(person, roleType)) {
            sp.getIPessoaPersistente().simpleLockWrite(person);
            person.getPersonRoles().add(sp.getIPersistentRole().readByRoleType(roleType));
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
            projectAccess.setCostCenter(isCostCenter);
        }
    }

    public void run(String userView, String costCenter, String username, String[] projectCodes, GregorianCalendar beginDate,
            GregorianCalendar endDate, String userNumber) throws ExcepcaoPersistencia {
        ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
        IPerson person = sp.getIPessoaPersistente().lerPessoaPorUsername(username);
        if (person == null)
            throw new IllegalArgumentException();
        sp.getIPersistentProjectAccess().deleteByPersonAndDate(person);

        Boolean isCostCenter = false;
        RoleType roleType = RoleType.PROJECTS_MANAGER;
        if (costCenter != null && !costCenter.equals("")) {
            roleType = RoleType.INSTITUCIONAL_PROJECTS_MANAGER;
            isCostCenter = true;
        }

        if (!hasProjectsManagerRole(person, roleType)) {
            sp.getIPessoaPersistente().simpleLockWrite(person);
            person.getPersonRoles().add(sp.getIPersistentRole().readByRoleType(roleType));
        }

        for (int i = 0; i < projectCodes.length; i++) {
            if (sp.getIPersistentProjectAccess().countByPersonAndProject(person, new Integer(projectCodes[i])) != 0)
                throw new IllegalArgumentException();

            IPersistentSuportOracle po = PersistentSuportOracle.getInstance();
            IProjectAccess projectAccess = new ProjectAccess();
            sp.getIPersistentProjectAccess().simpleLockWrite(projectAccess);
            projectAccess.setPerson(person);
            projectAccess.setKeyPerson(person.getIdInternal());
            projectAccess.setKeyProjectCoordinator(new Integer(userNumber));
            projectAccess.setKeyProject(new Integer(projectCodes[i]));
            projectAccess.setBeginDate(beginDate);
            projectAccess.setEndDate(endDate);
            projectAccess.setCostCenter(isCostCenter);
        }
    }

    private boolean hasProjectsManagerRole(IPerson person, RoleType roleType) {
        Iterator iterator = person.getPersonRoles().iterator();
        while (iterator.hasNext())
            if (((IRole) iterator.next()).getRoleType().equals(roleType))
                return true;
        return false;
    }

}