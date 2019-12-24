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

import org.fenixedu.academic.domain.ExecutionInterval;
import org.fenixedu.academic.domain.student.Registration;
import org.fenixedu.academic.domain.student.registrationStates.RegistrationStateType;
import org.fenixedu.academic.domain.util.workflow.StateBean;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class RegistrationStateBean extends StateBean implements Serializable {

    Registration registration;

    String remarks;

    ExecutionInterval executionInterval;

    public RegistrationStateBean(Registration registration) {
        super();
        this.registration = registration;
        this.executionInterval = ExecutionInterval.findFirstCurrentChild(registration.getDegree().getCalendar());
        setStateDate(null);
    }

    public RegistrationStateBean(final RegistrationStateType type) {
        super(type.name());
    }

    public Registration getRegistration() {
        return registration;
    }

    public String getRemarks() {
        return remarks;
    }

    public RegistrationStateType getStateType() {
        return getNextState() == null ? null : RegistrationStateType.valueOf(getNextState());
    }

    public void setRegistration(Registration registration) {
        this.registration = registration;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public void setStateType(final RegistrationStateType stateType) {
        setNextState(stateType == null ? null : stateType.name());
    }

    public ExecutionInterval getExecutionInterval() {
        return executionInterval;
    }

    public void setExecutionInterval(ExecutionInterval executionInterval) {
        this.executionInterval = executionInterval;
    }

}
