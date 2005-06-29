/*
 * Created on Jan 11, 2005
 */

package net.sourceforge.fenixedu.applicationTier.Servico.projectsManagement;

import net.sourceforge.fenixedu.dataTransferObject.projectsManagement.InfoRubric;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTierOracle.Oracle.PersistentSuportOracle;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Susana Fernandes
 */
public class ReadCostCenter implements IService {
    public InfoRubric run(String username, String costCenter, String userNumber) throws ExcepcaoPersistencia {
        return InfoRubric.newInfoFromDomain(PersistentSuportOracle.getInstance().getIPersistentProjectUser().getCostCenterByID(
                new Integer(costCenter)));
    }
}