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
package org.fenixedu.academic.ui.struts.action.directiveCouncil.manageExternalSupervision;

import java.io.Serializable;
import java.util.Set;

import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.student.RegistrationProtocol;

public class ManageExternalSupervisionBean implements Serializable {

    private RegistrationProtocol registrationProtocol;
    private Person newSupervisor;

    public ManageExternalSupervisionBean() {
        super();
        newSupervisor = null;
    }

    public ManageExternalSupervisionBean(RegistrationProtocol registrationProtocol) {
        this();
        this.registrationProtocol = registrationProtocol;
    }

    public RegistrationProtocol getRegistrationProtocol() {
        return registrationProtocol;
    }

    public void setRegistrationProtocol(RegistrationProtocol registrationProtocol) {
        this.registrationProtocol = registrationProtocol;
    }

    public Person getNewSupervisor() {
        return newSupervisor;
    }

    public void setNewSupervisor(Person newSupervisor) {
        this.newSupervisor = newSupervisor;
    }

    public Set<Person> getSupervisors() {
        return registrationProtocol.getSupervisorsSet();
    }

    public void addSupervisor() {
        registrationProtocol.addSupervisor(newSupervisor);
    }

    public void removeSupervisor(Person supervisor) {
        registrationProtocol.removeSupervisor(supervisor);
    }

}
