/*
 * Created on 25/Nov/2003
 * 
 */
package ServidorAplicacao.Servico.grant.contract;

import DataBeans.InfoObject;
import DataBeans.grant.contract.InfoGrantType;
import DataBeans.util.Cloner;
import Dominio.IDomainObject;
import Dominio.grant.contract.IGrantType;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentObject;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.grant.IPersistentGrantType;

/**
 * @author  Barbosa
 * @author  Pica
 *  
 */
public class CreateGrantType extends ServidorAplicacao.Servico.framework.EditDomainObjectService
{

	private static CreateGrantType service = new CreateGrantType();
	/**
	 * The singleton access method of this class.
	 */
	public static CreateGrantType getService()
	{
		return service;
	}
	/**
	 * The constructor of this class.
	 */
	private CreateGrantType()
	{
	}
	/**
	 * The name of the service
	 */
	public final String getNome()
	{
		return "CreateGrantType";
	}

	/* (non-Javadoc)
	 * @see ServidorAplicacao.Servico.framework.EditDomainObjectService#clone2DomainObject(DataBeans.InfoObject)
	 */
	protected IDomainObject clone2DomainObject(InfoObject infoObject)
	{
		return Cloner.copyInfoGrantType2IGrantType((InfoGrantType) infoObject);
	}

	/* (non-Javadoc)
	 * @see ServidorAplicacao.Servico.framework.EditDomainObjectService#getIPersistentObject(ServidorPersistente.ISuportePersistente)
	 */
	protected IPersistentObject getIPersistentObject(ISuportePersistente sp)
	{
		return sp.getIPersistentGrantType();
	}

	/* (non-Javadoc)
	 * @see ServidorAplicacao.Servico.framework.EditDomainObjectService#readObjectByUnique(IDomainObject,ServidorPersistente.ISuportePersistente)
	 */
	protected IDomainObject readObjectByUnique(IDomainObject domainObject, ISuportePersistente sp)
		throws ExcepcaoPersistencia
	{
		IPersistentGrantType pgt = sp.getIPersistentGrantType();
		IGrantType grantType = (IGrantType) domainObject;

		return pgt.readGrantTypeBySigla(grantType.getSigla());
	}

	/**
	 * Executes the service.
	 */
	public void run(InfoGrantType infoGrantType) throws FenixServiceException
	{
		super.run(new Integer(0),infoGrantType);
	}
}