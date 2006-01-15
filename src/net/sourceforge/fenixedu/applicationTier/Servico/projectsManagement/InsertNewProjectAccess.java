/*
 * Created on Jan 11, 2005
 */

package net.sourceforge.fenixedu.applicationTier.Servico.projectsManagement;

import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.domain.DomainFactory;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Role;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.projectsManagement.Project;
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

    public void run(String userView, String costCenter, String username, GregorianCalendar beginDate, GregorianCalendar endDate, String userNumber)
            throws ExcepcaoPersistencia {
        ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
        Person person = sp.getIPessoaPersistente().lerPessoaPorUsername(username);
        if (person == null)
            throw new IllegalArgumentException();
        sp.getIPersistentProjectAccess().deleteByPersonAndDate(person);
        IPersistentSuportOracle po = PersistentSuportOracle.getInstance();
        Integer coordinatorCode = new Integer(userNumber);
        Boolean isCostCenter = Boolean.FALSE;
        RoleType roleType = RoleType.PROJECTS_MANAGER;
        if (costCenter != null && !costCenter.equals("")) {
            roleType = RoleType.INSTITUCIONAL_PROJECTS_MANAGER;
            isCostCenter = Boolean.TRUE;
        }
        if (!hasProjectsManagerRole(person, roleType)) {
            person.getPersonRoles().add(sp.getIPersistentRole().readByRoleType(roleType));
        }
        List projectCodes = sp.getIPersistentProjectAccess().readProjectCodesByPersonUsernameAndCoordinator(username, coordinatorCode, true);
        List projectList = po.getIPersistentProject().readByCoordinatorAndNotProjectsCodes(coordinatorCode, projectCodes);

        for (int i = 0; i < projectList.size(); i++) {
            Project project = (Project) projectList.get(i);
            if (sp.getIPersistentProjectAccess().countByPersonAndProject(person, new Integer(project.getProjectCode())) != 0)
                throw new IllegalArgumentException();
            ProjectAccess projectAccess = DomainFactory.makeProjectAccess();
            projectAccess.setPerson(person);
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
        Person person = sp.getIPessoaPersistente().lerPessoaPorUsername(username);
        if (person == null)
            throw new IllegalArgumentException();
        sp.getIPersistentProjectAccess().deleteByPersonAndDate(person);

        Boolean isCostCenter = Boolean.FALSE;
        RoleType roleType = RoleType.PROJECTS_MANAGER;
        if (costCenter != null && !costCenter.equals("")) {
            roleType = RoleType.INSTITUCIONAL_PROJECTS_MANAGER;
            isCostCenter = Boolean.TRUE;
        }

        if (!hasProjectsManagerRole(person, roleType)) {
            person.getPersonRoles().add(sp.getIPersistentRole().readByRoleType(roleType));
        }

        for (int i = 0; i < projectCodes.length; i++) {
            if (sp.getIPersistentProjectAccess().countByPersonAndProject(person, new Integer(projectCodes[i])) != 0)
                throw new IllegalArgumentException();

            ProjectAccess projectAccess = DomainFactory.makeProjectAccess();
            projectAccess.setPerson(person);
            projectAccess.setKeyProjectCoordinator(new Integer(userNumber));
            projectAccess.setKeyProject(new Integer(projectCodes[i]));
            projectAccess.setBeginDate(beginDate);
            projectAccess.setEndDate(endDate);
            projectAccess.setCostCenter(isCostCenter);
        }
    }

    private boolean hasProjectsManagerRole(Person person, RoleType roleType) {
        Iterator iterator = person.getPersonRoles().iterator();
        while (iterator.hasNext())
            if (((Role) iterator.next()).getRoleType().equals(roleType))
                return true;
        return false;
    }

}