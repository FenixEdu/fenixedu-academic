/*
 * Created on Jan 11, 2005
 */

package ServidorAplicacao.Servico.projectsManagement;

import java.util.ArrayList;
import java.util.List;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.projectsManagement.InfoProject;
import Dominio.projectsManagement.IProject;
import ServidorAplicacao.Servico.UserView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import ServidorPersistenteOracle.IPersistentSuportOracle;
import ServidorPersistenteOracle.Oracle.PersistentSuportOracle;

/**
 * @author Susana Fernandes
 */
public class ReadProjectWithoutPersonAccess implements IService {

    public ReadProjectWithoutPersonAccess() {
    }

    public List run(UserView userView, String username) throws FenixServiceException, ExcepcaoPersistencia {
        ISuportePersistente sp = SuportePersistenteOJB.getInstance();
        Integer coordinatorId = (sp.getIPersistentTeacher().readTeacherByUsername(userView.getUtilizador())).getTeacherNumber();
        List projectCodes = sp.getIPersistentProjectAccess().readProjectCodesByPersonUsernameAndCoordinator(username, coordinatorId, true);
        IPersistentSuportOracle persistentSuportOracle = PersistentSuportOracle.getInstance();
        List projectList = persistentSuportOracle.getIPersistentProject().readByCoordinatorAndNotProjectsCodes(coordinatorId, projectCodes);
        List infoProjects = new ArrayList();
        for (int i = 0; i < projectList.size(); i++)
            infoProjects.add(InfoProject.newInfoFromDomain((IProject) projectList.get(i)));
        return infoProjects;
    }

}