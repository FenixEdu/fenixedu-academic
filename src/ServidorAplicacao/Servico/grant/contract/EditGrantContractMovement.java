/*
 * Created on 3/Jul/2004
 *  
 */
package ServidorAplicacao.Servico.grant.contract;

import DataBeans.InfoObject;
import DataBeans.grant.contract.InfoGrantContractMovement;
import DataBeans.grant.contract.InfoGrantContractMovementWithContract;
import Dominio.IDomainObject;
import Dominio.grant.contract.GrantContract;
import Dominio.grant.contract.GrantContractMovement;
import Dominio.grant.contract.IGrantContract;
import Dominio.grant.contract.IGrantContractMovement;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.framework.EditDomainObjectService;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentObject;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.grant.IPersistentGrantContractMovement;

/**
 * @author Barbosa
 * @author Pica
 */
public class EditGrantContractMovement extends EditDomainObjectService {

    /**
     * The constructor of this class.
     */
    public EditGrantContractMovement() {
    }

    protected IDomainObject clone2DomainObject(InfoObject infoObject) {
        return InfoGrantContractMovementWithContract.newDomainFromInfo((InfoGrantContractMovement) infoObject);
    }

    protected IPersistentObject getIPersistentObject(ISuportePersistente sp) {
        return sp.getIPersistentGrantContractMovement();
    }

    protected IDomainObject readObjectByUnique(IDomainObject domainObject,
            ISuportePersistente sp) throws ExcepcaoPersistencia {
        IPersistentGrantContractMovement persistentGrantContractMovement = sp
                .getIPersistentGrantContractMovement();
        IGrantContractMovement grantContractMovement = (IGrantContractMovement) domainObject;

        return persistentGrantContractMovement.readByOID(
                GrantContractMovement.class, grantContractMovement
                        .getIdInternal());
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.Servico.framework.EditDomainObjectService#doAfterLock(Dominio.IDomainObject,
     *      DataBeans.InfoObject, ServidorPersistente.ISuportePersistente)
     */
    protected void doAfterLock(IDomainObject domainObjectLocked,
            InfoObject infoObject, ISuportePersistente sp)
            throws FenixServiceException {

        try 
        {
            /*
    		 * In case of a new Movement, the Contract associated needs to be set.
    		 */
    		IGrantContractMovement grantContractMovement = (IGrantContractMovement) domainObjectLocked;
    		InfoGrantContractMovement infoGrantContractMovement = (InfoGrantContractMovement) infoObject;
    		IGrantContract grantContract = new GrantContract();
    		grantContract.setIdInternal(infoGrantContractMovement.getInfoGrantContract().getIdInternal());
    		grantContractMovement.setGrantContract(grantContract);
    		domainObjectLocked = grantContractMovement;
        } 
        catch (Exception e) {
            throw new FenixServiceException(e.getMessage());
        }
    }

    public void run(InfoGrantContractMovement infoGrantContractMovement) throws FenixServiceException {
        super.run(new Integer(0), infoGrantContractMovement);
    }
}