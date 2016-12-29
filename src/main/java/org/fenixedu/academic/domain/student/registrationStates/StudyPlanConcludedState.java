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
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.student.Registration;
import org.fenixedu.academic.domain.util.workflow.StateBean;
import org.joda.time.DateTime;

public class StudyPlanConcludedState extends StudyPlanConcludedState_Base {

    protected StudyPlanConcludedState(Registration registration, Person person, DateTime dateTime) {
        super();
        init(registration, person, dateTime);
        registration.getPerson().getUser().openLoginPeriod();
    }

    @Override
    public RegistrationStateType getStateType() {
        return RegistrationStateType.STUDYPLANCONCLUDED;
    }

    @Override
    public void checkConditionsToForward(final StateBean bean) {
        throw new DomainException("error.impossible.to.forward.from.studyPlanConcluded");
    }

}
