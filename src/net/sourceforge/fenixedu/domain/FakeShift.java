package net.sourceforge.fenixedu.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import jvstm.cps.ConsistencyPredicate;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.injectionCode.AccessControl;

public class FakeShift extends FakeShift_Base {

    public FakeShift() {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
    }

    public FakeShift(String name, Integer capacity) {
	this();
	setName(name);
	setCapacity(capacity);
    }

    public FakeShift(Shift shift) {
	this(shift.getNome(), shift.getCapacityBasedOnSmallestRoom());
    }

    public int getVacancies() {
	return getCapacity() - getFakeShiftEnrollmentsCount();
    }

    public Collection<FakeShiftEnrollment> getFakeShiftEnrollmentsForCurrentUser() {
	List<FakeShiftEnrollment> fakeShiftEnrollments = new ArrayList<FakeShiftEnrollment>();
	for (FakeShiftEnrollment fakeEnrollment : getFakeShiftEnrollments()) {
	    if (fakeEnrollment.getPerson().equals(AccessControl.getPerson())) {
		fakeShiftEnrollments.add(fakeEnrollment);
	    }
	}
	return fakeShiftEnrollments;
    }

    @ConsistencyPredicate
    public boolean checkCapacityGreaterThanEnrollments() {
	return getCapacity() >= getFakeShiftEnrollmentsCount();
    }

    public void delete() {
	for (FakeShiftEnrollment fakeEnrollment : getFakeShiftEnrollments()) {
	    fakeEnrollment.delete();
	}

	removeRootDomainObject();
	deleteDomainObject();
    }

    public void enroll() {
	if (getVacancies() == 0) {
	    throw new DomainException("This FakeShift has no vacancies left.");
	}
	new FakeShiftEnrollment(this, AccessControl.getPerson());
    }

    public void resetCurrentUserEnrollments() {
	for (FakeShiftEnrollment fakeEnrollment : getFakeShiftEnrollmentsForCurrentUser()) {
	    fakeEnrollment.delete();
	}
    }

    public static Collection<FakeShift> readAFewRandomFakeShifts() {
	ArrayList<FakeShift> aFewRandomFakeShifts = new ArrayList<FakeShift>();

	ArrayList<FakeShift> allFakeShiftsShuffled = new ArrayList<FakeShift>(RootDomainObject.getInstance().getFakeShifts());
	Collections.shuffle(allFakeShiftsShuffled);

	int maxElements = 10;
	maxElements = (maxElements > allFakeShiftsShuffled.size()) ? allFakeShiftsShuffled.size() : maxElements;
	for (int i = 0; i < maxElements; i++) {
	    aFewRandomFakeShifts.add(allFakeShiftsShuffled.get(i));
	}

	return aFewRandomFakeShifts;
    }

    public static void importFromLastSemesterShifts() {
	importFromShifts(ExecutionSemester.readActualExecutionSemester().getPreviousExecutionPeriod());
    }

    public static void importFromShifts(ExecutionSemester semester) {
	Collection<ExecutionCourse> courses = semester.getAssociatedExecutionCourses();
	for (ExecutionCourse course : courses) {
	    for (Shift shift : course.getAssociatedShifts()) {
		new FakeShift(shift);
	    }
	}
    }

    public static void deleteAllFakeShifts() {
	for (FakeShift fakeShift : RootDomainObject.getInstance().getFakeShifts()) {
	    fakeShift.delete();
	}
    }
}
