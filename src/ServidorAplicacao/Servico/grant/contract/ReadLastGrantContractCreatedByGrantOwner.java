/*
 * Created on 18/12/2003
 *  
 */
package ServidorAplicacao.Servico.grant.contract;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.grant.contract.InfoGrantContract;
import DataBeans.grant.contract.InfoGrantContractWithGrantOwnerAndGrantType;
import Dominio.grant.contract.IGrantContract;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import ServidorPersistente.grant.IPersistentGrantContract;

/**
 * @author Barbosa
 * @author Pica
 *  
 */
public class ReadLastGrantContractCreatedByGrantOwner implements IService
{
	/**
	 * The constructor of this class.
	 */
	public ReadLastGrantContractCreatedByGrantOwner()
	{
    }

	public InfoGrantContract run(Integer grantOwnerId) throws FenixServiceException
	{
		Integer grantContractNumber = null;
		IGrantContract grantContract = null;
		IPersistentGrantContract persistentGrantContract = null;
		try
		{	
			ISuportePersistente sp = SuportePersistenteOJB.getInstance();
			persistentGrantContract = sp.getIPersistentGrantContract();
			
			// set the contract number!
			grantContractNumber = persistentGrantContract.readMaxGrantContractNumberByGrantOwner(grantOwnerId);
			grantContract = persistentGrantContract.readGrantContractByNumberAndGrantOwner(grantContractNumber, grantOwnerId);
		} 
		catch (ExcepcaoPersistencia e)
		{
			throw new FenixServiceException(e.getMessage());
		}

		if (grantContract == null)
			return new InfoGrantContract();

		InfoGrantContract infoGrantContract = null;
		try
		{
			infoGrantContract = InfoGrantContractWithGrantOwnerAndGrantType.newInfoFromDomain(grantContract);
		}
		catch (Exception e)
		{
			throw new FenixServiceException(e.getMessage());
		}
		return infoGrantContract;
	}
}