/*
 * Created on Jun 26, 2004
 */
package ServidorAplicacao.Servico.grant.contract;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.grant.contract.InfoGrantInsurance;
import DataBeans.grant.contract.InfoGrantInsuranceWithContract;
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
public class ReadAllGrantInsurancesByGrantContractAndState implements IService {

    public ReadAllGrantInsurancesByGrantContractAndState() {
    }

	public List run(Integer idContract, Integer state) throws FenixServiceException
	{
		List insurances = null;
		IPersistentGrantInsurance persistentGrantInsurance = null;
		try
		{
			ISuportePersistente sp = SuportePersistenteOJB.getInstance();
			persistentGrantInsurance = sp.getIPersistentGrantInsurance();
			insurances = persistentGrantInsurance.readGrantInsuranceByGrantContractAndState(idContract,state);
		}
		catch (ExcepcaoPersistencia e)
		{
			throw new FenixServiceException(e.getMessage());
		}

		if (insurances == null)
			return new ArrayList();
        
        Iterator iterInsurance = insurances.iterator();
        List infoInsurances = new ArrayList();
        while(iterInsurance.hasNext())
        {
            InfoGrantInsurance infoGrantInsurance = InfoGrantInsuranceWithContract.newInfoFromDomain((IGrantInsurance) iterInsurance.next());
            infoInsurances.add(infoGrantInsurance);
        }

		return infoInsurances;
	}
    
}
