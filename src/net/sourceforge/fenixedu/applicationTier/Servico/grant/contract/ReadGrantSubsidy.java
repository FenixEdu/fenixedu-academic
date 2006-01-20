/*
 * Created on Jan 29, 2004
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.grant.contract;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.framework.ReadDomainObjectService;
import net.sourceforge.fenixedu.dataTransferObject.InfoObject;
import net.sourceforge.fenixedu.dataTransferObject.grant.contract.InfoGrantContract;
import net.sourceforge.fenixedu.dataTransferObject.grant.contract.InfoGrantContractWithGrantOwnerAndGrantType;
import net.sourceforge.fenixedu.dataTransferObject.grant.contract.InfoGrantSubsidy;
import net.sourceforge.fenixedu.dataTransferObject.grant.contract.InfoGrantSubsidyWithContract;
import net.sourceforge.fenixedu.dataTransferObject.grant.owner.InfoGrantOwner;
import net.sourceforge.fenixedu.domain.DomainObject;
import net.sourceforge.fenixedu.domain.grant.contract.GrantContract;
import net.sourceforge.fenixedu.domain.grant.contract.GrantSubsidy;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentObject;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.grant.IPersistentGrantContract;

/**
 * @author Pica
 * @author Barbosa
 */
public class ReadGrantSubsidy extends ReadDomainObjectService {

	protected Class getDomainObjectClass() {
		return GrantSubsidy.class;
	}

	protected IPersistentObject getIPersistentObject(ISuportePersistente persistentSupport) {
		return persistentSupport.getIPersistentGrantSubsidy();
	}

	protected InfoObject newInfoFromDomain(DomainObject domainObject) {
		return InfoGrantSubsidyWithContract.newInfoFromDomain((GrantSubsidy) domainObject);
	}

	public InfoObject run(Integer objectId) throws FenixServiceException, ExcepcaoPersistencia {
		InfoGrantSubsidy infoGrantSubsidy = (InfoGrantSubsidy) super.run(objectId);

		// TODO The ReadDomainObjectService only reads 2level depth of
		// references to other objects.
		// In this case we have InfoGrantSubsidy and its reference to
		// InfoGrantContract.
		// Now we need to get the references of InfoGrantContract, e.g.,
		// InfoGrantOwner
		IPersistentGrantContract pgc = persistentSupport.getIPersistentGrantContract();

		InfoGrantContract infoGrantContract = InfoGrantContractWithGrantOwnerAndGrantType
				.newInfoFromDomain((GrantContract) pgc.readByOID(GrantContract.class, infoGrantSubsidy
						.getInfoGrantContract().getIdInternal()));

		// this section of code is temporary!!!! (see above the reason)
		if (infoGrantSubsidy.getInfoGrantContract().getGrantOwnerInfo() == null)
			infoGrantSubsidy.getInfoGrantContract().setGrantOwnerInfo(new InfoGrantOwner());

		infoGrantSubsidy.getInfoGrantContract().getGrantOwnerInfo().setIdInternal(
				infoGrantContract.getGrantOwnerInfo().getIdInternal());

		return infoGrantSubsidy;
	}

}
