/*
 * Created on Jan 11, 2005
 */

package net.sourceforge.fenixedu.applicationTier.Servico.projectsManagement;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Role;
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
	IPersistentSuportOracle persistentSupportOracle = PersistentSuportOracle.getProjectDBInstance();

	Role role = Role.getRoleByRoleType(RoleType.PROJECTS_MANAGER);
	if (ProjectAccess.getAllByPersonAndCostCenter(person, false, true).size() == 0) {
	    Integer personNumber = getPersonNumber(person);
	    if (personNumber == null) {
		throw new FenixServiceException();
	    }
	    if ((persistentSupportOracle.getIPersistentProject().countUserProject(personNumber) == 0)) {
		cleanProjectsAccess(person, personNumber, role, false, false);
	    }
	}
	role = Role.getRoleByRoleType(RoleType.INSTITUCIONAL_PROJECTS_MANAGER);
	if (ProjectAccess.getAllByPersonAndCostCenter(person, true, true).size() == 0) {
	    Integer personNumber = getPersonNumber(person);
	    if (personNumber == null) {
		throw new FenixServiceException();
	    }
	    if ((persistentSupportOracle.getIPersistentProjectUser().getInstitucionalProjectCoordId(
		    personNumber).size() == 0)) {
		cleanProjectsAccess(person, personNumber, role, true, false);
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

    private void cleanProjectsAccess(Person person, Integer number, Role role, boolean isCostCenter,
	    boolean limitDates) throws FenixServiceException, ExcepcaoPersistencia {
	List<ProjectAccess> accessesToDelete = ProjectAccess.getAllByPersonAndCostCenter(person,
		isCostCenter, limitDates);
	for (ProjectAccess access : accessesToDelete) {
	    access.delete();
	}
	person.removePersonRoles(role);
    }
}