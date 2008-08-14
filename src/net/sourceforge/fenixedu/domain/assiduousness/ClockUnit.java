package net.sourceforge.fenixedu.domain.assiduousness;

import net.sourceforge.fenixedu.domain.RootDomainObject;

public class ClockUnit extends ClockUnit_Base {

    public ClockUnit(String acronym, String name) {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
	setAcronym(acronym);
	setName(name);
    }

}
