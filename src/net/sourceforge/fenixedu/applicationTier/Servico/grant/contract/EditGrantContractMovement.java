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
import net.sourceforge.fenixedu.domain.DomainObject;
import net.sourceforge.fenixedu.domain.grant.contract.GrantContract;
import net.sourceforge.fenixedu.domain.grant.contract.GrantContractMovement;
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
    protected DomainObject readObjectByUnique(InfoObject infoObject, ISuportePersistente sp)
            throws ExcepcaoPersistencia {
        IPersistentGrantContractMovement persistentGrantContractMovement = sp
                .getIPersistentGrantContractMovement();
        InfoGrantContractMovement infoGrantContractMovement = (InfoGrantContractMovement) infoObject;

        return persistentGrantContractMovement.readByOID(GrantContractMovement.class,
                infoGrantContractMovement.getIdInternal());
    }

    @Override
    protected void doAfterLock(DomainObject domainObjectLocked, InfoObject infoObject,
            ISuportePersistente sp) throws FenixServiceException {

        try {
            /*
             * In case of a new Movement, the Contract associated needs to be
             * set.
             */
            GrantContractMovement grantContractMovement = (GrantContractMovement) domainObjectLocked;
            InfoGrantContractMovement infoGrantContractMovement = (InfoGrantContractMovement) infoObject;

            GrantContract grantContract = (GrantContract) sp.getIPersistentGrantContract().readByOID(
                    GrantContract.class,
                    infoGrantContractMovement.getInfoGrantContract().getIdInternal());
            grantContractMovement.setGrantContract(grantContract);
            domainObjectLocked = grantContractMovement;
        } catch (Exception e) {
            throw new FenixServiceException(e.getMessage());
        }
    }

    public void run(InfoGrantContractMovement infoGrantContractMovement) throws Exception {
        super.run(new Integer(0), infoGrantContractMovement);
    }

    @Override
    protected void copyInformationFromInfoToDomain(ISuportePersistente sp, InfoObject infoObject,
            DomainObject domainObject) throws ExcepcaoPersistencia {
        InfoGrantContractMovement infoGrantContractMovement = (InfoGrantContractMovement) infoObject;
        GrantContractMovement grantContractMovement = (GrantContractMovement) domainObject;

        grantContractMovement.setArrivalDate(infoGrantContractMovement.getArrivalDate());
        grantContractMovement.setDepartureDate(infoGrantContractMovement.getDepartureDate());

        IPersistentGrantContract persistentGrantContract = sp.getIPersistentGrantContract();
        GrantContract grantContract = (GrantContract) persistentGrantContract.readByOID(GrantContract.class,
                infoGrantContractMovement.getInfoGrantContract().getIdInternal());
        grantContractMovement.setGrantContract(grantContract);

        grantContractMovement.setLocation(infoGrantContractMovement.getLocation());

    }

    @Override
    protected DomainObject createNewDomainObject(InfoObject infoObject) {
        return DomainFactory.makeGrantContractMovement();
    }

    @Override
    protected Class getDomainObjectClass() {
        return GrantContractMovement.class;
    }

}
