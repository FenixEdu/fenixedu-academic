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
import net.sourceforge.fenixedu.persistenceTierOracle.Oracle.PersistentSuportOracle;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Susana Fernandes
 */
public class ReadUserProjects implements IService {

    public ReadUserProjects() {
    }

    public List run(String username, String costCenter, Boolean all, String userNumber) throws ExcepcaoPersistencia {
        List<InfoProject> infoProjectList = new ArrayList<InfoProject>();

        ISuportePersistente persistentSuport = PersistenceSupportFactory.getDefaultPersistenceSupport();

        PersistentSuportOracle p = PersistentSuportOracle.getInstance();
        List<IProject> projectList = p.getIPersistentProject().readByUserLogin(userNumber);
        if (all)
            projectList.addAll(p.getIPersistentProject().readByProjectsCodes(
                    persistentSuport.getIPersistentProjectAccess().readProjectCodesByPersonUsernameAndDateAndCC(username, costCenter)));
        for (int i = 0; i < projectList.size(); i++)
            infoProjectList.add(InfoProject.newInfoFromDomain((IProject) projectList.get(i)));

        return infoProjectList;
    }
}