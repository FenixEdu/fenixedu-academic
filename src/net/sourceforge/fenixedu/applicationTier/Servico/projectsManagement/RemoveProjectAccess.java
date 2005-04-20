/*
 * Created on Jan 11, 2005
 */

package net.sourceforge.fenixedu.applicationTier.Servico.projectsManagement;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.IEmployee;
import net.sourceforge.fenixedu.domain.IPerson;
import net.sourceforge.fenixedu.domain.IRole;
import net.sourceforge.fenixedu.domain.ITeacher;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.projectsManagement.IProjectAccess;
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
public class RemoveProjectAccess implements IService {

    public void run(String personUsername, Integer projectCode) throws ExcepcaoPersistencia {
        ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
        IPersistentProjectAccess persistentProjectAccess = sp.getIPersistentProjectAccess();
        IPerson person = sp.getIPessoaPersistente().lerPessoaPorUsername(personUsername);
        if (persistentProjectAccess.countByPerson(person) == 1) {
            IPersistentSuportOracle persistentSuportOracle = PersistentSuportOracle.getInstance();
            if (persistentSuportOracle.getIPersistentProject().countUserProject(getUserNumber(sp, person)) == 0) {
                sp.getIPessoaPersistente().simpleLockWrite(person);

                List oldRolesList = (List) person.getPersonRoles();
                person.setPersonRoles(new ArrayList());
                for (int i = 0; i < oldRolesList.size(); i++) {
                    IRole role = (IRole) oldRolesList.get(i);
                    if (!role.getRoleType().equals(RoleType.PROJECTS_MANAGER)) {
                        person.getPersonRoles().add(role);
                    }
                }
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