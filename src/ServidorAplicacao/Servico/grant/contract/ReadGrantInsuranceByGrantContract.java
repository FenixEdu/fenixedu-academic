/*
 * Created on Jun 26, 2004
 */
package ServidorAplicacao.Servico.grant.contract;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.grant.contract.InfoGrantInsurance;
import Dominio.grant.contract.IGrantInsurance;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import ServidorPersistente.grant.IPersistentGrantInsurance;


/**
 * @author Barbosa
 * @author Pica
 */
public class ReadGrantInsuranceByGrantContract implements IService {

    public ReadGrantInsuranceByGrantContract() {
    }

	public InfoGrantInsurance run(Integer idContract) throws FenixServiceException
	{
		IGrantInsurance grantInsurance = null;
		IPersistentGrantInsurance persistentGrantInsurance = null;
		try
		{
			ISuportePersistente sp = SuportePersistenteOJB.getInstance();
			persistentGrantInsurance = sp.getIPersistentGrantInsurance();
			grantInsurance = persistentGrantInsurance.readGrantInsuranceByGrantContract(idContract);
		}
		catch (ExcepcaoPersistencia e)
		{
			throw new FenixServiceException(e.getMessage());
		}

		InfoGrantInsurance infoGrantInsurance = null;
		if(grantInsurance != null) {
			infoGrantInsurance = InfoGrantInsurance.newInfoFromDomain(grantInsurance);
		}
		return infoGrantInsurance;		
	}
    
}
