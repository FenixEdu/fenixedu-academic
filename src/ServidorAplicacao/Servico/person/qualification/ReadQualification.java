/*
 * Created on 12/Nov/2003
 */

package ServidorAplicacao.Servico.person.qualification;

import DataBeans.ISiteComponent;
import DataBeans.InfoObject;
import DataBeans.person.InfoQualification;
import DataBeans.util.Cloner;
import Dominio.IDomainObject;
import Dominio.IQualification;
import Dominio.Qualification;
import ServidorAplicacao.Servico.framework.ReadDomainObjectService;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentObject;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author Barbosa
 * @author Pica
 */

public class ReadQualification extends ReadDomainObjectService
{

	private static ReadQualification service = new ReadQualification();
	
	public static ReadQualification getService()
	{
		return service;
	}
	
	/* (non-Javadoc)
	 * @see ServidorAplicacao.Servico.framework.ReadDomainObjectService#getDomainObjectClass()
	 */
	protected Class getDomainObjectClass()
	{
		return Qualification.class;
	}
	
	private ReadQualification()
	{}

	/* (non-Javadoc)
	 * @see ServidorAplicacao.Servico.framework.ReadDomainObjectService#getIPersistentObject(ServidorPersistente.ISuportePersistente)
	 */
	protected IPersistentObject getIPersistentObject(ISuportePersistente sp) throws ExcepcaoPersistencia
	{
		ISuportePersistente persistentSuport = SuportePersistenteOJB.getInstance();
		return persistentSuport.getIPersistentQualification();
	}

	/* (non-Javadoc)
	 * @see ServidorAplicacao.Servico.framework.ReadDomainObjectService#clone2InfoObject(Dominio.IDomainObject)
	 */
	protected InfoObject clone2InfoObject(IDomainObject domainObject)
	{
		return Cloner.copyIQualification2InfoQualification((IQualification) domainObject);
	}

	/* (non-Javadoc)
	 * @see ServidorAplicacao.Servico.framework.ReadDomainObjectService#getISiteComponent(DataBeans.InfoObject)
	 */
	protected ISiteComponent getISiteComponent(InfoObject infoObject)
	{
		return (InfoQualification) infoObject;
	}

	/* (non-Javadoc)
	 * @see ServidorAplicacao.IServico#getNome()
	 */
	public String getNome()
	{
		return "ReadQualification";
	}
}
