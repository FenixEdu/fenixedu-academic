/*
 * Created on Jan 11, 2005
 */

package net.sourceforge.fenixedu.applicationTier.Servico.projectsManagement;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.projectsManagement.InfoProject;
import net.sourceforge.fenixedu.dataTransferObject.projectsManagement.InfoProjectAccess;
import net.sourceforge.fenixedu.domain.IEmployee;
import net.sourceforge.fenixedu.domain.IPerson;
import net.sourceforge.fenixedu.domain.ITeacher;
import net.sourceforge.fenixedu.domain.projectsManagement.IProjectAccess;
import net.sourceforge.fenixedu.applicationTier.Servico.ExcepcaoInexistente;
import net.sourceforge.fenixedu.applicationTier.Servico.UserView;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.SuportePersistenteOJB;
import net.sourceforge.fenixedu.persistenceTierOracle.IPersistentSuportOracle;
import net.sourceforge.fenixedu.persistenceTierOracle.Oracle.PersistentSuportOracle;
import pt.utl.ist.berserk.logic.serviceManager.IService;

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
            IPerson person = sp.getIPessoaPersistente().lerPessoaPorUsername(username);
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

    public InfoProjectAccess run(Integer personCode, Integer projectCode) throws ExcepcaoPersistencia {
        ISuportePersistente sp = SuportePersistenteOJB.getInstance();
        IPersistentSuportOracle spOracle = PersistentSuportOracle.getInstance();
        InfoProjectAccess infoProjectAccess = InfoProjectAccess.newInfoFromDomain(sp.getIPersistentProjectAccess()
                .readByPersonIdAndProjectAndDate(personCode, projectCode));
        infoProjectAccess.setInfoProject(InfoProject.newInfoFromDomain(spOracle.getIPersistentProject()
                .readProject(infoProjectAccess.getKeyProject())));
        return infoProjectAccess;
    }

}