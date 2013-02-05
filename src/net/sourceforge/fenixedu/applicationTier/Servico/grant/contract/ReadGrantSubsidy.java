/*
 * Created on Jan 29, 2004
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.grant.contract;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoObject;
import net.sourceforge.fenixedu.dataTransferObject.grant.contract.InfoGrantContract;
import net.sourceforge.fenixedu.dataTransferObject.grant.contract.InfoGrantContractWithGrantOwnerAndGrantType;
import net.sourceforge.fenixedu.dataTransferObject.grant.contract.InfoGrantSubsidy;
import net.sourceforge.fenixedu.dataTransferObject.grant.contract.InfoGrantSubsidyWithContract;
import net.sourceforge.fenixedu.dataTransferObject.grant.owner.InfoGrantOwner;
import net.sourceforge.fenixedu.domain.DomainObject;
import net.sourceforge.fenixedu.domain.grant.contract.GrantSubsidy;
import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixWebFramework.services.Service;

/**
 * @author Pica
 * @author Barbosa
 */
public class ReadGrantSubsidy extends FenixService {

    protected static InfoGrantSubsidy newInfoFromDomain(DomainObject domainObject) {
        return InfoGrantSubsidyWithContract.newInfoFromDomain((GrantSubsidy) domainObject);
    }

    @Checked("RolePredicates.GRANT_OWNER_MANAGER_PREDICATE")
    @Service
    public static InfoObject run(Integer objectId) throws FenixServiceException {
        final GrantSubsidy grantSubsidy = rootDomainObject.readGrantSubsidyByOID(objectId);
        InfoGrantSubsidy infoGrantSubsidy = newInfoFromDomain(grantSubsidy);

        InfoGrantContract infoGrantContract =
                InfoGrantContractWithGrantOwnerAndGrantType.newInfoFromDomain(rootDomainObject
                        .readGrantContractByOID(infoGrantSubsidy.getInfoGrantContract().getIdInternal()));

        // this section of code is temporary!!!! (see above the reason)
        if (infoGrantSubsidy.getInfoGrantContract().getGrantOwnerInfo() == null) {
            infoGrantSubsidy.getInfoGrantContract().setGrantOwnerInfo(new InfoGrantOwner());
        }

        infoGrantSubsidy.getInfoGrantContract().getGrantOwnerInfo()
                .setIdInternal(infoGrantContract.getGrantOwnerInfo().getIdInternal());

        return infoGrantSubsidy;
    }

}