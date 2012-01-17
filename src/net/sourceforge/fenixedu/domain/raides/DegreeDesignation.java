package net.sourceforge.fenixedu.domain.raides;

import net.sourceforge.fenixedu.domain.RootDomainObject;

public class DegreeDesignation extends DegreeDesignation_Base {

    public DegreeDesignation(String code, String description, DegreeClassification degreeClassification) {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
	setCode(code);
	setDescription(description);
	setDegreeClassification(degreeClassification);
    }

    public static DegreeDesignation readByName(String degreeDesignationName) {
	for (DegreeDesignation degreeDesignation : RootDomainObject.getInstance().getDegreeDesignationsSet()) {
	    if (degreeDesignation.getDescription().equalsIgnoreCase(degreeDesignationName)) {
		return degreeDesignation;
	    }
	}
	return null;
    }

    public void delete() {
	removeDegreeClassification();
	removeRootDomainObject();
	deleteDomainObject();
    }
}
