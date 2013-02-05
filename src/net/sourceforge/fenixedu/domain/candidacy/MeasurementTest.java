package net.sourceforge.fenixedu.domain.candidacy;

import java.util.HashSet;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import net.sourceforge.fenixedu.domain.EntryPhase;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.space.Campus;
import net.sourceforge.fenixedu.domain.student.Registration;

public class MeasurementTest extends MeasurementTest_Base {

    protected MeasurementTest() {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
    }

    public MeasurementTest(EntryPhase entryPhase, ExecutionYear executionYear, Campus campus) {
        this();

        check(entryPhase, "error.net.sourceforge.fenixedu.domain.candidacy.MeasurementTest.entryPhase.cannot.be.null");
        check(executionYear, "error.net.sourceforge.fenixedu.domain.candidacy.MeasurementTest.executionYear.cannot.be.null");
        check(campus, "error.net.sourceforge.fenixedu.domain.candidacy.MeasurementTest.campus.cannot.be.null");

        setEntryPhase(entryPhase);
        setExecutionYear(executionYear);
        setCampus(campus);
    }

    public void assignToRoom(Registration registration) {
        for (final MeasurementTestShift shift : getSortedShifts()) {
            if (shift.hasForDegreeCurricularPlan(registration.getActiveDegreeCurricularPlan()) && shift.hasAvailableRoom()) {
                shift.getAvailableRoom().addRegistrations(registration);

                return;
            }
        }

        throw new DomainException("error.candidacy.MeasurementTest.unable.to.find.empty.room.for.registration");

    }

    private SortedSet<MeasurementTestShift> getSortedShifts() {
        final SortedSet<MeasurementTestShift> result = new TreeSet<MeasurementTestShift>(MeasurementTestShift.COMPARATOR_BY_NAME);

        result.addAll(getShifts());

        return result;
    }

    public Set<Registration> getAssignedRegistrations() {
        final Set<Registration> result = new HashSet<Registration>();

        for (final MeasurementTestShift shift : getSortedShifts()) {
            for (MeasurementTestRoom room : shift.getSortedRooms()) {
                result.addAll(room.getRegistrations());
            }
        }

        return result;
    }

    public static MeasurementTest readBy(EntryPhase entryPhase, ExecutionYear executionYear, Campus campus) {
        for (final MeasurementTest test : RootDomainObject.getInstance().getMeasurementTests()) {
            if (test.isFor(entryPhase, executionYear, campus)) {
                return test;
            }
        }

        return null;
    }

    private boolean isFor(EntryPhase entryPhase, ExecutionYear executionYear, Campus campus) {
        return getEntryPhase().equals(entryPhase) && getExecutionYear() == executionYear && getCampus() == campus;
    }

    public MeasurementTestShift getShiftByName(String name) {
        for (final MeasurementTestShift each : getShifts()) {
            if (each.getName().equals(name)) {
                return each;
            }
        }

        return null;
    }

}
