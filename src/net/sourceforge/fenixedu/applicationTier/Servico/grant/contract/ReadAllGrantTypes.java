package net.sourceforge.fenixedu.applicationTier.Servico.grant.contract;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.dataTransferObject.grant.contract.InfoGrantType;
import net.sourceforge.fenixedu.domain.grant.contract.GrantType;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixWebFramework.services.Service;

public class ReadAllGrantTypes extends FenixService {

	@Checked("RolePredicates.GRANT_OWNER_MANAGER_PREDICATE")
	@Service
	public static List run() {
		List<GrantType> grantTypes = rootDomainObject.getGrantTypes();
		return (List) CollectionUtils.collect(grantTypes, new Transformer() {
			@Override
			public Object transform(Object input) {
				GrantType grantType = (GrantType) input;
				return InfoGrantType.newInfoFromDomain(grantType);

			}
		});

	}

}