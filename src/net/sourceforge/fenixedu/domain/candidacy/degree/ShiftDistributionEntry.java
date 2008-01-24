package net.sourceforge.fenixedu.domain.candidacy.degree;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

public class ShiftDistributionEntry extends ShiftDistributionEntry_Base {

    static final public Comparator<ShiftDistributionEntry> NUMBER_COMPARATOR = new Comparator<ShiftDistributionEntry>() {
        public int compare(ShiftDistributionEntry o1, ShiftDistributionEntry o2) {
            return o1.getAbstractStudentNumber().compareTo(o2.getAbstractStudentNumber());
        }
    };
    
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

    public void delete() {
	super.setShiftDistribution(null);
	super.setExecutionDegree(null);
	super.setShift(null);

	removeRootDomainObject();
	super.deleteDomainObject();
    }
    
    public void changeShift(Shift newShift) {
	super.setShift(newShift);
    }
    
    public static List<ShiftDistributionEntry> readByAbstractNumber(Integer abstractNumber) {
        List<ShiftDistributionEntry> result = new ArrayList<ShiftDistributionEntry>();
        for (ShiftDistributionEntry entry : RootDomainObject.getInstance().getShiftDistributionEntries()) {
            if (entry.getAbstractStudentNumber().equals(abstractNumber)) {
                result.add(entry);
            }
        }
        return result;
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
