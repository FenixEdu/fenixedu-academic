package net.sourceforge.fenixedu.domain.candidacyProcess.over23;

import java.util.Comparator;

import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

public class Over23IndividualCandidacyDegreeEntry extends Over23IndividualCandidacyDegreeEntry_Base {

    static Comparator<Over23IndividualCandidacyDegreeEntry> COMPARATOR_BY_ORDER = new Comparator<Over23IndividualCandidacyDegreeEntry>() {
	public int compare(Over23IndividualCandidacyDegreeEntry o1, Over23IndividualCandidacyDegreeEntry o2) {
	    return o1.getDegreeOrder().compareTo(o2.getDegreeOrder());
	}
    };

    private Over23IndividualCandidacyDegreeEntry() {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
    }

    Over23IndividualCandidacyDegreeEntry(final Over23IndividualCandidacy candidacy, final Degree degree, final int order) {
	this();
	checkParameters(candidacy, degree);
	checkOrder(candidacy, degree, order);
	super.setOver23IndividualCandidacy(candidacy);
	super.setDegree(degree);
	super.setDegreeOrder(order);
    }

    private void checkParameters(final Over23IndividualCandidacy candidacy, final Degree degree) {
	if (candidacy == null) {
	    throw new DomainException("error.Over23IndividualCandidacyDegreeEntry.invalid.candidacy");
	}
	if (degree == null) {
	    throw new DomainException("error.Over23IndividualCandidacyDegreeEntry.invalid.degree");
	}
    }

    private void checkOrder(final Over23IndividualCandidacy candidacy, final Degree degree, int order) {
	for (final Over23IndividualCandidacyDegreeEntry entry : candidacy.getOver23IndividualCandidacyDegreeEntries()) {
	    if (entry.isFor(degree) && entry.hasDegreeOrder(order)) {
		throw new DomainException("error.Over23IndividualCandidacyDegreeEntry.found.duplicated.order");
	    }
	}
    }

    public boolean isFor(Degree degree) {
	return getDegree().equals(degree);
    }

    public boolean hasDegreeOrder(int order) {
	return getDegreeOrder().intValue() == order;
    }

    void delete() {
	removeDegree();
	removeOver23IndividualCandidacy();
	removeRootDomainObject();
	super.deleteDomainObject();
    }

}
