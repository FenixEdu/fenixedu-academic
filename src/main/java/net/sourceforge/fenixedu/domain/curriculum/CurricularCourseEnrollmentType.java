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
package net.sourceforge.fenixedu.domain.curriculum;

public enum CurricularCourseEnrollmentType {

    TEMPORARY,

    DEFINITIVE,

    NOT_ALLOWED,

    VALIDATED,

    NO_EVALUATE;

    public CurricularCourseEnrollmentType and(CurricularCourseEnrollmentType other) {

        if ((this == NO_EVALUATE) || (other == NO_EVALUATE)) {
            return NO_EVALUATE;
        } else if ((this == NOT_ALLOWED) && (other == NOT_ALLOWED)) {
            return NOT_ALLOWED;
        } else if ((this == NOT_ALLOWED) && (other == TEMPORARY)) {
            return NOT_ALLOWED;
        } else if ((this == NOT_ALLOWED) && (other == DEFINITIVE)) {
            return NOT_ALLOWED;
        } else if ((this == TEMPORARY) && (other == NOT_ALLOWED)) {
            return NOT_ALLOWED;
        } else if ((this == TEMPORARY) && (other == TEMPORARY)) {
            return TEMPORARY;
        } else if ((this == TEMPORARY) && (other == DEFINITIVE)) {
            return TEMPORARY;
        } else if ((this == DEFINITIVE) && (other == NOT_ALLOWED)) {
            return NOT_ALLOWED;
        } else if ((this == DEFINITIVE) && (other == TEMPORARY)) {
            return TEMPORARY;
        } else {
            return DEFINITIVE;
        }
    }

    public CurricularCourseEnrollmentType or(CurricularCourseEnrollmentType other) {

        if (this == NO_EVALUATE) {
            return other;
        } else if (other == NO_EVALUATE) {
            return this;
        } else if ((this == NOT_ALLOWED) && (other == NOT_ALLOWED)) {
            return NOT_ALLOWED;
        } else if ((this == NOT_ALLOWED) && (other == TEMPORARY)) {
            return TEMPORARY;
        } else if ((this == NOT_ALLOWED) && (other == DEFINITIVE)) {
            return DEFINITIVE;
        } else if ((this == TEMPORARY) && (other == NOT_ALLOWED)) {
            return TEMPORARY;
        } else if ((this == TEMPORARY) && (other == TEMPORARY)) {
            return TEMPORARY;
        } else if ((this == TEMPORARY) && (other == DEFINITIVE)) {
            return DEFINITIVE;
        } else if ((this == DEFINITIVE) && (other == NOT_ALLOWED)) {
            return DEFINITIVE;
        } else if ((this == DEFINITIVE) && (other == TEMPORARY)) {
            return DEFINITIVE;
        } else {
            return DEFINITIVE;
        }
    }

    public String getName() {
        return name();
    }

}