/*
 * Created on 23/Jan/2004
 *  
 */

package ServidorAplicacao.Servico.grant.contract;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.InfoObject;
import DataBeans.grant.contract.InfoGrantCostCenter;
import DataBeans.grant.contract.InfoGrantPaymentEntity;
import DataBeans.grant.contract.InfoGrantProject;
import DataBeans.util.Cloner;
import Dominio.IDomainObject;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.framework.EditDomainObjectService;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentObject;
import ServidorPersistente.ISuportePersistente;
/**
 * @author Barbosa
 * @author Pica
 */
public class EditGrantPaymentEntity extends EditDomainObjectService implements IService
{

	/**
	 * The constructor of this class.
	 */
	public EditGrantPaymentEntity()
	{
	}
	
	public void run(InfoGrantCostCenter infoObject) throws FenixServiceException
	{
		super.run(new Integer(0), infoObject);
	}
	
	public void run(InfoGrantProject infoObject) throws FenixServiceException
	{
		super.run(new Integer(0), infoObject);
	}

	/* (non-Javadoc)
	 * @see ServidorAplicacao.Servico.framework.EditDomainObjectService#clone2DomainObject(DataBeans.InfoObject)
	 */
	protected IDomainObject clone2DomainObject(InfoObject infoObject)
	{
		return Cloner.copyInfoGrantPaymentEntity2IGrantPaymentEntity((InfoGrantPaymentEntity)infoObject);
	}

	/* (non-Javadoc)
	 * @see ServidorAplicacao.Servico.framework.EditDomainObjectService#getIPersistentObject(ServidorPersistente.ISuportePersistente)
	 */
	protected IPersistentObject getIPersistentObject(ISuportePersistente sp) throws ExcepcaoPersistencia
	{
		return sp.getIPersistentGrantPaymentEntity();
	}
}
