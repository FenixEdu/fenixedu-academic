package net.sourceforge.fenixedu.domain.phd;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

public class PhdIndividualProgramProcessNumber extends PhdIndividualProgramProcessNumber_Base implements
	Comparable<PhdIndividualProgramProcessNumber> {

    public static Comparator<PhdIndividualProgramProcessNumber> COMPARATOR_BY_NUMBER = new Comparator<PhdIndividualProgramProcessNumber>() {
	public int compare(PhdIndividualProgramProcessNumber left, PhdIndividualProgramProcessNumber right) {
	    int comparationResult = left.getNumber().compareTo(right.getNumber());
	    return (comparationResult == 0) ? left.getIdInternal().compareTo(right.getIdInternal()) : comparationResult;
	}
    };

    protected PhdIndividualProgramProcessNumber() {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
    }

    protected PhdIndividualProgramProcessNumber(Integer number, Integer year) {
	this();
	init(number, year);
    }

    private void init(Integer number, Integer year) {
	checkParameters(number, year);
	super.setNumber(number);
	super.setYear(year);

    }

    @Override
    public void setYear(Integer year) {
	throw new DomainException(
		"error.net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcessNumber.cannot.modify.year");
    }

    @Override
    public void setNumber(Integer number) {
	throw new DomainException(
		"error.net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcessNumber.cannot.modify.number");
    }

    private void checkParameters(Integer number, Integer year) {
	check(number, "error.net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcessNumber.number.cannot.be.null");
	check(year, "error.net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcessNumber.year.cannot.be.null");
    }

    static public PhdIndividualProgramProcessNumber generateNextForYear(final Integer year) {
	final PhdIndividualProgramProcessNumber maxByYear = readMaxByYear(year);
	final Integer number = maxByYear != null ? maxByYear.getNumber() + 1 : 1;

	return new PhdIndividualProgramProcessNumber(number, year);
    }

    static public PhdIndividualProgramProcessNumber readMaxByYear(final Integer year) {
	final Set<PhdIndividualProgramProcessNumber> processes = readByYear(year);

	return processes.isEmpty() ? null : Collections.max(processes, COMPARATOR_BY_NUMBER);
    }

    static public Set<PhdIndividualProgramProcessNumber> readByYear(final Integer year) {
	final Set<PhdIndividualProgramProcessNumber> result = new HashSet<PhdIndividualProgramProcessNumber>();

	for (final PhdIndividualProgramProcessNumber each : RootDomainObject.getInstance().getPhdIndividualProcessNumbers()) {
	    if (each.getYear().intValue() == year) {
		result.add(each);
	    }
	}

	return result;
    }

    public String getFullProcessNumber() {
	return getNumber() + "/" + getYear();
    }

    @Override
    public int compareTo(PhdIndividualProgramProcessNumber other) {
	int res = -1 * getYear().compareTo(other.getYear());
	return (res != 0) ? res : -1 * getNumber().compareTo(other.getNumber());
    }
}
