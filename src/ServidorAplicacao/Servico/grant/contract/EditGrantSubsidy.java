/*
 * Created on 22/Jan/2004
 *  
 */
package ServidorAplicacao.Servico.grant.contract;

import DataBeans.InfoObject;
import DataBeans.grant.contract.InfoGrantSubsidy;
import DataBeans.util.Cloner;
import Dominio.IDomainObject;
import Dominio.grant.contract.GrantContract;
import Dominio.grant.contract.GrantSubsidy;
import Dominio.grant.contract.IGrantContract;
import Dominio.grant.contract.IGrantSubsidy;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.framework.EditDomainObjectService;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentObject;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.grant.IPersistentGrantSubsidy;

/**
 * @author Barbosa
 * @author Pica
 */
public class EditGrantSubsidy extends EditDomainObjectService
{
	/**
	 * The constructor of this class.
	 */
	public EditGrantSubsidy()
	{
	}

	protected IDomainObject clone2DomainObject(InfoObject infoObject)
	{
		return Cloner.copyInfoGrantSubsidy2IGrantSubsidy((InfoGrantSubsidy) infoObject);
	}

	protected IPersistentObject getIPersistentObject(ISuportePersistente sp)
	{
		return sp.getIPersistentGrantSubsidy();
	}

	protected IDomainObject readObjectByUnique(IDomainObject domainObject, ISuportePersistente sp)
		throws ExcepcaoPersistencia
	{
		IPersistentGrantSubsidy pgs = sp.getIPersistentGrantSubsidy();
		IGrantSubsidy grantSubsidy = (IGrantSubsidy) domainObject;

		return pgs.readByOID(GrantSubsidy.class,grantSubsidy.getIdInternal());
	}
    
    /* (non-Javadoc)
	 * @see ServidorAplicacao.Servico.framework.EditDomainObjectService#doAfterLock(Dominio.IDomainObject, DataBeans.InfoObject, ServidorPersistente.ISuportePersistente)
	 */
	protected void doAfterLock(
		IDomainObject domainObjectLocked,
		InfoObject infoObject,
		ISuportePersistente sp)
	{
        IGrantSubsidy grantSubsidy = (IGrantSubsidy) domainObjectLocked;
        InfoGrantSubsidy infoGrantSubsidy = (InfoGrantSubsidy) infoObject;
        IGrantContract grantContract = new GrantContract();
        
        grantContract.setIdInternal(infoGrantSubsidy.getInfoGrantContract().getIdInternal());
        grantSubsidy.setGrantContract(grantContract);
        domainObjectLocked = (IDomainObject) grantSubsidy;
	}
    
	public void run(InfoGrantSubsidy infoGrantSubsidy) throws FenixServiceException
	{
		super.run(new Integer(0), infoGrantSubsidy);
	}
}
