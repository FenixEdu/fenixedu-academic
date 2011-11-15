/*
 * Created on Jan 11, 2005
 */

package net.sourceforge.fenixedu.applicationTier.Servico.projectsManagement;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.dataTransferObject.projectsManagement.InfoRubric;
import net.sourceforge.fenixedu.domain.projectsManagement.IRubric;
import net.sourceforge.fenixedu.domain.projectsManagement.ProjectAccess;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTierOracle.BackendInstance;
import net.sourceforge.fenixedu.persistenceTierOracle.Oracle.PersistentProject;
import net.sourceforge.fenixedu.persistenceTierOracle.Oracle.PersistentProjectUser;

import org.apache.commons.lang.StringUtils;

/**
 * @author Susana Fernandes
 */
public class ReadCoordinators extends FenixService {

    public List run(String username, String costCenter, BackendInstance instance, String userNumber) throws ExcepcaoPersistencia {
	List<InfoRubric> coordinatorsList = new ArrayList<InfoRubric>();

	Integer thisCoordinator = new Integer(userNumber);

	List<Integer> coordinatorsCodes = new ArrayList<Integer>();

	List<ProjectAccess> accesses = ProjectAccess.getAllByPersonUsernameAndDatesAndCostCenter(username, costCenter, instance);
	for (ProjectAccess access : accesses) {
	    Integer keyProjectCoordinator = access.getKeyProjectCoordinator();

	    if (!coordinatorsCodes.contains(keyProjectCoordinator)) {
		coordinatorsCodes.add(keyProjectCoordinator);
	    }
	}

	if (thisCoordinator != null && !coordinatorsCodes.contains(thisCoordinator)) {
	    if ((StringUtils.isEmpty(costCenter)) && new PersistentProject().countUserProject(thisCoordinator, instance) != 0) {
		coordinatorsCodes.add(thisCoordinator);
	    } else if ((!StringUtils.isEmpty(costCenter))) {
		if (new PersistentProjectUser().getInstitucionalProjectByCCIDs(thisCoordinator, BackendInstance.IT).size() != 0) {
		    coordinatorsCodes.add(thisCoordinator);
		}
		if (new PersistentProjectUser().getInstitucionalProjectByCCIDs(thisCoordinator, BackendInstance.IST_ID).size() != 0) {
		    coordinatorsCodes.add(thisCoordinator);
		}
	    }
	}
	PersistentProjectUser persistentProjectUser = new PersistentProjectUser();
	for (int coord = 0; coord < coordinatorsCodes.size(); coord++) {
	    IRubric rubric = persistentProjectUser.readProjectCoordinator(coordinatorsCodes.get(coord), instance);
	    if (rubric != null)
		coordinatorsList.add(InfoRubric.newInfoFromDomain(rubric));
	}

	return coordinatorsList;
    }
}