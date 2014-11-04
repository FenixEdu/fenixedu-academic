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
package org.fenixedu.academic.domain;

import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.util.Bundle;
import org.fenixedu.bennu.core.domain.Bennu;

public class ScientificCommission extends ScientificCommission_Base {

    public ScientificCommission(ExecutionDegree executionDegree, Person person) {
        super();

        if (executionDegree.isPersonInScientificCommission(person)) {
            throw new DomainException("scientificCommission.person.duplicate");
        }

        setRootDomainObject(Bennu.getInstance());

        setContact(false);
        setExecutionDegree(executionDegree);
        setPerson(person);

        ScientificCommissionLog.createLog(this.getExecutionDegree().getDegree(), this.getExecutionDegree().getExecutionYear(),
                Bundle.MESSAGING, "log.degree.scientificcomission.addmember", this.getPerson().getPresentationName(), this
                        .getExecutionDegree().getDegree().getPresentationName());
    }

    public Coordinator getCoordinator() {
        for (Coordinator coordinator : getExecutionDegree().getCoordinatorsListSet()) {
            if (this.getPerson().equals(coordinator.getPerson())) {
                return coordinator;
            }
        }
        return null;
    }

    public Boolean getHasCoordinator() {
        return getCoordinator() != null;
    }

    public Boolean isContact() {
        return getContact() == null ? false : getContact();
    }

    public void delete() {
        ScientificCommissionLog.createLog(getExecutionDegree().getDegree(), getExecutionDegree().getExecutionYear(),
                Bundle.MESSAGING, "log.degree.scientificcomission.removemember", this.getPerson().getName(), this.getPerson()
                        .getUsername(), this.getExecutionDegree().getDegree().getPresentationName());
        setPerson(null);
        setExecutionDegree(null);
        setRootDomainObject(null);
        deleteDomainObject();
    }

    public void changeContactStatus(Boolean contact) {
        if (!contact.equals(getContact())) {
            setContact(contact);
            logEditMember();
        }
    }

    public void logEditMember() {
        ScientificCommissionLog.createLog(this.getExecutionDegree().getDegree(), this.getExecutionDegree().getExecutionYear(),
                Bundle.MESSAGING, "log.degree.scientificcomission.editmember", this.getPerson().getPresentationName(), this
                        .getExecutionDegree().getDegree().getPresentationName());
    }

}
