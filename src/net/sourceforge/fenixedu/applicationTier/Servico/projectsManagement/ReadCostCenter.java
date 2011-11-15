/*
 * Created on Jan 11, 2005
 */

package net.sourceforge.fenixedu.applicationTier.Servico.projectsManagement;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.dataTransferObject.projectsManagement.InfoRubric;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTierOracle.BackendInstance;
import net.sourceforge.fenixedu.persistenceTierOracle.Oracle.PersistentProjectUser;

/**
 * @author Susana Fernandes
 */
public class ReadCostCenter extends FenixService {
    public InfoRubric run(String username, String costCenter, BackendInstance instance, String userNumber) throws ExcepcaoPersistencia {
	return InfoRubric.newInfoFromDomain(new PersistentProjectUser().getCostCenterByID(new Integer(costCenter), instance));
    }
}