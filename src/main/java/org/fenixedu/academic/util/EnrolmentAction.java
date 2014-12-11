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
/*
 * Created on Nov 4, 2004
 */
package org.fenixedu.academic.util;

/**
 * @author nmgo
 * @author lmre
 */
public class EnrolmentAction implements Comparable<EnrolmentAction> {

    public static final int ENROL_TYPE = 1;

    public static final int UNENROL_TYPE = 2;

    private final String name;
    private final int value;

    public static final EnrolmentAction ENROL = new EnrolmentAction("enrol", EnrolmentAction.ENROL_TYPE);

    public static final EnrolmentAction UNENROL = new EnrolmentAction("unenrol", EnrolmentAction.UNENROL_TYPE);

    private EnrolmentAction(String name, int value) {
        this.name = name;
        this.value = value;
    }

    public static EnrolmentAction getEnum(int actionType) {
        switch (actionType) {
        case ENROL_TYPE:
            return ENROL;
        case UNENROL_TYPE:
            return UNENROL;
        default:
            throw new IllegalArgumentException(actionType + " is not a valid enrolment action!");
        }
    }

    @Override
    public String toString() {
        return this.name;
    }

    public int getValue() {
        return this.value;
    }

    public String getName() {
        return this.name;
    }

    @Override
    public int compareTo(EnrolmentAction other) {
        return this.value - other.value;
    }

    @Override
    public int hashCode() {
        return 31 * value;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof EnrolmentAction) {
            EnrolmentAction other = (EnrolmentAction) obj;
            return this.value == other.value;
        }
        return false;
    }

}
