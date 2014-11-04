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
/*
 * Created on Oct 18, 2005
 *	by mrsp
 */
package org.fenixedu.academic.ui.faces.bean.departmentAdmOffice;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.faces.component.html.HtmlInputHidden;
import javax.faces.model.SelectItem;

import org.fenixedu.academic.service.services.departmentAdmOffice.AssociateNewFunctionToPerson;
import org.fenixedu.academic.service.services.departmentAdmOffice.DeletePersonFunction;
import org.fenixedu.academic.service.services.departmentAdmOffice.EditPersonFunction;
import org.fenixedu.academic.service.services.exceptions.FenixServiceException;
import org.fenixedu.academic.service.services.person.SearchPerson;
import org.fenixedu.academic.service.services.person.SearchPerson.SearchParameters;
import org.fenixedu.academic.service.services.person.SearchPerson.SearchPersonPredicate;
import org.fenixedu.academic.domain.Department;
import org.fenixedu.academic.domain.Employee;
import org.fenixedu.academic.domain.ExecutionSemester;
import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.organizationalStructure.AccountabilityTypeEnum;
import org.fenixedu.academic.domain.organizationalStructure.Function;
import org.fenixedu.academic.domain.organizationalStructure.PersonFunction;
import org.fenixedu.academic.domain.organizationalStructure.Unit;
import org.fenixedu.academic.domain.person.RoleType;
import org.fenixedu.academic.domain.student.Registration;
import org.fenixedu.academic.ui.struts.action.resourceAllocationManager.utils.PresentationConstants;
import org.fenixedu.academic.ui.faces.bean.base.FenixBackingBean;
import org.fenixedu.academic.util.Bundle;
import org.fenixedu.academic.util.PeriodState;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ReverseComparator;
import org.apache.commons.lang.StringUtils;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.joda.time.YearMonthDay;

import pt.ist.fenixframework.FenixFramework;
import pt.utl.ist.fenix.tools.util.CollectionPager;
import pt.utl.ist.fenix.tools.util.DateFormatUtil;

public class FunctionsManagementBackingBean extends FenixBackingBean {

    public Collection<Person> personsList, allPersonsList;

    public List<Function> inherentFunctions;

    public List<PersonFunction> activeFunctions, inactiveFunctions;

    public Integer pageIndex, numberOfFunctions;

    public String personFunctionID, personID, unitID, functionID, executionPeriod;

    public Integer duration, disabledVar, personNumber;

    public Unit unit;

    public Double credits;

    public String beginDate, endDate, personName, personType;

    public HtmlInputHidden creditsHidden, beginDateHidden, endDateHidden, personIDHidden, unitIDHidden, disabledVarHidden;

    public HtmlInputHidden functionIDHidden, personFunctionIDHidden, executionPeriodHidden, durationHidden;

    public Function function;

    public PersonFunction personFunction;

    public FunctionsManagementBackingBean() {
        getParametersFromLinks();
    }

    private void getParametersFromLinks() {

        if (!StringUtils.isEmpty(getRequestParameter("personID"))) {
            this.personID = getRequestParameter("personID");
        }
        if (!StringUtils.isEmpty(getRequestParameter("pageIndex"))) {
            this.pageIndex = Integer.valueOf(getRequestParameter("pageIndex").toString());
        }
        if (!StringUtils.isEmpty(getRequestParameter("name"))) {
            this.personName = getRequestParameter("name").toString();
        }
        if (!StringUtils.isEmpty(getRequestParameter("unitID"))) {
            this.unitID = getRequestParameter("unitID").toString();
        }
        if (!StringUtils.isEmpty(getRequestParameter("personFunctionID"))) {
            this.personFunctionID = getRequestParameter("personFunctionID").toString();
        }
        if (!StringUtils.isEmpty(getRequestParameter("functionID"))) {
            this.functionID = getRequestParameter("functionID").toString();
        }
        if (!StringUtils.isEmpty(getRequestParameter("disabledVar"))) {
            this.disabledVar = Integer.valueOf(getRequestParameter("disabledVar").toString());
        }
    }

    public String associateNewFunction() throws FenixServiceException, ParseException {

        if (this.getUnit() == null
                || (!this.getUnit().getTopUnits().isEmpty() && !this.getUnit().getTopUnits()
                        .contains(getEmployeeDepartmentUnit()))
                || (this.getUnit().getTopUnits().isEmpty() && !this.getUnit().equals(getEmployeeDepartmentUnit()))) {
            setErrorMessage("error.invalid.unit");
        } else if (!getUnit().getFunctionsSet().contains(this.getFunction())) {
            setErrorMessage("error.invalid.function");
        } else {

            DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
            Double credits = Double.valueOf(this.getCredits());
            Date beginDate_ = null, endDate_ = null;

            try {
                if (this.getBeginDate() != null) {
                    beginDate_ = format.parse(this.getBeginDate());
                } else {
                    setErrorMessage("error.notBeginDate");
                    return "";
                }
                if (this.getEndDate() != null) {
                    endDate_ = format.parse(this.getEndDate());
                } else {
                    setErrorMessage("error.notEndDate");
                    return "";
                }

                AssociateNewFunctionToPerson.runAssociateNewFunctionToPerson(this.getFunctionID(), this.getPersonID(), credits,
                        YearMonthDay.fromDateFields(beginDate_), YearMonthDay.fromDateFields(endDate_));
                setErrorMessage("message.success");
                return "success";

            } catch (ParseException e) {
                setErrorMessage("error.date1.format");
            } catch (FenixServiceException e) {
                setErrorMessage(e.getMessage());
            } catch (DomainException e) {
                setErrorMessage(e.getMessage());
            }
        }
        return "";
    }

    public String editFunction() throws FenixServiceException {

        DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        Double credits = Double.valueOf(this.getCredits());
        Date beginDate_ = null, endDate_ = null;

        try {
            if (this.getBeginDate() != null) {
                beginDate_ = format.parse(this.getBeginDate());
            } else {
                setErrorMessage("error.notBeginDate");
                return "";
            }
            if (this.getEndDate() != null) {
                endDate_ = format.parse(this.getEndDate());
            } else {
                setErrorMessage("error.notEndDate");
                return "";
            }

            EditPersonFunction.runEditPersonFunction(this.getPersonFunctionID(), this.getFunctionID(),
                    YearMonthDay.fromDateFields(beginDate_), YearMonthDay.fromDateFields(endDate_), credits);
            setErrorMessage("message.success");
            return "alterFunction";

        } catch (ParseException e) {
            setErrorMessage("error.date1.format");
        } catch (FenixServiceException e) {
            setErrorMessage(e.getMessage());
        } catch (DomainException e) {
            setErrorMessage(e.getMessage());
        }
        return "";
    }

    public String deletePersonFunction() throws FenixServiceException {
        DeletePersonFunction.runDeletePersonFunction(this.getPersonFunctionID());
        setErrorMessage("message.success");
        return "success";
    }

    public List<PersonFunction> getActiveFunctions() throws FenixServiceException {
        if (this.activeFunctions == null) {
            Person person = this.getPerson();
            List<PersonFunction> activeFunctions = PersonFunction.getActivePersonFunctions(person);
            this.activeFunctions = new ArrayList<PersonFunction>();

            addValidFunctions(activeFunctions, this.activeFunctions);
            Collections.sort(this.activeFunctions, new ReverseComparator(PersonFunction.COMPARATOR_BY_BEGIN_DATE));
        }
        return this.activeFunctions;
    }

    public List<PersonFunction> getInactiveFunctions() throws FenixServiceException {

        if (this.inactiveFunctions == null) {
            Person person = this.getPerson();
            List<PersonFunction> inactiveFunctions = PersonFunction.getInactivePersonFunctions(person);
            this.inactiveFunctions = new ArrayList<PersonFunction>();

            addValidFunctions(inactiveFunctions, this.inactiveFunctions);
            Collections.sort(this.inactiveFunctions, new ReverseComparator(PersonFunction.COMPARATOR_BY_BEGIN_DATE));
        }
        return this.inactiveFunctions;
    }

    private void addValidFunctions(List<PersonFunction> functions, List<PersonFunction> functions_) {
        for (PersonFunction person_function : functions) {
            Unit unit = person_function.getFunction().getUnit();
            if (unit.getParentUnits().isEmpty()) {
                if (unit.equals(this.getEmployeeDepartmentUnit())) {
                    functions_.add(person_function);
                }
            } else {
                if (addPersonFunction(unit.getParentUnits(), this.getEmployeeDepartmentUnit())) {
                    functions_.add(person_function);
                }
            }
        }
    }

    private boolean addPersonFunction(Collection<Unit> parentUnits, Unit employeeUnit) {
        boolean found = false;
        for (Unit unit : parentUnits) {
            if (unit.equals(employeeUnit)) {
                return true;
            } else {
                found = addPersonFunction(unit.getParentUnits(), employeeUnit);
                if (found) {
                    return found;
                }
            }
        }
        return false;
    }

    public int getNumberOfPages() throws FenixServiceException {
        if ((getPersonsNumber() % PresentationConstants.LIMIT_FINDED_PERSONS) != 0) {
            return (getPersonsNumber() / PresentationConstants.LIMIT_FINDED_PERSONS) + 1;
        } else {
            return (getPersonsNumber() / PresentationConstants.LIMIT_FINDED_PERSONS);
        }
    }

    public String searchPersonByName() throws FenixServiceException {

        if (allPersonsList == null) {
            allPersonsList = new ArrayList<Person>();
        }
        allPersonsList = getAllValidPersonsByName();

        if (allPersonsList.isEmpty()) {
            setErrorMessage("error.search.person");
        } else {
            setIntervalPersons();
        }
        return "";
    }

    private void setIntervalPersons() throws FenixServiceException {
        final Collection<Person> result = getPersonsList();
        final int begin = (this.getPageIndex() - 1) * PresentationConstants.LIMIT_FINDED_PERSONS;
        final int end = begin + PresentationConstants.LIMIT_FINDED_PERSONS;
        int i = 0;
        for (final Person person : allPersonsList) {
            if (i >= begin && i < end) {
                result.add(person);
            } else if (i >= end) {
                break;
            }
            i++;
        }
    }

    private Collection<Person> getAllValidPersonsByName() throws FenixServiceException {

        SearchParameters searchParameters =
                new SearchPerson.SearchParameters(personName, null, null, null, null, null, null, null, null, Boolean.TRUE, null,
                        (String) null);
        SearchPersonPredicate predicate = new SearchPerson.SearchPersonPredicate(searchParameters);

        CollectionPager<Person> allPersons = SearchPerson.runSearchPerson(searchParameters, predicate);

        return allPersons.getCollection();
    }

    private List<Person> getAllPersonsToSearchByClass() throws FenixServiceException {
        List<Person> allPersons = new ArrayList<Person>();
        RoleType personTypeAux = RoleType.valueOf(personType);
        if (personTypeAux.equals(RoleType.EMPLOYEE) || personTypeAux.equals(RoleType.TEACHER)
                || personTypeAux.equals(RoleType.GRANT_OWNER)) {
            List<Employee> allEmployees = new ArrayList<Employee>();
            allEmployees.addAll(rootDomainObject.getEmployeesSet());
            for (Employee employee : allEmployees) {
                if (employee.getEmployeeNumber().equals(personNumber)) {
                    allPersons.add(employee.getPerson());
                    return allPersons;
                }
            }
        } else if (personTypeAux.equals(RoleType.STUDENT)) {
            List<Registration> allStudents = new ArrayList<Registration>();
            allStudents.addAll(rootDomainObject.getRegistrationsSet());
            for (Registration registration : allStudents) {
                if (registration.getNumber().equals(personNumber)) {
                    allPersons.add(registration.getPerson());
                    return allPersons;
                }
            }
        }
        return allPersons;
    }

    public String searchPersonByNumber() throws FenixServiceException {
        getPersonsList();
        this.personsList = getAllPersonsToSearchByClass();
        if (this.personsList.isEmpty()) {
            setErrorMessage("error.search.person");
        } else {
            this.allPersonsList = this.personsList;
        }
        return "";
    }

    public Collection<Person> getPersonsList() throws FenixServiceException {
        if (this.personsList == null) {
            this.personsList = new ArrayList<Person>();
        }
        return this.personsList;
    }

    public List<SelectItem> getPersonTypes() {
        List<SelectItem> list = new ArrayList<SelectItem>();

        SelectItem selectItem1 = new SelectItem();
        selectItem1.setLabel(BundleUtil.getString(Bundle.ENUMERATION, RoleType.EMPLOYEE.getName()).trim());
        selectItem1.setValue(RoleType.EMPLOYEE.name());

        SelectItem selectItem2 = new SelectItem();
        selectItem2.setLabel(BundleUtil.getString(Bundle.ENUMERATION, RoleType.TEACHER.getName()).trim());
        selectItem2.setValue(RoleType.TEACHER.name());

        SelectItem selectItem3 = new SelectItem();
        selectItem3.setLabel(BundleUtil.getString(Bundle.ENUMERATION, RoleType.GRANT_OWNER.getName()).trim());
        selectItem3.setValue(RoleType.GRANT_OWNER.name());

        SelectItem selectItem4 = new SelectItem();
        selectItem4.setLabel(BundleUtil.getString(Bundle.ENUMERATION, RoleType.STUDENT.getName()).trim());
        selectItem4.setValue(RoleType.STUDENT.name());

        list.add(selectItem1);
        list.add(selectItem2);
        list.add(selectItem3);
        list.add(selectItem4);

        Collections.sort(list, new BeanComparator("label"));
        return list;
    }

    public List<SelectItem> getValidFunctions() throws FenixServiceException {
        List<SelectItem> list = new ArrayList<SelectItem>();
        SelectItem selectItem = null;
        for (Function function : getUnit().getFunctionsSet()) {
            if (!function.isInherentFunction()
                    && ((this.getPersonFunction() != null && function.equals(this.getPersonFunction().getFunction())) || (this
                            .getPersonFunction() == null && function.isActive(new YearMonthDay())))) {
                selectItem = new SelectItem();
                selectItem.setValue(function.getExternalId());
                selectItem.setLabel(function.getName());
                list.add(selectItem);
            }
        }
        Collections.sort(list, new BeanComparator("label"));
        this.numberOfFunctions = list.size();

        return list;
    }

    public List<SelectItem> getDurationList() throws FenixServiceException {
        List<SelectItem> list = new ArrayList<SelectItem>();
        SelectItem selectItem = null;

        for (int i = 1; i <= 2; i++) {
            selectItem = new SelectItem();
            selectItem.setValue(new Integer(i));
            selectItem.setLabel(i + " sem.");
            list.add(selectItem);
        }

        return list;
    }

    public List<SelectItem> getExecutionPeriods() throws FenixServiceException {
        Collection<ExecutionYear> allExecutionYears = rootDomainObject.getExecutionYearsSet();
        List<SelectItem> list = new ArrayList<SelectItem>();
        String[] year = null;

        for (ExecutionYear executionYear : allExecutionYears) {
            year = executionYear.getYear().split("/");
            if (Integer.valueOf(year[0].trim()) >= 2005) {
                newSelectItem(executionYear, list);
            }
        }
        Collections.sort(list, new BeanComparator("label"));
        return list;
    }

    private void newSelectItem(ExecutionYear executionYear, List<SelectItem> list) {
        for (ExecutionSemester executionSemester : executionYear.getExecutionPeriodsSet()) {
            SelectItem selectItem = new SelectItem();
            selectItem.setValue(executionSemester.getExternalId());
            selectItem.setLabel(executionYear.getYear() + " - " + executionSemester.getSemester() + "º Semestre");
            list.add(selectItem);
        }
    }

    protected Set<Unit> getSubUnits(Unit unit, YearMonthDay currentDate) {
        Set<Unit> units = new HashSet();
        for (AccountabilityTypeEnum accountabilityTypeEnum : getAccountabilityTypes()) {
            units.addAll(unit.getActiveSubUnits(currentDate, accountabilityTypeEnum));
        }
        return units;
    }

    protected AccountabilityTypeEnum[] getAccountabilityTypes() {
        AccountabilityTypeEnum[] accountabilityTypeEnums =
                { AccountabilityTypeEnum.ORGANIZATIONAL_STRUCTURE, AccountabilityTypeEnum.ACADEMIC_STRUCTURE };
        return accountabilityTypeEnums;
    }

    public String getUnits() throws FenixServiceException {
        StringBuilder buffer = new StringBuilder();
        YearMonthDay currentDate = new YearMonthDay();
        buffer.append("<ul class='padding nobullet'>");
        if (this.getEmployeeDepartmentUnit() != null && this.getEmployeeDepartmentUnit().isActive(new YearMonthDay())) {
            getUnitsList(this.getEmployeeDepartmentUnit(), null, buffer, currentDate);
        } else {
            return "";
        }
        buffer.append("</ul>");
        return buffer.toString();
    }

    private void getUnitsList(Unit parentUnit, Unit parentUnitParent, StringBuilder buffer, YearMonthDay currentDate) {

        openLITag(buffer);

        List<Unit> subUnits = new ArrayList<Unit>(getSubUnits(parentUnit, currentDate));
        Collections.sort(subUnits, Unit.COMPARATOR_BY_NAME_AND_ID);

        if (!subUnits.isEmpty()) {
            putImage(parentUnit, buffer, parentUnitParent);
        }

        buffer.append("<a href=\"").append(getContextPath())
                .append("/departmentAdmOffice/functionsManagement/chooseFunction.faces?personID=").append(personID)
                .append("&unitID=").append(parentUnit.getExternalId()).append("\">").append(parentUnit.getPresentationName())
                .append("</a>").append("</li>");

        if (!subUnits.isEmpty()) {
            openULTag(parentUnit, buffer, parentUnitParent);
        }

        for (Unit subUnit : subUnits) {
            getUnitsList(subUnit, parentUnit, buffer, currentDate);
        }

        if (!subUnits.isEmpty()) {
            closeULTag(buffer);
        }
    }

    public Unit getEmployeeDepartmentUnit() {

        User userView = getUserView();
        Employee personEmployee = userView.getPerson().getEmployee();
        Unit departmentUnit = null;

        if (personEmployee != null) {
            Department department = personEmployee.getCurrentDepartmentWorkingPlace();
            if (department != null) {
                departmentUnit = department.getDepartmentUnit();
            }
        }

        return departmentUnit;
    }

    public Person getPerson() throws FenixServiceException {
        return (Person) FenixFramework.getDomainObject(getPersonID());
    }

    public int getPersonsNumber() throws FenixServiceException {
        return getAllPersonsList().size();
    }

    public Collection<Person> getAllPersonsList() throws FenixServiceException {
        if (allPersonsList == null) {
            if (this.pageIndex != null) {
                if (this.personName != null) {
                    searchPersonByName();
                } else if (this.personNumber != null) {
                    searchPersonByNumber();
                }
            } else {
                allPersonsList = new ArrayList<Person>();
            }
        }
        return allPersonsList;
    }

    public void setAllPersonsList(List<Person> allPersonsList) {
        this.allPersonsList = allPersonsList;
    }

    public String getPersonID() {
        if (this.personID == null && this.personIDHidden != null && this.personIDHidden.getValue() != null
                && !this.personIDHidden.getValue().equals("")) {
            this.personID = this.personIDHidden.getValue().toString();
        }
        return personID;
    }

    public Integer getPageIndex() {
        if (pageIndex == null) {
            pageIndex = Integer.valueOf(1);
        }
        return pageIndex;
    }

    public Unit getUnit() throws FenixServiceException {
        if (this.unit == null && this.getUnitID() != null) {
            this.unit = (Unit) FenixFramework.getDomainObject(getUnitID());
        } else if (this.unit == null && this.getPersonFunctionID() != null) {
            this.unit = this.getPersonFunction().getFunction().getUnit();
        }

        return this.unit;
    }

    public String getUnitID() {
        if (this.unitID == null && this.unitIDHidden != null && this.unitIDHidden.getValue() != null
                && !this.unitIDHidden.getValue().equals("")) {
            this.unitID = this.unitIDHidden.getValue().toString();
        }
        return unitID;
    }

    public HtmlInputHidden getPersonIDHidden() {
        if (this.personIDHidden == null) {
            this.personIDHidden = new HtmlInputHidden();
            this.personIDHidden.setValue(this.getPersonID());
        }
        return personIDHidden;
    }

    public void setPersonIDHidden(HtmlInputHidden personIDHidden) {
        if (this.personIDHidden != null) {
            this.setPersonID(this.personIDHidden.getValue().toString());
        }
        this.personIDHidden = personIDHidden;
    }

    public HtmlInputHidden getUnitIDHidden() {
        if (this.unitIDHidden == null) {
            this.unitIDHidden = new HtmlInputHidden();
            this.unitIDHidden.setValue(this.getUnitID());
        }
        return unitIDHidden;
    }

    public void setUnitIDHidden(HtmlInputHidden unitIDHidden) {
        if (this.unitIDHidden != null) {
            this.setUnitID(this.unitIDHidden.getValue().toString());
        }
        this.unitIDHidden = unitIDHidden;
    }

    public Function getFunction() throws FenixServiceException {
        if (this.function == null && this.getFunctionID() != null) {
            this.function = (Function) FenixFramework.getDomainObject(getFunctionID());
        } else if (this.function == null && this.getPersonFunctionID() != null) {
            this.function = this.getPersonFunction().getFunction();
        }
        return this.function;
    }

    public void setFunction(Function function) {
        this.function = function;
    }

    public void setUnitID(String unitID) {
        this.unitID = unitID;
    }

    public String getFunctionID() {
        if (this.functionID == null && this.functionIDHidden != null && this.functionIDHidden.getValue() != null
                && !this.functionIDHidden.getValue().equals("")) {
            this.functionID = this.functionIDHidden.getValue().toString();
        }
        return functionID;
    }

    public void setFunctionID(String functionID) {
        this.functionID = functionID;
    }

    public String getBeginDate() throws FenixServiceException {

        if (this.beginDate == null && this.beginDateHidden != null && this.beginDateHidden.getValue() != null
                && !this.beginDateHidden.getValue().equals("")) {
            this.beginDate = this.beginDateHidden.getValue().toString();

        } else if (this.beginDate == null && this.getPersonFunctionID() != null) {
            this.beginDate = DateFormatUtil.format("dd/MM/yyyy", this.getPersonFunction().getBeginDateInDateType());

        } else if (this.beginDate == null && this.getPersonFunctionID() == null
                && (this.beginDateHidden == null || this.beginDateHidden.getValue() == null) && this.getExecutionPeriod() != null) {

            ExecutionSemester executionSemester = FenixFramework.getDomainObject(this.executionPeriod);

            this.beginDate =
                    (executionSemester != null) ? DateFormatUtil.format("dd/MM/yyyy", executionSemester.getBeginDate()) : null;
        }
        return this.beginDate;
    }

    public void setBeginDate(String beginDate) {
        this.beginDate = beginDate;
    }

    public Double getCredits() throws FenixServiceException {
        if (this.credits == null && this.creditsHidden != null && this.creditsHidden.getValue() != null
                && !this.creditsHidden.getValue().equals("")) {
            this.credits = Double.valueOf(this.creditsHidden.getValue().toString());

        } else if (this.credits == null && this.getPersonFunctionID() != null) {
            this.credits = this.getPersonFunction().getCredits();
        } else if (this.credits == null) {
            this.credits = new Double(0);
        }
        return credits;
    }

    public void setCredits(Double credits) {
        this.credits = credits;
    }

    public String getEndDate() throws FenixServiceException {

        if (this.endDate == null && this.endDateHidden != null && this.endDateHidden.getValue() != null
                && !this.endDateHidden.getValue().equals("")) {
            this.endDate = this.endDateHidden.getValue().toString();

        } else if (this.endDate == null && this.getPersonFunctionID() != null && this.getPersonFunction().getEndDate() != null) {
            this.endDate = DateFormatUtil.format("dd/MM/yyyy", this.getPersonFunction().getEndDateInDateType());

        } else if (this.endDate == null && this.getPersonFunctionID() == null
                && (this.endDateHidden == null || this.endDateHidden.getValue() == null) && this.getExecutionPeriod() != null) {

            ExecutionSemester executionSemester = FenixFramework.getDomainObject(this.executionPeriod);

            ExecutionSemester executionPeriodWithDuration = getDurationEndDate(executionSemester);
            this.endDate = DateFormatUtil.format("dd/MM/yyyy", executionPeriodWithDuration.getEndDate());
        }
        return this.endDate;
    }

    private ExecutionSemester getDurationEndDate(ExecutionSemester executionSemester) {
        ExecutionSemester finalExecutionPeriod = executionSemester;
        if (this.getDurationHidden() != null && this.durationHidden.getValue() != null
                && !this.durationHidden.getValue().equals("")) {
            Integer duration = Integer.valueOf(this.durationHidden.getValue().toString());
            for (int i = 1; i < duration; i++) {
                if (finalExecutionPeriod.getNextExecutionPeriod() != null) {
                    finalExecutionPeriod = finalExecutionPeriod.getNextExecutionPeriod();
                } else {
                    return executionSemester;
                }
            }
        }
        return finalExecutionPeriod;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public void setUnit(Unit unit) {
        this.unit = unit;
    }

    public void setPersonID(String personID) {
        this.personID = personID;
    }

    public void setPageIndex(Integer page) {
        this.pageIndex = page;
    }

    public void setPersonsList(List<Person> personsList) {
        this.personsList = personsList;
    }

    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }

    public HtmlInputHidden getBeginDateHidden() throws FenixServiceException {
        if (this.beginDateHidden == null) {
            this.beginDateHidden = new HtmlInputHidden();
            this.beginDateHidden.setValue(this.getBeginDate());
        }
        return beginDateHidden;
    }

    public void setBeginDateHidden(HtmlInputHidden beginDateHidden) {
        if (this.beginDateHidden != null) {
            this.setBeginDate(this.beginDateHidden.getValue().toString());
        }
        this.beginDateHidden = beginDateHidden;
    }

    public HtmlInputHidden getCreditsHidden() {
        if (this.creditsHidden == null) {
            this.creditsHidden = new HtmlInputHidden();
            this.creditsHidden.setValue(this.credits);
        }

        return creditsHidden;
    }

    public void setCreditsHidden(HtmlInputHidden creditsHidden) {
        if (this.creditsHidden != null) {
            this.setCredits(Double.valueOf(this.creditsHidden.getValue().toString()));
        }
        this.creditsHidden = creditsHidden;
    }

    public HtmlInputHidden getEndDateHidden() throws FenixServiceException {
        if (this.endDateHidden == null) {
            this.endDateHidden = new HtmlInputHidden();
            this.endDateHidden.setValue(this.getEndDate());
        }
        return endDateHidden;
    }

    public void setEndDateHidden(HtmlInputHidden endDateHidden) {
        if (this.endDateHidden != null) {
            this.setEndDate(this.endDateHidden.getValue().toString());
        }
        this.endDateHidden = endDateHidden;
    }

    public HtmlInputHidden getFunctionIDHidden() {
        if (this.functionIDHidden == null) {
            this.functionIDHidden = new HtmlInputHidden();
            this.functionIDHidden.setValue(this.functionID);
        }
        return functionIDHidden;
    }

    public void setFunctionIDHidden(HtmlInputHidden functionIDHidden) {
        this.functionIDHidden = functionIDHidden;
    }

    public Integer getNumberOfFunctions() throws FenixServiceException {
        if (this.numberOfFunctions == null) {
            getValidFunctions();
        }
        return numberOfFunctions;
    }

    public void setNumberOfFunctions(Integer numberOfFunctions) {
        this.numberOfFunctions = numberOfFunctions;
    }

    public String getPersonFunctionID() {
        if (this.personFunctionID == null && this.personFunctionIDHidden != null
                && this.personFunctionIDHidden.getValue() != null && !this.personFunctionIDHidden.getValue().equals("")) {
            this.personFunctionID = this.personFunctionIDHidden.getValue().toString();
        }
        return personFunctionID;
    }

    public void setPersonFunctionID(String personFunctionID) {
        this.personFunctionID = personFunctionID;
    }

    public PersonFunction getPersonFunction() throws FenixServiceException {
        if (this.personFunction == null) {
            this.personFunction = (PersonFunction) FenixFramework.getDomainObject(this.getPersonFunctionID());
        }
        return personFunction;
    }

    public void setPersonFunction(PersonFunction person_Function) {
        this.personFunction = person_Function;
    }

    public HtmlInputHidden getPersonFunctionIDHidden() {
        if (this.personFunctionIDHidden == null) {
            this.personFunctionIDHidden = new HtmlInputHidden();
            this.personFunctionIDHidden.setValue(this.personFunctionID);
        }
        return personFunctionIDHidden;
    }

    public void setPersonFunctionIDHidden(HtmlInputHidden personFunctionIDHidden) {
        if (this.personFunctionIDHidden != null) {
            this.personFunctionID = this.personFunctionIDHidden.getValue().toString();
        }
        this.personFunctionIDHidden = personFunctionIDHidden;
    }

    public List<Function> getInherentFunctions() throws FenixServiceException {
        if (this.inherentFunctions == null) {
            this.inherentFunctions = new ArrayList<Function>();
            for (PersonFunction personFunction : this.getActiveFunctions()) {
                this.inherentFunctions.addAll(personFunction.getFunction().getInherentFunctionsSet());
            }
        }
        return inherentFunctions;
    }

    public void setInherentFunctions(List<Function> inherentFunctions) {
        this.inherentFunctions = inherentFunctions;
    }

    public void setActiveFunctions(List<PersonFunction> activeFunctions) {
        this.activeFunctions = activeFunctions;
    }

    public void setInactiveFunctions(List<PersonFunction> inactiveFunctions) {
        this.inactiveFunctions = inactiveFunctions;
    }

    public String getExecutionPeriod() throws FenixServiceException {
        if (executionPeriod == null && executionPeriodHidden != null && executionPeriodHidden.getValue() != null
                && !executionPeriodHidden.getValue().equals("")) {
            executionPeriod = executionPeriodHidden.getValue().toString();
        } else if (executionPeriod == null) {
            executionPeriod = getCurrentExecutionPeriodID();
        }
        return executionPeriod;
    }

    private String getCurrentExecutionPeriodID() throws FenixServiceException {
        Collection<ExecutionSemester> allExecutionPeriods = rootDomainObject.getExecutionPeriodsSet();
        for (ExecutionSemester period : allExecutionPeriods) {
            if (period.getState().equals(PeriodState.CURRENT)) {
                return period.getExternalId();
            }
        }
        return null;
    }

    public void setExecutionPeriod(String executionPeriodID) {
        this.executionPeriod = executionPeriodID;
        if (executionPeriodID != null) {
            this.executionPeriodHidden.setValue(executionPeriodID);
        }
    }

    public Integer getDuration() {
        if (duration == null && durationHidden != null && durationHidden.getValue() != null
                && !durationHidden.getValue().equals("")) {
            duration = Integer.valueOf(durationHidden.getValue().toString());

        } else if (this.duration == null) {
            this.duration = new Integer(1);
        }
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
        if (duration != null) {
            this.durationHidden.setValue(duration);
        }
    }

    public HtmlInputHidden getExecutionPeriodHidden() throws FenixServiceException {
        if (executionPeriodHidden == null) {
            executionPeriodHidden = new HtmlInputHidden();
            executionPeriodHidden.setValue(this.getExecutionPeriod());

        } else if (executionPeriodHidden.getValue() == null) {
            executionPeriodHidden.setValue(this.getExecutionPeriod());
        }
        return executionPeriodHidden;
    }

    public void setExecutionPeriodHidden(HtmlInputHidden executionPeriodHidden) {
        this.executionPeriodHidden = executionPeriodHidden;
    }

    public HtmlInputHidden getDurationHidden() {
        if (this.durationHidden == null) {
            this.durationHidden = new HtmlInputHidden();
            this.durationHidden.setValue(new Integer(1));
        }
        return durationHidden;
    }

    public void setDurationHidden(HtmlInputHidden durationHidden) {
        this.durationHidden = durationHidden;
    }

    public HtmlInputHidden getDisabledVarHidden() {
        if (disabledVarHidden == null) {
            disabledVarHidden = new HtmlInputHidden();
            if (getRequestParameter("disabledVar") != null && !getRequestParameter("disabledVar").equals("")) {
                this.disabledVarHidden.setValue(Integer.valueOf(getRequestParameter("disabledVar").toString()));
            }
        }
        return disabledVarHidden;
    }

    public void setDisabledVarHidden(HtmlInputHidden disabledVarHidden) {
        this.disabledVarHidden = disabledVarHidden;
    }

    public Integer getDisabledVar() {
        if (disabledVar == null && disabledVarHidden != null && disabledVarHidden.getValue() != null
                && !disabledVarHidden.getValue().equals("")) {
            disabledVar = Integer.valueOf(disabledVarHidden.getValue().toString());
        } else if (disabledVar == null) {
            disabledVar = 0;
        }
        return disabledVar;
    }

    public void setDisabledVar(Integer disabledVar) {
        this.disabledVar = disabledVar;
    }

    public Integer getPersonNumber() {
        return personNumber;
    }

    public void setPersonNumber(Integer personNumber) {
        this.personNumber = personNumber;
    }

    public String getPersonType() {
        return personType;
    }

    public void setPersonType(String personType) {
        this.personType = personType;
    }

    protected void openULTag(Unit parentUnit, StringBuilder buffer, Unit parentUnitParent) {
        buffer.append("<ul class='mvert0 nobullet' id=\"").append("aa").append(parentUnit.getExternalId())
                .append((parentUnitParent != null) ? parentUnitParent.getExternalId() : "").append("\" ")
                .append("style='display:none'>\r\n");
    }

    protected void putImage(Unit parentUnit, StringBuilder buffer, Unit parentUnitParent) {
        buffer.append("<img ").append("src='").append(getContextPath()).append("/images/toggle_plus10.gif' id=\"")
                .append(parentUnit.getExternalId()).append((parentUnitParent != null) ? parentUnitParent.getExternalId() : "")
                .append("\" ").append("indexed='true' onClick=\"").append("check(document.getElementById('").append("aa")
                .append(parentUnit.getExternalId()).append((parentUnitParent != null) ? parentUnitParent.getExternalId() : "")
                .append("'),document.getElementById('").append(parentUnit.getExternalId())
                .append((parentUnitParent != null) ? parentUnitParent.getExternalId() : "").append("'));return false;")
                .append("\"> ");
    }

    protected void closeULTag(StringBuilder buffer) {
        buffer.append("</ul>");
    }

    protected void openLITag(StringBuilder buffer) {
        buffer.append("<li>");
    }
}
