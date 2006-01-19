/*
 * Created on Jan 11, 2005
 */

package net.sourceforge.fenixedu.applicationTier.Servico.projectsManagement;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.projectsManagement.InfoRubric;
import net.sourceforge.fenixedu.domain.projectsManagement.IRubric;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.persistenceTierOracle.Oracle.PersistentSuportOracle;
import net.sourceforge.fenixedu.applicationTier.IService;

/**
 * @author Susana Fernandes
 */
public class ReadCoordinators implements IService {

    public ReadCoordinators() {
    }

    public List run(String username, String costCenter, String userNumber) throws ExcepcaoPersistencia {
        List<InfoRubric> coordinatorsList = new ArrayList<InfoRubric>();

        ISuportePersistente persistentSuport = PersistenceSupportFactory.getDefaultPersistenceSupport();

        PersistentSuportOracle p = PersistentSuportOracle.getInstance();
        Integer thisCoordinator = new Integer(userNumber);

        List<Integer> coordinatorsCodes = persistentSuport.getIPersistentProjectAccess().readCoordinatorsCodesByPersonUsernameAndDatesAndCC(username,
                costCenter);
        if (thisCoordinator != null && !coordinatorsCodes.contains(thisCoordinator))
            coordinatorsCodes.add(thisCoordinator);
        for (int coord = 0; coord < coordinatorsCodes.size(); coord++) {
            IRubric rubric = p.getIPersistentProjectUser().readProjectCoordinator(coordinatorsCodes.get(coord));
            if (rubric != null)
                coordinatorsList.add(InfoRubric.newInfoFromDomain(rubric));
        }

        return coordinatorsList;
    }
}