/*
 * Created on Jan 11, 2005
 */

package net.sourceforge.fenixedu.applicationTier.Servico.projectsManagement;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Role;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.projectsManagement.ProjectAccess;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTierOracle.Oracle.PersistentProject;
import net.sourceforge.fenixedu.persistenceTierOracle.Oracle.PersistentProjectUser;

/**
 * @author Susana Fernandes
 */
public class ReviewProjectAccess extends FenixService {

    public void run(Person person, String costCenter, Boolean it, String userNumber) throws FenixServiceException,
	    ExcepcaoPersistencia {

	Role role = Role.getRoleByRoleType(RoleType.PROJECTS_MANAGER);
	if (it) {
	    role = Role.getRoleByRoleType(RoleType.IT_PROJECTS_MANAGER);
	}
	if (ProjectAccess.getAllByPersonAndCostCenter(person, false, true, it).size() == 0) {
	    Integer personNumber = getPersonNumber(person);
	    if (personNumber == null) {
		throw new FenixServiceException();
	    }
	    if ((new PersistentProject().countUserProject(personNumber, it) == 0)) {
		cleanProjectsAccess(person, personNumber, role, false, false, it);
	    }
	}
	role = Role.getRoleByRoleType(RoleType.INSTITUCIONAL_PROJECTS_MANAGER);
	if (ProjectAccess.getAllByPersonAndCostCenter(person, true, true, false).size() == 0) {
	    Integer personNumber = getPersonNumber(person);
	    if (personNumber == null) {
		throw new FenixServiceException();
	    }
	    if ((new PersistentProjectUser().getInstitucionalProjectCoordId(personNumber, false).size() == 0)) {
		cleanProjectsAccess(person, personNumber, role, true, false, false);
	    }
	}
    }

    private Integer getPersonNumber(Person person) {
	if (person.getTeacher() != null) {
	    return person.getTeacher().getTeacherNumber();
	}
	if (person.getEmployee() != null) {
	    return person.getEmployee().getEmployeeNumber();
	}
	return null;
    }

    private void cleanProjectsAccess(Person person, Integer number, Role role, boolean isCostCenter, boolean limitDates,
	    boolean it) throws FenixServiceException {
	// List<ProjectAccess> accessesToDelete =
	// ProjectAccess.getAllByPersonAndCostCenter(person, isCostCenter,
	// limitDates, it);
	// for (ProjectAccess access : accessesToDelete) {
	// access.delete();
	// }
	person.removePersonRoles(role);
    }
}