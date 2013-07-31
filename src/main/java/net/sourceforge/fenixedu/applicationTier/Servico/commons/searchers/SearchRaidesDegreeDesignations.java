package net.sourceforge.fenixedu.applicationTier.Servico.commons.searchers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.SchoolLevelType;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.raides.DegreeDesignation;
import pt.ist.bennu.core.presentationTier.renderers.autoCompleteProvider.AutoCompleteProvider;
import net.sourceforge.fenixedu.util.StringUtils;
import pt.ist.fenixframework.FenixFramework;

public class SearchRaidesDegreeDesignations implements AutoCompleteProvider<DegreeDesignation> {

    private static int DEFAULT_SIZE = 50;

    @Override
    public Collection<DegreeDesignation> getSearchResults(Map<String, String> argsMap, String value, int maxCount) {

        int maxLimit = getSize(argsMap);

        Unit unit = getFilterUnit(argsMap);
        SchoolLevelType schoolLevel = getFilterSchoolLevel(argsMap);

        value = StringUtils.normalize(value);
        List<DegreeDesignation> result = new ArrayList<DegreeDesignation>();
        Collection<DegreeDesignation> possibleDesignations = null;
        if (unit == null) {
            possibleDesignations = RootDomainObject.getInstance().getDegreeDesignationsSet();
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

        return (Unit) FenixFramework.getDomainObject(filterUnitOID);
    }
}
