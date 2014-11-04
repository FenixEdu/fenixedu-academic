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
package org.fenixedu.academic.domain.student;

import java.util.List;

import org.fenixedu.academic.domain.CurricularYear;
import org.fenixedu.academic.domain.Degree;
import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.organizationalStructure.DegreeUnit;
import org.fenixedu.academic.domain.organizationalStructure.FunctionType;
import org.fenixedu.academic.domain.organizationalStructure.PersonFunction;

public class YearDelegate extends YearDelegate_Base {

    public YearDelegate() {
        super();
    }

    public YearDelegate(Registration registration, PersonFunction delegateFunction) {
        this();
        setRegistration(registration);
        setDelegateFunction(delegateFunction);
    }

    public CurricularYear getCurricularYear() {
        return getDelegateFunction().getCurricularYear();
    }

    public Person getPerson() {
        return getRegistration().getPerson();
    }

    public boolean isAfter(YearDelegate yearDelegate) {
        return getDelegateFunction().getEndDate().isAfter(yearDelegate.getDelegateFunction().getEndDate());
    }

    public static PersonFunction getActiveYearDelegatePersonFunctionByCurricularYear(DegreeUnit degreeUnit,
            CurricularYear curricularYear) {
        final List<PersonFunction> delegateFunctions =
                Delegate.getAllActiveDelegatePersonFunctionsByFunctionType(degreeUnit, FunctionType.DELEGATE_OF_YEAR, null);
        for (PersonFunction delegateFunction : delegateFunctions) {
            if (delegateFunction.getCurricularYear() != null && delegateFunction.getCurricularYear().equals(curricularYear)) {
                return delegateFunction;
            }
        }
        return null;
    }

    public static PersonFunction getLastYearDelegatePersonFunctionByExecutionYearAndCurricularYear(DegreeUnit degreeUnit,
            ExecutionYear executionYear, CurricularYear curricularYear) {
        final List<PersonFunction> delegateFunctions =
                Delegate.getAllDelegatePersonFunctionsByExecutionYearAndFunctionType(degreeUnit, executionYear,
                        FunctionType.DELEGATE_OF_YEAR);

        PersonFunction lastDelegateFunction = null;
        for (PersonFunction delegateFunction : delegateFunctions) {
            if (delegateFunction.getCurricularYear().equals(curricularYear)
                    && delegateFunction.belongsToPeriod(executionYear.getBeginDateYearMonthDay(),
                            executionYear.getEndDateYearMonthDay())) {
                if (lastDelegateFunction == null || lastDelegateFunction.getEndDate().isBefore(delegateFunction.getEndDate())) {
                    lastDelegateFunction = delegateFunction;
                }
            }
        }
        return lastDelegateFunction;
    }

    public static Student getActiveYearDelegateByCurricularYear(Degree degree, CurricularYear curricularYear) {
        if (degree.isEmpty() || degree.getUnit() == null) {
            return null;
        }
        final PersonFunction delegateFunction =
                getActiveYearDelegatePersonFunctionByCurricularYear(degree.getUnit(), curricularYear);
        return delegateFunction != null ? delegateFunction.getPerson().getStudent() : null;
    }

    /*
     * DELEGATES FROM GIVEN EXECUTION YEAR (PAST DELEGATES)
     */
    public static Student getYearDelegateByExecutionYearAndCurricularYear(Degree degree, ExecutionYear executionYear,
            CurricularYear curricularYear) {
        if (degree.isEmpty() || degree.getUnit() == null) {
            return null;
        }
        final List<PersonFunction> delegateFunctions =
                Delegate.getAllDelegatePersonFunctionsByExecutionYearAndFunctionType(degree.getUnit(), executionYear,
                        FunctionType.DELEGATE_OF_YEAR);
        for (PersonFunction delegateFunction : delegateFunctions) {
            if (delegateFunction.getCurricularYear().equals(curricularYear)
                    && delegateFunction.belongsToPeriod(executionYear.getBeginDateYearMonthDay(),
                            executionYear.getEndDateYearMonthDay())) {
                return delegateFunction.getPerson().getStudent();
            }
        }
        return null;
    }

}
