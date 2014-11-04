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
package org.fenixedu.academic.service.services.person.function;

import static org.fenixedu.academic.predicate.AccessControl.check;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.fenixedu.academic.service.services.exceptions.FenixServiceException;
import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.organizationalStructure.AccountabilityTypeEnum;
import org.fenixedu.academic.domain.organizationalStructure.PersonFunction;
import org.fenixedu.academic.predicate.RolePredicates;

import org.joda.time.YearMonthDay;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

/**
 * 
 * @author naat
 * 
 */
public class ReadPersonFunctionsByPersonIDAndExecutionYearID {

    @Atomic
    public static List<PersonFunction> run(String personID, String executionYearID) throws FenixServiceException {
        check(RolePredicates.PERSON_PREDICATE);
        Person person = (Person) FenixFramework.getDomainObject(personID);

        List<PersonFunction> personFunctions = null;

        if (executionYearID != null) {
            ExecutionYear executionYear = FenixFramework.getDomainObject(executionYearID);
            Date beginDate = executionYear.getBeginDate();
            Date endDate = executionYear.getEndDate();
            personFunctions =
                    PersonFunction.getPersonFuntions(person, YearMonthDay.fromDateFields(beginDate),
                            YearMonthDay.fromDateFields(endDate));

        } else {
            personFunctions =
                    new ArrayList<PersonFunction>((Collection<PersonFunction>) person.getParentAccountabilities(
                            AccountabilityTypeEnum.MANAGEMENT_FUNCTION, PersonFunction.class));
        }

        return personFunctions;
    }
}