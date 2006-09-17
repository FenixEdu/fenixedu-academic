package net.sourceforge.fenixedu.domain.candidacy;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.SortedSet;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.util.workflow.Operation;

import org.apache.commons.beanutils.BeanComparator;

public abstract class Candidacy extends Candidacy_Base {

    protected Candidacy() {
	super();
	setOjbConcreteClass(this.getClass().getName());
	setNumber(createCandidacyNumber());
	setRootDomainObject(RootDomainObject.getInstance());
    }

    public Candidacy(CandidacySituation candidacySituation) {
	this();
	if (candidacySituation == null) {
	    throw new DomainException("candidacy situation cannot be null");
	}
	this.addCandidacySituations(candidacySituation);
    }

    public final Integer createCandidacyNumber() {
	if (RootDomainObject.getInstance().getCandidaciesCount() == 0) {
	    return Integer.valueOf(1);
	}
	Candidacy candidacy = (Candidacy) Collections.max(RootDomainObject.getInstance()
		.getCandidaciesSet(), new BeanComparator("number"));
	return candidacy.getNumber() + 1;
    }

    public CandidacySituation getActiveCandidacySituation() {
	return Collections.max(getCandidacySituations(), CandidacySituation.DATE_COMPARATOR);
    }

    // static methods

    public static Candidacy readByCandidacyNumber(Integer candidacyNumber) {
	for (Candidacy candidacy : RootDomainObject.getInstance().getCandidacies()) {
	    if (candidacy.getNumber().equals(candidacyNumber)) {
		return candidacy;
	    }
	}
	return null;
    }

    public static Set<Candidacy> readCandidaciesBetween(final Integer from, final Integer to) {
	final Set<Candidacy> result = new HashSet<Candidacy>();
	for (final Candidacy candidacy : RootDomainObject.getInstance().getCandidaciesSet()) {
	    if (candidacy.getNumber() >= from && candidacy.getNumber() <= to) {
		result.add(candidacy);
	    }
	}
	return result;
    }

    public static Set<Candidacy> readDegreeCandidaciesBetween(final Integer from, final Integer to) {
	final Set<Candidacy> result = new HashSet<Candidacy>();
	for (final Candidacy candidacy : RootDomainObject.getInstance().getCandidaciesSet()) {
	    if (candidacy instanceof DegreeCandidacy) {
		if (candidacy.getNumber() >= from && candidacy.getNumber() <= to) {
		    result.add(candidacy);
		}
	    }
	}
	return result;
    }

    public static Set<Candidacy> readDFACandidaciesBetween(final Integer from, final Integer to) {
	final Set<Candidacy> result = new HashSet<Candidacy>();
	for (final Candidacy candidacy : RootDomainObject.getInstance().getCandidaciesSet()) {
	    if (candidacy instanceof DFACandidacy) {
		if (candidacy.getNumber() >= from && candidacy.getNumber() <= to) {
		    result.add(candidacy);
		}
	    }
	}
	return result;
    }

    public abstract String getDescription();

    abstract Set<Operation> getOperations(CandidacySituation candidacySituation);

    abstract void moveToNextState(CandidacyOperationType candidacyOperationType, Person person);

    public abstract boolean isConcluded();
}
