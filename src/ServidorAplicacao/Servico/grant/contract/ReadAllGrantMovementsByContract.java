/*
 * Created on 3/Jul/2004
 *  
 */
package ServidorAplicacao.Servico.grant.contract;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.grant.contract.InfoGrantContractMovementWithContract;
import Dominio.grant.contract.IGrantContractMovement;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import ServidorPersistente.grant.IPersistentGrantContractMovement;

/**
 * @author Barbosa
 * @author Pica
 *  
 */
public class ReadAllGrantMovementsByContract implements IService
{
	/**
	 * The constructor of this class.
	 */
	public ReadAllGrantMovementsByContract()
	{
	}

	public List run(Integer grantContractId) throws FenixServiceException
	{
		List result = null;
		IPersistentGrantContractMovement pgcm = null;
		try
		{
			ISuportePersistente sp = SuportePersistenteOJB.getInstance();
			pgcm = sp.getIPersistentGrantContractMovement();
			List grantMovements = pgcm.readAllMovementsByContract(grantContractId);

            if(grantMovements != null)
            {    
			result = (List) CollectionUtils.collect(grantMovements, new Transformer()
			{
				public Object transform(Object o)
				{
					IGrantContractMovement grantMovement = (IGrantContractMovement) o;
					return InfoGrantContractMovementWithContract.newInfoFromDomain(grantMovement);
				}
			});
            }
		}
		catch (ExcepcaoPersistencia e)
		{
			throw new FenixServiceException(e.getMessage());
		}

		return result;
	}
}