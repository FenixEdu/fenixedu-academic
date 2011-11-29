package net.sourceforge.fenixedu.applicationTier.Servico.commons.searchers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.applicationTier.Servico.commons.AutoCompleteSearchService;
import net.sourceforge.fenixedu.domain.raides.DegreeDesignation;
import net.sourceforge.fenixedu.util.StringUtils;

public class SearchRaidesDegreeDesignations extends FenixService implements AutoCompleteSearchService {

    private static int DEFAULT_SIZE = 50;

    public Collection run(Class type, String value, final int limit, Map<String, String> arguments) {

	int maxLimit = getSize(arguments);

	value = StringUtils.normalize(value);
	List<DegreeDesignation> result = new ArrayList<DegreeDesignation>();
	for (DegreeDesignation degreeDesignation : rootDomainObject.getDegreeDesignationsSet()) {
	    String normalizedDesignation = StringUtils.normalize(degreeDesignation.getDescription());
	    if (normalizedDesignation.contains(value)) {
		result.add(degreeDesignation);
	    }
	    if (result.size() >= maxLimit) {
		break;
	    }
	}
	return result;
    }

    private int getSize(Map<String, String> arguments) {
	String size = arguments.get("size");

	if (size == null) {
	    return DEFAULT_SIZE;
	} else {
	    return Integer.parseInt(size);
	}
    }
}
