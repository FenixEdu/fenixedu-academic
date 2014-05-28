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

import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.student.registrationStates.RegistrationStateType;
import net.sourceforge.fenixedu.domain.util.workflow.StateBean;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class RegistrationStateBean extends StateBean implements Serializable {

    Registration registration;

    String remarks;

    public RegistrationStateBean(Registration registration) {
        super();
        this.registration = registration;
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

}
