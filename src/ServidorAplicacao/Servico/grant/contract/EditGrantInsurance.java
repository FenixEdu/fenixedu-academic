/*
 * Created on Jun 26, 2004
 */
package ServidorAplicacao.Servico.grant.contract;

import java.util.List;

import DataBeans.InfoObject;
import DataBeans.grant.contract.InfoGrantInsurance;
import DataBeans.util.Cloner;
import Dominio.IDomainObject;
import Dominio.grant.contract.GrantContract;
import Dominio.grant.contract.GrantInsurance;
import Dominio.grant.contract.IGrantContract;
import Dominio.grant.contract.IGrantInsurance;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.framework.EditDomainObjectService;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentObject;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.grant.IPersistentGrantInsurance;


/**
 * @author Barbosa
 * @author Pica
 */
public class EditGrantInsurance extends EditDomainObjectService {

    public EditGrantInsurance() {
    }
    
    protected IDomainObject clone2DomainObject(InfoObject infoObject)
	{
        return copyFromInfoToDomain((InfoGrantInsurance) infoObject);
	}
	protected IPersistentObject getIPersistentObject(ISuportePersistente sp)
	{
		return sp.getIPersistentGrantInsurance();
	}
	
	protected IDomainObject readObjectByUnique(IDomainObject domainObject, ISuportePersistente sp)
			throws ExcepcaoPersistencia
	{
		IPersistentGrantInsurance persistentGrantInsurance = sp.getIPersistentGrantInsurance();
		IGrantInsurance grantInsurance = (IGrantInsurance) domainObject;
		return persistentGrantInsurance.readByOID(GrantInsurance.class, grantInsurance.getIdInternal());
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see ServidorAplicacao.Servico.framework.EditDomainObjectService#doAfterLock(Dominio.IDomainObject,
	 *      DataBeans.InfoObject, ServidorPersistente.ISuportePersistente)
	 */
	protected void doAfterLock(IDomainObject domainObjectLocked, InfoObject infoObject,
			ISuportePersistente sp) throws FenixServiceException
	{
		/*
		 * In case of a new Insurance, the Contract associated needs to be set.
		 */
		IGrantInsurance grantInsurance = (IGrantInsurance) domainObjectLocked;
		InfoGrantInsurance infoGrantInsurance = (InfoGrantInsurance) infoObject;
		
		IGrantContract grantContract = new GrantContract();
		grantContract.setIdInternal(infoGrantInsurance.getInfoGrantContract().getIdInternal());
		
		grantInsurance.setGrantContract(grantContract);
		domainObjectLocked = (IDomainObject) grantInsurance;
		
		/*
		 * If this is a active insurance, set all others to state 0 (Desactive)
		 */
		if (grantInsurance.getState().equals(new Integer(1)))
		{
			try
			{
				IPersistentGrantInsurance persistentGrantInsurance = sp.getIPersistentGrantInsurance();
				List activeInsurances = persistentGrantInsurance.readGrantInsuranceByGrantContractAndState(
						grantInsurance.getGrantContract().getIdInternal(), new Integer(1));
				if (activeInsurances != null && activeInsurances.size() != 0)
				{
					//Desactivate the Insurances
					for (int i = 0; i < activeInsurances.size(); i++)
					{
						IGrantInsurance grantInsuranceTemp = (IGrantInsurance) activeInsurances.get(i);
						if (!grantInsuranceTemp.equals(grantInsurance))
						{
						    persistentGrantInsurance.simpleLockWrite(grantInsuranceTemp);
							grantInsuranceTemp.setState(new Integer(0));
						}
					}
				}
			}
			catch (ExcepcaoPersistencia persistentException)
			{
				throw new FenixServiceException(persistentException.getMessage());
			}
		}
	}
	public void run(InfoGrantInsurance infoGrantInsurance) throws FenixServiceException
	{
		super.run(new Integer(0), infoGrantInsurance);
	}
	
	private IGrantInsurance copyFromInfoToDomain(InfoGrantInsurance infoGrantInsurance) {
	    IGrantInsurance grantInsurance = new GrantInsurance();
	    if(infoGrantInsurance != null) {
	        grantInsurance.setIdInternal(infoGrantInsurance.getIdInternal());
	        grantInsurance.setState(infoGrantInsurance.getState());

	        //TODO.. copy properties
	        
	        IGrantContract grantContract = Cloner.copyInfoGrantContract2IGrantContract(infoGrantInsurance.getInfoGrantContract());
	        grantInsurance.setGrantContract(grantContract);
	    }
	    return grantInsurance;
	}
}
