/*
 * Created on Jan 11, 2005
 */

package ServidorAplicacao.Servico.projectsManagement;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import Dominio.IEmployee;
import Dominio.IPersonRole;
import Dominio.IPessoa;
import Dominio.IRole;
import Dominio.ITeacher;
import Dominio.PersonRole;
import Dominio.projectsManagement.IProjectAccess;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import ServidorPersistente.projectsManagement.IPersistentProjectAccess;
import ServidorPersistenteOracle.IPersistentSuportOracle;
import ServidorPersistenteOracle.Oracle.PersistentSuportOracle;
import Util.RoleType;

/**
 * @author Susana Fernandes
 */
public class RemoveProjectAccess implements IService {

    public RemoveProjectAccess() {
    }

    public void run(IUserView userView, String personUsername, Integer projectCode) throws FenixServiceException, ExcepcaoPersistencia {
        ISuportePersistente sp = SuportePersistenteOJB.getInstance();
        IPersistentProjectAccess persistentProjectAccess = sp.getIPersistentProjectAccess();
        IPessoa person = (IPessoa) sp.getIPessoaPersistente().lerPessoaPorUsername(personUsername);
        if (persistentProjectAccess.countByPerson(person) == 1) {
            IPersistentSuportOracle persistentSuportOracle = PersistentSuportOracle.getInstance();
            if (persistentSuportOracle.getIPersistentProject().countUserProject(getUserNumber(sp, person)) == 0) {
                IRole role = (IRole) sp.getIPersistentRole().readByRoleType(RoleType.PROJECTS_MANAGER);
                role.setRoleType(RoleType.PROJECTS_MANAGER);
                IPersonRole personRole = (IPersonRole) sp.getIPersistentPersonRole().readByPersonAndRole(person, role);
                sp.getIPersistentPersonRole().deleteByOID(PersonRole.class, personRole.getIdInternal());
            }
        }
        IProjectAccess projectAccess = persistentProjectAccess.readByPersonIdAndProjectAndDate(person.getIdInternal(), projectCode);
        persistentProjectAccess.delete(projectAccess);
    }

    private Integer getUserNumber(ISuportePersistente sp, IPessoa person) throws ExcepcaoPersistencia {
        Integer userNumber = null;
        ITeacher teacher = (ITeacher) sp.getIPersistentTeacher().readTeacherByUsername(person.getUsername());
        if (teacher != null)
            userNumber = teacher.getTeacherNumber();
        else {
            IEmployee employee = (IEmployee) sp.getIPersistentEmployee().readByPerson(person);
            if (employee != null)
                userNumber = employee.getEmployeeNumber();
        }
        return userNumber;
    }
}