/*
 * Created on Jan 11, 2005
 */

package ServidorAplicacao.Servico.projectsManagement;

import java.util.ArrayList;
import java.util.List;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.projectsManagement.InfoProject;
import DataBeans.projectsManagement.InfoProjectAccess;
import Dominio.IEmployee;
import Dominio.IPessoa;
import Dominio.ITeacher;
import Dominio.projectsManagement.IProjectAccess;
import ServidorAplicacao.Servico.ExcepcaoInexistente;
import ServidorAplicacao.Servico.UserView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.InvalidArgumentsServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import ServidorPersistenteOracle.IPersistentSuportOracle;
import ServidorPersistenteOracle.Oracle.PersistentSuportOracle;

/**
 * @author Susana Fernandes
 */
public class ReadProjectAccesses implements IService {

    public List run(UserView userView) throws FenixServiceException, ExcepcaoPersistencia {
        ISuportePersistente sp = SuportePersistenteOJB.getInstance();
        ITeacher personCoordinator = sp.getIPersistentTeacher().readTeacherByUsername(userView.getUtilizador());
        if (personCoordinator == null)
            throw new InvalidArgumentsServiceException();

        IPersistentSuportOracle persistentSuportOracle = PersistentSuportOracle.getInstance();
        if (persistentSuportOracle.getIPersistentProject().countUserProject(personCoordinator.getTeacherNumber()) == 0)
            throw new InvalidArgumentsServiceException();

        List projectAcessesList = sp.getIPersistentProjectAccess().readByCoordinator(personCoordinator.getTeacherNumber());
        List infoProjectAcessesList = new ArrayList();
        for (int i = 0; i < projectAcessesList.size(); i++) {
            InfoProjectAccess infoProjectAccess = InfoProjectAccess.newInfoFromDomain((IProjectAccess) projectAcessesList.get(i));
            infoProjectAccess.setInfoProject(InfoProject.newInfoFromDomain(persistentSuportOracle.getIPersistentProject().readProject(
                    infoProjectAccess.getKeyProject())));
            infoProjectAcessesList.add(infoProjectAccess);
        }

        return infoProjectAcessesList;
    }

    public List run(UserView userView, String username) throws FenixServiceException, ExcepcaoPersistencia {
        ISuportePersistente sp = SuportePersistenteOJB.getInstance();

        ITeacher personTeacher = sp.getIPersistentTeacher().readTeacherByUsername(username);
        if (personTeacher == null) {
            IPessoa person = sp.getIPessoaPersistente().lerPessoaPorUsername(username);
            if (person == null)
                throw new ExcepcaoInexistente();
            IEmployee employee = sp.getIPersistentEmployee().readByPerson(person.getIdInternal().intValue());
            if (employee == null)
                throw new InvalidArgumentsServiceException();
        }

        ITeacher personCoordinator = sp.getIPersistentTeacher().readTeacherByUsername(userView.getUtilizador());

        List projectAcessesList = sp.getIPersistentProjectAccess().readByPersonUsernameAndCoordinatorAndDate(username,
                personCoordinator.getTeacherNumber());
        List infoProjectAcessesList = new ArrayList();
        IPersistentSuportOracle persistentSuportOracle = PersistentSuportOracle.getInstance();
        for (int i = 0; i < projectAcessesList.size(); i++) {
            InfoProjectAccess infoProjectAccess = InfoProjectAccess.newInfoFromDomain((IProjectAccess) projectAcessesList.get(i));
            infoProjectAccess.setInfoProject(InfoProject.newInfoFromDomain(persistentSuportOracle.getIPersistentProject().readProject(
                    infoProjectAccess.getKeyProject())));
            infoProjectAcessesList.add(infoProjectAccess);
        }
        return infoProjectAcessesList;
    }

    public InfoProjectAccess run(Integer personCode, Integer projectCode) throws FenixServiceException, ExcepcaoPersistencia {
        ISuportePersistente sp = SuportePersistenteOJB.getInstance();
        IPersistentSuportOracle spOracle = PersistentSuportOracle.getInstance();
        InfoProjectAccess infoProjectAccess = InfoProjectAccess.newInfoFromDomain((IProjectAccess) sp.getIPersistentProjectAccess()
                .readByPersonIdAndProjectAndDate(personCode, projectCode));
        infoProjectAccess.setInfoProject(InfoProject.newInfoFromDomain(spOracle.getIPersistentProject()
                .readProject(infoProjectAccess.getKeyProject())));
        return infoProjectAccess;
    }

}