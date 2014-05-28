/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Core.
 *
 * FenixEdu Core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.sourceforge.fenixedu.domain.candidacy;

import java.util.Comparator;
import java.util.SortedSet;
import java.util.TreeSet;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;

import org.fenixedu.bennu.core.domain.Bennu;
import org.joda.time.DateTime;

import pt.ist.fenixframework.dml.runtime.RelationAdapter;

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
        getRelationMeasurementTestShiftMeasurementTest().addListener(
                new RelationAdapter<MeasurementTest, MeasurementTestShift>() {

                    @Override
                    public void beforeAdd(MeasurementTest test, MeasurementTestShift toAdd) {

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

        super.setRootDomainObject(Bennu.getInstance());
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
    public boolean hasBennu() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasDate() {
        return getDate() != null;
    }

}
