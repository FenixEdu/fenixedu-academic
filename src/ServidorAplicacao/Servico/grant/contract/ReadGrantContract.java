/*
 * Created on 18/12/2003
 * 
 */
package ServidorAplicacao.Servico.grant.contract;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.InfoObject;
import DataBeans.util.Cloner;
import Dominio.IDomainObject;
import Dominio.grant.contract.GrantContract;
import Dominio.grant.contract.IGrantContract;
import ServidorAplicacao.Servico.framework.ReadDomainObjectService;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentObject;
import ServidorPersistente.ISuportePersistente;

/**
 * @author  Barbosa
 * @author  Pica
 *  
 */
public class ReadGrantContract extends ReadDomainObjectService implements IService
{
	private static ReadGrantContract service = new ReadGrantContract();
	/**
	 * The singleton access method of this class.
	 */
	public static ReadGrantContract getService()
	{
		return service;
	}
	/**
	 * The constructor of this class.
	 */
	private ReadGrantContract()
	{
	}
	/**
	 * The name of the service
	 */
	public final String getNome()
	{
		return "ReadGrantContract";
	}

	/* (non-Javadoc)
	 * @see ServidorAplicacao.Servico.framework.ReadDomainObjectService#getIPersistentObject(ServidorPersistente.ISuportePersistente)
	 */
	protected IPersistentObject getIPersistentObject(ISuportePersistente sp) throws ExcepcaoPersistencia
	{
		return sp.getIPersistentGrantContract();
	}

	/* (non-Javadoc)
	 * @see ServidorAplicacao.Servico.framework.ReadDomainObjectService#clone2InfoObject(Dominio.IDomainObject)
	 */
	protected InfoObject clone2InfoObject(IDomainObject domainObject)
	{
		return Cloner.copyIGrantContract2InfoGrantContract((IGrantContract) domainObject);
	}

	/* (non-Javadoc)
	 * @see ServidorAplicacao.Servico.framework.ReadDomainObjectService#getDomainObjectClass()
	 */
	protected Class getDomainObjectClass()
	{
		return GrantContract.class;
	}
}