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
}
