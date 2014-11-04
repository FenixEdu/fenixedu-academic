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
/**
 * 
 */
package org.fenixedu.academic.dto.administrativeOffice.candidacy;

import java.io.Serializable;

import org.fenixedu.academic.domain.candidacy.StudentCandidacy;
import org.fenixedu.academic.domain.student.EnrolmentModel;
import org.joda.time.YearMonthDay;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class RegisterCandidacyBean implements Serializable {

    private EnrolmentModel enrolmentModel;

    private YearMonthDay startDate = new YearMonthDay();

    private StudentCandidacy candidacy;

    public RegisterCandidacyBean(StudentCandidacy candidacy) {
        super();
        this.candidacy = candidacy;
    }

    public EnrolmentModel getEnrolmentModel() {
        return enrolmentModel;
    }

    public void setEnrolmentModel(EnrolmentModel enrolmentModel) {
        this.enrolmentModel = enrolmentModel;
    }

    public YearMonthDay getStartDate() {
        return startDate;
    }

    public void setStartDate(YearMonthDay startDate) {
        this.startDate = startDate;
    }

    public StudentCandidacy getCandidacy() {
        return candidacy;
    }

}
