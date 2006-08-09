/*
 * Created on Jan 11, 2005
 */

package net.sourceforge.fenixedu.applicationTier.Servico.projectsManagement;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.projectsManagement.InfoProject;
import net.sourceforge.fenixedu.dataTransferObject.projectsManagement.InfoProjectAccess;
import net.sourceforge.fenixedu.domain.projectsManagement.Project;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTierOracle.IPersistentSuportOracle;
import net.sourceforge.fenixedu.persistenceTierOracle.Oracle.PersistentSuportOracle;

/**
 * @author Susana Fernandes
 */
public class ReadProjectWithoutPersonAccess extends Service {

    public List run(String userName, String costCenter, List<InfoProjectAccess> projectAccessList, String userNumber) throws ExcepcaoPersistencia {
        Integer coordinatorId = new Integer(userNumber);
        
        List<Integer> projectCodes = new ArrayList<Integer>();
        
//        List<ProjectAccess> accesses = ProjectAccess.getAllByPersonUsernameAndDatesAndCostCenter(userName, costCenter);
//        for (ProjectAccess access : accesses) {
//            Integer keyProject = access.getKeyProject();
//            
//            if (! projectCodes.contains(keyProject)) {
//                projectCodes.add(keyProject);
//            }
//        }
        
        for (InfoProjectAccess infoProjectAccess : projectAccessList) {
            projectCodes.add(infoProjectAccess.getKeyProject());
        }
        
        IPersistentSuportOracle persistentSupportOracle = PersistentSuportOracle.getInstance();
        List<Project> projectList = persistentSupportOracle.getIPersistentProject().readByCoordinatorAndNotProjectsCodes(coordinatorId, projectCodes);
        List<InfoProject> infoProjects = new ArrayList<InfoProject>();
        for (Project project : projectList) {
            infoProjects.add(InfoProject.newInfoFromDomain(project));
        }
        return infoProjects;
    }

}