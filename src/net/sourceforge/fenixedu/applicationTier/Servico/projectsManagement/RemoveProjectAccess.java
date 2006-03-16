/*
 * Created on Jan 11, 2005
 */

package net.sourceforge.fenixedu.applicationTier.Servico.projectsManagement;

import java.util.Iterator;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Role;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.projectsManagement.ProjectAccess;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.projectsManagement.IPersistentProjectAccess;
import net.sourceforge.fenixedu.persistenceTierOracle.IPersistentSuportOracle;
import net.sourceforge.fenixedu.persistenceTierOracle.Oracle.PersistentSuportOracle;

/**
 * @author Susana Fernandes
 */
public class RemoveProjectAccess extends Service {

    public void run(String username, String costCenter, String personUsername, Integer projectCode, String userNumber) throws ExcepcaoPersistencia {
        IPersistentProjectAccess persistentProjectAccess = persistentSupport.getIPersistentProjectAccess();
        Person person = Person.readPersonByUsername(personUsername);

        RoleType roleType = RoleType.PROJECTS_MANAGER;
        Boolean isCostCenter = false;
        if (costCenter != null && !costCenter.equals("")) {
            roleType = RoleType.INSTITUCIONAL_PROJECTS_MANAGER;
            isCostCenter = true;
        }

        if (persistentProjectAccess.countByPersonAndCC(person, isCostCenter) == 1) {
            IPersistentSuportOracle persistentSupportOracle = PersistentSuportOracle.getInstance();
            if (persistentSupportOracle.getIPersistentProject().countUserProject(getUserNumber(persistentSupport, person)) == 0) {
                Iterator iter = person.getPersonRolesIterator();
                while (iter.hasNext()) {
                    Role role = (Role) iter.next();
                    if (role.getRoleType().equals(roleType)) {
                        iter.remove();
                    }                    
                }
            }
        }
        ProjectAccess projectAccess = persistentProjectAccess.readByPersonIdAndProjectAndDate(person.getIdInternal(), projectCode);
        persistentProjectAccess.delete(projectAccess);
    }

    private Integer getUserNumber(ISuportePersistente persistentSupport, Person person) throws ExcepcaoPersistencia {
        Integer userNumber = null;
        Teacher teacher = Teacher.readTeacherByUsername(person.getUsername());
        if (teacher != null)
            userNumber = teacher.getTeacherNumber();
        else {
            Employee employee = persistentSupport.getIPersistentEmployee().readByPerson(person);
            if (employee != null)
                userNumber = employee.getEmployeeNumber();
        }
        return userNumber;
    }
}
