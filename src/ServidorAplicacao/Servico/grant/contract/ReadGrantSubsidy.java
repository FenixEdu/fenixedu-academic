/*
 * Created on Jan 29, 2004
 *  
 */
package ServidorAplicacao.Servico.grant.contract;

import DataBeans.InfoObject;
import DataBeans.grant.contract.InfoGrantContract;
import DataBeans.grant.contract.InfoGrantSubsidy;
import DataBeans.util.Cloner;
import Dominio.IDomainObject;
import Dominio.grant.contract.GrantContract;
import Dominio.grant.contract.GrantSubsidy;
import Dominio.grant.contract.IGrantContract;
import Dominio.grant.contract.IGrantSubsidy;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.framework.ReadDomainObjectService;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentObject;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import ServidorPersistente.grant.IPersistentGrantContract;

/**
 * @author Pica
 * @author Barbosa
 */
public class ReadGrantSubsidy extends ReadDomainObjectService
{
	public ReadGrantSubsidy()
	{
	}

	protected Class getDomainObjectClass()
	{
		return GrantSubsidy.class;
	}

	protected IPersistentObject getIPersistentObject(ISuportePersistente sp)
	{
		return sp.getIPersistentGrantSubsidy();
	}

	protected InfoObject clone2InfoObject(IDomainObject domainObject)
	{
		return Cloner.copyIGrantSubsidy2InfoGrantSubsidy((IGrantSubsidy) domainObject);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ServidorAplicacao.Servico.framework.ReadDomainObjectService#run(java.lang.Integer)
	 */
	public InfoObject run(Integer objectId) throws FenixServiceException
	{
		InfoGrantSubsidy infoGrantSubsidy = (InfoGrantSubsidy) super.run(objectId);

		//The ReadDomainObjectService only reads 2level depth of references to other objects.
		//In this case we have InfoGrantSubsidy and its reference to InfoGrantContract.
		//Now we need to get the references of InfoGrantContract, e.g., InfoGrantOwner
		try
		{
			ISuportePersistente sp = SuportePersistenteOJB.getInstance();
			IPersistentGrantContract pgc = sp.getIPersistentGrantContract();

			InfoGrantContract infoGrantContract =
				Cloner.copyIGrantContract2InfoGrantContract(
					(IGrantContract) pgc.readByOID(
						GrantContract.class,
						infoGrantSubsidy.getInfoGrantContract().getIdInternal()));

			infoGrantSubsidy.getInfoGrantContract().getGrantOwnerInfo().setIdInternal(
				infoGrantContract.getGrantOwnerInfo().getIdInternal());
		}
		catch (ExcepcaoPersistencia e)
		{
            throw new FenixServiceException(e.getMessage());
		}
		return infoGrantSubsidy;
	}
}
