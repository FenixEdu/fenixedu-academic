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
package net.sourceforge.fenixedu.dataTransferObject.degreeAdministrativeOffice.enrolment;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.curriculum.EnrollmentCondition;

public class SpecialSeasonToEnrolBean implements Serializable {

    private Enrolment enrolment;
    private boolean toSubmit;
    private EnrollmentCondition enrolmentCondition;

    public EnrollmentCondition getEnrolmentCondition() {
        return enrolmentCondition;
    }

    public void setEnrolmentCondition(EnrollmentCondition enrollmentCondition) {
        this.enrolmentCondition = enrollmentCondition;
    }

    public Enrolment getEnrolment() {
        return enrolment;
    }

    public void setEnrolment(Enrolment enrolment) {
        this.enrolment = enrolment;
    }

    public boolean isToSubmit() {
        return toSubmit;
    }

    public void setToSubmit(boolean toSubmit) {
        this.toSubmit = toSubmit;
    }

}
