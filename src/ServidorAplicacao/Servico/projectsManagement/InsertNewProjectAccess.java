/*
 * Created on Jan 11, 2005
 */

package ServidorAplicacao.Servico.projectsManagement;

import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import Dominio.IEmployee;
import Dominio.IPersonRole;
import Dominio.IPessoa;
import Dominio.IRole;
import Dominio.ITeacher;
import Dominio.PersonRole;
import Dominio.projectsManagement.IProject;
import Dominio.projectsManagement.IProjectAccess;
import Dominio.projectsManagement.ProjectAccess;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.UserView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import ServidorPersistenteOracle.IPersistentSuportOracle;
import ServidorPersistenteOracle.Oracle.PersistentSuportOracle;
import Util.RoleType;

/**
 * @author Susana Fernandes
 */
public class InsertNewProjectAccess implements IService {

    public InsertNewProjectAccess() {
    }

    public void run(UserView userView, String username, GregorianCalendar beginDate, GregorianCalendar endDate) throws FenixServiceException,
            ExcepcaoPersistencia {
        ISuportePersistente sp = SuportePersistenteOJB.getInstance();
        IPessoa person = sp.getIPessoaPersistente().lerPessoaPorUsername(username);
        if (person == null)
            throw new IllegalArgumentException();
        sp.getIPersistentProjectAccess().deleteByPersonAndDate(person);
        IPersistentSuportOracle po = PersistentSuportOracle.getInstance();
        Integer coordinatorCode = po.getIPersistentProjectUser().getUserCoordId(getUserNumber(sp, userView));
        if (!hasProjectsManagerRole(person)) {
            IRole role = (IRole) sp.getIPersistentRole().readByRoleType(RoleType.PROJECTS_MANAGER);
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

    public void run(UserView userView, String username, String[] projectCodes, GregorianCalendar beginDate, GregorianCalendar endDate)
            throws FenixServiceException, ExcepcaoPersistencia {
        ISuportePersistente sp = SuportePersistenteOJB.getInstance();
        IPessoa person = sp.getIPessoaPersistente().lerPessoaPorUsername(username);
        if (person == null)
            throw new IllegalArgumentException();
        sp.getIPersistentProjectAccess().deleteByPersonAndDate(person);
        if (!hasProjectsManagerRole(person)) {
            IRole role = (IRole) sp.getIPersistentRole().readByRoleType(RoleType.PROJECTS_MANAGER);
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

    private boolean hasProjectsManagerRole(IPessoa person) {
        Iterator iterator = person.getPersonRoles().iterator();
        while (iterator.hasNext())
            if (((IRole) iterator.next()).getRoleType().equals(RoleType.PROJECTS_MANAGER))
                return true;
        return false;
    }

    private Integer getUserNumber(ISuportePersistente sp, IUserView userView) throws ExcepcaoPersistencia {
        Integer userNumber = null;
        ITeacher teacher = (ITeacher) sp.getIPersistentTeacher().readTeacherByUsername(userView.getUtilizador());
        if (teacher != null)
            userNumber = teacher.getTeacherNumber();
        else {
            IPessoa person = sp.getIPessoaPersistente().lerPessoaPorUsername(userView.getUtilizador());
            IEmployee employee = (IEmployee) sp.getIPersistentEmployee().readByPerson(person);
            if (employee != null)
                userNumber = employee.getEmployeeNumber();
        }
        return userNumber;
    }
}