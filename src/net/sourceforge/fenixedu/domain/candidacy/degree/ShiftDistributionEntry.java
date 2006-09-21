package net.sourceforge.fenixedu.domain.candidacy.degree;

import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

public class ShiftDistributionEntry extends ShiftDistributionEntry_Base {

    private ShiftDistributionEntry() {
	super();
	super.setRootDomainObject(RootDomainObject.getInstance());
	super.setDistributed(Boolean.FALSE);
    }

    public ShiftDistributionEntry(ShiftDistribution shiftDistribution, ExecutionDegree executionDegree,
	    Shift shift, Integer abstractStudentNumber) {
	this();
	init(shiftDistribution, executionDegree, shift, abstractStudentNumber);
    }

    private void checkParameters(ShiftDistribution shiftDistribution, ExecutionDegree executionDegree, Shift shift,
	    Integer abstractStudentNumber) {
	if (shiftDistribution == null) {
	    throw new DomainException(
		    "error.candidacy.degree.ShiftDistributionEntry.shiftDistribution.cannot.be.null");
	}
	if (executionDegree == null) {
	    throw new DomainException("error.candidacy.degree.ShiftDistributionEntry.executionDegree.cannot.be.null");
	}
	if (shift == null) {
	    throw new DomainException(
		    "error.candidacy.degree.ShiftDistributionEntry.shift.cannot.be.null");
	}
	if (abstractStudentNumber == null) {
	    throw new DomainException(
		    "error.candidacy.degree.ShiftDistributionEntry.abstractStudentNumber.cannot.be.null");
	}
    }

    protected void init(ShiftDistribution shiftDistribution, ExecutionDegree executionDegree, Shift shift, Integer abstractStudentNumber) {

	checkParameters(shiftDistribution, executionDegree, shift, abstractStudentNumber);

	super.setShiftDistribution(shiftDistribution);
	super.setExecutionDegree(executionDegree);
	super.setShift(shift);
	super.setAbstractStudentNumber(abstractStudentNumber);
    }
    

    public void changeShift(Shift newShift) {
	super.setShift(newShift);
    }

    @Override
    public void setShiftDistribution(ShiftDistribution shiftDistribution) {
	throw new DomainException(
		"error.candidacy.degree.ShiftDistributionEntry.cannot.modify.shiftDistribution");
    }
    
    @Override
    public void setExecutionDegree(ExecutionDegree executionDegree) {
	throw new DomainException("error.candidacy.degree.ShiftDistributionEntry.cannot.modify.executionDegree");
    }

    @Override
    public void setShift(Shift shift) {
	throw new DomainException(
	"error.candidacy.degree.ShiftDistributionEntry.cannot.modify.shiftName");
    }
    
    @Override
    public void setAbstractStudentNumber(Integer abstractStudentNumber) {
	throw new DomainException(
		"error.candidacy.degree.ShiftDistributionEntry.cannot.modify.studentNumber");
    }

    public boolean alreadyDistributed() {
	return getDistributed().booleanValue();
    }

}
