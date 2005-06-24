/*
 * Created on Jan 11, 2005
 */

package net.sourceforge.fenixedu.applicationTier.Servico.projectsManagement;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.IEmployee;
import net.sourceforge.fenixedu.domain.IPerson;
import net.sourceforge.fenixedu.domain.IPersonRole;
import net.sourceforge.fenixedu.domain.IRole;
import net.sourceforge.fenixedu.domain.ITeacher;
import net.sourceforge.fenixedu.domain.PersonRole;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.persistenceTier.projectsManagement.IPersistentProjectAccess;
import net.sourceforge.fenixedu.persistenceTierOracle.IPersistentSuportOracle;
import net.sourceforge.fenixedu.persistenceTierOracle.Oracle.PersistentSuportOracle;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Susana Fernandes
 */
public class ReviewProjectAccess implements IService {

    public ReviewProjectAccess() {
    }

    public void run(String username, String costCenter, String userNumber) throws FenixServiceException, ExcepcaoPersistencia {
        ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
        IPersistentProjectAccess persistentProjectAccess = sp.getIPersistentProjectAccess();
        IPerson person = sp.getIPessoaPersistente().lerPessoaPorUsername(username);
        IRole role = sp.getIPersistentRole().readByRoleType(RoleType.PROJECTS_MANAGER);
        if (persistentProjectAccess.countByPersonAndCC(person, false) == 0) {
            ITeacher teacher = sp.getIPersistentTeacher().readTeacherByUsername(person.getUsername());
            if (teacher == null) {
                IEmployee employee = sp.getIPersistentEmployee().readByPerson(person);
                if (employee != null) {
                    IPersistentSuportOracle persistentSuportOracle = PersistentSuportOracle.getInstance();
                    if ((persistentSuportOracle.getIPersistentProject().countUserProject(employee.getEmployeeNumber()) == 0)) {
                        persistentProjectAccess.deleteByPersonAndCC(person, false);
                        IPersonRole personRole = sp.getIPersistentPersonRole().readByPersonAndRole(person, role);
                        if (personRole != null)
                            sp.getIPersistentPersonRole().deleteByOID(PersonRole.class, personRole.getIdInternal());
                    }
                } else
                    throw new FenixServiceException();
            }
        }

        role = sp.getIPersistentRole().readByRoleType(RoleType.INSTITUCIONAL_PROJECTS_MANAGER);
        if (persistentProjectAccess.countByPersonAndCC(person, true) == 0) {
            ITeacher teacher = sp.getIPersistentTeacher().readTeacherByUsername(person.getUsername());
            if (teacher == null) {
                IEmployee employee = sp.getIPersistentEmployee().readByPerson(person);
                if (employee != null) {
                    IPersistentSuportOracle persistentSuportOracle = PersistentSuportOracle.getInstance();
                    if ((persistentSuportOracle.getIPersistentProject().countUserProject(employee.getEmployeeNumber()) == 0)) {
                        persistentProjectAccess.deleteByPersonAndCC(person, true);
                        IPersonRole personRole = sp.getIPersistentPersonRole().readByPersonAndRole(person, role);
                        if (personRole != null)
                            sp.getIPersistentPersonRole().deleteByOID(PersonRole.class, personRole.getIdInternal());
                    }
                } else
                    throw new FenixServiceException();
            }
        }
    }
}