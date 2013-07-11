package net.sourceforge.fenixedu.domain.candidacy;

import java.util.Comparator;
import java.util.SortedSet;
import java.util.TreeSet;

import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

import org.joda.time.DateTime;

import dml.runtime.RelationAdapter;

public class MeasurementTestShift extends MeasurementTestShift_Base {

    public static Comparator<MeasurementTestShift> COMPARATOR_BY_NAME = new Comparator<MeasurementTestShift>() {
        @Override
        public int compare(MeasurementTestShift leftMeasurementTestShift, MeasurementTestShift rightMeasurementTestShift) {
            int comparationResult = leftMeasurementTestShift.getName().compareTo(rightMeasurementTestShift.getName());
            return (comparationResult == 0) ? leftMeasurementTestShift.getExternalId().compareTo(
                    rightMeasurementTestShift.getExternalId()) : comparationResult;
        }
    };

    static {
        MeasurementTestShiftMeasurementTest.addListener(new RelationAdapter<MeasurementTestShift, MeasurementTest>() {

            @Override
            public void beforeAdd(MeasurementTestShift toAdd, MeasurementTest test) {

                if (toAdd != null && test != null) {
                    if (test.getShiftByName(toAdd.getName()) != null) {
                        throw new DomainException(
                                "error.net.sourceforge.fenixedu.domain.candidacy.MeasurementTestShift.already.contains.shift.with.same.name");

                    }
                }

            }

        });
    }

    protected MeasurementTestShift() {
        super();

        super.setRootDomainObject(RootDomainObject.getInstance());
    }

    public MeasurementTestShift(String name, DateTime date, MeasurementTest test) {
        this();
        String[] args2 = {};

        if (name == null || name.isEmpty()) {
            throw new DomainException("error.net.sourceforge.fenixedu.domain.candidacy.MeasurementTestShift.name.cannot.be.null",
                    args2);
        }
        String[] args = {};
        if (date == null) {
            throw new DomainException("error.net.sourceforge.fenixedu.domain.candidacy.MeasurementTestShift.date.cannot.be.null",
                    args);
        }
        String[] args1 = {};
        if (test == null) {
            throw new DomainException("error.net.sourceforge.fenixedu.domain.candidacy.MeasurementTestShift.test.cannot.be.null",
                    args1);
        }

        setDate(date);
        setName(name);
        setTest(test);
    }

    public MeasurementTestRoom getAvailableRoom() {
        for (final MeasurementTestRoom room : getSortedRooms()) {
            if (room.isAvailable()) {
                return room;
            }
        }

        return null;
    }

    public boolean hasAvailableRoom() {
        return getAvailableRoom() != null;
    }

    public SortedSet<MeasurementTestRoom> getSortedRooms() {
        final SortedSet<MeasurementTestRoom> result =
                new TreeSet<MeasurementTestRoom>(MeasurementTestRoom.COMPARATOR_BY_ROOM_ORDER);

        result.addAll(getRooms());

        return result;
    }

    public MeasurementTestRoom getRoomByName(String name) {
        for (final MeasurementTestRoom room : getRooms()) {
            if (room.getName().equals(name)) {
                return room;
            }
        }

        return null;
    }
    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.candidacy.MeasurementTestRoom> getRooms() {
        return getRoomsSet();
    }

    @Deprecated
    public boolean hasAnyRooms() {
        return !getRoomsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.DegreeCurricularPlan> getForDegreeCurricularPlan() {
        return getForDegreeCurricularPlanSet();
    }

    @Deprecated
    public boolean hasAnyForDegreeCurricularPlan() {
        return !getForDegreeCurricularPlanSet().isEmpty();
    }

    @Deprecated
    public boolean hasName() {
        return getName() != null;
    }

    @Deprecated
    public boolean hasTest() {
        return getTest() != null;
    }

    @Deprecated
    public boolean hasRootDomainObject() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasDate() {
        return getDate() != null;
    }

}
