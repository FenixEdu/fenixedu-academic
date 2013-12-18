package net.sourceforge.fenixedu.applicationTier.Servico.commons;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.fenixedu.bennu.core.domain.Bennu;

import net.sourceforge.fenixedu.applicationTier.Servico.commons.searchers.SearchParties;
import net.sourceforge.fenixedu.domain.organizationalStructure.Party;
import net.sourceforge.fenixedu.domain.organizationalStructure.PartySocialSecurityNumber;

public class SearchPartyByNif extends SearchParties<Party> {

    @Override
    protected Collection<Party> search(String value, int size) {
        List<Party> result = new ArrayList<Party>();
        Collection<PartySocialSecurityNumber> partySocialSecurityNumbers = Bennu.getInstance().getPartySocialSecurityNumbersSet();
        for (PartySocialSecurityNumber partySocialSecurityNumber : partySocialSecurityNumbers) {
            if (partySocialSecurityNumber.getSocialSecurityNumber().startsWith(value)) {
                result.add(partySocialSecurityNumber.getParty());
            }
        }

        return result;
    }
}