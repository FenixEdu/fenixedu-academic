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
package org.fenixedu.academic.dto.messaging;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.fenixedu.academic.domain.student.Registration;

public class RegistrationsBean implements Serializable {

    private Registration selected;
    private List<Registration> registrations;

    public RegistrationsBean() {
        this.selected = null;
    }

    public List<Registration> getRegistrations() {
        List<Registration> result = new ArrayList<Registration>();
        for (Registration registration : registrations) {
            result.add(registration);
        }
        return result;
    }

    public void setRegistrations(List<Registration> registrations) {
        this.registrations = new ArrayList<Registration>();

        for (Registration registration : registrations) {
            this.registrations.add(registration);
        }
    }

    public Registration getSelected() {
        return selected;
    }

    public void setSelected(Registration selected) {
        this.selected = selected;
    }

}
