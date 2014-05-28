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
package net.sourceforge.fenixedu.presentationTier.Action.directiveCouncil.manageExternalSupervision;

import java.io.Serializable;
import java.util.Set;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.student.RegistrationAgreement;
import net.sourceforge.fenixedu.domain.student.RegistrationProtocol;

public class ManageExternalSupervisionBean implements Serializable {

    private RegistrationProtocol registrationProtocol;
    private RegistrationAgreement registrationAgreement;
    private Person newSupervisor;

    public ManageExternalSupervisionBean() {
        super();
        newSupervisor = null;
    }

    public ManageExternalSupervisionBean(RegistrationProtocol registrationProtocol) {
        this();
        this.registrationProtocol = registrationProtocol;
        this.registrationAgreement = registrationProtocol.getRegistrationAgreement();
    }

    public RegistrationProtocol getRegistrationProtocol() {
        return registrationProtocol;
    }

    public void setRegistrationProtocol(RegistrationProtocol registrationProtocol) {
        this.registrationProtocol = registrationProtocol;
    }

    public RegistrationAgreement getRegistrationAgreement() {
        return registrationAgreement;
    }

    public void setRegistrationAgreement(RegistrationAgreement registrationAgreement) {
        this.registrationAgreement = registrationAgreement;
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
