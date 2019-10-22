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
package org.fenixedu.academic.domain.candidacy;

import org.fenixedu.academic.domain.EntryPhase;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.Qualification;
import org.fenixedu.academic.domain.degreeStructure.CycleType;
import org.fenixedu.academic.domain.student.Registration;
import org.fenixedu.academic.domain.student.RegistrationProtocol;
import org.fenixedu.academic.domain.student.Student;
import org.fenixedu.academic.domain.util.workflow.IState;
import org.fenixedu.academic.domain.util.workflow.StateBean;
import org.fenixedu.academic.predicate.AccessControl;
import org.joda.time.YearMonthDay;

public class RegisteredCandidacySituation extends RegisteredCandidacySituation_Base {

    public RegisteredCandidacySituation(Candidacy candidacy) {
        this(candidacy, null);
    }

    public RegisteredCandidacySituation(Candidacy candidacy, Person person) {
        this(candidacy, person, null, null);
    }

    public RegisteredCandidacySituation(Candidacy candidacy, RegistrationProtocol registrationProtocol, CycleType cycleType,
            IngressionType ingressionType, EntryPhase entryPhase, Integer studentNumber) {
        super();
        init(candidacy, AccessControl.getPerson());
        registerCandidacy(registrationProtocol, cycleType, studentNumber);

        ((StudentCandidacy) candidacy).setIngressionType(ingressionType);
        ((StudentCandidacy) candidacy).setEntryPhase(entryPhase);
    }

    private RegisteredCandidacySituation(Candidacy candidacy, Person person, RegistrationProtocol registrationProtocol,
            CycleType cycleType) {
        super();
        init(candidacy, person == null ? AccessControl.getPerson() : person);
        registerCandidacy(registrationProtocol, cycleType, null);
    }

    private void registerCandidacy(RegistrationProtocol registrationProtocol, CycleType cycleType, Integer studentNumber) {
        Person person = getCandidacy().getPerson();
        Registration registration = null;

        if (person.getStudent() == null && studentNumber == null) {
            new Student(person);
        } else if (person.getStudent() == null && studentNumber != null) {
            Student.createStudentWithCustomNumber(person, studentNumber);
        }
    }

    @Override
    public CandidacySituationType getCandidacySituationType() {
        return CandidacySituationType.REGISTERED;
    }

    @Override
    public boolean canExecuteOperationAutomatically() {
        return false;
    }

    @Override
    public IState nextState() {
        return null;
    }

    @Override
    public IState nextState(final StateBean bean) {
        return null;
    }

}
