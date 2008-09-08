package net.sourceforge.fenixedu.domain;

public class District extends District_Base {

    public District() {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
    }

    static public District readByCode(final String code) {
	for (final District district : RootDomainObject.getInstance().getDistricts()) {
	    if (district.getCode().equals(code)) {
		return district;
	    }
	}

	return null;
    }

}
