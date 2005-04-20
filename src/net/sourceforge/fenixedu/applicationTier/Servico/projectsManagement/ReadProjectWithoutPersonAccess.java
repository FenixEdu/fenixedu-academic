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

    public List run(String userView, String username) throws ExcepcaoPersistencia {
        ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
        Integer coordinatorId = (sp.getIPersistentTeacher().readTeacherByUsername(userView)).getTeacherNumber();
        List projectCodes = sp.getIPersistentProjectAccess().readProjectCodesByPersonUsernameAndCoordinator(username, coordinatorId, true);
        IPersistentSuportOracle persistentSuportOracle = PersistentSuportOracle.getInstance();
        List projectList = persistentSuportOracle.getIPersistentProject().readByCoordinatorAndNotProjectsCodes(coordinatorId, projectCodes);
        List infoProjects = new ArrayList();
        for (int i = 0; i < projectList.size(); i++)
            infoProjects.add(InfoProject.newInfoFromDomain((IProject) projectList.get(i)));
        return infoProjects;
    }

}