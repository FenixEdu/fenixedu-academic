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

import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.student.EnrolmentModel;
import org.fenixedu.academic.domain.student.Registration;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class ManageEnrolmentModelBean implements Serializable {

    private EnrolmentModel enrolmentModel;

    private ExecutionYear executionYear;

    private Registration registration;

    public ManageEnrolmentModelBean(Registration registration) {
        super();
        this.registration = registration;
        setExecutionYear(ExecutionYear.readCurrentExecutionYear());
        setEnrolmentModel(getRegistration().getEnrolmentModelForExecutionYear(getExecutionYear()));
    }

    public EnrolmentModel getEnrolmentModel() {
        return enrolmentModel;
    }

    public void setEnrolmentModel(EnrolmentModel enrolmentModel) {
        this.enrolmentModel = enrolmentModel;
    }

    public ExecutionYear getExecutionYear() {
        return executionYear;
    }

    public void setExecutionYear(ExecutionYear executionYear) {
        this.executionYear = executionYear;
    }

    public Registration getRegistration() {
        return registration;
    }

}
