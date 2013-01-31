/*
 * Created on 18/12/2003
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.grant.contract;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.grant.contract.InfoGrantContractRegime;
import net.sourceforge.fenixedu.dataTransferObject.grant.contract.InfoGrantContractRegimeWithTeacherAndContract;
import net.sourceforge.fenixedu.domain.grant.contract.GrantContract;
import net.sourceforge.fenixedu.domain.grant.contract.GrantContractRegime;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixWebFramework.services.Service;

/**
 * @author Barbosa
 * @author Pica
 * 
 */
public class ReadGrantContractRegimeByContractAndState extends FenixService {

	@Checked("RolePredicates.GRANT_OWNER_MANAGER_PREDICATE")
	@Service
	public static List run(Integer grantContractId, Integer state) throws FenixServiceException {
		List contractRegimes = null;

		GrantContract grantContract = rootDomainObject.readGrantContractByOID(grantContractId);
		if (grantContract != null) {
			contractRegimes = grantContract.readGrantContractRegimeByGrantContractAndState(state);
		}

		if (contractRegimes == null) {
			return new ArrayList();
		}

		List infoContractRegimeList = (ArrayList) CollectionUtils.collect(contractRegimes, new Transformer() {
			@Override
			public Object transform(Object input) {
				GrantContractRegime grantContractRegime = (GrantContractRegime) input;
				InfoGrantContractRegime infoGrantContractRegime =
						InfoGrantContractRegimeWithTeacherAndContract.newInfoFromDomain(grantContractRegime);
				return infoGrantContractRegime;
			}
		});

		return infoContractRegimeList;

	}
}