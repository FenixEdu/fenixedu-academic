package ServidorAplicacao.Servico.grant.contract;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.grant.contract.InfoGrantCostCenter;
import Dominio.grant.contract.GrantCostCenter;
import Dominio.grant.contract.IGrantCostCenter;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.grant.GrantOrientationTeacherNotFoundException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import ServidorPersistente.grant.IPersistentGrantCostCenter;

/**
 * 
 * @author jpvl
 */

public class ReadCostCenterByNumber implements IService {

 
 
 public InfoGrantCostCenter run(String costContractNumber)
 		throws FenixServiceException {
		//When creating a New Contract its needed to verify if the costContract exists
		//chosen for orientator really exists
		InfoGrantCostCenter infoGrantCostCenter = new InfoGrantCostCenter();
		IGrantCostCenter costCenter = new GrantCostCenter();
		
		try {  
			ISuportePersistente sp = SuportePersistenteOJB.getInstance();
			IPersistentGrantCostCenter pCostContract = sp.getIPersistentGrantCostCenter();
			costCenter = pCostContract.readGrantCostCenterByNumber(costContractNumber);
		    if (costCenter == null)
		        throw new GrantOrientationTeacherNotFoundException();
		    infoGrantCostCenter = InfoGrantCostCenter.newInfoFromDomain(costCenter);
		} catch (ExcepcaoPersistencia persistentException) {
		    throw new FenixServiceException(persistentException.getMessage());
		}
		return infoGrantCostCenter;
	}
}