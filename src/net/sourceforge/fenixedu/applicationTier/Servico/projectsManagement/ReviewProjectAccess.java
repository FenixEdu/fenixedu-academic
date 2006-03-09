/*
 * Created on Jan 11, 2005
 */

package net.sourceforge.fenixedu.applicationTier.Servico.projectsManagement;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Role;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.projectsManagement.IPersistentProjectAccess;
import net.sourceforge.fenixedu.persistenceTierOracle.IPersistentSuportOracle;
import net.sourceforge.fenixedu.persistenceTierOracle.Oracle.PersistentSuportOracle;

/**
 * @author Susana Fernandes
 */
public class ReviewProjectAccess extends Service {

    public void run(String username, String costCenter, String userNumber) throws FenixServiceException, ExcepcaoPersistencia {
        IPersistentProjectAccess persistentProjectAccess = persistentSupport.getIPersistentProjectAccess();
        Person person = Person.readPersonByUsername(username);
        Role role = Role.getRoleByRoleType(RoleType.PROJECTS_MANAGER);
        if (persistentProjectAccess.countByPersonAndCC(person, false) == 0) {
            Teacher teacher = persistentSupport.getIPersistentTeacher().readTeacherByUsername(person.getUsername());
            if (teacher == null) {
                Employee employee = persistentSupport.getIPersistentEmployee().readByPerson(person);
                if (employee != null) {
                    IPersistentSuportOracle persistentSupportOracle = PersistentSuportOracle.getInstance();
                    if ((persistentSupportOracle.getIPersistentProject().countUserProject(employee.getEmployeeNumber()) == 0)) {
                        persistentProjectAccess.deleteByPersonAndCC(person, false);
                        person.removePersonRoles(role);
                    }
                } else
                    throw new FenixServiceException();
            }
        }

        role = Role.getRoleByRoleType(RoleType.INSTITUCIONAL_PROJECTS_MANAGER);
        if (persistentProjectAccess.countByPersonAndCC(person, true) == 0) {
            Teacher teacher = persistentSupport.getIPersistentTeacher().readTeacherByUsername(person.getUsername());
            if (teacher == null) {
                Employee employee = persistentSupport.getIPersistentEmployee().readByPerson(person);
                if (employee != null) {
                    IPersistentSuportOracle persistentSupportOracle = PersistentSuportOracle.getInstance();
                    if ((persistentSupportOracle.getIPersistentProject().countUserProject(employee.getEmployeeNumber()) == 0)) {
                        persistentProjectAccess.deleteByPersonAndCC(person, true);
                        person.removePersonRoles(role);
                    }
                } else
                    throw new FenixServiceException();
            }
        }
    }
}