package net.sourceforge.fenixedu.applicationTier.Servico.commons.searchers;

import java.util.Collection;
import java.util.Map;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.commons.AutoCompleteSearchService;
import net.sourceforge.fenixedu.domain.organizationalStructure.UnitName;

public class SearchExternalUnits extends Service implements AutoCompleteSearchService {

    public Collection<UnitName> run(Class type, String value, int limit, Map<String, String> arguments) {
	if (type != UnitName.class) {
	    return null;
	}

	final int size = Integer.parseInt(arguments.get("size"));
	return UnitName.findExternalUnit(value, size);
    }

}
