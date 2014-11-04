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
 * Created on Nov 10, 2005
 *	by angela
 */
package org.fenixedu.academic.ui.faces.bean.person;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.faces.component.html.HtmlInputHidden;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

import org.fenixedu.academic.service.services.commons.ReadExecutionYearsService;
import org.fenixedu.academic.service.services.exceptions.FenixServiceException;
import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.organizationalStructure.AccountabilityTypeEnum;
import org.fenixedu.academic.domain.organizationalStructure.Contract;
import org.fenixedu.academic.domain.organizationalStructure.EmployeeContract;
import org.fenixedu.academic.domain.organizationalStructure.Function;
import org.fenixedu.academic.domain.organizationalStructure.PersonFunction;
import org.fenixedu.academic.domain.organizationalStructure.Unit;
import org.fenixedu.academic.domain.organizationalStructure.UnitUtils;
import org.fenixedu.academic.ui.faces.bean.base.FenixBackingBean;
import org.fenixedu.academic.util.Bundle;
import org.fenixedu.academic.util.PeriodState;

import org.apache.commons.lang.StringUtils;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.fenixedu.commons.StringNormalizer;
import org.joda.time.YearMonthDay;

import pt.ist.fenixframework.FenixFramework;

public class OrganizationalStructureBackingBean extends FenixBackingBean {

    public String choosenExecutionYearID;

    public Unit parentUnit;

    public Integer personID;

    public String listType;

    private HtmlInputHidden unitIDHidden;

    public OrganizationalStructureBackingBean() {
        if (getRequestParameter("unitID") != null) {
            getUnitIDHidden().setValue(getRequestParameter("unitID"));
        }
    }

    public List<SelectItem> getExecutionYears() throws FenixServiceException {
        final Set<ExecutionYear> executionYears = rootDomainObject.getExecutionYearsSet();

        List<SelectItem> result = new ArrayList<SelectItem>(executionYears.size());
        for (ExecutionYear executionYear : executionYears) {
            if (executionYear.getYear().compareTo("2005/2006") >= 0) {
                result.add(new SelectItem(executionYear.getExternalId(), executionYear.getYear(), executionYear.getState()
                        .getStateCode()));
            }
        }

        Collections.reverse(result);
        if (getChoosenExecutionYearID() == null) {
            for (SelectItem selectExecutionYear : result) {
                if (selectExecutionYear.getDescription().equals(PeriodState.CURRENT_CODE)) {
                    setChoosenExecutionYearID((String) selectExecutionYear.getValue());
                }
            }
        }

        return result;
    }

    public String getUnits() throws FenixServiceException {
        StringBuilder buffer = new StringBuilder();
        YearMonthDay currentDate = new YearMonthDay();
        String partyTypeOrClassificationName = null;

        Map<String, Set<Unit>> allInstitutionSubUnits = getAllInstitutionSubUnits();

        for (String typeOrClassificationName : allInstitutionSubUnits.keySet()) {

            partyTypeOrClassificationName = null;

            for (Unit unit : allInstitutionSubUnits.get(typeOrClassificationName)) {

                // Title
                if (partyTypeOrClassificationName == null) {
                    partyTypeOrClassificationName = typeOrClassificationName;

                    buffer.append("<h3 class='mtop2'>")
                            .append(BundleUtil.getString(Bundle.ENUMERATION, partyTypeOrClassificationName)).append("</h3>\r\n");
                }

                buffer.append("<ul class='padding nobullet'>\r\n");

                List<Unit> activeSubUnits = unit.getActiveSubUnits(currentDate, AccountabilityTypeEnum.ORGANIZATIONAL_STRUCTURE);
                Collections.sort(activeSubUnits, Unit.COMPARATOR_BY_NAME_AND_ID);

                if (!activeSubUnits.isEmpty()) {

                    buffer.append("\t<li><img ").append("src='").append(getContextPath())
                            .append("/images/toggle_plus10.gif' id='img").append(unit.getExternalId()).append("'")
                            .append("onClick=\"check(document.getElementById('aa").append(unit.getExternalId())
                            .append("'),document.getElementById('").append(unit.getExternalId()).append("'));return false;\"/> ");

                    buffer.append("<a href='").append(getContextPath())
                            .append("/messaging/organizationalStructure/chooseUnit.faces?unitID=").append(unit.getExternalId())
                            .append("'>").append(unit.getNameWithAcronym()).append("</a></li>\r\n");

                } else {

                    buffer.append("\t<li><a href='").append(getContextPath())
                            .append("/messaging/organizationalStructure/chooseUnit.faces?unitID=").append(unit.getExternalId())
                            .append("'>").append(unit.getNameWithAcronym()).append("</a></li>\r\n");

                }

                getInstitutionSubUnitsTree(buffer, unit, activeSubUnits, currentDate);
                buffer.append("</ul>\r\n");
            }
        }

        return buffer.toString();
    }

    private void getInstitutionSubUnitsTree(StringBuilder buffer, Unit parentUnit, List<Unit> activeSubUnits,
            YearMonthDay currentDate) {

        if (!activeSubUnits.isEmpty()) {
            buffer.append("\t<li class='nobullet'><ul class='mvert0' id='aa").append(parentUnit.getExternalId())
                    .append("' style='display:none'>\r\n");
            for (Unit subUnit : activeSubUnits) {
                getSubUnitsWithoutAggregatedUnitsList(buffer, currentDate, subUnit);
            }
            buffer.append("\t</ul></li>\r\n");
        }
    }

    private void getSubUnitsList(Unit parentUnit, StringBuilder buffer, YearMonthDay currentDate) {

        buffer.append("\t\t<li><a href='").append(getContextPath())
                .append("/messaging/organizationalStructure/chooseUnit.faces?unitID=").append(parentUnit.getExternalId())
                .append("'>").append(parentUnit.getNameWithAcronym()).append("</a></li>\r\n");

        List<Unit> activeSubUnits = parentUnit.getActiveSubUnits(currentDate, AccountabilityTypeEnum.ORGANIZATIONAL_STRUCTURE);

        if (!activeSubUnits.isEmpty()) {
            buffer.append("\t\t<li class='nobullet'><ul class='mvert0'>\r\n");
        }

        for (Unit subUnit : activeSubUnits) {
            getSubUnitsWithoutAggregatedUnitsList(buffer, currentDate, subUnit);
        }

        if (!activeSubUnits.isEmpty()) {
            buffer.append("\t\t</ul></li>\r\n");
        }
    }

    private void getSubUnitsWithoutAggregatedUnitsList(StringBuilder buffer, YearMonthDay currentDate, Unit subUnit) {
        List<Unit> validInstitutionSubUnits = null;
        if (subUnit.isAggregateUnit()) {
            validInstitutionSubUnits = getValidSubUnits(subUnit, currentDate);
        }
        if (validInstitutionSubUnits != null) {
            for (Unit validSubUnit : validInstitutionSubUnits) {
                getSubUnitsList(validSubUnit, buffer, currentDate);
            }
        } else {
            getSubUnitsList(subUnit, buffer, currentDate);
        }
    }

    public Map<String, Set<Unit>> getAllInstitutionSubUnits() throws FenixServiceException {

        YearMonthDay currentDate = new YearMonthDay();

        Map<String, Set<Unit>> resultMap = new TreeMap<String, Set<Unit>>(new Comparator<String>() {
            @Override
            public int compare(String arg0, String arg1) {
                String firstString = StringNormalizer.normalize(BundleUtil.getString(Bundle.ENUMERATION, arg0));
                String secondString = StringNormalizer.normalize(BundleUtil.getString(Bundle.ENUMERATION, arg1));
                return firstString.compareToIgnoreCase(secondString);
            }
        });

        Unit istUnit = UnitUtils.readInstitutionUnit();
        if (istUnit == null) {
            return new HashMap<String, Set<Unit>>();
        }

        for (Unit subUnit : istUnit.getActiveSubUnits(currentDate, AccountabilityTypeEnum.ORGANIZATIONAL_STRUCTURE)) {
            if (subUnit.isAggregateUnit()) {
                for (Unit unit : getValidSubUnits(subUnit, currentDate)) {
                    addUnitToMap(resultMap, unit);
                }
            } else {
                addUnitToMap(resultMap, subUnit);
            }
        }

        return resultMap;
    }

    private void addUnitToMap(Map<String, Set<Unit>> resultMap, Unit subUnit) {

        String typeName = subUnit.getClassification() != null ? subUnit.getClassification().getName() : null;
        if (StringUtils.isEmpty(typeName)) {
            typeName = subUnit.getType() != null ? subUnit.getType().getName() : null;
        }

        if (typeName != null) {
            if (!resultMap.containsKey(typeName)) {
                Set<Unit> newSet = new TreeSet<Unit>(Unit.COMPARATOR_BY_NAME_AND_ID);
                newSet.add(subUnit);
                resultMap.put(typeName, newSet);
            } else {
                resultMap.get(typeName).add(subUnit);
            }
        }
    }

    private List<Unit> getValidSubUnits(Unit unit, YearMonthDay currentDate) {
        List<Unit> result = new ArrayList<Unit>();
        for (Unit subUnit : unit.getActiveSubUnits(currentDate, AccountabilityTypeEnum.ORGANIZATIONAL_STRUCTURE)) {
            if (!subUnit.isAggregateUnit()) {
                result.add(subUnit);
            } else {
                result.addAll(getValidSubUnits(subUnit, currentDate));
            }
        }
        return result;
    }

    public String getInstituitionName() throws FenixServiceException {
        Unit institution = UnitUtils.readInstitutionUnit();
        if (institution != null) {
            return institution.getName();
        }
        return null;
    }

    public String getTitle() throws FenixServiceException {
        StringBuilder buffer = new StringBuilder();
        buffer.append("<p><em>");
        buffer.append(this.getUnit().getParentUnitsPresentationName());
        buffer.append("</em></p>");
        buffer.append("<h2>").append(this.getUnit().getNameWithAcronym()).append("</h2>");
        return buffer.toString();
    }

    public String getFunctions() throws FenixServiceException {

        StringBuilder buffer = new StringBuilder();
        YearMonthDay currentDate = new YearMonthDay();
        Unit chooseUnit = this.getUnit();
        ExecutionYear iExecutionYear = getExecutionYear(getChoosenExecutionYearID());

        buffer.append("<ul class='mtop3 nobullet noindent'><li>");
        // buffer.append("<image
        // src='").append(getContextPath()).append("/images/unit-icon.gif'/>")
        // .append(" ");
        buffer.append("<strong class='highlight6' id='aa");
        buffer.append(chooseUnit.getExternalId()).append("'>");
        buffer.append(chooseUnit.getName()).append("</strong>");

        if (StringUtils.isEmpty(getListType()) || getListType().equals("#") || getListType().equals("0")) {
            printUnitWorkingEmployees(chooseUnit, iExecutionYear, buffer);
        }

        for (Function function : getSortFunctionList(chooseUnit)) {
            if (function.belongsToPeriod(iExecutionYear.getBeginDateYearMonthDay(), iExecutionYear.getEndDateYearMonthDay())) {
                if (StringUtils.isEmpty(getListType()) || getListType().equals("#") || getListType().equals("1")) {
                    buffer.append("<ul><li class='tree_label'>").append(function.getName()).append(": ");
                    buffer.append((function.getParentInherentFunction() != null) ? " (Cargo Inerente)" : "");
                    getPersonFunctionsList(chooseUnit, function, buffer, iExecutionYear);
                    buffer.append("</li></ul>");
                }
            }
        }

        List<Unit> activeSubUnit = chooseUnit.getActiveSubUnits(currentDate, AccountabilityTypeEnum.ORGANIZATIONAL_STRUCTURE);
        Collections.sort(activeSubUnit, Unit.COMPARATOR_BY_NAME_AND_ID);

        for (Unit subUnit : activeSubUnit) {
            getSubUnitsWithoutAggregatedUnitsToFunctionList(buffer, iExecutionYear, currentDate, subUnit);
        }

        buffer.append("</li></ul>");
        return buffer.toString();
    }

    private void getSubUnitsFunctions(Unit subUnit, YearMonthDay currentDate, ExecutionYear iExecutionYear, StringBuilder buffer) {

        buffer.append("<ul class='mtop1 nobullet'><li>");
        // buffer.append("<image
        // src='").append(getContextPath()).append("/images/unit-icon.gif'/>")
        // .append(" ");
        buffer.append("<strong id='aa").append(subUnit.getExternalId()).append("' >").append(subUnit.getName())
                .append("</strong>");

        if (StringUtils.isEmpty(getListType()) || getListType().equals("#") || getListType().equals("0")) {
            printUnitWorkingEmployees(subUnit, iExecutionYear, buffer);
        }

        for (Function function : getSortFunctionList(subUnit)) {
            if (function.belongsToPeriod(iExecutionYear.getBeginDateYearMonthDay(), iExecutionYear.getEndDateYearMonthDay())) {
                if (StringUtils.isEmpty(getListType()) || getListType().equals("#") || getListType().equals("1")) {
                    buffer.append("<ul><li class='tree_label'>").append(function.getName()).append(": ");
                    buffer.append((function.getParentInherentFunction() != null) ? " (Cargo Inerente)" : "");
                    getPersonFunctionsList(subUnit, function, buffer, iExecutionYear);
                    buffer.append("</li></ul>");
                }
            }
        }

        for (Unit subUnit2 : subUnit.getActiveSubUnits(currentDate, AccountabilityTypeEnum.ORGANIZATIONAL_STRUCTURE)) {
            getSubUnitsWithoutAggregatedUnitsToFunctionList(buffer, iExecutionYear, currentDate, subUnit2);
        }

        buffer.append("</li></ul>");
    }

    private String getHomePageUrl(Person person) {
        return person.getHomepageWebAddress();
    }

    private void printPersonHomePage(Person person, StringBuilder buffer) {
        String homePageUrl = getHomePageUrl(person);
        if (!StringUtils.isEmpty(homePageUrl)) {
            buffer.append("<a href='").append(homePageUrl).append("' target='_blank'>").append(person.getNickname())
                    .append("</a>");
            // buffer.append(" <image
            // src='").append(getContextPath()).append("/images/external.gif'/>");
        } else {
            buffer.append(person.getNickname());
        }
    }

    private void printUnitWorkingEmployees(Unit subUnit, ExecutionYear iExecutionYear, StringBuilder buffer) {

        buffer.append("<ul class='unit3'>");
        List<Contract> contractsByContractType = EmployeeContract.getWorkingContracts(subUnit);
        Collections.sort(contractsByContractType, Contract.CONTRACT_COMPARATOR_BY_PERSON_NAME);

        for (Contract contract : contractsByContractType) {
            if (contract.belongsToPeriod(iExecutionYear.getBeginDateYearMonthDay(), iExecutionYear.getEndDateYearMonthDay())) {

                buffer.append("<li>");

                // if
                // (contract.getEmployee().getPerson().getGender().equals(Gender.
                // MALE))
                // {
                // buffer.append("<image
                // src='").append(getContextPath()).append(
                // "/images/worker-icon.png'/>").append(" ");
                // } else if
                // (contract.getEmployee().getPerson().getGender().equals(Gender.
                // FEMALE))
                // {
                // buffer.append("<image
                // src='").append(getContextPath()).append(
                // "/images/woman-icon.png'/>").append(" ");
                // } else {
                // buffer.append("<image
                // src='").append(getContextPath()).append(
                // "/images/person-icon.gif'/>").append(" ");
                // }

                printPersonHomePage(contract.getPerson(), buffer);
                buffer.append("</li>");
            }
        }
        buffer.append("</ul>");
    }

    private void getSubUnitsWithoutAggregatedUnitsToFunctionList(StringBuilder buffer, ExecutionYear iExecutionYear,
            YearMonthDay currentDate, Unit subUnit) {
        List<Unit> validInstitutionSubUnits = null;
        if (subUnit.isAggregateUnit()) {
            validInstitutionSubUnits = getValidSubUnits(subUnit, currentDate);
        }
        if (validInstitutionSubUnits != null) {
            for (Unit validSubUnit : validInstitutionSubUnits) {
                getSubUnitsFunctions(validSubUnit, currentDate, iExecutionYear, buffer);
            }
        } else {
            getSubUnitsFunctions(subUnit, currentDate, iExecutionYear, buffer);
        }
    }

    private void getPersonFunctionsList(Unit unit, Function function, StringBuilder buffer, ExecutionYear iExecutionYear) {
        addPersonFunctions(function, buffer, iExecutionYear);
        if (function.getParentInherentFunction() != null) {
            addPersonFunctions(function.getParentInherentFunction(), buffer, iExecutionYear);
        }
    }

    private void addPersonFunctions(Function function, StringBuilder buffer, ExecutionYear iExecutionYear) {
        Collection<PersonFunction> validPersonFunction = getValidPersonFunction(iExecutionYear, function);
        if (!validPersonFunction.isEmpty()) {
            buffer.append("<ul class='unit1'>");
            for (PersonFunction personFunction : validPersonFunction) {
                buffer.append("<li>");
                // buffer.append("<image
                // src='").append(getContextPath()).append(
                // "/images/person-icon.gif'/>").append(" ");
                printPersonHomePage(personFunction.getPerson(), buffer);
                buffer.append(" (");
                buffer.append(personFunction.getBeginDate().toString()).append(" - ");
                if (personFunction.getEndDate() != null) {
                    buffer.append(personFunction.getEndDate().toString());
                }
                buffer.append(")").append("</li>");
            }
            buffer.append("</ul>");
        }
    }

    private SortedSet<Function> getSortFunctionList(Unit unit) {
        SortedSet<Function> functions = unit.getOrderedFunctions();

        Iterator<Function> iterator = functions.iterator();
        while (iterator.hasNext()) {
            Function function = iterator.next();

            if (function.isVirtual()) {
                iterator.remove();
            }
        }

        return functions;
    }

    public ExecutionYear getExecutionYear(String executionYear) throws FenixServiceException {

        ExecutionYear iExecutionYear = ReadExecutionYearsService.run(executionYear);
        return iExecutionYear;
    }

    public SortedSet<PersonFunction> getValidPersonFunction(ExecutionYear iExecutionYear, Function function) {
        SortedSet<PersonFunction> personFunctions = new TreeSet<PersonFunction>(PersonFunction.COMPARATOR_BY_PERSON_NAME);
        for (PersonFunction personFunction : PersonFunction.getPersonFunctions(function)) {
            if (personFunction
                    .belongsToPeriod(iExecutionYear.getBeginDateYearMonthDay(), iExecutionYear.getEndDateYearMonthDay())) {
                personFunctions.add(personFunction);
            }
        }
        return personFunctions;
    }

    public List<SelectItem> getListingType() {
        List<SelectItem> list = new ArrayList<SelectItem>();

        SelectItem selectItem = new SelectItem();
        selectItem.setLabel("Funcionários");
        selectItem.setValue("0");
        SelectItem selectItem2 = new SelectItem();
        selectItem2.setLabel("Cargos de Gestão");
        selectItem2.setValue("1");

        list.add(selectItem);
        list.add(selectItem2);

        addDefaultSelectedItem(list);

        return list;
    }

    private void addDefaultSelectedItem(List<SelectItem> list) {
        SelectItem firstItem = new SelectItem();
        firstItem.setLabel(BundleUtil.getString(Bundle.MESSAGING, "label.find.organization.listing.type.default"));
        firstItem.setValue("#");
        list.add(0, firstItem);
    }

    public Unit getUnit() throws FenixServiceException {
        if (parentUnit == null) {
            this.parentUnit = (Unit) FenixFramework.getDomainObject((String) getUnitIDHidden().getValue());
        }
        return parentUnit;
    }

    public void setUnit(Unit unit) {
        this.parentUnit = unit;
    }

    public Integer getPersonID() {
        return personID;
    }

    public void setPersonID(Integer personID) {
        this.personID = personID;
    }

    @Override
    protected String getRequestParameter(String parameterName) {
        return (String) FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get(parameterName);
    }

    public String getListType() {
        return listType;
    }

    public void setListType(String listType) {
        this.listType = listType;
    }

    public HtmlInputHidden getUnitIDHidden() {
        if (this.unitIDHidden == null) {
            this.unitIDHidden = new HtmlInputHidden();
        }
        return unitIDHidden;
    }

    public void setUnitIDHidden(HtmlInputHidden unitIDHidden) {
        this.unitIDHidden = unitIDHidden;
    }

    public String getChoosenExecutionYearID() {
        return choosenExecutionYearID;
    }

    public void setChoosenExecutionYearID(String choosenExecutionYearID) {
        this.choosenExecutionYearID = choosenExecutionYearID;
    }

}