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
package net.sourceforge.fenixedu.domain;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.util.Bundle;

import org.fenixedu.bennu.core.domain.Bennu;

import pt.ist.fenixframework.dml.runtime.RelationAdapter;

public class ScientificCommission extends ScientificCommission_Base {

    static {
        getRelationScientificCommissionPerson().addListener(new ManageCoordinatorRole());
    }

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
                Bundle.MESSAGING, "log.degree.scientificcomission.addmember", this.getPerson()
                        .getPresentationName(), this.getExecutionDegree().getDegree().getPresentationName());
    }

    public Coordinator getCoordinator() {
        for (Coordinator coordinator : getExecutionDegree().getCoordinatorsList()) {
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
                Bundle.MESSAGING, "log.degree.scientificcomission.removemember", this.getPerson().getName(), this
                        .getPerson().getIstUsername(), this.getExecutionDegree().getDegree().getPresentationName());
        setPerson(null);
        setExecutionDegree(null);
        setRootDomainObject(null);
        deleteDomainObject();
    }

    /**
     * Manage the role COORDINATOR associated with the person. The person
     * becomes a COORDINATOR when it's added to a scientific commission. This
     * listerner also removes the role from the person when it's removed from
     * every scientific commissions and it's not in a coordination team.
     * 
     * @author cfgi
     */
    private static class ManageCoordinatorRole extends RelationAdapter<ScientificCommission, Person> {

        @Override
        public void afterAdd(ScientificCommission commission, Person person) {
            super.afterAdd(commission, person);

            if (person != null && commission != null) {
                person.addPersonRoleByRoleType(RoleType.COORDINATOR);
            }
        }

        @Override
        public void afterRemove(ScientificCommission commission, Person person) {
            super.afterRemove(commission, person);

            if (person != null && commission != null) {
                if (person.hasAnyCoordinators()) {
                    return;
                }

                if (person.hasAnyScientificCommissions()) {
                    return;
                }

                person.removeRoleByType(RoleType.COORDINATOR);
            }
        }

    }

    public void changeContactStatus(Boolean contact) {
        if (!contact.equals(getContact())) {
            setContact(contact);
            logEditMember();
        }
    }

    public void logEditMember() {
        ScientificCommissionLog.createLog(this.getExecutionDegree().getDegree(), this.getExecutionDegree().getExecutionYear(),
                Bundle.MESSAGING, "log.degree.scientificcomission.editmember", this.getPerson()
                        .getPresentationName(), this.getExecutionDegree().getDegree().getPresentationName());
    }

    @Deprecated
    public boolean hasBennu() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasExecutionDegree() {
        return getExecutionDegree() != null;
    }

    @Deprecated
    public boolean hasContact() {
        return getContact() != null;
    }

    @Deprecated
    public boolean hasPerson() {
        return getPerson() != null;
    }

}
