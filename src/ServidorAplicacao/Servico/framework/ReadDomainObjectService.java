/*
 * Created on 14/Nov/2003
 *  
 */
package ServidorAplicacao.Servico.framework;

import DataBeans.InfoObject;
import Dominio.IDomainObject;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentObject;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author Leonor Almeida
 * @author Sergio Montelobo
 * @author jpvl
 */
public abstract class ReadDomainObjectService implements IServico
{
	public InfoObject run(Integer objectId) throws FenixServiceException
	{
		try
		{
			ISuportePersistente sp = SuportePersistenteOJB.getInstance();
			IPersistentObject persistentObject = getIPersistentObject(sp);

			IDomainObject domainObject = persistentObject.readByOID(getDomainObjectClass(), objectId);

			InfoObject infoObject = clone2InfoObject(domainObject);

			return infoObject;
		} catch (ExcepcaoPersistencia e)
		{
			throw new FenixServiceException(e);
		} catch (Exception e)
		{
			throw new FenixServiceException(e);
		}
	}

	/**
	 * This is the class in witch the broker will read and delete the DomainObject
	 * 
	 * @return
	 */
	protected abstract Class getDomainObjectClass();

	/**
	 * @param sp
	 * @return
	 */
	protected abstract IPersistentObject getIPersistentObject(ISuportePersistente sp)
		throws ExcepcaoPersistencia;

	/**
	 * This method invokes the Cloner to convert from IDomainObject to InfoObject
	 * 
	 * @param infoObject
	 * @return
	 */
	protected abstract InfoObject clone2InfoObject(IDomainObject domainObject);
}
