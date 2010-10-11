package net.sourceforge.fenixedu.domain;

import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

public class DegreeSpecializationArea extends DegreeSpecializationArea_Base {
    public DegreeSpecializationArea(DegreeOfficialPublication officialPublication, MultiLanguageString area) {
	super();
	setOfficialPublication(officialPublication);
	setName(area);
    }

    @Override
    protected RootDomainObject getRootDomainObject() {
	return getOfficialPublication().getRootDomainObject();
    }

    public void delete() {
	removeOfficialPublication();
	deleteDomainObject();
    }
}
