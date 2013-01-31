package net.sourceforge.fenixedu.applicationTier.Servico.commons;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.commons.searchers.SearchParties;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.organizationalStructure.Party;
import net.sourceforge.fenixedu.domain.organizationalStructure.PartySocialSecurityNumber;

public class SearchPartyByNif extends SearchParties {

	@Override
	protected Collection search(String value, int size) {
		List<Party> result = new ArrayList<Party>();
		List<PartySocialSecurityNumber> partySocialSecurityNumbers =
				RootDomainObject.getInstance().getPartySocialSecurityNumbers();
		for (PartySocialSecurityNumber partySocialSecurityNumber : partySocialSecurityNumbers) {
			if (partySocialSecurityNumber.getSocialSecurityNumber().startsWith(value)) {
				result.add(partySocialSecurityNumber.getParty());
			}
		}

		return result;
	}
}