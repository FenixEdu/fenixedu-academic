/*
 * Created on Jan 11, 2005
 */

package net.sourceforge.fenixedu.applicationTier.Servico.projectsManagement;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.dataTransferObject.projectsManagement.InfoProject;
import net.sourceforge.fenixedu.dataTransferObject.projectsManagement.InfoProjectAccess;
import net.sourceforge.fenixedu.domain.projectsManagement.Project;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTierOracle.Oracle.PersistentProject;

/**
 * @author Susana Fernandes
 */
public class ReadProjectWithoutPersonAccess extends FenixService {

    public List run(String userName, String costCenter, List<InfoProjectAccess> projectAccessList, Boolean it, String userNumber)
	    throws ExcepcaoPersistencia {
	Integer coordinatorId = new Integer(userNumber);

	List<Integer> projectCodes = new ArrayList<Integer>();

	for (InfoProjectAccess infoProjectAccess : projectAccessList) {
	    projectCodes.add(infoProjectAccess.getKeyProject());
	}

	List<Project> projectList = new PersistentProject().readByCoordinatorAndNotProjectsCodes(coordinatorId, projectCodes, it);
	List<InfoProject> infoProjects = new ArrayList<InfoProject>();
	for (Project project : projectList) {
	    infoProjects.add(InfoProject.newInfoFromDomain(project));
	}
	return infoProjects;
    }

}