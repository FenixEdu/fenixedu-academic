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
package org.fenixedu.academic.dto.student;

import java.io.Serializable;

import org.apache.commons.lang.StringUtils;
import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.student.Registration;
import org.fenixedu.academic.domain.student.Student;
import org.fenixedu.academic.domain.student.StudentDataShareAuthorization;
import org.fenixedu.academic.domain.student.registrationStates.RegistrationStateType;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
@SuppressWarnings("serial")
public class RegistrationWithStateForExecutionYearBean implements Serializable {

    private final Registration registration;

    private final ExecutionYear executionYear;

    private RegistrationStateType registrationState;

    public RegistrationWithStateForExecutionYearBean(Registration registration, ExecutionYear executionYear) {
        super();
        this.registration = registration;
        this.executionYear = executionYear;
    }

    public RegistrationWithStateForExecutionYearBean(Registration registration, RegistrationStateType registrationState,
            ExecutionYear executionYear) {
        this.registration = registration;
        this.registrationState = registrationState;
        this.executionYear = executionYear;
    }

    public RegistrationStateType getActiveStateType() {
        return registrationState != null ? registrationState : getRegistration().getLastRegistrationState(getExecutionYear())
                .getStateType();
    }

    private ExecutionYear getExecutionYear() {
        return executionYear;
    }

    public Registration getRegistration() {
        return registration;
    }

    public Student getStudent() {
        return getRegistration().getStudent();
    }

    public String getPersonalDataAuthorization() {
        StudentDataShareAuthorization dataAccess =
                getStudent().getPersonalDataAuthorizationAt(getExecutionYear().getEndDateYearMonthDay().toDateTimeAtMidnight());
        return dataAccess != null ? dataAccess.getAuthorizationChoice().getDescription() : StringUtils.EMPTY;
    }

}
