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
package org.fenixedu.academic.domain.student.registrationStates;

import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.accessControl.academicAdministration.AcademicAccessRule;
import org.fenixedu.academic.domain.accessControl.academicAdministration.AcademicOperationType;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.serviceRequests.documentRequests.DocumentRequestType;
import org.fenixedu.academic.domain.student.Registration;
import org.fenixedu.academic.domain.util.workflow.IState;
import org.fenixedu.academic.domain.util.workflow.StateBean;
import org.fenixedu.academic.predicate.AccessControl;
import org.fenixedu.bennu.core.domain.UserLoginPeriod;
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
        UserLoginPeriod.createOpenPeriod(person.getUser());
    }

    @Override
    public void delete() {
        checkRulesToDelete();
        super.delete();
    }

    private void checkRulesToDelete() {
        final Person person = AccessControl.getPerson();
        if (AcademicAccessRule.isProgramAccessibleToFunction(AcademicOperationType.REPEAT_CONCLUSION_PROCESS, getRegistration()
                .getDegree(), person.getUser())) {
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
