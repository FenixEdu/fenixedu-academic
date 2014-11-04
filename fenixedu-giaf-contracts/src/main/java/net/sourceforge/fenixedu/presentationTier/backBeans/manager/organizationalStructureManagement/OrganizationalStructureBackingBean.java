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
 * Created on Nov 21, 2005
 *	by mrsp
 */
package org.fenixedu.academic.ui.faces.bean.manager.organizationalStructureManagement;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import javax.faces.component.html.HtmlInputHidden;
import javax.faces.model.SelectItem;

import org.fenixedu.academic.service.services.exceptions.FenixServiceException;
import org.fenixedu.academic.service.services.manager.organizationalStructureManagement.AddParentInherentFunction;
import org.fenixedu.academic.service.services.manager.organizationalStructureManagement.AssociateParentUnit;
import org.fenixedu.academic.service.services.manager.organizationalStructureManagement.CreateFunction;
import org.fenixedu.academic.service.services.manager.organizationalStructureManagement.CreateUnit;
import org.fenixedu.academic.service.services.manager.organizationalStructureManagement.DeleteFunction;
import org.fenixedu.academic.service.services.manager.organizationalStructureManagement.DeleteUnit;
import org.fenixedu.academic.service.services.manager.organizationalStructureManagement.DisassociateParentUnit;
import org.fenixedu.academic.service.services.manager.organizationalStructureManagement.EditFunction;
import org.fenixedu.academic.service.services.manager.organizationalStructureManagement.EditUnit;
import org.fenixedu.academic.service.services.manager.organizationalStructureManagement.RemoveParentInherentFunction;
import org.fenixedu.academic.service.services.manager.organizationalStructureManagement.SetRootUnit;
import org.fenixedu.academic.domain.Country;
import org.fenixedu.academic.domain.Degree;
import org.fenixedu.academic.domain.Department;
import org.fenixedu.academic.domain.administrativeOffice.AdministrativeOffice;
import org.fenixedu.academic.domain.degree.DegreeType;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.organizationalStructure.Accountability;
import org.fenixedu.academic.domain.organizationalStructure.AccountabilityType;
import org.fenixedu.academic.domain.organizationalStructure.AccountabilityTypeEnum;
import org.fenixedu.academic.domain.organizationalStructure.CountryUnit;
import org.fenixedu.academic.domain.organizationalStructure.Function;
import org.fenixedu.academic.domain.organizationalStructure.FunctionType;
import org.fenixedu.academic.domain.organizationalStructure.PartyTypeEnum;
import org.fenixedu.academic.domain.organizationalStructure.PersonFunction;
import org.fenixedu.academic.domain.organizationalStructure.Unit;
import org.fenixedu.academic.domain.organizationalStructure.UnitClassification;
import org.fenixedu.academic.domain.organizationalStructure.UnitUtils;
import org.fenixedu.academic.ui.faces.bean.base.FenixBackingBean;
import org.fenixedu.academic.util.Bundle;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.lang.StringUtils;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.fenixedu.spaces.domain.Space;
import org.joda.time.YearMonthDay;

import pt.ist.fenixframework.FenixFramework;
import pt.utl.ist.fenix.tools.util.DateFormatUtil;
import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

public class OrganizationalStructureBackingBean extends FenixBackingBean {

    private static final String ORG_UNIT_PACKAGE =
            "org.fenixedu.academic.service.services.manager.organizationalStructureManagement";

    private String unitName, unitCostCenter, unitTypeName, unitBeginDate, unitEndDate, unitAcronym, administrativeOfficeID;

    private String functionName, functionTypeName, functionBeginDate, functionEndDate, unitWebAddress, unitRelationTypeValue;

    private String listingTypeValueToUnits, listingTypeValueToFunctions, departmentID, degreeID, unitClassificationName,
            campusID;

    private String functionNameEn, unitNameEn;

    private String unitNameCard;

    private String principalFunctionID;

    private String accountabilityID;

    private Unit unit, chooseUnit;

    private Function function;

    private HtmlInputHidden unitIDHidden, chooseUnitIDHidden, functionIDHidden, listingTypeValueToFunctionsHidden,
            listingTypeValueToUnitsHidden;

    private HashMap<String, String> unitRelationsAccountabilityTypes;

    private Boolean toRemoveParentUnit, viewExternalUnits, institutionUnit, externalInstitutionUnit, earthUnit,
            canBeResponsibleOfSpaces;

    private Boolean viewUnitsWithoutParents;

    private String selectedCountry;

    private final String NOT_SELECTED_VALUE = "-1";

    private final String NOT_SELECTED_DESCRIPTION = "Seleccione um item";

    public OrganizationalStructureBackingBean() {
        if (!StringUtils.isEmpty(getRequestParameter("unitID"))) {
            getUnitIDHidden().setValue(getRequestParameter("unitID").toString());
        }
        if (!StringUtils.isEmpty(getRequestParameter("chooseUnitID"))) {
            getChooseUnitIDHidden().setValue(getRequestParameter("chooseUnitID").toString());
        }
        if (!StringUtils.isEmpty(getRequestParameter("functionID"))) {
            getFunctionIDHidden().setValue(getRequestParameter("functionID").toString());
        }
        if (!StringUtils.isEmpty(getRequestParameter("principalFunctionID"))) {
            this.principalFunctionID = getRequestParameter("principalFunctionID").toString();
        }
        if (!StringUtils.isEmpty(getRequestParameter("accountabilityID"))) {
            this.accountabilityID = getRequestParameter("accountabilityID").toString();
        }
        if (!StringUtils.isEmpty(getRequestParameter("isToRemoveParentUnit"))) {
            this.toRemoveParentUnit = Boolean.valueOf(getRequestParameter("isToRemoveParentUnit").toString());
        } else {
            this.toRemoveParentUnit = false;
        }
        if (getViewExternalUnits() == null) {
            setViewExternalUnits(Boolean.FALSE);
        }
        if (getViewUnitsWithoutParents() == null) {
            setViewUnitsWithoutParents(Boolean.FALSE);
        }
    }

    public List<Unit> getAllSubUnits() throws FenixServiceException {
        YearMonthDay currentDate = new YearMonthDay();
        boolean active = this.getListingTypeValueToUnitsHidden().getValue().toString().equals("0");
        return getSubUnits(active, this.getUnit(), currentDate);
    }

    public List<Function> getAllNonInherentFunctions() throws FenixServiceException {
        if (this.getUnit() != null) {
            List<Function> allNonInherentFunctions = new ArrayList<Function>();
            YearMonthDay currentDate = new YearMonthDay();
            for (Function function : getUnit().getFunctionsSet()) {
                if (!function.isInherentFunction()
                        && ((this.getListingTypeValueToFunctionsHidden().getValue().toString().equals("0") && function
                                .isActive(currentDate)) || (this.getListingTypeValueToFunctionsHidden().getValue().toString()
                                .equals("1") && !function.isActive(currentDate)))) {
                    allNonInherentFunctions.add(function);
                }
            }
            return allNonInherentFunctions;
        } else {
            return new ArrayList();
        }
    }

    public List<Function> getAllInherentFunctions() throws FenixServiceException {
        if (this.getUnit() != null) {
            List<Function> allInherentFunctions = new ArrayList<Function>();
            YearMonthDay currentDate = new YearMonthDay();
            for (Function function : getUnit().getFunctionsSet()) {
                if (function.isInherentFunction()
                        && ((this.getListingTypeValueToFunctionsHidden().getValue().toString().equals("0") && function
                                .isActive(currentDate)) || (this.getListingTypeValueToFunctionsHidden().getValue().toString()
                                .equals("1") && !function.isActive(currentDate)))) {
                    allInherentFunctions.add(function);
                }
            }
            return allInherentFunctions;
        } else {
            return new ArrayList();
        }
    }

    public String getAllUnitsToChooseParentUnit() throws FenixServiceException {
        StringBuilder buffer = new StringBuilder();
        YearMonthDay currentDate = new YearMonthDay();
        if (this.getUnit().isNoOfficialExternal()) {
            buffer.append("<ul class='padding1 nobullet'>");
            getSubUnitsListToChooseParentUnit(UnitUtils.readExternalInstitutionUnit(), null, buffer, currentDate);
            closeULTag(buffer);
        } else {
            Unit earthUnit = UnitUtils.readEarthUnit();
            if (!this.getUnit().equals(earthUnit)) {
                getSubUnitsListToChooseParentUnit(earthUnit, null, buffer, currentDate);
            }
            closeULTag(buffer);
        }
        return buffer.toString();
    }

    private void getSubUnitsListToChooseParentUnit(Unit parentUnit, Unit parentUnitParent, StringBuilder buffer,
            YearMonthDay currentDate) throws FenixServiceException {

        openLITag(buffer);

        List<Unit> subUnits = null;
        if (this.getUnit().isActive(currentDate)) {
            subUnits = getSubUnits(true, parentUnit, currentDate);
        } else {
            subUnits = getSubUnits(false, parentUnit, currentDate);
        }

        if (!subUnits.isEmpty()) {
            putImage(parentUnit, buffer, parentUnitParent);
        }

        buffer.append("<a href=\"").append(getContextPath()).append("/manager/organizationalStructureManagament/")
                .append("chooseParentUnit.faces?").append("unitID=").append(this.getUnit().getExternalId())
                .append("&chooseUnitID=").append(parentUnit.getExternalId()).append("\">")
                .append(parentUnit.getNameWithAcronym()).append("</a>").append("</li>");

        if (!subUnits.isEmpty()) {
            openULTag(parentUnit, buffer, parentUnitParent);
            Collections.sort(subUnits, Unit.COMPARATOR_BY_NAME_AND_ID);
        }

        for (Unit subUnit : subUnits) {
            if (!subUnit.equals(this.getUnit())) {
                getSubUnitsListToChooseParentUnit(subUnit, parentUnit, buffer, currentDate);
            }
        }

        if (!subUnits.isEmpty()) {
            closeULTag(buffer);
        }
    }

    private List<Unit> getSubUnits(boolean active, Unit unit, YearMonthDay currentDate) {
        if (unit != null) {
            List<Unit> subUnits = (active) ? unit.getActiveSubUnits(currentDate) : unit.getInactiveSubUnits(currentDate);
            if (!subUnits.isEmpty()) {
                Collections.sort(subUnits, Unit.COMPARATOR_BY_NAME_AND_ID);
            }
            return subUnits;
        } else {
            // HACK: What is this suposed to do, Patch??
            return new ArrayList<>();
        }
    }

    private List<Unit> getAllSubUnits(boolean active, Unit unit, YearMonthDay currentDate) {
        List<Unit> subUnits = (active) ? unit.getAllActiveSubUnits(currentDate) : unit.getAllInactiveSubUnits(currentDate);
        if (!subUnits.isEmpty()) {
            Collections.sort(subUnits, Unit.COMPARATOR_BY_NAME_AND_ID);
        }
        return subUnits;
    }

    public String prepareListAllUnits() {
        HtmlInputHidden hidden = new HtmlInputHidden();
        hidden.setValue("0");
        this.setListingTypeValueToUnitsHidden(hidden);
        this.setListingTypeValueToUnits("0");
        this.setViewExternalUnits(Boolean.FALSE);
        return "listAllUnits";
    }

    public String getUnits() throws FenixServiceException {

        StringBuilder buffer = new StringBuilder();
        List<Unit> unitsToShow = null;
        YearMonthDay currentDate = new YearMonthDay();

        if (getViewExternalUnits()) {
            unitsToShow = new ArrayList<Unit>();
            unitsToShow.add(UnitUtils.readExternalInstitutionUnit());

        } else if (getViewUnitsWithoutParents()) {
            unitsToShow = UnitUtils.readAllUnitsWithoutParents();

        } else {
            unitsToShow = new ArrayList<Unit>();
            unitsToShow.add(UnitUtils.readEarthUnit());
        }

        Collections.sort(unitsToShow, Unit.COMPARATOR_BY_NAME_AND_ID);
        for (Unit unit : unitsToShow) {
            boolean active = this.getListingTypeValueToUnitsHidden().getValue().toString().equals("0");
            if (active) {
                if (unit.isActive(currentDate) || !getAllSubUnits(active, unit, currentDate).isEmpty()) {
                    buffer.append("<ul class='padding1 nobullet'>");
                    getSubUnitsList(unit, null, getSubUnits(active, unit, currentDate), buffer, currentDate, active);
                    buffer.append("</ul>");
                }
            } else {
                if (!unit.isActive(currentDate) || !getAllSubUnits(active, unit, currentDate).isEmpty()) {
                    buffer.append("<ul class='padding1 nobullet'>");
                    getSubUnitsList(unit, null, getSubUnits(active, unit, currentDate), buffer, currentDate, active);
                    buffer.append("</ul>");
                }
            }
        }
        return buffer.toString();
    }

    private void getSubUnitsList(Unit parentUnit, Unit parentUnitParent, List<Unit> subUnits, StringBuilder buffer,
            YearMonthDay currentDate, boolean active) {

        openLITag(buffer);

        if (!subUnits.isEmpty()) {
            putImage(parentUnit, buffer, parentUnitParent);
        }

        buffer.append("<a href=\"").append(getContextPath()).append("/manager/organizationalStructureManagament/")
                .append("unitDetails.faces?").append("unitID=").append(parentUnit.getExternalId()).append("\">")
                .append(parentUnit.getNameWithAcronym()).append("</a>").append("</li>");

        if (!subUnits.isEmpty()) {
            openULTag(parentUnit, buffer, parentUnitParent);
            Collections.sort(subUnits, Unit.COMPARATOR_BY_NAME_AND_ID);
        }

        for (Unit subUnit : subUnits) {
            getSubUnitsList(subUnit, parentUnit, getSubUnits(active, subUnit, currentDate), buffer, currentDate, active);
        }

        if (!subUnits.isEmpty()) {
            closeULTag(buffer);
        }
    }

    public String getUnitsToChoosePrincipalFunction() throws FenixServiceException {

        YearMonthDay currentDate = new YearMonthDay();
        StringBuilder buffer = new StringBuilder();
        buffer.append("<ul class='padding1 nobullet'>");
        getSubUnitsListToChoosePrincipalFunction(UnitUtils.readInstitutionUnit(), null, buffer, currentDate);
        buffer.append("</ul>");
        return buffer.toString();
    }

    private void getSubUnitsListToChoosePrincipalFunction(Unit parentUnit, Unit parentUnitParent, StringBuilder buffer,
            YearMonthDay currentDate) throws FenixServiceException {

        openLITag(buffer);

        List<Unit> subUnits = null;
        if (this.getFunction().isActive(currentDate)) {
            subUnits = getSubUnits(true, parentUnit, currentDate);
        } else {
            subUnits = getSubUnits(false, parentUnit, currentDate);
        }

        if (!subUnits.isEmpty()) {
            putImage(parentUnit, buffer, parentUnitParent);
        }

        buffer.append("<a href=\"").append(getContextPath()).append("/manager/organizationalStructureManagament/")
                .append("chooseFunction.faces?").append("unitID=").append(this.getUnit().getExternalId())
                .append("&chooseUnitID=").append(parentUnit.getExternalId()).append("&functionID=")
                .append(this.getFunction().getExternalId()).append("\">").append(parentUnit.getNameWithAcronym()).append("</a>")
                .append("</li>");

        if (!subUnits.isEmpty()) {
            openULTag(parentUnit, buffer, parentUnitParent);
            Collections.sort(subUnits, Unit.COMPARATOR_BY_NAME_AND_ID);
        }

        for (Unit subUnit : subUnits) {
            getSubUnitsListToChoosePrincipalFunction(subUnit, parentUnit, buffer, currentDate);
        }

        if (!subUnits.isEmpty()) {
            closeULTag(buffer);
        }
    }

    public List<SelectItem> getValidUnitType() {
        List<SelectItem> list = new ArrayList<SelectItem>();

        SelectItem selectItem = null;
        for (PartyTypeEnum type : PartyTypeEnum.values()) {
            selectItem = new SelectItem();
            selectItem.setLabel(BundleUtil.getString(Bundle.ENUMERATION, type.getName()));
            selectItem.setValue(type.getName());
            list.add(selectItem);
        }
        Collections.sort(list, new BeanComparator("label"));

        addDefaultSelectedItem(list);

        return list;
    }

    public List<SelectItem> getValidUnitClassifications() {
        List<SelectItem> list = new ArrayList<SelectItem>();

        SelectItem selectItem = null;
        for (UnitClassification classification : UnitClassification.values()) {
            selectItem = new SelectItem();
            selectItem.setLabel(BundleUtil.getString(Bundle.ENUMERATION, classification.getName()));
            selectItem.setValue(classification.getName());
            list.add(selectItem);
        }
        Collections.sort(list, new BeanComparator("label"));

        addDefaultSelectedItem(list);

        return list;
    }

    public List<SelectItem> getDepartments() throws FenixServiceException {
        List<SelectItem> list = new ArrayList<SelectItem>();
        SelectItem selectItem = null;

        Collection<Department> allDepartments = rootDomainObject.getDepartmentsSet();

        for (Department department : allDepartments) {
            selectItem = new SelectItem();
            selectItem.setLabel(department.getRealName());
            selectItem.setValue(department.getExternalId().toString());
            list.add(selectItem);
        }

        Collections.sort(list, new BeanComparator("label"));
        addDefaultSelectedItem(list);
        return list;
    }

    public List<SelectItem> getAdministrativeOffices() throws FenixServiceException {
        List<SelectItem> list = new ArrayList<SelectItem>();
        SelectItem selectItem = null;

        Collection<AdministrativeOffice> allAdministrativeOffices = rootDomainObject.getAdministrativeOfficesSet();

        for (AdministrativeOffice administrativeOffice : allAdministrativeOffices) {
            selectItem = new SelectItem();
            String name =
                    administrativeOffice.getAdministrativeOfficeType().getClass().getSimpleName() + "."
                            + administrativeOffice.getAdministrativeOfficeType().getName();
            selectItem.setLabel(BundleUtil.getString(Bundle.ENUMERATION, name));
            selectItem.setValue(administrativeOffice.getExternalId().toString());
            list.add(selectItem);
        }

        Collections.sort(list, new BeanComparator("label"));
        addDefaultSelectedItem(list);
        return list;
    }

    public List<SelectItem> getDegrees() throws FenixServiceException {
        List<SelectItem> list = new ArrayList<SelectItem>();
        SelectItem selectItem = null;

        List<Degree> allDegrees = Degree.readNotEmptyDegrees();

        for (Degree degree : allDegrees) {
            selectItem = new SelectItem();

            if (!degree.isBolonhaDegree()) {
                if (degree.getDegreeType().equals(DegreeType.DEGREE)) {
                    selectItem.setLabel("(L) " + degree.getNome());
                } else if (degree.getDegreeType().equals(DegreeType.MASTER_DEGREE)) {
                    selectItem.setLabel("(M) " + degree.getNome());
                }
            } else if (degree.isBolonhaDegree()) {
                if (degree.getDegreeType().equals(DegreeType.BOLONHA_DEGREE)) {
                    selectItem.setLabel("(L-B) " + degree.getNome());
                } else if (degree.getDegreeType().equals(DegreeType.BOLONHA_MASTER_DEGREE)) {
                    selectItem.setLabel("(M-B) " + degree.getNome());
                } else if (degree.getDegreeType().equals(DegreeType.BOLONHA_INTEGRATED_MASTER_DEGREE)) {
                    selectItem.setLabel("(MI) " + degree.getNome());
                } else if (degree.getDegreeType().equals(DegreeType.BOLONHA_ADVANCED_FORMATION_DIPLOMA)) {
                    selectItem.setLabel("(DFA) " + degree.getNome());
                } else if (degree.getDegreeType().equals(DegreeType.BOLONHA_ADVANCED_SPECIALIZATION_DIPLOMA)) {
                    selectItem.setLabel("(DEA) " + degree.getNome());
                } else if (degree.getDegreeType().equals(DegreeType.BOLONHA_SPECIALIZATION_DEGREE)) {
                    selectItem.setLabel("(SD) " + degree.getNome());
                }
            }

            selectItem.setValue(degree.getExternalId().toString());
            list.add(selectItem);
        }

        Collections.sort(list, new BeanComparator("label"));
        addDefaultSelectedItem(list);
        return list;
    }

    public List<SelectItem> getListingTypeToUnits() {
        List<SelectItem> list = new ArrayList<SelectItem>();

        SelectItem selectItem = new SelectItem();
        selectItem.setLabel("Activas");
        selectItem.setValue("0");
        SelectItem selectItem2 = new SelectItem();
        selectItem2.setLabel("Inactivas");
        selectItem2.setValue("1");

        list.add(selectItem);
        list.add(selectItem2);

        return list;
    }

    public List<SelectItem> getCampuss() {
        List<SelectItem> list = new ArrayList<SelectItem>();
        Set<Space> activeCampus = Space.getAllCampus();
        for (Space campus : activeCampus) {
            SelectItem selectItem = new SelectItem();
            selectItem.setLabel(campus.getName());
            selectItem.setValue(campus.getExternalId().toString());
            list.add(selectItem);
        }
        Collections.sort(list, new BeanComparator("label"));
        addDefaultSelectedItem(list);
        return list;
    }

    public List<SelectItem> getListingTypeToFunctions() {
        List<SelectItem> list = new ArrayList<SelectItem>();

        SelectItem selectItem = new SelectItem();
        selectItem.setLabel("Activos");
        selectItem.setValue("0");
        SelectItem selectItem2 = new SelectItem();
        selectItem2.setLabel("Inactivos");
        selectItem2.setValue("1");

        list.add(selectItem);
        list.add(selectItem2);

        return list;
    }

    public List<SelectItem> getValidFunctionType() {
        List<SelectItem> list = new ArrayList<SelectItem>();

        SelectItem selectItem = null;
        for (FunctionType type : FunctionType.values()) {
            selectItem = new SelectItem();
            selectItem.setLabel(BundleUtil.getString(Bundle.ENUMERATION, type.getName()));
            selectItem.setValue(type.getName());
            list.add(selectItem);
        }
        Collections.sort(list, new BeanComparator("label"));

        addDefaultSelectedItem(list);

        return list;
    }

    public List<SelectItem> getUnitRelationTypes() {
        List<SelectItem> list = new ArrayList<SelectItem>();

        SelectItem selectItem = null;
        for (AccountabilityTypeEnum type : AccountabilityTypeEnum.values()) {
            if (type.equals(AccountabilityTypeEnum.ACADEMIC_STRUCTURE)
                    || type.equals(AccountabilityTypeEnum.ORGANIZATIONAL_STRUCTURE)
                    || type.equals(AccountabilityTypeEnum.ADMINISTRATIVE_STRUCTURE)
                    || type.equals(AccountabilityTypeEnum.GEOGRAPHIC)) {

                selectItem = new SelectItem();
                selectItem.setLabel(BundleUtil.getString(Bundle.ENUMERATION, type.getName()));
                selectItem.setValue(type.getName());
                list.add(selectItem);
            }
        }
        Collections.sort(list, new BeanComparator("label"));

        addDefaultSelectedItem(list);

        return list;
    }

    private void addDefaultSelectedItem(List<SelectItem> list) {
        SelectItem firstItem = new SelectItem();
        firstItem.setLabel(BundleUtil.getString(Bundle.ENUMERATION, "dropDown.Default"));
        firstItem.setValue("#");
        list.add(0, firstItem);
    }

    public String createTopUnit() throws FenixServiceException {

        PrepareDatesResult datesResult = prepareDates(this.getUnitBeginDate(), this.getUnitEndDate());
        if (datesResult.isTest()) {
            return "";
        }

        CreateNewUnitParameters parameters = new CreateNewUnitParameters(this, 1);

        MultiLanguageString unitName =
                new MultiLanguageString(MultiLanguageString.pt, this.getUnitName()).with(MultiLanguageString.en,
                        this.getUnitNameEn());

        try {
            CreateUnit.run(null, unitName, this.getUnitNameCard(), this.getUnitCostCenter(), this.getUnitAcronym(),
                    datesResult.getBeginDate(), datesResult.getEndDate(), getUnitType(), parameters.getDepartmentID(),
                    parameters.getDegreeID(), parameters.getAdministrativeOfficeID(), null, this.getUnitWebAddress(),
                    this.getUnitClassification(), this.getCanBeResponsibleOfSpaces(), parameters.getCampusID());
        } catch (DomainException e) {
            setErrorMessage(e.getMessage());
            return "";
        }

        return "listAllUnits";
    }

    public String createSubUnit() throws FenixServiceException {

        if (getUnitRelationTypeValue().equals("#")) {
            addErrorMessage(BundleUtil.getString(Bundle.MANAGER, "error.no.unit.relation.type"));
            return "";
        }

        PrepareDatesResult datesResult = prepareDates(this.getUnitBeginDate(), this.getUnitEndDate());
        if (datesResult.isTest()) {
            return "";
        }

        AccountabilityType accountabilityType =
                AccountabilityType.readByType(AccountabilityTypeEnum.valueOf(getUnitRelationTypeValue()));

        CreateNewUnitParameters parameters = new CreateNewUnitParameters(this, 1);

        MultiLanguageString unitName = new MultiLanguageString();
        unitName = unitName.with(MultiLanguageString.pt, this.getUnitName());
        unitName = unitName.with(MultiLanguageString.en, this.getUnitNameEn());

        try {
            CreateUnit.run(this.getUnit(), unitName, this.getUnitNameCard(), this.getUnitCostCenter(), this.getUnitAcronym(),
                    datesResult.getBeginDate(), datesResult.getEndDate(), getUnitType(), parameters.getDepartmentID(),
                    parameters.getDegreeID(), parameters.getAdministrativeOfficeID(), accountabilityType,
                    this.getUnitWebAddress(), this.getUnitClassification(), this.getCanBeResponsibleOfSpaces(),
                    parameters.getCampusID());
        } catch (DomainException e) {
            setErrorMessage(e.getMessage());
            return "";
        }

        return "backToUnitDetails";
    }

    public String editUnit() throws FenixServiceException {
        PrepareDatesResult datesResult = prepareDates(this.getUnitBeginDate(), this.getUnitEndDate());
        if (datesResult.isTest()) {
            return "";
        }

        CreateNewUnitParameters parameters = new CreateNewUnitParameters(this, 1);

        MultiLanguageString unitName = new MultiLanguageString();
        unitName = unitName.with(MultiLanguageString.pt, this.getUnitName());
        unitName = unitName.with(MultiLanguageString.en, this.getUnitNameEn());

        try {
            EditUnit.run(this.getChooseUnit().getExternalId(), unitName, this.getUnitNameCard(), this.getUnitCostCenter(),
                    this.getUnitAcronym(), datesResult.getBeginDate(), datesResult.getEndDate(), parameters.getDepartmentID(),
                    parameters.getDegreeID(), parameters.getAdministrativeOfficeID(), this.getUnitWebAddress(),
                    this.getUnitClassification(), this.getCanBeResponsibleOfSpaces(), parameters.getCampusID());
        } catch (DomainException e) {
            setErrorMessage(e.getMessage());
            return "";
        }

        return "backToUnitDetails";
    }

    private PartyTypeEnum getUnitType() throws FenixServiceException {
        PartyTypeEnum type = null;
        if (!this.getUnitTypeName().equals("#")) {
            type = PartyTypeEnum.valueOf(this.getUnitTypeName());
        }
        return type;
    }

    private UnitClassification getUnitClassification() throws FenixServiceException {
        UnitClassification classification = null;
        if (!this.getUnitClassificationName().equals("#")) {
            classification = UnitClassification.valueOf(this.getUnitClassificationName());
        }
        return classification;
    }

    public String associateParentUnit() throws FenixServiceException {
        if (getUnitRelationTypeValue().equals("#")) {
            addErrorMessage(BundleUtil.getString(Bundle.MANAGER, "error.no.unit.relation.type"));
            return "";
        }

        AccountabilityType accountabilityType =
                AccountabilityType.readByType(AccountabilityTypeEnum.valueOf(getUnitRelationTypeValue()));

        try {
            AssociateParentUnit.run(this.getUnit().getExternalId(), this.getChooseUnit().getExternalId(), accountabilityType);
        } catch (DomainException e) {
            setErrorMessage(e.getMessage());
            return "";
        }

        return "backToUnitDetails";
    }

    public String disassociateParentUnit() throws FenixServiceException {
        try {
            DisassociateParentUnit.run(this.getAccountabilityID());
        } catch (DomainException e) {
            setErrorMessage(e.getMessage());
            return "";
        }

        return "backToUnitDetails";
    }

    private FunctionType getFunctionType() throws FenixServiceException {
        FunctionType type = null;
        if (!this.getFunctionTypeName().equals("#")) {
            type = FunctionType.valueOf(this.getFunctionTypeName());
        }
        return type;
    }

    public String createFunction() throws FenixServiceException {

        PrepareDatesResult datesResult = prepareDates(this.getFunctionBeginDate(), this.getFunctionEndDate());
        if (datesResult.isTest()) {
            return "";
        }

        FunctionType type = getFunctionType();

        MultiLanguageString functionName = new MultiLanguageString();
        functionName = functionName.with(MultiLanguageString.pt, this.getFunctionName());
        functionName = functionName.with(MultiLanguageString.en, this.getFunctionNameEn());

        try {
            CreateFunction.run(functionName, datesResult.getBeginDate(), datesResult.getEndDate(), type, this.getUnit()
                    .getExternalId());
        } catch (DomainException e) {
            setErrorMessage(e.getMessage());
            return "";
        }

        return "backToUnitDetails";
    }

    public String editFunction() throws FenixServiceException {

        PrepareDatesResult datesResult = prepareDates(this.getFunctionBeginDate(), this.getFunctionEndDate());

        if (datesResult.isTest()) {
            return "";
        }

        FunctionType type = getFunctionType();

        MultiLanguageString functionName = new MultiLanguageString();
        functionName = functionName.with(MultiLanguageString.pt, this.getFunctionName());
        functionName = functionName.with(MultiLanguageString.en, this.getFunctionNameEn());

        try {
            EditFunction.run(this.getFunction().getExternalId(), functionName, datesResult.getBeginDate(),
                    datesResult.getEndDate(), type);
        } catch (DomainException e) {
            setErrorMessage(e.getMessage());
            return "";
        }

        return "backToUnitDetails";
    }

    public String associateInherentParentFunction() throws FenixServiceException {
        Function function = this.getFunction();
        try {
            AddParentInherentFunction.run(function.getExternalId(), this.principalFunctionID);
        } catch (DomainException e) {
            setErrorMessage(e.getMessage());
            return "";
        }

        return "backToUnitDetails";
    }

    public String prepareAssociateInherentParentFunction() throws FenixServiceException {

        Function function = this.getFunction();
        if (!PersonFunction.getPersonFunctions(function).isEmpty()) {
            setErrorMessage("error.becomeInherent");
            return "";
        }
        return "chooseInherentParentFunction";
    }

    public String disassociateInherentFunction() throws FenixServiceException {

        Function function = this.getFunction();

        try {
            RemoveParentInherentFunction.run(function.getExternalId());
        } catch (FenixServiceException e) {
            setErrorMessage(e.getMessage());
            return "";
        }

        return "";
    }

    public String deleteSubUnit() {

        try {
            DeleteUnit.run(this.getChooseUnitIDHidden().getValue().toString());

        } catch (FenixServiceException e) {
            setErrorMessage(e.getMessage());
        } catch (DomainException e1) {
            setErrorMessage(e1.getMessage());
            return "";
        }
        return "";
    }

    public String deleteUnit() {

        try {
            DeleteUnit.run(this.getChooseUnitIDHidden().getValue().toString());

        } catch (FenixServiceException e) {
            setErrorMessage(e.getMessage());
            return "";
        } catch (DomainException e1) {
            setErrorMessage(e1.getMessage());
            return "";
        }
        return "listAllUnits";
    }

    public String deleteFunction() {

        try {
            DeleteFunction.run(this.getFunctionIDHidden().getValue().toString());

        } catch (FenixServiceException e) {
            setErrorMessage(e.getMessage());
        } catch (DomainException e1) {
            setErrorMessage(e1.getMessage());
        }
        return "";
    }

    private PrepareDatesResult prepareDates(String beginDate, String endDate) throws FenixServiceException {
        boolean test = false;
        DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        Date beginDate_ = null, endDate_ = null;
        try {
            beginDate_ = format.parse(beginDate);
            if (endDate != null && !endDate.equals("")) {
                endDate_ = format.parse(endDate);
            }
        } catch (ParseException exception) {
            setErrorMessage("error.date.format");
            test = true;
        }

        return new PrepareDatesResult(test, beginDate_, endDate_);
    }

    private void openULTag(Unit parentUnit, StringBuilder buffer, Unit parentUnitParent) {
        buffer.append("<ul class='mvert0 nobullet' id=\"").append("aa").append(parentUnit.getExternalId())
                .append((parentUnitParent != null) ? parentUnitParent.getExternalId() : "").append("\" ")
                .append("style='display:none'>\r\n");
    }

    private void putImage(Unit parentUnit, StringBuilder buffer, Unit parentUnitParent) {

        buffer.append("<img ").append("src='").append(getContextPath()).append("/images/toggle_plus10.gif' id=\"")
                .append(parentUnit.getExternalId()).append((parentUnitParent != null) ? parentUnitParent.getExternalId() : "")
                .append("\" ").append("indexed='true' onClick=\"").append("check(document.getElementById('").append("aa")
                .append(parentUnit.getExternalId()).append((parentUnitParent != null) ? parentUnitParent.getExternalId() : "")
                .append("'),document.getElementById('").append(parentUnit.getExternalId())
                .append((parentUnitParent != null) ? parentUnitParent.getExternalId() : "").append("'));return false;")
                .append("\"> ");
    }

    private void closeULTag(StringBuilder buffer) {
        buffer.append("</ul>");
    }

    private void openLITag(StringBuilder buffer) {
        buffer.append("<li>");
    }

    public String getUnitCostCenter() throws FenixServiceException {
        if (this.unitCostCenter == null && this.getChooseUnit() != null && this.getChooseUnit().getCostCenterCode() != null) {
            this.unitCostCenter = this.getChooseUnit().getCostCenterCode().toString();
        }
        return unitCostCenter;
    }

    public void setUnitCostCenter(String costCenter) {
        this.unitCostCenter = costCenter;
    }

    public String getUnitBeginDate() throws FenixServiceException {
        if (this.unitBeginDate == null && this.getChooseUnit() != null) {
            this.unitBeginDate = processDate(this.getChooseUnit().getBeginDate());
        }
        return unitBeginDate;
    }

    public void setUnitBeginDate(String unitBeginDate) {
        this.unitBeginDate = unitBeginDate;
    }

    public String getUnitEndDate() throws FenixServiceException {
        if (this.unitEndDate == null && this.getChooseUnit() != null && this.getChooseUnit().getEndDate() != null) {
            this.unitEndDate = processDate(this.getChooseUnit().getEndDate());
        }
        return unitEndDate;
    }

    public void setUnitEndDate(String unitEndDate) {
        this.unitEndDate = unitEndDate;
    }

    public String getUnitAcronym() throws FenixServiceException {
        if (this.unitAcronym == null && this.getChooseUnit() != null) {
            this.unitAcronym = this.getChooseUnit().getAcronym();
        }
        return unitAcronym;
    }

    public void setUnitAcronym(String unitAcronym) {
        this.unitAcronym = unitAcronym;
    }

    public String getUnitWebAddress() throws FenixServiceException {
        if (this.unitWebAddress == null && this.getChooseUnit() != null) {
            this.unitWebAddress = this.getChooseUnit().getDefaultWebAddressUrl();
        }
        return unitWebAddress;
    }

    public void setUnitWebAddress(String webAddress) {
        this.unitWebAddress = webAddress;
    }

    public String getUnitName() throws FenixServiceException {
        if (this.unitName == null && this.getChooseUnit() != null) {
            this.unitName = this.getChooseUnit().getName();
        }
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public String getUnitTypeName() throws FenixServiceException {
        if (this.unitTypeName == null && this.getChooseUnit() != null && this.getChooseUnit().getType() != null) {
            this.unitTypeName = this.getChooseUnit().getType().getName();
        }
        return unitTypeName;
    }

    public void setUnitTypeName(String unitTypeName) {
        this.unitTypeName = unitTypeName;
    }

    public String getUnitClassificationName() throws FenixServiceException {
        if (this.unitClassificationName == null && this.getChooseUnit() != null
                && this.getChooseUnit().getClassification() != null) {
            this.unitClassificationName = this.getChooseUnit().getClassification().getName();
        }
        return unitClassificationName;
    }

    public void setUnitClassificationName(String unitClassificationName) {
        this.unitClassificationName = unitClassificationName;
    }

    public Unit getUnit() throws FenixServiceException {
        if (this.unit == null && this.getUnitIDHidden() != null && this.getUnitIDHidden().getValue() != null
                && !this.getUnitIDHidden().getValue().equals("")) {

            this.unit = (Unit) FenixFramework.getDomainObject(this.getUnitIDHidden().getValue().toString());
        }
        if (toRemoveParentUnit) {
            getParentUnitsRelationTypes();
        } else {
            getSubUnitsRelationTypes();
        }
        return unit;
    }

    public List<Accountability> getParentAccountabilities() throws FenixServiceException {
        return new ArrayList<Accountability>(getUnit().getParentAccountabilitiesByParentClass(Unit.class));
    }

    public List<Accountability> getChildAccountabilities() throws FenixServiceException {
        return new ArrayList<Accountability>(getUnit().getChildAccountabilitiesByChildClass(Unit.class));
    }

    private void getParentUnitsRelationTypes() throws FenixServiceException {
        if (unit != null) {
            getUnitRelationsAccountabilityTypes().clear();
            for (Accountability accountability : unit.getParentsSet()) {
                String accountabilityTypeName = accountability.getAccountabilityType().getType().getName();
                if (accountability.getParentParty().isUnit()) {
                    getUnitRelationsAccountabilityTypes().put(accountability.getExternalId(),
                            BundleUtil.getString(Bundle.ENUMERATION, accountabilityTypeName));
                }
            }
        }
    }

    private void getSubUnitsRelationTypes() throws FenixServiceException {
        if (unit != null) {
            getUnitRelationsAccountabilityTypes().clear();
            for (Accountability accountability : unit.getChildsSet()) {
                String accountabilityTypeName = accountability.getAccountabilityType().getType().getName();
                if (accountability.getChildParty().isUnit()) {
                    String subUnitExternalId = accountability.getChildParty().getExternalId();
                    if (getUnitRelationsAccountabilityTypes().containsKey(subUnitExternalId)) {
                        getUnitRelationsAccountabilityTypes().put(
                                subUnitExternalId,
                                getUnitRelationsAccountabilityTypes().get(subUnitExternalId).concat(
                                        ", " + BundleUtil.getString(Bundle.ENUMERATION, accountabilityTypeName)));
                    } else {
                        getUnitRelationsAccountabilityTypes().put(subUnitExternalId,
                                (BundleUtil.getString(Bundle.ENUMERATION, accountabilityTypeName)));
                    }
                }
            }
        }
    }

    public void setUnit(Unit unit) {
        this.unit = unit;
    }

    private String processDate(Date date) throws FenixServiceException {
        return DateFormatUtil.format("dd/MM/yyyy", date);
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

    public Function getFunction() throws FenixServiceException {
        if (this.function == null && this.getFunctionIDHidden() != null && this.getFunctionIDHidden().getValue() != null
                && !this.getFunctionIDHidden().getValue().equals("")) {

            this.function = (Function) FenixFramework.getDomainObject(this.getFunctionIDHidden().getValue().toString());
        }
        return function;
    }

    public void setFunction(Function function) {
        this.function = function;
    }

    public String getFunctionBeginDate() throws FenixServiceException {
        if (this.functionBeginDate == null && this.getFunction() != null && this.getFunction().getBeginDate() != null) {
            this.functionBeginDate = processDate(this.getFunction().getBeginDate());
        }
        return functionBeginDate;
    }

    public void setFunctionBeginDate(String functionBeginDate) {
        this.functionBeginDate = functionBeginDate;
    }

    public String getFunctionEndDate() throws FenixServiceException {
        if (this.functionEndDate == null && this.getFunction() != null && this.getFunction().getEndDate() != null) {
            this.functionEndDate = processDate(this.getFunction().getEndDate());
        }
        return functionEndDate;
    }

    public void setFunctionEndDate(String functionEndDate) {
        this.functionEndDate = functionEndDate;
    }

    public String getFunctionName() throws FenixServiceException {
        if (this.functionName == null && this.getFunction() != null) {
            this.functionName = this.getFunction().getTypeName().getContent(MultiLanguageString.pt);
        }
        return functionName;
    }

    public void setFunctionName(String functionName) {
        this.functionName = functionName;
    }

    public String getFunctionTypeName() throws FenixServiceException {
        if (this.functionTypeName == null && this.getFunction() != null && this.getFunction().getFunctionType() != null) {
            this.functionTypeName = this.getFunction().getFunctionType().getName();
        }
        return functionTypeName;
    }

    public void setFunctionTypeName(String functionTypeName) {
        this.functionTypeName = functionTypeName;
    }

    public Unit getChooseUnit() throws FenixServiceException {
        if (this.chooseUnit == null && this.getChooseUnitIDHidden() != null && this.getChooseUnitIDHidden().getValue() != null
                && !this.getChooseUnitIDHidden().getValue().equals("")) {

            this.chooseUnit = (Unit) FenixFramework.getDomainObject(this.getChooseUnitIDHidden().getValue().toString());
        }

        return chooseUnit;
    }

    public void setChooseUnit(Unit chooseUnit) {
        this.chooseUnit = chooseUnit;
    }

    public HtmlInputHidden getChooseUnitIDHidden() {
        if (this.chooseUnitIDHidden == null) {
            this.chooseUnitIDHidden = new HtmlInputHidden();
        }
        return chooseUnitIDHidden;
    }

    public void setChooseUnitIDHidden(HtmlInputHidden chooseUnitIDHidden) {
        this.chooseUnitIDHidden = chooseUnitIDHidden;
    }

    public HtmlInputHidden getFunctionIDHidden() {
        if (this.functionIDHidden == null) {
            this.functionIDHidden = new HtmlInputHidden();
        }
        return functionIDHidden;
    }

    public void setFunctionIDHidden(HtmlInputHidden functionIDHidden) {
        this.functionIDHidden = functionIDHidden;
    }

    public String getPrincipalFunctionID() {
        return principalFunctionID;
    }

    public void setPrincipalFunctionID(String principalFunctionID) {
        this.principalFunctionID = principalFunctionID;
    }

    public String getListingTypeValueToUnits() {
        if (listingTypeValueToUnits == null) {
            this.listingTypeValueToUnits = "0";
        }
        return listingTypeValueToUnits;
    }

    public void setListingTypeValueToUnits(String listingTypeValue) {
        this.listingTypeValueToUnits = listingTypeValue;
        this.listingTypeValueToUnitsHidden.setValue(listingTypeValue);
    }

    public String getListingTypeValueToFunctions() {
        if (listingTypeValueToFunctions == null) {
            listingTypeValueToFunctions = "0";
        }
        return listingTypeValueToFunctions;
    }

    public void setListingTypeValueToFunctions(String listingTypeValueToFunctions) {
        this.listingTypeValueToFunctions = listingTypeValueToFunctions;
        this.listingTypeValueToFunctionsHidden.setValue(listingTypeValueToFunctions);
    }

    public HtmlInputHidden getListingTypeValueToFunctionsHidden() {
        if (listingTypeValueToFunctionsHidden == null) {
            listingTypeValueToFunctionsHidden = new HtmlInputHidden();
            listingTypeValueToFunctionsHidden.setValue(getListingTypeValueToFunctions());
        }
        return listingTypeValueToFunctionsHidden;
    }

    public void setListingTypeValueToFunctionsHidden(HtmlInputHidden listingTypeValueToFunctionsHidden) {
        this.listingTypeValueToFunctionsHidden = listingTypeValueToFunctionsHidden;
    }

    public HtmlInputHidden getListingTypeValueToUnitsHidden() {
        if (listingTypeValueToUnitsHidden == null) {
            listingTypeValueToUnitsHidden = new HtmlInputHidden();
            listingTypeValueToUnitsHidden.setValue(getListingTypeValueToUnits());
        }
        return listingTypeValueToUnitsHidden;
    }

    public void setListingTypeValueToUnitsHidden(HtmlInputHidden listingTypeValueToUnitsHidden) {
        this.listingTypeValueToUnitsHidden = listingTypeValueToUnitsHidden;
    }

    public String getAdministrativeOfficeID() throws FenixServiceException {
        if (this.administrativeOfficeID == null && this.getChooseUnit() != null
                && this.getChooseUnit().getAdministrativeOffice() != null) {
            this.administrativeOfficeID = this.getChooseUnit().getAdministrativeOffice().getExternalId().toString();
        }
        return administrativeOfficeID;
    }

    public void setAdministrativeOfficeID(String administrativeOfficeID) {
        this.administrativeOfficeID = administrativeOfficeID;
    }

    public String getDegreeID() throws FenixServiceException {
        if (this.degreeID == null && this.getChooseUnit() != null && this.getChooseUnit().getDegree() != null) {
            this.degreeID = this.getChooseUnit().getDegree().getExternalId().toString();
        }
        return degreeID;
    }

    public void setDegreeID(String degreeID) {
        this.degreeID = degreeID;
    }

    public String getDepartmentID() throws FenixServiceException {
        if (this.departmentID == null && this.getChooseUnit() != null && this.getChooseUnit().getDepartment() != null) {
            this.departmentID = this.getChooseUnit().getDepartment().getExternalId().toString();
        }
        return departmentID;
    }

    public void setDepartmentID(String departmentID) {
        this.departmentID = departmentID;
    }

    public String getCampusID() throws FenixServiceException {
        if (this.campusID == null && this.getChooseUnit() != null && this.getChooseUnit().getCampus() != null) {
            this.campusID = this.getChooseUnit().getCampus().getExternalId().toString();
        }
        return this.campusID;
    }

    public void setCampusID(String campusID) {
        this.campusID = campusID;
    }

    public String getUnitRelationTypeValue() {
        return unitRelationTypeValue;
    }

    public void setUnitRelationTypeValue(String unitRelationTypeValue) {
        this.unitRelationTypeValue = unitRelationTypeValue;
    }

    public HashMap<String, String> getUnitRelationsAccountabilityTypes() {
        if (unitRelationsAccountabilityTypes == null) {
            unitRelationsAccountabilityTypes = new HashMap<String, String>();
        }
        return unitRelationsAccountabilityTypes;
    }

    public void setUnitRelationsAccountabilityTypes(HashMap<String, String> unitRelationsAccountabilityTypes) {
        this.unitRelationsAccountabilityTypes = unitRelationsAccountabilityTypes;
    }

    public Boolean getToRemoveParentUnit() {
        return toRemoveParentUnit;
    }

    public void setToRemoveParentUnit(Boolean toRemoveParentUnit) {
        this.toRemoveParentUnit = toRemoveParentUnit;
    }

    public class PrepareDatesResult {

        private final boolean test;

        private final Date beginDate, endDate;

        public PrepareDatesResult(boolean test, Date beginDate, Date endDate) {
            this.test = test;
            this.beginDate = beginDate;
            this.endDate = endDate;
        }

        public YearMonthDay getBeginDate() {
            return beginDate != null ? YearMonthDay.fromDateFields(beginDate) : null;
        }

        public YearMonthDay getEndDate() {
            return endDate != null ? YearMonthDay.fromDateFields(endDate) : null;
        }

        public boolean isTest() {
            return test;
        }
    }

    public class CreateNewUnitParameters {

        private String degreeID, departmentID, administrativeOfficeID, campusID;

        public CreateNewUnitParameters(OrganizationalStructureBackingBean bean, int mode) throws NumberFormatException,
                FenixServiceException {

            if (mode == 1) {
                this.departmentID =
                        (bean.getDepartmentID() != null && !bean.getDepartmentID().equals("#")) ? bean.getDepartmentID() : null;
                this.degreeID = (bean.getDegreeID() != null && !bean.getDegreeID().equals("#")) ? bean.getDegreeID() : null;
                this.administrativeOfficeID =
                        (bean.getAdministrativeOfficeID() != null && !bean.getAdministrativeOfficeID().equals("#")) ? bean
                                .getAdministrativeOfficeID() : null;
                this.campusID = (bean.getCampusID() != null && !bean.getCampusID().equals("#")) ? bean.getCampusID() : null;

            } else if (mode == 2) {
                this.departmentID =
                        (bean.getUnit().getDepartment() != null) ? bean.getUnit().getDepartment().getExternalId() : null;
                this.degreeID = (bean.getUnit().getDegree() != null) ? bean.getUnit().getDegree().getExternalId() : null;
                this.administrativeOfficeID =
                        (bean.getUnit().getAdministrativeOffice() != null) ? bean.getUnit().getAdministrativeOffice()
                                .getExternalId() : null;
                this.campusID = (bean.getUnit().getCampus() != null) ? bean.getUnit().getCampus().getExternalId() : null;
            }
        }

        public String getDegreeID() {
            return degreeID;
        }

        public String getDepartmentID() {
            return departmentID;
        }

        public String getAdministrativeOfficeID() {
            return administrativeOfficeID;
        }

        public String getCampusID() {
            return campusID;
        }
    }

    public String getAccountabilityID() {
        return accountabilityID;
    }

    public void setAccountabilityID(String accountabilityID) {
        this.accountabilityID = accountabilityID;
    }

    public Boolean getViewExternalUnits() {
        return viewExternalUnits;
    }

    public void setViewExternalUnits(Boolean viewExternalUnits) {
        this.viewExternalUnits = viewExternalUnits;
    }

    public Boolean getViewUnitsWithoutParents() {
        return viewUnitsWithoutParents;
    }

    public void setViewUnitsWithoutParents(Boolean viewUnitsWithoutParents) {
        this.viewUnitsWithoutParents = viewUnitsWithoutParents;
    }

    public Boolean getExternalInstitutionUnit() throws FenixServiceException {
        if (getUnit() != null && UnitUtils.readExternalInstitutionUnit() != null
                && UnitUtils.readExternalInstitutionUnit().equals(getUnit())) {
            this.externalInstitutionUnit = Boolean.TRUE;
        }
        return externalInstitutionUnit;
    }

    public void setExternalInstitutionUnit(Boolean externalInstitutionUnit) throws FenixServiceException {

        if (externalInstitutionUnit
                && getUnit() != null
                && (UnitUtils.readExternalInstitutionUnit() == null || !UnitUtils.readExternalInstitutionUnit().equals(getUnit()))) {

            SetRootUnit.run(getUnit(), Boolean.FALSE);
            this.externalInstitutionUnit = externalInstitutionUnit;
        }
    }

    public Boolean getInstitutionUnit() throws FenixServiceException {
        if (getUnit() != null && UnitUtils.readInstitutionUnit() != null && UnitUtils.readInstitutionUnit().equals(getUnit())) {
            this.institutionUnit = Boolean.TRUE;
        }
        return institutionUnit;
    }

    public void setInstitutionUnit(Boolean institutionUnit) throws FenixServiceException {

        if (institutionUnit && getUnit() != null
                && (UnitUtils.readInstitutionUnit() == null || !UnitUtils.readInstitutionUnit().equals(getUnit()))) {

            SetRootUnit.run(getUnit(), Boolean.TRUE);
            this.institutionUnit = institutionUnit;
        }
    }

    public Boolean getEarthUnit() throws FenixServiceException {
        if (getUnit() != null && UnitUtils.readEarthUnit() != null && getUnit() == UnitUtils.readEarthUnit()) {
            this.earthUnit = Boolean.TRUE;
        }
        return this.earthUnit;
    }

    public void setEarthUnit(Boolean earthUnit) throws FenixServiceException {
        if (earthUnit && getUnit() != null && UnitUtils.readEarthUnit() != getUnit()) {
            SetRootUnit.run(getUnit(), null);
            this.earthUnit = earthUnit;
        }
    }

    public Boolean getCanBeResponsibleOfSpaces() throws FenixServiceException {
        if (this.canBeResponsibleOfSpaces == null && this.getChooseUnit() != null) {
            this.canBeResponsibleOfSpaces = this.getChooseUnit().getCanBeResponsibleOfSpaces();
        }
        return canBeResponsibleOfSpaces;
    }

    public void setCanBeResponsibleOfSpaces(Boolean toBeResponsibleOfSpaces) {
        this.canBeResponsibleOfSpaces = toBeResponsibleOfSpaces;
    }

    public String getParentUnitsLinks() throws FenixServiceException {
        StringBuilder result = new StringBuilder();
        Unit unit = this.getUnit();
        List<Unit> parentUnitsPathWithoutAggregateUnits = unit.getParentUnitsPath();
        int index = 1;
        for (Unit parentUnit : parentUnitsPathWithoutAggregateUnits) {
            if (index == parentUnitsPathWithoutAggregateUnits.size()) {
                result.append("<a href=\"").append(getContextPath())
                        .append("/manager/organizationalStructureManagament/unitDetails.faces?unitID=")
                        .append(parentUnit.getExternalId()).append("\">");
                result.append(parentUnit.getNameWithAcronym());
                result.append("</a>");
            } else {
                result.append("<a href=\"").append(getContextPath())
                        .append("/manager/organizationalStructureManagament/unitDetails.faces?unitID=")
                        .append(parentUnit.getExternalId()).append("\">");
                result.append(parentUnit.getNameWithAcronym());
                result.append("</a> - ");
            }
            index++;
        }
        return result.toString();
    }

    public String getFunctionNameEn() throws FenixServiceException {
        if (this.functionNameEn == null && this.getFunction() != null) {
            this.functionNameEn = this.getFunction().getTypeName().getContent(MultiLanguageString.en);
        }
        return functionNameEn;
    }

    public void setFunctionNameEn(String functionNameEn) {
        this.functionNameEn = functionNameEn;
    }

    public String getUnitNameEn() throws FenixServiceException {
        if (this.unitNameEn == null && this.getChooseUnit() != null) {
            this.unitNameEn = this.getChooseUnit().getPartyName().getContent(MultiLanguageString.en);
        }
        return unitNameEn;
    }

    public void setUnitNameEn(String unitNameEn) {
        this.unitNameEn = unitNameEn;
    }

    public String getUnitNameCard() throws FenixServiceException {
        if (this.unitNameCard == null && this.getChooseUnit() != null) {
            this.unitNameCard = this.getChooseUnit().getIdentificationCardLabel();
        }
        return unitNameCard;
    }

    public void setUnitNameCard(String unitNameCard) {
        this.unitNameCard = unitNameCard;
    }

    public List<SelectItem> getCountries() {
        List<SelectItem> selectedCountriesItems = new ArrayList<SelectItem>();

        List<Country> countryList = new ArrayList<Country>(Country.readDistinctCountries());
        Collections.sort(countryList, new BeanComparator("localizedName.content"));

        selectedCountriesItems.add(new SelectItem(NOT_SELECTED_VALUE, NOT_SELECTED_DESCRIPTION));
        for (Country country : countryList) {
            selectedCountriesItems.add(new SelectItem(country.getCode(), country.getLocalizedName().getContent()));
        }

        return selectedCountriesItems;
    }

    public String getSelectedCountry() throws FenixServiceException {
        if (this.selectedCountry != null) {
            return this.selectedCountry;
        }

        if (getUnit() == null) {
            return null;
        }

        if (getUnit().getCountry() == null) {
            return null;
        }

        return getUnit().getCountry().getCode();
    }

    public void setSelectedCountry(String code) {
        this.selectedCountry = code;
    }

    public String associateCountry() throws FenixServiceException {
        try {
            ((CountryUnit) this.getUnit()).associateCountry(Country.readByTwoLetterCode(getSelectedCountry()));
        } catch (DomainException e) {
            setErrorMessage(e.getMessage());
            return "";
        }

        return "backToUnitDetails";
    }

}