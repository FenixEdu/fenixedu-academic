/*
 * Created on 9/Mai/2003
 *
 */
package net.sourceforge.fenixedu.domain;

import java.util.Comparator;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;

import org.apache.commons.beanutils.BeanComparator;

import dml.runtime.RelationAdapter;

/**
 * @author asnr and scpo
 */
public class StudentGroup extends StudentGroup_Base {

    public static final Comparator<StudentGroup> COMPARATOR_BY_GROUP_NUMBER = new BeanComparator(
	    "groupNumber");

    static {
	StudentGroupAttend.addListener(new StudentGroupAttendListener());
    }

    private static class StudentGroupAttendListener extends RelationAdapter<StudentGroup, Attends> {
	@Override
	public void beforeRemove(StudentGroup studentGroup, Attends attends) {
	    if (!studentGroup.getProjectSubmissions().isEmpty()) {
		throw new DomainException(
			"error.studentGroup.cannotRemoveAttendsBecauseAlreadyHasProjectSubmissions");
	    }

	    super.beforeRemove(studentGroup, attends);
	}

    }

    public StudentGroup() {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
    }

    public StudentGroup(Integer groupNumber, Grouping grouping) {
	this();
	super.setGroupNumber(groupNumber);
	super.setGrouping(grouping);
    }

    public StudentGroup(Integer groupNumber, Grouping grouping, Shift shift) {
	this();
	super.setGroupNumber(groupNumber);
	super.setGrouping(grouping);
	super.setShift(shift);
    }

    public void delete() {
	if (!hasAnyAttends() && !hasAnyProjectSubmissions()) {
	    removeShift();
	    removeGrouping();
	    removeRootDomainObject();
	    deleteDomainObject();
	} else {
	    throw new DomainException("student.group.cannot.be.deleted");
	}
    }

    public void editShift(Shift shift) {
	if (this.getGrouping().getShiftType() == null
		|| (!this.getGrouping().getShiftType().equals(shift.getTipo()))) {
	    throw new DomainException(this.getClass().getName(), "");
	}

	this.setShift(shift);
    }

}
