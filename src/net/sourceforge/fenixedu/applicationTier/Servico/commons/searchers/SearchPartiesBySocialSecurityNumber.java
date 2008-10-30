package net.sourceforge.fenixedu.applicationTier.Servico.commons.searchers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.sourceforge.fenixedu.domain.organizationalStructure.Party;
import net.sourceforge.fenixedu.domain.organizationalStructure.PartySocialSecurityNumber;

public class SearchPartiesBySocialSecurityNumber extends SearchParties {

    @Override
    protected Collection search(final String value, final int size) {
	final List<Party> parties = new ArrayList<Party>();
	for (final PartySocialSecurityNumber partySocialSecurityNumber : rootDomainObject.getPartySocialSecurityNumbersSet()) {
	    if (partySocialSecurityNumber.getSocialSecurityNumber().indexOf(value) >= 0) {
		parties.add(partySocialSecurityNumber.getParty());
	    }
	}
	return parties;
    }

}
