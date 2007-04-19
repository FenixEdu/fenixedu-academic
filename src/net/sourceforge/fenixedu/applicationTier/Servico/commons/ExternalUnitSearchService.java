package net.sourceforge.fenixedu.applicationTier.Servico.commons;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.organizationalStructure.Party;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import pt.utl.ist.fenix.tools.util.StringNormalizer;

public class ExternalUnitSearchService extends Service implements AutoCompleteSearchService {

    public Collection run(Class type, String value, int limit, Map<String, String> arguments) {
        final List<Unit> result = new ArrayList<Unit>();
        if (value != null && value.length() > 0) {
            final String[] nameValues = StringNormalizer.normalize(value).toLowerCase().split("\\p{Space}+");

            for (final Party party : RootDomainObject.getInstance().getExternalInstitutionUnit().getSubUnits()) {
        	if (result.size() >= limit) {
        	    break;
        	}
        	if (!party.isPerson()) {
        	    final Unit unit = (Unit) party;
        	    if (areNamesPresent(unit.getName(), nameValues)) {
        		result.add(unit);
        	    }
        	}
            }
            Collections.sort(result, Unit.COMPARATOR_BY_NAME_AND_ID);
        }
        return result;
    }

    private boolean areNamesPresent(String name, String[] searchNameParts) {
        String nameNormalized = StringNormalizer.normalize(name).toLowerCase();
        for (int i = 0; i < searchNameParts.length; i++) {
            String namePart = searchNameParts[i];
            if (!nameNormalized.contains(namePart)) {
                return false;
            }
        }
        return true;
    }
}
