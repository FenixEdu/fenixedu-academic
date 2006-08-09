/*
 * Created on 3/Jul/2004
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.grant.contract;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.framework.EditDomainObjectService;
import net.sourceforge.fenixedu.dataTransferObject.InfoObject;
import net.sourceforge.fenixedu.dataTransferObject.grant.contract.InfoGrantContractMovement;
import net.sourceforge.fenixedu.domain.DomainObject;
import net.sourceforge.fenixedu.domain.grant.contract.GrantContract;
import net.sourceforge.fenixedu.domain.grant.contract.GrantContractMovement;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

/**
 * @author Barbosa
 * @author Pica
 */
public class EditGrantContractMovement extends EditDomainObjectService {

    @Override
    protected DomainObject readObjectByUnique(InfoObject infoObject) throws ExcepcaoPersistencia {
        InfoGrantContractMovement infoGrantContractMovement = (InfoGrantContractMovement) infoObject;
        return rootDomainObject.readGrantContractMovementByOID(infoGrantContractMovement.getIdInternal());
    }

    @Override
    protected void doAfterLock(DomainObject domainObjectLocked, InfoObject infoObject) throws FenixServiceException {

        try {
            /*
             * In case of a new Movement, the Contract associated needs to be
             * set.
             */
            GrantContractMovement grantContractMovement = (GrantContractMovement) domainObjectLocked;
            InfoGrantContractMovement infoGrantContractMovement = (InfoGrantContractMovement) infoObject;

            GrantContract grantContract = rootDomainObject.readGrantContractByOID(
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
    protected void copyInformationFromInfoToDomain(InfoObject infoObject,
            DomainObject domainObject) throws ExcepcaoPersistencia {
        InfoGrantContractMovement infoGrantContractMovement = (InfoGrantContractMovement) infoObject;
        GrantContractMovement grantContractMovement = (GrantContractMovement) domainObject;

        grantContractMovement.setArrivalDate(infoGrantContractMovement.getArrivalDate());
        grantContractMovement.setDepartureDate(infoGrantContractMovement.getDepartureDate());

        GrantContract grantContract = rootDomainObject.readGrantContractByOID(infoGrantContractMovement.getInfoGrantContract().getIdInternal());
        grantContractMovement.setGrantContract(grantContract);

        grantContractMovement.setLocation(infoGrantContractMovement.getLocation());

    }

    @Override
    protected DomainObject createNewDomainObject(InfoObject infoObject) {
        return new GrantContractMovement();
    }

	@Override
	protected DomainObject readDomainObject(Integer idInternal) {
		return rootDomainObject.readGrantContractMovementByOID(idInternal);
	}

}
