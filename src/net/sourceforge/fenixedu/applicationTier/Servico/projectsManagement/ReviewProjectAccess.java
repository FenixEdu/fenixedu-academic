/*
 * Created on Jan 11, 2005
 */

package net.sourceforge.fenixedu.applicationTier.Servico.projectsManagement;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Role;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.projectsManagement.ProjectAccess;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTierOracle.IPersistentSuportOracle;
import net.sourceforge.fenixedu.persistenceTierOracle.Oracle.PersistentSuportOracle;

/**
 * @author Susana Fernandes
 */
public class ReviewProjectAccess extends Service {

    public void run(Person person, String costCenter, String userNumber) throws FenixServiceException,
            ExcepcaoPersistencia {
        Role role = Role.getRoleByRoleType(RoleType.PROJECTS_MANAGER);
        if (ProjectAccess.getAllByPersonAndCostCenter(person, false, true).size() == 0) {
            Teacher teacher = person.getTeacher();
            if (teacher == null) {
                Employee employee = person.getEmployee();
                if (employee != null) {
                    IPersistentSuportOracle persistentSupportOracle = PersistentSuportOracle
                            .getInstance();
                    if ((persistentSupportOracle.getIPersistentProject().countUserProject(
                            employee.getEmployeeNumber()) == 0)) {
                        List<ProjectAccess> accessesToDelete = ProjectAccess
                                .getAllByPersonAndCostCenter(person, false, false);

                        for (ProjectAccess access : accessesToDelete) {
                            access.delete();
                        }

                        person.removePersonRoles(role);
                    }
                } else
                    throw new FenixServiceException();
            }
        }

        role = Role.getRoleByRoleType(RoleType.INSTITUCIONAL_PROJECTS_MANAGER);
        if (ProjectAccess.getAllByPersonAndCostCenter(person, true, true).size() == 0) {
            Teacher teacher = person.getTeacher();
            if (teacher == null) {
                Employee employee = person.getEmployee();
                if (employee != null) {
                    IPersistentSuportOracle persistentSupportOracle = PersistentSuportOracle
                            .getInstance();
                    if ((persistentSupportOracle.getIPersistentProject().countUserProject(
                            employee.getEmployeeNumber()) == 0)) {
                        List<ProjectAccess> accessesToDelete = ProjectAccess
                                .getAllByPersonAndCostCenter(person, true, false);

                        for (ProjectAccess access : accessesToDelete) {
                            access.delete();
                        }

                        person.removePersonRoles(role);
                    }
                } else
                    throw new FenixServiceException();
            }
        }
    }
}