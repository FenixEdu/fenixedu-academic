/*
 * Created on Jan 11, 2005
 */

package net.sourceforge.fenixedu.applicationTier.Servico.projectsManagement;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.projectsManagement.InfoRubric;
import net.sourceforge.fenixedu.domain.projectsManagement.IRubric;
import net.sourceforge.fenixedu.domain.projectsManagement.ProjectAccess;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTierOracle.Oracle.PersistentSuportOracle;

/**
 * @author Susana Fernandes
 */
public class ReadCoordinators extends Service {

    public ReadCoordinators() {
    }

    public List run(String username, String costCenter, String userNumber) throws ExcepcaoPersistencia {
	List<InfoRubric> coordinatorsList = new ArrayList<InfoRubric>();

	PersistentSuportOracle p = PersistentSuportOracle.getProjectDBInstance();
	Integer thisCoordinator = new Integer(userNumber);

	List<Integer> coordinatorsCodes = new ArrayList<Integer>();

	List<ProjectAccess> accesses = ProjectAccess.getAllByPersonUsernameAndDatesAndCostCenter(username, costCenter);
	for (ProjectAccess access : accesses) {
	    Integer keyProjectCoordinator = access.getKeyProjectCoordinator();

	    if (!coordinatorsCodes.contains(keyProjectCoordinator)) {
		coordinatorsCodes.add(keyProjectCoordinator);
	    }
	}

	if (thisCoordinator != null && !coordinatorsCodes.contains(thisCoordinator))
	    coordinatorsCodes.add(thisCoordinator);
	for (int coord = 0; coord < coordinatorsCodes.size(); coord++) {
	    IRubric rubric = p.getIPersistentProjectUser().readProjectCoordinator(coordinatorsCodes.get(coord));
	    if (rubric != null)
		coordinatorsList.add(InfoRubric.newInfoFromDomain(rubric));
	}

	return coordinatorsList;
    }
}