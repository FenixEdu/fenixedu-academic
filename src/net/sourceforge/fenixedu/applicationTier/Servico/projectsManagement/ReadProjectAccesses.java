/*
 * Created on Jan 11, 2005
 */

package net.sourceforge.fenixedu.applicationTier.Servico.projectsManagement;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.dataTransferObject.projectsManagement.InfoProject;
import net.sourceforge.fenixedu.dataTransferObject.projectsManagement.InfoProjectAccess;
import net.sourceforge.fenixedu.domain.projectsManagement.ProjectAccess;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.persistenceTierOracle.IPersistentSuportOracle;
import net.sourceforge.fenixedu.persistenceTierOracle.Oracle.PersistentSuportOracle;

/**
 * @author Susana Fernandes
 */
public class ReadProjectAccesses extends Service {

    public List run(String username, String costCenter, String userNumber) throws FenixServiceException, ExcepcaoPersistencia {
        ISuportePersistente persistentSupport = PersistenceSupportFactory.getDefaultPersistenceSupport();

        IPersistentSuportOracle persistentSupportOracle = PersistentSuportOracle.getInstance();
        if (persistentSupportOracle.getIPersistentProject().countUserProject(new Integer(userNumber)) == 0)
            throw new InvalidArgumentsServiceException();

        List<ProjectAccess> projectAcessesList = persistentSupport.getIPersistentProjectAccess().readByCoordinator(new Integer(userNumber));
        List<InfoProjectAccess> infoProjectAcessesList = new ArrayList<InfoProjectAccess>();
        for (ProjectAccess projectAccess : projectAcessesList) {
            InfoProjectAccess infoProjectAccess = InfoProjectAccess.newInfoFromDomain(projectAccess);
            infoProjectAccess.setInfoProject(InfoProject.newInfoFromDomain(persistentSupportOracle.getIPersistentProject().readProject(
                    infoProjectAccess.getKeyProject())));
            infoProjectAcessesList.add(infoProjectAccess);
        }

        return infoProjectAcessesList;
    }

    public List run(String userView, String costCenter, String username, String userNumber) throws FenixServiceException, ExcepcaoPersistencia {
        ISuportePersistente persistentSupport = PersistenceSupportFactory.getDefaultPersistenceSupport();
        Integer personCoordinator = new Integer(userNumber);
        List<ProjectAccess> projectAcessesList = persistentSupport.getIPersistentProjectAccess().readByPersonUsernameAndCoordinatorAndDate(username,
                personCoordinator);
        List<InfoProjectAccess> infoProjectAcessesList = new ArrayList<InfoProjectAccess>();
        IPersistentSuportOracle persistentSupportOracle = PersistentSuportOracle.getInstance();
        for (ProjectAccess projectAccess : projectAcessesList) {
            InfoProjectAccess infoProjectAccess = InfoProjectAccess.newInfoFromDomain(projectAccess);
            infoProjectAccess.setInfoProject(InfoProject.newInfoFromDomain(persistentSupportOracle.getIPersistentProject().readProject(
                    infoProjectAccess.getKeyProject())));
            infoProjectAcessesList.add(infoProjectAccess);
        }
        return infoProjectAcessesList;
    }

    public InfoProjectAccess run(String username, String costCenter, Integer personCode, Integer projectCode, String userNumber)
            throws ExcepcaoPersistencia {
        ISuportePersistente persistentSupport = PersistenceSupportFactory.getDefaultPersistenceSupport();
        IPersistentSuportOracle spOracle = PersistentSuportOracle.getInstance();
        InfoProjectAccess infoProjectAccess = InfoProjectAccess.newInfoFromDomain(persistentSupport.getIPersistentProjectAccess().readByPersonIdAndProjectAndDate(
                personCode, projectCode));
        infoProjectAccess.setInfoProject(InfoProject.newInfoFromDomain(spOracle.getIPersistentProject()
                .readProject(infoProjectAccess.getKeyProject())));
        return infoProjectAccess;
    }

}