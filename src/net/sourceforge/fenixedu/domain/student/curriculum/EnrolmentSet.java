package net.sourceforge.fenixedu.domain.student.curriculum;

import java.util.Collection;
import java.util.TreeSet;

import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.ExecutionYear;

public class EnrolmentSet extends TreeSet<Enrolment> {

    private final ExecutionYear executionYear;

    public EnrolmentSet(final ExecutionYear executionYear) {
	super(Enrolment.REVERSE_COMPARATOR_BY_EXECUTION_PERIOD_AND_ID);
	this.executionYear = executionYear;
    }

    @Override
    public boolean add(final Enrolment enrolment) {
	final ExecutionYear enrolmentExecutionYear = enrolment.getExecutionPeriod().getExecutionYear();
	return executionYear == null || executionYear.compareTo(enrolmentExecutionYear) > 0 ? super
		.add(enrolment) : false;
    }

    @Override
    public boolean addAll(final Collection<? extends Enrolment> enrolments) {
	boolean changed = false;
	for (final Enrolment enrolment : enrolments) {
	    changed &= add(enrolment);
	}
	return changed;
    }

    @Override
    public EnrolmentSet clone() {
	final EnrolmentSet clone = new EnrolmentSet(executionYear);
	clone.addAll(this);
	return clone;
    }

}