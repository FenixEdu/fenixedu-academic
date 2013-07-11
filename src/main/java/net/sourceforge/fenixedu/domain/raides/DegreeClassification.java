package net.sourceforge.fenixedu.domain.raides;

import net.sourceforge.fenixedu.domain.RootDomainObject;

public class DegreeClassification extends DegreeClassification_Base {

    public DegreeClassification(String code, String description1, String description2, String abbreviation, Integer order) {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
        setCode(code);
        setDescription1(description1);
        setDescription2(description2);
        setAbbreviation(abbreviation);
        setClassificationOrder(order);
    }

    public DegreeClassification(String code, String description1, String description2, String abbreviation) {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
        setCode(code);
        setDescription1(description1);
        setDescription2(description2);
        setAbbreviation(abbreviation);
    }

    public void delete() {
        for (DegreeDesignation designation : getDegreeDesignations()) {
            removeDegreeDesignations(designation);
        }
        setRootDomainObject(null);
        deleteDomainObject();
    }

    public static DegreeClassification readByCode(String code) {
        for (DegreeClassification degreeClassification : RootDomainObject.getInstance().getDegreeClassificationsSet()) {
            if (degreeClassification.getCode().equals(code)) {
                return degreeClassification;
            }
        }
        return null;
    }
}
