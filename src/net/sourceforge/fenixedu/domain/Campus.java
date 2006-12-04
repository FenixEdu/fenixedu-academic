package net.sourceforge.fenixedu.domain;

public class Campus extends Campus_Base {

    public Campus() {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
    }

    public static Campus readCampusByName(final String name) {
	for (final Campus campus : RootDomainObject.getInstance().getCampussSet()) {
	    if (campus.getName().equals(name)) {
		return campus;
	    }
	}
	return null;
    }

    // TODO : fix this when the new spaces structure is introduced
    // and the location of each campus is known.
    public String getLocation() {
	if (getName().equals("Alameda")) {
	    return "Lisboa";
	} else if (getName().equals("TagusPark")) {
	    return "Oeiras";
	}
	
	return null;
    }

}
