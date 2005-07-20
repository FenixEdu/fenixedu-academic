/*
 * Created on 22/Jan/2004
 *  
 */

package net.sourceforge.fenixedu.applicationTier.Servico.grant.contract;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.framework.EditDomainObjectService;
import net.sourceforge.fenixedu.dataTransferObject.InfoObject;
import net.sourceforge.fenixedu.dataTransferObject.grant.contract.InfoGrantSubsidy;
import net.sourceforge.fenixedu.domain.IDomainObject;
import net.sourceforge.fenixedu.domain.grant.contract.GrantContract;
import net.sourceforge.fenixedu.domain.grant.contract.GrantSubsidy;
import net.sourceforge.fenixedu.domain.grant.contract.IGrantContract;
import net.sourceforge.fenixedu.domain.grant.contract.IGrantSubsidy;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentObject;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.grant.IPersistentGrantContract;
import net.sourceforge.fenixedu.persistenceTier.grant.IPersistentGrantSubsidy;

/**
 * @author Barbosa
 * @author Pica
 */
public class EditGrantSubsidy extends EditDomainObjectService {

    @Override
	protected void copyInformationFromInfoToDomain(ISuportePersistente sp, InfoObject infoObject, IDomainObject domainObject) throws ExcepcaoPersistencia {
		InfoGrantSubsidy infoGrantSubsidy = (InfoGrantSubsidy)infoObject;
		IGrantSubsidy grantSubsidy = (IGrantSubsidy) domainObject;
		grantSubsidy.setDateBeginSubsidy(infoGrantSubsidy.getDateBeginSubsidy());
		grantSubsidy.setDateEndSubsidy(infoGrantSubsidy.getDateEndSubsidy());
		
		
		IPersistentGrantContract persistentGrantContract = sp.getIPersistentGrantContract();
		IGrantContract grantContract = (IGrantContract) persistentGrantContract.readByOID(GrantContract.class ,infoGrantSubsidy.getInfoGrantContract().getIdInternal());
		grantSubsidy.setGrantContract(grantContract);
		
		grantSubsidy.setState(infoGrantSubsidy.getState());
		grantSubsidy.setTotalCost(infoGrantSubsidy.getTotalCost());
		grantSubsidy.setValue(infoGrantSubsidy.getValue());
		
		
	}

	@Override
	protected IDomainObject createNewDomainObject(InfoObject infoObject) {
		// TODO Auto-generated method stub
		return new GrantSubsidy();
	}

	@Override
	protected Class getDomainObjectClass() {
		// TODO Auto-generated method stub
		return GrantSubsidy.class ;
	}


    protected IPersistentObject getIPersistentObject(ISuportePersistente sp) {
        return sp.getIPersistentGrantSubsidy();
    }

    protected IDomainObject readObjectByUnique(IDomainObject domainObject, ISuportePersistente sp)
            throws ExcepcaoPersistencia {
        IPersistentGrantSubsidy pgs = sp.getIPersistentGrantSubsidy();
        IGrantSubsidy grantSubsidy = (IGrantSubsidy) domainObject;
        return pgs.readByOID(GrantSubsidy.class, grantSubsidy.getIdInternal());
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.Servico.framework.EditDomainObjectService#doAfterLock(Dominio.IDomainObject,
     *      net.sourceforge.fenixedu.dataTransferObject.InfoObject, ServidorPersistente.ISuportePersistente)
     */
    protected void doAfterLock(IDomainObject domainObjectLocked, InfoObject infoObject,
            ISuportePersistente sp) throws FenixServiceException,ExcepcaoPersistencia{
        /*
         * In case of a new Subsidy, the Contract associated needs to be set.
         */
        IGrantSubsidy grantSubsidy = (IGrantSubsidy) domainObjectLocked;
        InfoGrantSubsidy infoGrantSubsidy = (InfoGrantSubsidy) infoObject;
        IPersistentGrantContract persistentGrantContract = sp.getIPersistentGrantContract();
        IGrantContract grantContract = (IGrantContract)persistentGrantContract.readByOID(GrantContract.class,infoGrantSubsidy.getInfoGrantContract().getIdInternal());
        grantSubsidy.setGrantContract(grantContract);
        domainObjectLocked = grantSubsidy;
        /*
         * If this is a active subsidy, set all others to state 0 (Desactive)
         */
        if (grantSubsidy.getState().equals(InfoGrantSubsidy.getActiveStateValue())) {
            try {
                IPersistentGrantSubsidy persistentGrantSubsidy = sp.getIPersistentGrantSubsidy();
                List activeSubsidy = persistentGrantSubsidy.readAllSubsidiesByGrantContractAndState(
                        grantSubsidy.getGrantContract().getIdInternal(), InfoGrantSubsidy
                                .getActiveStateValue());
                if (activeSubsidy != null && !activeSubsidy.isEmpty()) {
                    //Desactivate the Subsidy
                    for (int i = 0; i < activeSubsidy.size(); i++) {
                        IGrantSubsidy grantSubsidyTemp = (IGrantSubsidy) activeSubsidy.get(i);
                        if (!grantSubsidyTemp.equals(grantSubsidy)) {
                            persistentGrantSubsidy.simpleLockWrite(grantSubsidyTemp);
                            grantSubsidyTemp.setState(InfoGrantSubsidy.getInactiveStateValue());
                        }
                    }
                }
            } catch (ExcepcaoPersistencia persistentException) {
                throw new FenixServiceException(persistentException.getMessage());
            }
        }
    }

    public void run(InfoGrantSubsidy infoGrantSubsidy) throws FenixServiceException {
        super.run(new Integer(0), infoGrantSubsidy);
    }
}