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
package net.sourceforge.fenixedu.domain.organizationalStructure;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.sourceforge.fenixedu.domain.CurricularYear;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.UnitSite;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOffice;
import net.sourceforge.fenixedu.domain.elections.DelegateElection;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.student.Student;

import org.apache.commons.lang.StringUtils;
import org.fenixedu.spaces.domain.Space;
import org.joda.time.YearMonthDay;

import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

public class DegreeUnit extends DegreeUnit_Base {

    private DegreeUnit() {
        super();
        super.setType(PartyTypeEnum.DEGREE_UNIT);
    }

    public static DegreeUnit createNewInternalDegreeUnit(MultiLanguageString unitName, String unitNameCard,
            Integer costCenterCode, String acronym, YearMonthDay beginDate, YearMonthDay endDate, Unit parentUnit,
            AccountabilityType accountabilityType, String webAddress, Degree degree, UnitClassification classification,
            Boolean canBeResponsibleOfSpaces, Space campus) {

        DegreeUnit degreeUnit = new DegreeUnit();
        degreeUnit.init(unitName, unitNameCard, costCenterCode, acronym, beginDate, endDate, webAddress, classification, null,
                canBeResponsibleOfSpaces, campus);
        degreeUnit.setDegree(degree);
        degreeUnit.addParentUnit(parentUnit, accountabilityType);

        checkIfAlreadyExistsOneDegreeWithSameAcronym(degreeUnit);

        return degreeUnit;
    }

    @Override
    public void edit(MultiLanguageString unitName, String unitNameCard, Integer unitCostCenter, String acronym,
            YearMonthDay beginDate, YearMonthDay endDate, String webAddress, UnitClassification classification,
            Department department, Degree degree, AdministrativeOffice administrativeOffice, Boolean canBeResponsibleOfSpaces,
            Space campus) {

        super.edit(unitName, unitNameCard, unitCostCenter, acronym, beginDate, endDate, webAddress, classification, department,
                degree, administrativeOffice, canBeResponsibleOfSpaces, campus);
        setDegree(degree);

        checkIfAlreadyExistsOneDegreeWithSameAcronym(this);
    }

    @Override
    public Accountability addParentUnit(Unit parentUnit, AccountabilityType accountabilityType) {
        if (parentUnit != null && !parentUnit.isInternal()) {
            throw new DomainException("error.unit.invalid.parentUnit");
        }
        return super.addParentUnit(parentUnit, accountabilityType);
    }

    @Override
    public void setAcronym(String acronym) {
        if (StringUtils.isEmpty(acronym)) {
            throw new DomainException("error.unit.empty.acronym");
        }
        super.setAcronym(acronym);
    }

    @Override
    public void setType(PartyTypeEnum partyTypeEnum) {
        throw new DomainException("unit.impossible.set.type");
    }

    @Override
    public boolean isDegreeUnit() {
        return true;
    }

    @Override
    public void setDegree(Degree degree) {
        if (degree == null) {
            throw new DomainException("error.DegreeUnit.empty.degree");
        }
        super.setDegree(degree);
    }

    @Override
    public void delete() {
        super.setDegree(null);
        super.delete();
    }

    private static void checkIfAlreadyExistsOneDegreeWithSameAcronym(DegreeUnit degreeUnit) {
        for (Unit unit : UnitUtils.readInstitutionUnit().getAllSubUnits()) {
            if (!unit.equals(degreeUnit) && unit.isDegreeUnit() && degreeUnit.getAcronym().equalsIgnoreCase(unit.getAcronym())) {
                throw new DomainException("error.unit.already.exists.unit.with.same.name.or.acronym");
            }
        }
    }

    /*
     * METHODS FOR DELEGATE FUNCTIONS
     */

    /* For each delegate function type, there can be only one active function */
    public Function getActiveDelegateFunctionByType(FunctionType functionType) {
        YearMonthDay today = new YearMonthDay();
        for (Function function : getFunctions()) {
            if (functionType.equals(function.getFunctionType()) && function.isActive(today)) {
                return function;
            }
        }
        return null;
    }

    /*
     * ACTIVE DELEGATE PERSON FUNCTIONS
     */
    public List<PersonFunction> getAllActiveDelegatePersonFunctions() {
        List<PersonFunction> result = new ArrayList<PersonFunction>();
        for (FunctionType functionType : FunctionType.getAllDelegateFunctionTypes()) {
            result.addAll(getAllActiveDelegatePersonFunctionsByFunctionType(functionType, null));
        }
        return result;
    }

    public List<PersonFunction> getAllActiveDelegatePersonFunctionsByFunctionType(FunctionType functionType,
            ExecutionYear executionYear) {
        executionYear = executionYear != null ? executionYear : ExecutionYear.readCurrentExecutionYear();
        List<PersonFunction> result = new ArrayList<PersonFunction>();
        final Function function = getActiveDelegateFunctionByType(functionType);
        if (function != null) {
            return function.getActivePersonFunctionsStartingIn(executionYear);
        }
        return result;
    }

    public PersonFunction getActiveYearDelegatePersonFunctionByCurricularYear(CurricularYear curricularYear) {
        final List<PersonFunction> delegateFunctions =
                getAllActiveDelegatePersonFunctionsByFunctionType(FunctionType.DELEGATE_OF_YEAR, null);
        for (PersonFunction delegateFunction : delegateFunctions) {
            if (delegateFunction.hasCurricularYear() && delegateFunction.getCurricularYear().equals(curricularYear)) {
                return delegateFunction;
            }
        }
        return null;
    }

    /*
     * DELEGATE PERSON FUNCTIONS FROM GIVEN EXECUTION YEAR (PAST DELEGATES)
     */
    public List<PersonFunction> getAllDelegatePersonFunctionsByExecutionYear(ExecutionYear executionYear) {
        List<PersonFunction> result = new ArrayList<PersonFunction>();
        for (FunctionType functionType : FunctionType.getAllDelegateFunctionTypes()) {
            result.addAll(getAllDelegatePersonFunctionsByExecutionYearAndFunctionType(executionYear, functionType));
        }
        return result;
    }

    public List<PersonFunction> getAllDelegatePersonFunctionsByExecutionYearAndFunctionType(ExecutionYear executionYear,
            FunctionType functionType) {
        List<PersonFunction> result = new ArrayList<PersonFunction>();
        final Function function = getDelegateFunctionByTypeAndExecutionYear(executionYear, functionType);
        if (function != null) {
            return function.getPersonFunctions();
        }
        return result;
    }

    public PersonFunction getYearDelegatePersonFunctionByExecutionYearAndCurricularYear(ExecutionYear executionYear,
            CurricularYear curricularYear) {
        final List<PersonFunction> delegateFunctions =
                getAllDelegatePersonFunctionsByExecutionYearAndFunctionType(executionYear, FunctionType.DELEGATE_OF_YEAR);
        for (PersonFunction delegateFunction : delegateFunctions) {
            if (delegateFunction.getCurricularYear().equals(curricularYear)
                    && delegateFunction.belongsToPeriod(executionYear.getBeginDateYearMonthDay(),
                            executionYear.getEndDateYearMonthDay())) {
                return delegateFunction;
            }
        }
        return null;
    }

    public PersonFunction getLastYearDelegatePersonFunctionByExecutionYearAndCurricularYear(ExecutionYear executionYear,
            CurricularYear curricularYear) {
        final List<PersonFunction> delegateFunctions =
                getAllDelegatePersonFunctionsByExecutionYearAndFunctionType(executionYear, FunctionType.DELEGATE_OF_YEAR);

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

    /* Return delegate function, of the given type, in the given execution year */
    public Function getDelegateFunctionByTypeAndExecutionYear(ExecutionYear executionYear, FunctionType functionType) {
        for (Function function : getFunctions()) {
            if (function.getFunctionType() != null && function.getFunctionType().equals(functionType)
                    && function.belongsToPeriod(executionYear.getBeginDateYearMonthDay(), executionYear.getEndDateYearMonthDay())) {
                return function;
            }
        }
        return null;
    }

    public List<Function> getAllDelegateFunctionsByType(FunctionType functionType) {
        List<Function> result = new ArrayList<Function>();
        for (Function function : getFunctions()) {
            if (function.getFunctionType().equals(functionType)) {
                result.add(function);
            }
        }
        return result;
    }

    public List<PersonFunction> getAllDelegatePersonFunctionsByFunctionType(FunctionType functionType) {
        List<PersonFunction> result = new ArrayList<PersonFunction>();
        for (Function function : getAllDelegateFunctionsByType(functionType)) {
            result.addAll(function.getPersonFunctions());
        }
        return result;
    }

    // TODO: controlo de acesso?
    public PersonFunction addYearDelegatePersonFunction(Student student, CurricularYear curricularYear) {

        Registration lastActiveRegistration = student.getActiveRegistrationFor(getDegree());
        if (lastActiveRegistration == null || !lastActiveRegistration.getDegree().equals(getDegree())) {
            throw new DomainException("error.delegates.studentNotBelongsToDegree");
        }

        YearMonthDay currentDate = new YearMonthDay();

        // The following restriction tries to guarantee that a new delegate is
        // elected before this person function ends
        ExecutionYear currentExecutionYear = ExecutionYear.readCurrentExecutionYear();
        YearMonthDay endDate = currentExecutionYear.getEndDateYearMonthDay().plusYears(1);

        Function function = getActiveDelegateFunctionByType(FunctionType.DELEGATE_OF_YEAR);

        /* Check if there is another active person function with this type */
        if (function != null) {

            List<PersonFunction> delegateFunctions = function.getActivePersonFunctions();

            for (PersonFunction personFunction : delegateFunctions) {
                if (personFunction.getCurricularYear().equals(curricularYear)
                        || personFunction.getDelegate().getRegistration().getStudent().equals(student)) {
                    Student oldStudent = personFunction.getPerson().getStudent();

                    if (personFunction.getBeginDate().equals(currentDate)) {
                        personFunction.getDelegate().delete();
                    } else {
                        personFunction.setOccupationInterval(personFunction.getBeginDate(), currentDate.minusDays(1));
                    }
                    final DelegateElection election =
                            getDegree().getYearDelegateElectionWithLastCandidacyPeriod(ExecutionYear.readCurrentExecutionYear(),
                                    curricularYear);

                    if (election != null && election.getElectedStudent() == oldStudent) {
                        election.setElectedStudent(null);
                    }
                }
            }

        }

        return PersonFunction.createYearDelegatePersonFunction(this, student.getPerson(), currentDate, endDate, function,
                curricularYear);
    }

    // TODO: controlo de acesso?
    public void addDelegatePersonFunction(Student student, FunctionType functionType) {

        Registration lastActiveRegistration = student.getLastActiveRegistration();
        if (lastActiveRegistration == null || !lastActiveRegistration.getDegree().equals(getDegree())) {
            throw new DomainException("error.delegates.studentNotBelongsToDegree");
        }

        YearMonthDay currentDate = new YearMonthDay();

        // The following restriction tries to guarantee that a new delegate is
        // elected before this person function ends
        ExecutionYear currentExecutionYear = ExecutionYear.readCurrentExecutionYear();
        YearMonthDay endDate = currentExecutionYear.getEndDateYearMonthDay().plusYears(1);

        Function function = getActiveDelegateFunctionByType(functionType);

        /* Check if there is another active person function with this type */
        if (function != null) {
            List<PersonFunction> delegateFunctions = function.getActivePersonFunctions();
            for (PersonFunction personFunction : delegateFunctions) {
                if (personFunction.getBeginDate().equals(currentDate)) {
                    if (personFunction.getFunction().getFunctionType().equals(FunctionType.DELEGATE_OF_YEAR)) {
                        personFunction.getDelegate().delete();
                    } else {
                        personFunction.delete();
                    }
                } else {
                    personFunction.setOccupationInterval(personFunction.getBeginDate(), currentDate.minusDays(1));
                }
            }
        }

        PersonFunction.createDelegatePersonFunction(this, student.getPerson(), currentDate, endDate, function);
    }

    // TODO: controlo de acesso?
    public void removeAllActiveDelegatePersonFunctionsFromStudent(Student student) {
        for (FunctionType functionType : FunctionType.getAllDelegateFunctionTypes()) {
            removeActiveDelegatePersonFunctionFromStudentByFunctionType(student, functionType);
        }
    }

    // TODO: controlo de acesso?
    public void removeActiveDelegatePersonFunctionFromStudentByFunctionType(Student student, FunctionType functionType) {
        YearMonthDay today = new YearMonthDay();
        List<PersonFunction> delegatesFunctions = getAllActiveDelegatePersonFunctionsByFunctionType(functionType, null);
        if (!delegatesFunctions.isEmpty()) {
            for (PersonFunction function : delegatesFunctions) {
                Student delegateStudent = function.getPerson().getStudent();
                if (delegateStudent.equals(student)) {
                    function.setOccupationInterval(function.getBeginDate(), today.minusDays(1));
                }
            }
        }
    }

    @Override
    public UnitSite getSite() {
        final Degree degree = getDegree();
        return degree == null ? null : getDegree().getSite();
    }

    public SchoolUnit getSchoolUnit() {
        Unit current = this;
        while (current != null) {
            if (current.getType().equals(PartyTypeEnum.SCHOOL)) {
                return (SchoolUnit) current;
            }
            Collection<Unit> parentUnits = current.getParentUnits(AccountabilityTypeEnum.ACADEMIC_STRUCTURE);
            current = parentUnits.size() > 0 ? parentUnits.iterator().next() : null;
        }
        return null;
    }

    @Override
    @Deprecated
    public boolean hasDegree() {
        return getDegree() != null;
    }

}
