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
package org.fenixedu.academic.domain.util.email;

import java.util.ArrayList;
import java.util.List;

import org.fenixedu.academic.domain.Degree;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.accessControl.DelegatesGroup;
import org.fenixedu.academic.domain.organizationalStructure.Function;
import org.fenixedu.academic.domain.organizationalStructure.FunctionType;
import org.fenixedu.academic.domain.organizationalStructure.PersonFunction;
import org.fenixedu.academic.domain.organizationalStructure.Unit;
import org.fenixedu.academic.domain.student.Delegate;
import org.fenixedu.academic.domain.student.Registration;
import org.fenixedu.academic.domain.student.Student;

import org.fenixedu.bennu.core.groups.Group;
import org.fenixedu.bennu.core.groups.UserGroup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.ist.fenixframework.Atomic;

public class PersonFunctionSender extends PersonFunctionSender_Base {

    private static final Logger logger = LoggerFactory.getLogger(PersonFunctionSender.class);

    public PersonFunctionSender(PersonFunction personFunction) {
        super();

        Person person = personFunction.getPerson();
        setPersonFunction(personFunction);
        setFromAddress(Sender.getNoreplyMail());
        addReplyTos(new CurrentUserReplyTo());
        setMembers(UserGroup.of(person.getUser()));
        getRecipientsSet().addAll(Recipient.newInstance(getPossibleReceivers(person)));
        setFromName(createFromName());
    }

    public String createFromName() {

        if (getPersonFunction() == null || getPersonFunction().getFunction() == null) {
            return getFromName();
        }

        String institutionAcronym = Unit.getInstitutionAcronym();
        Function function = getPersonFunction().getFunction();

        if (function.isOfAnyFunctionType(FunctionType.getAllDegreeDelegateFunctionTypes())) {
            String courseAcronym = function.getUnit().getDegree().getSigla();
            String functionTypeName;
            if (function.isOfFunctionType(FunctionType.DELEGATE_OF_YEAR)) {
                String year = getPersonFunction().getCurricularYear().getYear().toString();
                functionTypeName = String.format("Delegado do %sº ano", year);
            } else {
                functionTypeName = function.getTypeName().toString();
            }
            return String.format("%s (%s: %s)", institutionAcronym, courseAcronym, functionTypeName);
        } else {
            String functionTypeName = function.getTypeName().toString();
            return String.format("%s (%s)", institutionAcronym, functionTypeName);
        }
    }

    public static List<Group> getPossibleReceivers(Person person) {
        List<Group> groups = new ArrayList<Group>();
        PersonFunction delegateFunction = null;
        if (person.getStudent() != null) {
            final Student delegate = person.getStudent();
            logger.info("Delegate: " + person.getName());
            final Registration lastRegistration = delegate.getLastActiveRegistration();
            // it should not be a delegate
            if (lastRegistration == null) {
                return groups;
            }
            final Degree degree = lastRegistration.getDegree();
            delegateFunction = Delegate.getMostSignificantDelegateFunctionForStudent(degree, delegate, null);

            /* All other delegates from delegate degree */
            groups.add(DelegatesGroup.get(degree));

            /* All delegates with same delegate function from other degrees */
            if (delegateFunction != null
                    && !delegateFunction.getFunction().getFunctionType().equals(FunctionType.DELEGATE_OF_YEAR)) {
                for (PersonFunction function : Delegate.getAllActiveDelegateFunctions(delegate)) {
                    groups.add(DelegatesGroup.get(function.getFunction().getFunctionType()));
                }
            }

            /* A student can have a GGAE delegate role too */
            if (PersonFunction.getActiveGGAEDelegatePersonFunction(person) != null) {
                groups.add(DelegatesGroup.get(PersonFunction.getActiveGGAEDelegatePersonFunction(person).getFunction().getFunctionType()));
            }
        } else {
            delegateFunction = PersonFunction.getActiveGGAEDelegatePersonFunction(person);
            groups.add(DelegatesGroup.get(delegateFunction.getFunction().getFunctionType()));
        }

        return groups;
    }

    @Override
    public void delete() {
        setPersonFunction(null);
        super.delete();
    }

    @Atomic
    public static PersonFunctionSender newInstance(PersonFunction personFunction) {
        PersonFunctionSender sender = personFunction.getSender();
        return sender == null ? new PersonFunctionSender(personFunction) : sender;
    }

}
