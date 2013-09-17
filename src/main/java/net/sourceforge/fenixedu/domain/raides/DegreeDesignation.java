package net.sourceforge.fenixedu.domain.raides;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.SchoolLevelType;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;

public class DegreeDesignation extends DegreeDesignation_Base {

    public DegreeDesignation(String code, String description, DegreeClassification degreeClassification) {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
        setCode(code);
        setDescription(description);
        setDegreeClassification(degreeClassification);
    }

    public static DegreeDesignation readByNameAndSchoolLevel(String degreeDesignationName, SchoolLevelType schoolLevel) {
        if ((schoolLevel == null) || (degreeDesignationName == null)) {
            return null;
        }

        List<DegreeClassification> possibleClassifications = new ArrayList<DegreeClassification>();
        for (String code : schoolLevel.getEquivalentDegreeClassifications()) {
            possibleClassifications.add(DegreeClassification.readByCode(code));
        }

        List<DegreeDesignation> possibleDesignations = new ArrayList<DegreeDesignation>();
        for (DegreeClassification classification : possibleClassifications) {
            if (classification.hasAnyDegreeDesignations()) {
                possibleDesignations.addAll(classification.getDegreeDesignations());
            }
        }

        for (DegreeDesignation degreeDesignation : possibleDesignations) {
            if (degreeDesignation.getDescription().equalsIgnoreCase(degreeDesignationName)) {
                return degreeDesignation;
            }
        }
        return null;
    }

    public void delete() {
        for (Unit institution : getInstitutionUnitSet()) {
            removeInstitutionUnit(institution);
        }
        setDegreeClassification(null);
        setRootDomainObject(null);
        deleteDomainObject();
    }
    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.organizationalStructure.Unit> getInstitutionUnit() {
        return getInstitutionUnitSet();
    }

    @Deprecated
    public boolean hasAnyInstitutionUnit() {
        return !getInstitutionUnitSet().isEmpty();
    }

    @Deprecated
    public boolean hasDescription() {
        return getDescription() != null;
    }

    @Deprecated
    public boolean hasRootDomainObject() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasDegreeClassification() {
        return getDegreeClassification() != null;
    }

    @Deprecated
    public boolean hasCode() {
        return getCode() != null;
    }

}
