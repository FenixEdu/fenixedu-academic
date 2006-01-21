/*
 * Created on Jan 11, 2005
 */

package net.sourceforge.fenixedu.applicationTier.Servico.projectsManagement;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.projectsManagement.InfoRubric;
import net.sourceforge.fenixedu.domain.projectsManagement.IRubric;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTierOracle.Oracle.PersistentSuportOracle;

/**
 * @author Susana Fernandes
 */
public class ReadUserCostCenters extends Service {

    public List run(String username, String costCenter, String userNumber) throws ExcepcaoPersistencia {
        List<InfoRubric> infoCostCenterList = new ArrayList<InfoRubric>();

        PersistentSuportOracle p = PersistentSuportOracle.getInstance();
        List<IRubric> costCenterList = p.getIPersistentProjectUser().getInstitucionalProjectCoordId(new Integer(userNumber));

        costCenterList.addAll(p.getIPersistentProjectUser().getInstitucionalProjectByCCIDs(
                persistentSupport.getIPersistentProjectAccess().readCCCodesByPersonUsernameAndDate(username)));

        for (IRubric cc : costCenterList) {
            infoCostCenterList.add(InfoRubric.newInfoFromDomain(cc));
        }
        return infoCostCenterList;
    }
}