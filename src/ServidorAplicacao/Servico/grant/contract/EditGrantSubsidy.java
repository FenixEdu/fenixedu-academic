/*
 * Created on 22/Jan/2004
 *  
 */

package ServidorAplicacao.Servico.grant.contract;

import java.util.List;

import DataBeans.InfoObject;
import DataBeans.grant.contract.InfoGrantSubsidy;
import DataBeans.grant.contract.InfoGrantSubsidyWithContract;
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
public class EditGrantSubsidy extends EditDomainObjectService {

    public EditGrantSubsidy() {
    }

    protected IDomainObject clone2DomainObject(InfoObject infoObject) {
        return InfoGrantSubsidyWithContract.newDomainFromInfo((InfoGrantSubsidy) infoObject);
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
     *      DataBeans.InfoObject, ServidorPersistente.ISuportePersistente)
     */
    protected void doAfterLock(IDomainObject domainObjectLocked, InfoObject infoObject,
            ISuportePersistente sp) throws FenixServiceException {
        /*
         * In case of a new Subsidy, the Contract associated needs to be set.
         */
        IGrantSubsidy grantSubsidy = (IGrantSubsidy) domainObjectLocked;
        InfoGrantSubsidy infoGrantSubsidy = (InfoGrantSubsidy) infoObject;
        IGrantContract grantContract = new GrantContract();
        grantContract.setIdInternal(infoGrantSubsidy.getInfoGrantContract().getIdInternal());
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