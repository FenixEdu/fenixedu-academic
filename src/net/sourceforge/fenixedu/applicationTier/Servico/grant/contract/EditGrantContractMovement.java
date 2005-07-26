/*
 * Created on 3/Jul/2004
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.grant.contract;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.framework.EditDomainObjectService;
import net.sourceforge.fenixedu.dataTransferObject.InfoObject;
import net.sourceforge.fenixedu.dataTransferObject.grant.contract.InfoGrantContractMovement;
import net.sourceforge.fenixedu.domain.DomainFactory;
import net.sourceforge.fenixedu.domain.IDomainObject;
import net.sourceforge.fenixedu.domain.grant.contract.GrantContract;
import net.sourceforge.fenixedu.domain.grant.contract.GrantContractMovement;
import net.sourceforge.fenixedu.domain.grant.contract.IGrantContract;
import net.sourceforge.fenixedu.domain.grant.contract.IGrantContractMovement;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentObject;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.grant.IPersistentGrantContract;
import net.sourceforge.fenixedu.persistenceTier.grant.IPersistentGrantContractMovement;

/**
 * @author Barbosa
 * @author Pica
 */
public class EditGrantContractMovement extends EditDomainObjectService {

    @Override
    protected IPersistentObject getIPersistentObject(ISuportePersistente sp) {
        return sp.getIPersistentGrantContractMovement();
    }

    @Override
    protected IDomainObject readObjectByUnique(InfoObject infoObject, ISuportePersistente sp)
            throws ExcepcaoPersistencia {
        IPersistentGrantContractMovement persistentGrantContractMovement = sp
                .getIPersistentGrantContractMovement();
        InfoGrantContractMovement infoGrantContractMovement = (InfoGrantContractMovement) infoObject;

        return persistentGrantContractMovement.readByOID(GrantContractMovement.class,
                infoGrantContractMovement.getIdInternal());
    }

    @Override
    protected void doAfterLock(IDomainObject domainObjectLocked, InfoObject infoObject,
            ISuportePersistente sp) throws FenixServiceException {

        try {
            /*
             * In case of a new Movement, the Contract associated needs to be
             * set.
             */
            IGrantContractMovement grantContractMovement = (IGrantContractMovement) domainObjectLocked;
            InfoGrantContractMovement infoGrantContractMovement = (InfoGrantContractMovement) infoObject;

            IGrantContract grantContract = (IGrantContract) sp.getIPersistentGrantContract().readByOID(
                    GrantContract.class,
                    infoGrantContractMovement.getInfoGrantContract().getIdInternal());
            grantContractMovement.setGrantContract(grantContract);
            domainObjectLocked = grantContractMovement;
        } catch (Exception e) {
            throw new FenixServiceException(e.getMessage());
        }
    }

    public void run(InfoGrantContractMovement infoGrantContractMovement) throws FenixServiceException,
            ExcepcaoPersistencia {
        super.run(new Integer(0), infoGrantContractMovement);
    }

    @Override
    protected void copyInformationFromInfoToDomain(ISuportePersistente sp, InfoObject infoObject,
            IDomainObject domainObject) throws ExcepcaoPersistencia {
        InfoGrantContractMovement infoGrantContractMovement = (InfoGrantContractMovement) infoObject;
        IGrantContractMovement grantContractMovement = (IGrantContractMovement) domainObject;

        grantContractMovement.setArrivalDate(infoGrantContractMovement.getArrivalDate());
        grantContractMovement.setDepartureDate(infoGrantContractMovement.getDepartureDate());

        IGrantContract grantContract = new GrantContract();
        IPersistentGrantContract persistentGrantContract = sp.getIPersistentGrantContract();
        grantContract = (IGrantContract) persistentGrantContract.readByOID(GrantContract.class,
                infoGrantContractMovement.getInfoGrantContract().getIdInternal());
        grantContractMovement.setGrantContract(grantContract);

        grantContractMovement.setKeyGrantContract(infoGrantContractMovement.getInfoGrantContract()
                .getIdInternal());
        grantContractMovement.setLocation(infoGrantContractMovement.getLocation());

    }

    @Override
    protected IDomainObject createNewDomainObject(InfoObject infoObject) {
        return DomainFactory.makeGrantContractMovement();
    }

    @Override
    protected Class getDomainObjectClass() {
        return GrantContractMovement.class;
    }

}
