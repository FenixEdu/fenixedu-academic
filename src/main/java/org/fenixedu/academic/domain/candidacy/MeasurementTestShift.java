/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Academic.
 *
 * FenixEdu Academic is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Academic is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Academic.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.fenixedu.academic.domain.candidacy;

import java.util.Comparator;
import java.util.SortedSet;
import java.util.TreeSet;

import org.fenixedu.academic.domain.exceptions.DomainException;
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
                                        "error.org.fenixedu.academic.domain.candidacy.MeasurementTestShift.already.contains.shift.with.same.name");

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
            throw new DomainException("error.org.fenixedu.academic.domain.candidacy.MeasurementTestShift.name.cannot.be.null",
                    args2);
        }
        String[] args = {};
        if (date == null) {
            throw new DomainException("error.org.fenixedu.academic.domain.candidacy.MeasurementTestShift.date.cannot.be.null",
                    args);
        }
        String[] args1 = {};
        if (test == null) {
            throw new DomainException("error.org.fenixedu.academic.domain.candidacy.MeasurementTestShift.test.cannot.be.null",
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

        result.addAll(getRoomsSet());

        return result;
    }

    public MeasurementTestRoom getRoomByName(String name) {
        for (final MeasurementTestRoom room : getRoomsSet()) {
            if (room.getName().equals(name)) {
                return room;
            }
        }

        return null;
    }

}
