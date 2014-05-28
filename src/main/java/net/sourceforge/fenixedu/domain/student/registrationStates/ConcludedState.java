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
package net.sourceforge.fenixedu.domain.student.registrationStates;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accessControl.AcademicAuthorizationGroup;
import net.sourceforge.fenixedu.domain.accessControl.academicAdministration.AcademicOperationType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.DocumentRequestType;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.util.workflow.IState;
import net.sourceforge.fenixedu.domain.util.workflow.StateBean;
import net.sourceforge.fenixedu.injectionCode.AccessControl;

import org.joda.time.DateTime;

/**
 * 
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class ConcludedState extends ConcludedState_Base {

    protected ConcludedState(Registration registration, Person person, DateTime dateTime) {
        super();

        if (registration.isBolonha() && !registration.hasConcluded()) {
            throw new DomainException("error.registration.is.not.concluded");
        }

        init(registration, person, dateTime);
        registration.getPerson().addPersonRoleByRoleType(RoleType.ALUMNI);
        // if (registration.getStudent().getRegistrationsCount() == 1) {
        // registration.getPerson().removeRoleByType(RoleType.STUDENT);
        // }
    }

    @Override
    public void delete() {
        checkRulesToDelete();
        super.delete();
    }

    private void checkRulesToDelete() {
        final Person person = AccessControl.getPerson();
        if (AcademicAuthorizationGroup.getProgramsForOperation(person, AcademicOperationType.REPEAT_CONCLUSION_PROCESS).contains(
                this.getRegistration().getDegree())) {
            return;
        }

        if (!getRegistration().getSucessfullyFinishedDocumentRequests(DocumentRequestType.DEGREE_FINALIZATION_CERTIFICATE)
                .isEmpty()) {
            throw new DomainException("cannot.delete.concluded.state.of.registration.with.concluded.degree.finalization.request");
        }

        if (!getRegistration().getSucessfullyFinishedDocumentRequests(DocumentRequestType.DIPLOMA_REQUEST).isEmpty()) {
            throw new DomainException("cannot.delete.concluded.state.of.registration.with.concluded.diploma.request");
        }
    }

    @Override
    public void checkConditionsToForward(final StateBean bean) {
        throw new DomainException("error.impossible.to.forward.from.concluded");
    }

    @Override
    public IState nextState(final StateBean bean) {
        throw new DomainException("error.impossible.to.forward.from.concluded");
    }

    @Override
    public RegistrationStateType getStateType() {
        return RegistrationStateType.CONCLUDED;
    }

}
