/*
 * Created on Jan 11, 2005
 */

package net.sourceforge.fenixedu.applicationTier.Servico.projectsManagement;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.projectsManagement.InfoProject;
import net.sourceforge.fenixedu.domain.projectsManagement.IProject;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.persistenceTierOracle.IPersistentSuportOracle;
import net.sourceforge.fenixedu.persistenceTierOracle.Oracle.PersistentSuportOracle;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Susana Fernandes
 */
public class ReadProjectWithoutPersonAccess implements IService {

    public ReadProjectWithoutPersonAccess() {
    }

    public List run(String userName, String costCenter, String newUserName, String userNumber) throws ExcepcaoPersistencia {
        ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
        Integer coordinatorId = new Integer(userNumber);
        List projectCodes = sp.getIPersistentProjectAccess().readProjectCodesByPersonUsernameAndCoordinator(newUserName, coordinatorId, true);
        IPersistentSuportOracle persistentSuportOracle = PersistentSuportOracle.getInstance();
        List<IProject> projectList = persistentSuportOracle.getIPersistentProject().readByCoordinatorAndNotProjectsCodes(coordinatorId, projectCodes);
        List<InfoProject> infoProjects = new ArrayList<InfoProject>();
        for (IProject project : projectList) {
            infoProjects.add(InfoProject.newInfoFromDomain(project));
        }
        return infoProjects;
    }

}