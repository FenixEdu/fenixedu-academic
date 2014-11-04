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
/**
 * 
 */
package net.sourceforge.fenixedu.dataTransferObject.student;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.domain.student.StudentDataShareAuthorization;
import net.sourceforge.fenixedu.domain.student.registrationStates.RegistrationStateType;

import org.apache.commons.lang.StringUtils;

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
