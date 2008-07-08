package net.sourceforge.fenixedu.domain.residence;

import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.util.Month;

import org.joda.time.DateTime;

public class ResidenceYear extends ResidenceYear_Base {

    public ResidenceYear() {
	this(getNextYear());
    }

    public ResidenceYear(Integer year) {
	super();
	setYear(year);
	for (Month month : Month.values()) {
	    new ResidenceMonth(month, this);
	}
    }

    private static Integer getNextYear() {
	Integer next = null;
	for (ResidenceYear year : RootDomainObject.getInstance().getResidenceYears()) {
	    if (next == null || year.getYear() > next) {
		next = year.getYear();
	    }
	}

	return next != null ? next : new DateTime().getYear();
    }
}
