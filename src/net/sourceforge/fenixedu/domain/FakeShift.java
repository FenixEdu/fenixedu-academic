package net.sourceforge.fenixedu.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

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

    public void delete() {
	removeRootDomainObject();
	deleteDomainObject();
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
