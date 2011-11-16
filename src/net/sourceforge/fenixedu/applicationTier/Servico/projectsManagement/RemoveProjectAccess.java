/*
 * Created on Jan 11, 2005
 */

package net.sourceforge.fenixedu.applicationTier.Servico.projectsManagement;

import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Role;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.projectsManagement.ProjectAccess;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTierOracle.BackendInstance;
import net.sourceforge.fenixedu.persistenceTierOracle.Oracle.PersistentProject;

import org.joda.time.DateTime;

/**
 * @author Susana Fernandes
 */
public class RemoveProjectAccess extends FenixService {

    public void run(String username, String costCenter, String personUsername, String projectCode, BackendInstance instance, String userNumber)
	    throws ExcepcaoPersistencia {
	final Person person = Person.readPersonByUsername(personUsername);

	final boolean isCostCenter = costCenter != null && !costCenter.equals("");
	final RoleType roleType = isCostCenter ? instance.institutionalRoleType : instance.roleType;

	List<ProjectAccess> projectAccesses = ProjectAccess.getAllByPersonAndCostCenter(person, isCostCenter, true, instance);

	if (projectAccesses.size() == 1) {
	    if (new PersistentProject().countUserProject(getUserNumber(person), instance) == 0) {
		Iterator iter = person.getPersonRolesIterator();
		while (iter.hasNext()) {
		    Role role = (Role) iter.next();
		    if (role.getRoleType().equals(roleType)) {
			iter.remove();
		    }
		}
	    }
	}

	ProjectAccess projectAccess = ProjectAccess.getByPersonAndProject(person, projectCode, instance);
	DateTime yesterday = new DateTime().minusDays(1);
	if (yesterday.isAfter(projectAccess.getBeginDateTime())) {
	    projectAccess.setEndDateTime(yesterday);
	} else {
	    projectAccess.delete();
	}

    }

    private Integer getUserNumber(Person person) {
	if (person.getEmployee() != null) {
	    return person.getEmployee().getEmployeeNumber();
	}
	if (person.getGrantOwner() != null) {
	    return person.getGrantOwner().getNumber();
	}
	return null;
    }
}
