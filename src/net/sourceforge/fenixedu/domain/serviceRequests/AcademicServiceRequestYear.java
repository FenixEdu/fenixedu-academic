package net.sourceforge.fenixedu.domain.serviceRequests;

import net.sourceforge.fenixedu.domain.RootDomainObject;

public class AcademicServiceRequestYear extends AcademicServiceRequestYear_Base {

    public AcademicServiceRequestYear(final int year) {
	super();
	super.setRootDomainObject(RootDomainObject.getInstance());

	setYear(Integer.valueOf(year));
	setLatestServiceRequestNumber(Integer.valueOf(0));
    }

    static final public AcademicServiceRequestYear readByYear(final int year) {
	for (final AcademicServiceRequestYear requestYear : RootDomainObject.getInstance().getAcademicServiceRequestYearsSet()) {
	    if (requestYear.getYear().intValue() == year) {
		return requestYear;
	    }
	}

	return null;
    }

    protected Integer generateServiceRequestNumber() {
	setLatestServiceRequestNumber(Integer.valueOf(getLatestServiceRequestNumber().intValue() + 1));
	return getLatestServiceRequestNumber();
    }

}
