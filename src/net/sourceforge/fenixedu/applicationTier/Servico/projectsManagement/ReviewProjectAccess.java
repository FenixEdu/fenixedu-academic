/*
 * Created on Jan 11, 2005
 */

package net.sourceforge.fenixedu.applicationTier.Servico.projectsManagement;

import net.sourceforge.fenixedu.domain.IEmployee;
import net.sourceforge.fenixedu.domain.IPerson;
import net.sourceforge.fenixedu.domain.IPersonRole;
import net.sourceforge.fenixedu.domain.IRole;
import net.sourceforge.fenixedu.domain.ITeacher;
import net.sourceforge.fenixedu.domain.PersonRole;
import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.SuportePersistenteOJB;
import net.sourceforge.fenixedu.persistenceTier.projectsManagement.IPersistentProjectAccess;
import net.sourceforge.fenixedu.persistenceTierOracle.IPersistentSuportOracle;
import net.sourceforge.fenixedu.persistenceTierOracle.Oracle.PersistentSuportOracle;
import net.sourceforge.fenixedu.util.RoleType;
import pt.utl.ist.berserk.logic.serviceManager.IService;

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