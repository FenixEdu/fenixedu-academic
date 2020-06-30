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
import org.joda.time.DateTime;

import pt.ist.fenixframework.Atomic;

public class Coordinator extends Coordinator_Base {

    private Coordinator() {
        super();
        setRootDomainObject(Bennu.getInstance());

    }

    private Coordinator(final ExecutionDegree executionDegree, final Person person, final Boolean responsible) {
        this();

        for (final Coordinator coordinator : executionDegree.getCoordinatorsListSet()) {
            if (coordinator.getPerson() == person) {
                throw new Error("error.person.already.is.coordinator.for.same.execution.degree");
            }
        }

        setExecutionDegree(executionDegree);
        setPerson(person);
        setResponsible(responsible);
        // CoordinatorLog.createCoordinatorLog(new DateTime(),
        // OperationType.ADD, this);
    }

    public void delete() throws DomainException {
        setExecutionDegree(null);
        setPerson(null);

        setRootDomainObject(null);
        super.deleteDomainObject();
    }

    public boolean isResponsible() {
        return getResponsible().booleanValue();
    }

    public void setResponsible(boolean responsible) {
        super.setResponsible(responsible);
    }

    public Teacher getTeacher() {
        return getPerson().getTeacher();
    }

    @Atomic
    public static Coordinator createCoordinator(ExecutionDegree executionDegree, Person person, Boolean responsible) {

        CoordinationTeamLog.createLog(executionDegree.getDegree(), executionDegree.getExecutionYear(), Bundle.MESSAGING,
                "log.degree.coordinationteam.addmember", person.getPresentationName(), executionDegree.getPresentationName());

        return new Coordinator(executionDegree, person, responsible);
    }

    @Atomic
    public void removeCoordinator() {
        this.delete();
    }

    /**
     * Method to create coordinator logs for adding responsibility
     * 
     * @param personAddingResponsible
     */
    public void setAsResponsible(Person personAddingResponsible) {

    }

    @Atomic
    public void setAsResponsible() {
        this.setResponsible(Boolean.valueOf(true));
    }

    @Atomic
    public void setAsNotResponsible() {
        this.setResponsible(Boolean.valueOf(false));
    }

    /**
     * Method to apply a certain operation on coordinator
     * 
     * @param operationType
     * @param personMakingAction
     */
    public void makeAction(OperationType operationType, Person personMakingAction) {
        if (operationType.compareTo(OperationType.CHANGERESPONSIBLE_FALSE) == 0) {
            CoordinatorLog.createCoordinatorLog(new DateTime(), OperationType.CHANGERESPONSIBLE_FALSE, personMakingAction, this);
            setAsNotResponsible();
        } else if (operationType.compareTo(OperationType.CHANGERESPONSIBLE_TRUE) == 0) {
            CoordinatorLog.createCoordinatorLog(new DateTime(), OperationType.CHANGERESPONSIBLE_TRUE, personMakingAction, this);
            this.setAsResponsible();
        } else if (operationType.compareTo(OperationType.REMOVE) == 0) {
            CoordinatorLog.createCoordinatorLog(new DateTime(), OperationType.REMOVE, personMakingAction, this);
            this.removeCoordinator();
        }
    }

    public static Coordinator makeCreation(Person personMakingAction, ExecutionDegree executionDegree, Person person,
            Boolean responsible) {
        Coordinator coordinator = createCoordinator(executionDegree, person, responsible);
        CoordinatorLog.createCoordinatorLog(new DateTime(), OperationType.ADD, personMakingAction, coordinator);
        return coordinator;
    }

}
