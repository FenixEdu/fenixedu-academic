package net.sourceforge.fenixedu.applicationTier.Servico.commons.searchers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.applicationTier.Servico.commons.AutoCompleteSearchService;
import net.sourceforge.fenixedu.domain.SchoolLevelType;
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
	SchoolLevelType schoolLevel = getFilterSchoolLevel(arguments);

	value = StringUtils.normalize(value);
	List<DegreeDesignation> result = new ArrayList<DegreeDesignation>();
	Collection<DegreeDesignation> possibleDesignations = null;
	if (unit == null) {
	    possibleDesignations = rootDomainObject.getDegreeDesignationsSet();
	} else {
	    possibleDesignations = unit.getDegreeDesignation();
	}

	if (schoolLevel != null) {
	    possibleDesignations = filterDesignationsBySchoolLevel(possibleDesignations, schoolLevel);
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

    private Collection<DegreeDesignation> filterDesignationsBySchoolLevel(Collection<DegreeDesignation> possibleDesignations,
	    SchoolLevelType schoolLevel) {
	List<DegreeDesignation> designationsToKeep = new ArrayList<DegreeDesignation>();
	for (DegreeDesignation designation : possibleDesignations) {
	    if (schoolLevel.getEquivalentDegreeClassifications().contains(designation.getDegreeClassification().getCode())) {
		designationsToKeep.add(designation);
	    }
	}
	
	return designationsToKeep;
    }

    private int getSize(Map<String, String> arguments) {
	String size = arguments.get("size");

	if (size == null) {
	    return DEFAULT_SIZE;
	} else {
	    return Integer.parseInt(size);
	}
    }

    private SchoolLevelType getFilterSchoolLevel(Map<String, String> arguments) {
	String schoolLevelName = arguments.get("filterSchoolLevelName");
	if ((schoolLevelName == null) || (schoolLevelName.equals("null"))) {
	    return null;
	}
	return Enum.<SchoolLevelType> valueOf(SchoolLevelType.class, schoolLevelName);
    }

    private Unit getFilterUnit(Map<String, String> arguments) {
	String filterUnitOID = arguments.get("filterUnitOID");
	if ((filterUnitOID == null) || (filterUnitOID.equals("null"))) {
	    return null;
	}

	return (Unit) AbstractDomainObject.fromExternalId(filterUnitOID);
    }
}
