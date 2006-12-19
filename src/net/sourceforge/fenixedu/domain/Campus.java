package net.sourceforge.fenixedu.domain;


/**
 * @Deprecated
 * Use net.sourceforge.fenixedu.domain.space.Campus instead
 * @author lmre
 *
 */

@Deprecated
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

    public void delete() {
        removeRootDomainObject();
        deleteDomainObject();
    }

    
    @Override
    public String getName() {
        return getSpaceCampus().getName();
    }

}
