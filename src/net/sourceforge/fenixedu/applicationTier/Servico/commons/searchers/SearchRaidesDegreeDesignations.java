package net.sourceforge.fenixedu.applicationTier.Servico.commons.searchers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.applicationTier.Servico.commons.AutoCompleteSearchService;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.raides.DegreeDesignation;
import net.sourceforge.fenixedu.util.StringUtils;
import pt.ist.fenixframework.pstm.AbstractDomainObject;

public class SearchRaidesDegreeDesignations extends FenixService implements AutoCompleteSearchService {

    private static int DEFAULT_SIZE = 50;

    @Override
    public Collection run(Class type, String value, final int limit, Map<String, String> arguments) {

	int maxLimit = getSize(arguments);

	Unit unit = getFilterUnit(arguments);

	value = StringUtils.normalize(value);
	List<DegreeDesignation> result = new ArrayList<DegreeDesignation>();
	Collection<DegreeDesignation> possibleDesignations = null;
	if (unit == null) {
	    possibleDesignations = rootDomainObject.getDegreeDesignationsSet();
	} else {
	    possibleDesignations = unit.getDegreeDesignation();
	}
	for (DegreeDesignation degreeDesignation : possibleDesignations) {
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

    private Unit getFilterUnit(Map<String, String> arguments) {
	String filterUnitOID = arguments.get("filterUnitOID");

	if ((filterUnitOID == null) || (filterUnitOID.equals("null"))) {
	    return null;
	}
	return (Unit) AbstractDomainObject.fromExternalId(filterUnitOID);
    }
}
