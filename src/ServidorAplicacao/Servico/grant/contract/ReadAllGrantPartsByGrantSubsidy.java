/*
 * Created on 23/Jan/2004
 *  
 */
package ServidorAplicacao.Servico.grant.contract;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.util.Cloner;
import Dominio.grant.contract.IGrantPart;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import ServidorPersistente.grant.IPersistentGrantPart;

/**
 * @author Barbosa
 * @author Pica
 *  
 */
public class ReadAllGrantPartsByGrantSubsidy implements IService
{
	/**
	 * The constructor of this class.
	 */
	public ReadAllGrantPartsByGrantSubsidy()
	{
	}

	public List run(Integer grantSubsidyId) throws FenixServiceException
	{
		List result = null;
		IPersistentGrantPart pgp = null;
		try
		{
			ISuportePersistente sp = SuportePersistenteOJB.getInstance();
			pgp = sp.getIPersistentGrantPart();
			List grantParts = pgp.readAllGrantPartsByGrantSubsidy(grantSubsidyId);

            if(grantParts != null)
            {    
			result = (List) CollectionUtils.collect(grantParts, new Transformer()
			{
				public Object transform(Object o)
				{
					IGrantPart grantPart = (IGrantPart) o;
					return Cloner.copyIGrantPart2InfoGrantPart(grantPart);
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