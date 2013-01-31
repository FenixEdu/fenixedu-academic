/*
 * Created on 04 Mar 2004
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.grant.contract;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.grant.contract.InfoGrantSubsidy;
import net.sourceforge.fenixedu.dataTransferObject.grant.contract.InfoGrantSubsidyWithContract;
import net.sourceforge.fenixedu.domain.grant.contract.GrantContract;
import net.sourceforge.fenixedu.domain.grant.contract.GrantSubsidy;
import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixWebFramework.services.Service;

/**
 * @author Barbosa
 * @author Pica
 * 
 */
public class ReadAllGrantSubsidiesByGrantContract extends FenixService {

	@Checked("RolePredicates.GRANT_OWNER_MANAGER_PREDICATE")
	@Service
	public static List run(Integer idContract) throws FenixServiceException {
		GrantContract grantContract = rootDomainObject.readGrantContractByOID(idContract);
		List<GrantSubsidy> subsidies = grantContract.getAssociatedGrantSubsidies();

		if (subsidies == null) {
			return new ArrayList();
		}

		final List infoSubsidyList = new ArrayList();
		for (final GrantSubsidy grantSubsidy : grantContract.getAssociatedGrantSubsidiesSet()) {
			InfoGrantSubsidy infoGrantSubsidy = InfoGrantSubsidyWithContract.newInfoFromDomain(grantSubsidy);
			infoSubsidyList.add(infoGrantSubsidy);
		}
		return infoSubsidyList;
	}
}