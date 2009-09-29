package net.sourceforge.fenixedu.domain;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;

import org.joda.time.LocalDate;

public class DegreeOfficialPublication extends DegreeOfficialPublication_Base {
    public DegreeOfficialPublication(Degree degree, LocalDate date) {
	if (degree == null) {
	    throw new DomainException("error.degree.officialpublication.unlinked");
	}
	if (date == null) {
	    throw new DomainException("error.degree.officialpublication.undated");
	}
	setDegree(degree);
	setPublication(date);
    }

    @Override
    protected RootDomainObject getRootDomainObject() {
	return getDegree().getRootDomainObject();
    }
}
