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
import net.sourceforge.fenixedu.domain.projectsManagement.IProjectAccess;
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
public class RemoveProjectAccess implements IService {

    public RemoveProjectAccess() {
    }

    public void run(IUserView userView, String personUsername, Integer projectCode) throws FenixServiceException, ExcepcaoPersistencia {
        ISuportePersistente sp = SuportePersistenteOJB.getInstance();
        IPersistentProjectAccess persistentProjectAccess = sp.getIPersistentProjectAccess();
        IPerson person = sp.getIPessoaPersistente().lerPessoaPorUsername(personUsername);
        if (persistentProjectAccess.countByPerson(person) == 1) {
            IPersistentSuportOracle persistentSuportOracle = PersistentSuportOracle.getInstance();
            if (persistentSuportOracle.getIPersistentProject().countUserProject(getUserNumber(sp, person)) == 0) {
                IRole role = sp.getIPersistentRole().readByRoleType(RoleType.PROJECTS_MANAGER);
                role.setRoleType(RoleType.PROJECTS_MANAGER);
                IPersonRole personRole = sp.getIPersistentPersonRole().readByPersonAndRole(person, role);
                sp.getIPersistentPersonRole().deleteByOID(PersonRole.class, personRole.getIdInternal());
            }
        }
        IProjectAccess projectAccess = persistentProjectAccess.readByPersonIdAndProjectAndDate(person.getIdInternal(), projectCode);
        persistentProjectAccess.delete(projectAccess);
    }

    private Integer getUserNumber(ISuportePersistente sp, IPerson person) throws ExcepcaoPersistencia {
        Integer userNumber = null;
        ITeacher teacher = sp.getIPersistentTeacher().readTeacherByUsername(person.getUsername());
        if (teacher != null)
            userNumber = teacher.getTeacherNumber();
        else {
            IEmployee employee = sp.getIPersistentEmployee().readByPerson(person);
            if (employee != null)
                userNumber = employee.getEmployeeNumber();
        }
        return userNumber;
    }
}