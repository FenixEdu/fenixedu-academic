/*
 * Created on Jan 11, 2005
 */

package ServidorAplicacao.Servico.projectsManagement;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import Dominio.IEmployee;
import Dominio.IPersonRole;
import Dominio.IPerson;
import Dominio.IRole;
import Dominio.ITeacher;
import Dominio.PersonRole;
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
public class ReviewProjectAccess implements IService {

    public ReviewProjectAccess() {
    }

    public void run(IUserView userView) throws FenixServiceException, ExcepcaoPersistencia {
        ISuportePersistente sp = SuportePersistenteOJB.getInstance();
        IPersistentProjectAccess persistentProjectAccess = sp.getIPersistentProjectAccess();
        IPerson person = (IPerson) sp.getIPessoaPersistente().lerPessoaPorUsername(userView.getUtilizador());
        if (persistentProjectAccess.countByPerson(person) == 0) {
            Integer userNumber = null;
            ITeacher teacher = (ITeacher) sp.getIPersistentTeacher().readTeacherByUsername(person.getUsername());
            if (teacher == null) {
                IEmployee employee = (IEmployee) sp.getIPersistentEmployee().readByPerson(person);
                if (employee != null) {
                    IPersistentSuportOracle persistentSuportOracle = PersistentSuportOracle.getInstance();
                    if ((persistentSuportOracle.getIPersistentProject().countUserProject(employee.getEmployeeNumber()) == 0)) {
                        IRole role = (IRole) sp.getIPersistentRole().readByRoleType(RoleType.PROJECTS_MANAGER);
                        role.setRoleType(RoleType.PROJECTS_MANAGER);
                        persistentProjectAccess.deleteByPerson(person);
                        IPersonRole personRole = (IPersonRole) sp.getIPersistentPersonRole().readByPersonAndRole(person, role);
                        if (personRole != null)
                            sp.getIPersistentPersonRole().deleteByOID(PersonRole.class, personRole.getIdInternal());
                    }
                } else
                    throw new FenixServiceException();
            }
        }
    }
}