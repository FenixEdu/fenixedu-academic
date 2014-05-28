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
package net.sourceforge.fenixedu.domain.util.email;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accessControl.DelegatesGroup;
import net.sourceforge.fenixedu.domain.organizationalStructure.Function;
import net.sourceforge.fenixedu.domain.organizationalStructure.FunctionType;
import net.sourceforge.fenixedu.domain.organizationalStructure.PersonFunction;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.student.Student;

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
        getRecipients().addAll(Recipient.newInstance(getPossibleReceivers(person)));
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
        if (person.hasStudent()) {
            final Student delegate = person.getStudent();
            logger.info("Delegate: " + person.getName());
            final Registration lastRegistration = delegate.getLastActiveRegistration();
            // it should not be a delegate
            if (lastRegistration == null) {
                return groups;
            }
            final Degree degree = lastRegistration.getDegree();
            delegateFunction = degree.getMostSignificantDelegateFunctionForStudent(delegate, null);

            /* All other delegates from delegate degree */
            groups.add(DelegatesGroup.get(degree));

            /* All delegates with same delegate function from other degrees */
            if (delegateFunction != null
                    && !delegateFunction.getFunction().getFunctionType().equals(FunctionType.DELEGATE_OF_YEAR)) {
                for (PersonFunction function : delegate.getAllActiveDelegateFunctions()) {
                    groups.add(DelegatesGroup.get(function.getFunction().getFunctionType()));
                }
            }

            /* A student can have a GGAE delegate role too */
            if (person.getActiveGGAEDelegatePersonFunction() != null) {
                groups.add(DelegatesGroup.get(person.getActiveGGAEDelegatePersonFunction().getFunction()
                .getFunctionType()));
            }
        } else {
            delegateFunction = person.getActiveGGAEDelegatePersonFunction();
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

    @Deprecated
    public boolean hasPersonFunction() {
        return getPersonFunction() != null;
    }

}
