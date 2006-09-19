package net.sourceforge.fenixedu.domain.candidacy.degree;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

public class ShiftDistribution extends ShiftDistribution_Base {

    private ShiftDistribution() {
	super();
	super.setRootDomainObject(RootDomainObject.getInstance());
    }

    public ShiftDistribution(ExecutionYear executionYear) {
	this();
	init(executionYear);
    }

    private void checkParameters(ExecutionYear executionYear) {
	if (executionYear == null) {
	    throw new DomainException(
		    "error.candidacy.degree.ShiftDistribution.executionYear.cannot.be.null");
	}
    }

    protected void init(ExecutionYear executionYear) {
	checkParameters(executionYear);
	if (executionYear.hasShiftDistribution()) {
	    throw new DomainException("error.candidacy.degree.ShiftDistribution.executionYear.already.has.a.shiftDistribution");
	}
	super.setExecutionYear(executionYear);
    }

    public List<ShiftDistributionEntry> getEntriesByStudentNumber(Integer studentNumber) {
	final List<ShiftDistributionEntry> result = new ArrayList<ShiftDistributionEntry>();
	for (final ShiftDistributionEntry shiftDistributionEntry : getShiftDistributionEntriesSet()) {
	    if (shiftDistributionEntry.getAbstractStudentNumber().equals(studentNumber)) {
		result.add(shiftDistributionEntry);
	    }
	}
	return result;
    }

    @Override
    public void setExecutionYear(ExecutionYear executionYear) {
	throw new DomainException("error.candidacy.degree.ShiftDistribution.cannot.modify.executionYear");
    }

    public boolean contains(int abstractStudentNumber, final ExecutionDegree executionDegree) {
	for (final ShiftDistributionEntry distributionEntry : executionDegree.getShiftDistributionEntriesSet()) {
	    if (distributionEntry.getAbstractStudentNumber().intValue() == abstractStudentNumber) {
		return true;
	    }
	}
	return false;
    }

}
