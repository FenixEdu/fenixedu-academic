/*
 * Created on Jan 11, 2005
 */

package net.sourceforge.fenixedu.applicationTier.Servico.projectsManagement;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.dataTransferObject.projectsManagement.InfoProject;
import net.sourceforge.fenixedu.domain.projectsManagement.ProjectAccess;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTierOracle.BackendInstance;
import net.sourceforge.fenixedu.persistenceTierOracle.Oracle.PersistentProject;

import org.apache.commons.lang.StringUtils;

/**
 * @author Susana Fernandes
 */
public class ReadUserProjects extends FenixService {

    public List<InfoProject> run(String username, String costCenter, Boolean all, BackendInstance instance, String userNumber)
            throws ExcepcaoPersistencia {
        List<InfoProject> infoProjectList = new ArrayList<InfoProject>();

        List<String> projectCodes = new ArrayList<String>();
        List<Integer> costCenterCodes = new ArrayList<Integer>();
        List<ProjectAccess> accesses = ProjectAccess.getAllByPersonUsernameAndDatesAndCostCenter(username, costCenter, instance);
        for (ProjectAccess access : accesses) {
            String keyProject = access.getKeyProject();
            if (keyProject == null && access.getCostCenter() && !costCenterCodes.contains(access.getKeyProjectCoordinator())) {
                costCenterCodes.add(access.getKeyProjectCoordinator());
            } else if (!projectCodes.contains(keyProject)) {
                projectCodes.add(keyProject);
            }
        }
        PersistentProject persistentProject = new PersistentProject();

        if (StringUtils.isEmpty(costCenter) || costCenter.equals(userNumber)) {
            infoProjectList = persistentProject.readByUserLogin(userNumber, instance);
        }

        if (all) {
            infoProjectList.addAll(persistentProject.readByProjectsCodes(projectCodes, instance));
            for (Integer ccCode : costCenterCodes) {
                infoProjectList.addAll(persistentProject.readByCoordinatorAndNotProjectsCodes(ccCode, null, instance));
            }
        }
        return infoProjectList;
    }
}