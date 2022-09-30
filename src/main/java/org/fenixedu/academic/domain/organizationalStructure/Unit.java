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
/*
 * Created on Sep 16, 2005
 *	by mrsp
 */
package org.fenixedu.academic.domain.organizationalStructure;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;
import org.fenixedu.academic.domain.Country;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.util.email.UnitBasedSender;
import org.fenixedu.academic.util.Bundle;
import org.fenixedu.academic.util.LocaleUtils;
import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.fenixedu.commons.i18n.LocalizedString;
import org.fenixedu.spaces.domain.Space;
import org.joda.time.YearMonthDay;

import pt.ist.fenixframework.Atomic;

public class Unit extends Unit_Base {

    protected Unit() {
        super();
    }

    public static Unit createNewUnit(Optional<PartyType> partyType, LocalizedString unitName, String acronym, Unit parentUnit,
            AccountabilityType accountabilityType) {

        final Unit unit = new Unit();
        partyType.ifPresent(pt -> unit.setPartyType(pt));

        if (parentUnit != null && accountabilityType != null) {
            unit.addParentUnit(parentUnit, accountabilityType); // this must be before setName and setAcronym in order to validations to work
        } else if (!unit.isPlanetUnit()) {
            throw new DomainException("error.unit.create.noParentOrAccountabilityType");
        }

        unit.setPartyName(unitName);
        unit.setAcronym(acronym);

        unit.setBeginDateYearMonthDay(new YearMonthDay());

        return unit;
    }

    public static Unit createNewNoOfficialExternalInstitution(String unitName) {
        final Unit externalInstitutionUnit = UnitUtils.readExternalInstitutionUnit();
        return Unit.createNewUnit(Optional.empty(), new LocalizedString(Locale.getDefault(), unitName), null,
                externalInstitutionUnit, AccountabilityType.readByType(AccountabilityTypeEnum.ORGANIZATIONAL_STRUCTURE));
    }

    public void edit(LocalizedString name, String acronym) {
        setPartyName(name);
        setAcronym(acronym);
    }

//    protected void init(LocalizedString name, String unitNameCard, Integer costCenterCode, String acronym, YearMonthDay beginDate,
//            YearMonthDay endDate, String webAddress, UnitClassification classification, AdministrativeOffice administrativeOffice,
//            Boolean canBeResponsibleOfSpaces, Space campus) {
//
//        setPartyName(name);
//        if (acronym != null) {
//            setAcronym(acronym);
//        }
//        if (getCostCenterCode() == null || !getCostCenterCode().equals(costCenterCode)) {
//            setCostCenterCode(costCenterCode);
//        }
//        setIdentificationCardLabel(unitNameCard);
//        setBeginDateYearMonthDay(beginDate);
//        setEndDateYearMonthDay(endDate);
//        setClassification(classification);
//        setAdministrativeOffice(administrativeOffice);
//        setCanBeResponsibleOfSpaces(canBeResponsibleOfSpaces);
//        setCampus(campus);
//        setDefaultWebAddressUrl(webAddress);
//    }

    @Override
    public void setPartyName(LocalizedString partyName) {
        if (partyName == null || partyName.isEmpty()) {
            throw new DomainException("error.Party.empty.partyName");
        }
        super.setPartyName(partyName);

        setName(LocaleUtils.getPreferedContent(partyName));
    }

    @Override
    public String getName() {
        return LocaleUtils.getPreferedContent(getPartyName());
    }

    public void setName(String name) {

        if (name == null || StringUtils.isEmpty(name.trim())) {
            throw new DomainException("error.person.empty.name");
        }

        LocalizedString partyName = getPartyName();

        partyName =
                partyName == null ? new LocalizedString(Locale.getDefault(), name) : partyName.with(Locale.getDefault(), name);

        super.setPartyName(partyName);

        UnitName unitName = getUnitName();
        unitName = unitName == null ? new UnitName(this) : unitName;
        unitName.setName(name);
    }

    @Override
    public void setAcronym(String acronym) {
        super.setAcronym(acronym);
        checkUniqueAcronymInSiblingUnits();

        setUnitAcronym(StringUtils.isBlank(acronym) ? null : UnitAcronym.readUnitAcronymByAcronym(acronym)
                .orElseGet(() -> new UnitAcronym(acronym)));
    }

    private void checkUniqueAcronymInSiblingUnits() {
        if (StringUtils.isNotBlank(getAcronym())) {
            final Predicate<Unit> predicate = u -> getAcronym().equalsIgnoreCase(u.getAcronym());
            if (getParentUnits().stream().flatMap(pu -> pu.getSubUnits().stream()).filter(u -> u != this).anyMatch(predicate)) {
                throw new DomainException("error.unit.already.exists.unit.with.same.acronym");
            }
        }
    }

//    public void edit(LocalizedString unitName, String unitNameCard, Integer unitCostCenter, String acronym,
//            YearMonthDay beginDate, YearMonthDay endDate, String webAddress, UnitClassification classification,
//            Department department, Degree degree, AdministrativeOffice administrativeOffice, Boolean canBeResponsibleOfSpaces,
//            Space campus) {
//
//        init(unitName, unitNameCard, unitCostCenter, acronym, beginDate, endDate, webAddress, classification,
//                administrativeOffice, canBeResponsibleOfSpaces, campus);
//    }

//    @Override
//    public void setCanBeResponsibleOfSpaces(Boolean canBeResponsibleOfSpaces) {
//        super.setCanBeResponsibleOfSpaces(canBeResponsibleOfSpaces != null ? canBeResponsibleOfSpaces : Boolean.FALSE);
//    }

//    public void setCostCenterCode(Integer costCenterCode) {
//        final UnitCostCenterCode otherUnitCostCenterCode = UnitCostCenterCode.find(costCenterCode);
//        if (otherUnitCostCenterCode != null && otherUnitCostCenterCode.getUnit() != this) {
//            throw new DomainException("error.costCenter.alreadyExists");
//        }
//        final UnitCostCenterCode unitCostCenterCode = getUnitCostCenterCode();
//        if (unitCostCenterCode == null && costCenterCode != null) {
//            new UnitCostCenterCode(this, costCenterCode);
//        } else if (unitCostCenterCode != null && costCenterCode != null) {
//            unitCostCenterCode.setCostCenterCode(costCenterCode);
//        } else if (unitCostCenterCode != null && costCenterCode == null) {
//            unitCostCenterCode.delete();
//        }
//    }
//
//    public Integer getCostCenterCode() {
//        final UnitCostCenterCode unitCostCenterCode = getUnitCostCenterCode();
//        return unitCostCenterCode == null ? null : unitCostCenterCode.getCostCenterCode();
//    }

    @jvstm.cps.ConsistencyPredicate
    protected boolean checkDateInterval() {
        final YearMonthDay start = getBeginDateYearMonthDay();
        final YearMonthDay end = getEndDateYearMonthDay();
        return start != null && (end == null || !start.isAfter(end));
    }

    @Override
    public void delete() {

        DomainException.throwWhenDeleteBlocked(getDeletionBlockers());

        if (!getParentsSet().isEmpty()) {
            getParentsSet().iterator().next().delete();
        }

        getUnitName().delete();

        setRootDomainObjectForEarthUnit(null);
        setRootDomainObjectForExternalInstitutionUnit(null);
        setRootDomainObjectForInstitutionUnit(null);
        setCampus(null);
        setUnitAcronym(null);
        setAdministrativeOffice(null);

        getParticipatingAnyCurricularCourseCurricularRulesSet().forEach(acc -> acc.delete()); // if department unit
        super.setDepartment(null); // if department unit
        super.setDegree(null); // if degree unit

        super.delete();
    }

    @Override
    protected void checkForDeletionBlockers(Collection<String> blockers) {
        super.checkForDeletionBlockers(blockers);
        if (!(getParentsSet().isEmpty() || (getParentsSet().size() == 1 && getParentUnits().size() == 1))
                && getChildsSet().isEmpty()) {
            blockers.add(BundleUtil.getString(Bundle.APPLICATION, "error.unit.cannot.be.deleted"));
        }

        if (!(getExternalCurricularCoursesSet().isEmpty() && getPrecedentDegreeInformationsSet().isEmpty()
                && getUnitGroupSet().isEmpty())) {
            blockers.add(BundleUtil.getString(Bundle.APPLICATION, "error.unit.cannot.be.deleted"));
        }

        if (!getCompetenceCourseInformationsSet().isEmpty()) {
            blockers.add(BundleUtil.getString(Bundle.APPLICATION, "error.unit.cannot.be.deleted"));
        }
    }

    @Override
    public Space getCampus() {

        Space campus = super.getCampus();

        if (campus != null) {
            return campus;
        }

        Collection<Unit> parentUnits = getParentUnits();
        if (parentUnits.size() == 1) {
            campus = parentUnits.iterator().next().getCampus();
        }

        return campus;
    }

    public boolean isInternal() {
        if (this.equals(UnitUtils.readInstitutionUnit())) {
            return true;
        }

        for (final Unit parentUnit : getParentUnits()) {
            if (parentUnit.isInternal()) {
                return true;
            }
        }

        return false;
    }

    public boolean isNoOfficialExternal() {
        if (this.equals(UnitUtils.readExternalInstitutionUnit())) {
            return true;
        }
        for (final Unit parentUnit : getParentUnits()) {
            if (parentUnit.isNoOfficialExternal()) {
                return true;
            }
        }
        return false;
    }

//    public boolean isOfficialExternal() {
//        return !isInternal() && !isNoOfficialExternal();
//    }

    public boolean isActive(YearMonthDay currentDate) {
        return (!this.getBeginDateYearMonthDay().isAfter(currentDate)
                && (this.getEndDateYearMonthDay() == null || !this.getEndDateYearMonthDay().isBefore(currentDate)));
    }

    @Override
    public boolean isUnit() {
        return true;
    }

//    public List<Unit> getTopUnits() {
//        Unit unit = this;
//        List<Unit> allTopUnits = new ArrayList<Unit>();
//        if (unit.hasAnyParentUnits()) {
//            for (Unit parentUnit : this.getParentUnits()) {
//                if (!parentUnit.hasAnyParentUnits() && !allTopUnits.contains(parentUnit)) {
//                    allTopUnits.add(parentUnit);
//                } else if (parentUnit.hasAnyParentUnits()) {
//                    for (Unit parentUnit2 : parentUnit.getTopUnits()) {
//                        if (!allTopUnits.contains(parentUnit2)) {
//                            allTopUnits.add(parentUnit2);
//                        }
//                    }
//                }
//            }
//        }
//        return allTopUnits;
//    }

//    public Department getDepartment() {
//        return null;
//    }
//
//    public Degree getDegree() {
//        return null;
//    }

//    public DepartmentUnit getAssociatedDepartmentUnit() {
//        if (this.isDepartmentUnit()) {
//            return (DepartmentUnit) this;
//        } else {
//            for (Unit parentUnit : getParentUnits()) {
//                if (parentUnit.isDepartmentUnit()) {
//                    return (DepartmentUnit) parentUnit;
//                } else if (parentUnit.hasAnyParentUnits()) {
//                    Unit departmentUnit = parentUnit.getAssociatedDepartmentUnit();
//                    if (departmentUnit == null) {
//                        continue;
//                    } else {
//                        return (DepartmentUnit) departmentUnit;
//                    }
//                }
//            }
//        }
//        return null;
//    }

    private List<Unit> getInactiveSubUnits(YearMonthDay currentDate) {
        return getSubUnitsByState(currentDate, false);
    }

    private List<Unit> getActiveSubUnits(YearMonthDay currentDate) {
        return getSubUnitsByState(currentDate, true);
    }

    private List<Unit> getSubUnitsByState(YearMonthDay currentDate, boolean state) {
        List<Unit> allSubUnits = new ArrayList<Unit>();
        for (Unit subUnit : this.getSubUnits()) {
            if (subUnit.isActive(currentDate) == state) {
                allSubUnits.add(subUnit);
            }
        }
        return allSubUnits;
    }

//    private List<Unit> getInactiveParentUnits(YearMonthDay currentDate) {
//        return getParentUnitsByState(currentDate, false);
//    }

//    public List<Unit> getActiveParentUnits(YearMonthDay currentDate) {
//        return getParentUnitsByState(currentDate, true);
//    }

//    private List<Unit> getParentUnitsByState(YearMonthDay currentDate, boolean state) {
//        List<Unit> allParentUnits = new ArrayList<Unit>();
//        for (Unit subUnit : this.getParentUnits()) {
//            if (subUnit.isActive(currentDate) == state) {
//                allParentUnits.add(subUnit);
//            }
//        }
//        return allParentUnits;
//    }

//    public List<Unit> getInactiveSubUnits(YearMonthDay currentDate, AccountabilityTypeEnum accountabilityTypeEnum) {
//        return getSubUnitsByState(currentDate, accountabilityTypeEnum, false);
//    }

//    public List<Unit> getActiveSubUnits(YearMonthDay currentDate, AccountabilityTypeEnum accountabilityTypeEnum) {
//        return getSubUnitsByState(currentDate, accountabilityTypeEnum, true);
//    }

//    private List<Unit> getSubUnitsByState(YearMonthDay currentDate, AccountabilityTypeEnum accountabilityTypeEnum,
//            boolean state) {
//        List<Unit> allSubUnits = new ArrayList<Unit>();
//        for (Unit subUnit : getSubUnits(accountabilityTypeEnum)) {
//            if (subUnit.isActive(currentDate) == state) {
//                allSubUnits.add(subUnit);
//            }
//        }
//        return allSubUnits;
//    }

//    public List<Unit> getActiveSubUnits(YearMonthDay currentDate, List<AccountabilityTypeEnum> accountabilityTypeEnums) {
//        return getSubUnitsByState(currentDate, accountabilityTypeEnums, true);
//    }
//
//    public List<Unit> getInactiveSubUnits(YearMonthDay currentDate, List<AccountabilityTypeEnum> accountabilityTypeEnums) {
//        return getSubUnitsByState(currentDate, accountabilityTypeEnums, false);
//    }
//
//    private List<Unit> getSubUnitsByState(YearMonthDay currentDate, List<AccountabilityTypeEnum> accountabilityTypeEnums,
//            boolean state) {
//        List<Unit> allSubUnits = new ArrayList<Unit>();
//        for (Unit subUnit : this.getSubUnits(accountabilityTypeEnums)) {
//            if (subUnit.isActive(currentDate) == state) {
//                allSubUnits.add(subUnit);
//            }
//        }
//        return allSubUnits;
//    }

//    public List<Unit> getAllInactiveParentUnits(YearMonthDay currentDate) {
//        Set<Unit> allInactiveParentUnits = new HashSet<Unit>();
//        allInactiveParentUnits.addAll(getInactiveParentUnits(currentDate));
//        for (Unit subUnit : getParentUnits()) {
//            allInactiveParentUnits.addAll(subUnit.getAllInactiveParentUnits(currentDate));
//        }
//        return new ArrayList<Unit>(allInactiveParentUnits);
//    }

//    public List<Unit> getAllActiveParentUnits(YearMonthDay currentDate) {
//        Set<Unit> allActiveParentUnits = new HashSet<Unit>();
//        allActiveParentUnits.addAll(getActiveParentUnits(currentDate));
//        for (Unit subUnit : getParentUnits()) {
//            allActiveParentUnits.addAll(subUnit.getAllActiveParentUnits(currentDate));
//        }
//        return new ArrayList<Unit>(allActiveParentUnits);
//    }

    private List<Unit> getAllInactiveSubUnits(YearMonthDay currentDate) {
        Set<Unit> allInactiveSubUnits = new HashSet<Unit>();
        allInactiveSubUnits.addAll(getInactiveSubUnits(currentDate));
        for (Unit subUnit : getSubUnits()) {
            allInactiveSubUnits.addAll(subUnit.getAllInactiveSubUnits(currentDate));
        }
        return new ArrayList<Unit>(allInactiveSubUnits);
    }

    private List<Unit> getAllActiveSubUnits(YearMonthDay currentDate) {
        Set<Unit> allActiveSubUnits = new HashSet<Unit>();
        allActiveSubUnits.addAll(getActiveSubUnits(currentDate));
        for (Unit subUnit : getSubUnits()) {
            allActiveSubUnits.addAll(subUnit.getAllActiveSubUnits(currentDate));
        }
        return new ArrayList<Unit>(allActiveSubUnits);
    }

//    public List<Unit> getAllActiveSubUnits(YearMonthDay currentDate, AccountabilityTypeEnum accountabilityTypeEnum) {
//        Set<Unit> allActiveSubUnits = new HashSet<Unit>();
//        allActiveSubUnits.addAll(getActiveSubUnits(currentDate, accountabilityTypeEnum));
//        for (Unit subUnit : getSubUnits(accountabilityTypeEnum)) {
//            allActiveSubUnits.addAll(subUnit.getAllActiveSubUnits(currentDate));
//        }
//        return new ArrayList<Unit>(allActiveSubUnits);
//    }

    public List<Unit> getAllActiveSubUnitsWithAllowedChildParties(final YearMonthDay currentDate, final PartyType childType) {
        final Set<Unit> allActiveSubUnits = new HashSet<Unit>();
        allActiveSubUnits.addAll(getActiveSubUnitsWithAllowedChildParties(currentDate, childType));
        for (Unit subUnit : getSubUnits()) {
            allActiveSubUnits.addAll(subUnit.getAllActiveSubUnitsWithAllowedChildParties(currentDate, childType));
        }
        return new ArrayList<Unit>(allActiveSubUnits);
    }

    private List<Unit> getActiveSubUnitsWithAllowedChildParties(YearMonthDay currentDate, final PartyType childType) {
        final List<Unit> allSubUnits = new ArrayList<Unit>();
        for (Unit subUnit : this.getSubUnits()) {
            if (subUnit.isActive(currentDate) && subUnit.getAllowedChildPartyTypes(null).contains(childType)) {
                allSubUnits.add(subUnit);
            }
        }
        return allSubUnits;
    }

//    private Collection<AccountabilityType> getAllowedAccountabilityTypes(final PartyType partyType) {
//        if (isAggregateUnit()) {
//            return getParentUnits().stream().flatMap(u -> u.getAllowedAccountabilityTypes(partyType).stream())
//                    .collect(Collectors.toSet());
//        }
//        return getPartyType().getAllowedAccountabilityTypesFor(partyType);
//    }

//    private AccountabilityType getAllowedAccountabilityType(final PartyType partyType) {
//        AccountabilityType result = null;
//        final Collection<AccountabilityType> allowed = getAllowedAccountabilityTypes(partyType);
//        if (allowed.size() == 1) {
//            result = allowed.iterator().next();
//        }
//        if (result == null) {
//            throw new DomainException("error.Unit.not.found.AccountabilityType.unique", getPartyType().getExternalId(),
//                    partyType.getExternalId());
//        }
//        return result;
//    }

//    private AccountabilityType getAllowedAccountabilityType(final Unit unit) {
//        final PartyType partyType = unit.getPartyType();
//        return getAllowedAccountabilityType(partyType);
//    }

    public Collection<PartyType> getAllowedChildPartyTypes(final Boolean managedByUser) {
        if (isAggregateUnit()) {
            return getParentUnits().stream().flatMap(u -> u.getAllowedChildPartyTypes(managedByUser).stream())
                    .collect(Collectors.toSet());
        }
        return Optional.ofNullable(getPartyType()).map(pt -> pt.getAllowedChildPartyTypes(managedByUser))
                .orElseGet(() -> Set.of());
    }

//    public Accountability getMostRecentParentUnitAccountability() {
//        return getParentUnitAccountabilities().isEmpty() ? null : Collections.max(getParentUnitAccountabilities(),
//                Accountability.getComparatorByBeginDate());
//    }

//    public Collection<? extends Accountability> getParentUnitAccountabilities() {
//        return getParentAccountabilitiesByParentClass(Unit.class);
//    }

//    public Collection<? extends Accountability> getChildUnitAccountabilities() {
//        return getChildAccountabilitiesByChildClass(Unit.class);
//    }

//    public List<Unit> getAllInactiveSubUnits(YearMonthDay currentDate, AccountabilityTypeEnum accountabilityTypeEnum) {
//        Set<Unit> allInactiveSubUnits = new HashSet<Unit>();
//        allInactiveSubUnits.addAll(getInactiveSubUnits(currentDate, accountabilityTypeEnum));
//        for (Unit subUnit : getSubUnits(accountabilityTypeEnum)) {
//            allInactiveSubUnits.addAll(subUnit.getAllInactiveSubUnits(currentDate));
//        }
//        return new ArrayList<Unit>(allInactiveSubUnits);
//    }

    public Collection<Unit> getAllSubUnits() {
        Set<Unit> allSubUnits = new HashSet<Unit>();
        Collection<Unit> subUnits = getSubUnits();
        allSubUnits.addAll(subUnits);
        for (Unit subUnit : subUnits) {
            allSubUnits.addAll(subUnit.getAllSubUnits());
        }
        return allSubUnits;
    }

    public Collection<Unit> getAllParentUnits() {
        Set<Unit> allParentUnits = new HashSet<Unit>();
        Collection<Unit> parentUnits = getParentUnits();
        allParentUnits.addAll(parentUnits);
        for (Unit subUnit : parentUnits) {
            allParentUnits.addAll(subUnit.getAllParentUnits());
        }
        return allParentUnits;
    }

//    @Override
//    public Collection<Unit> getParentUnits() {
//        return (Collection<Unit>) getParentParties(Unit.class);
//    }

//    @Override
//    public Collection<Unit> getParentUnits(String accountabilityTypeEnum) {
//        return (Collection<Unit>) getParentParties(AccountabilityTypeEnum.valueOf(accountabilityTypeEnum), Unit.class);
//    }

//    @Override
//    public Collection<Unit> getParentUnits(AccountabilityTypeEnum accountabilityTypeEnum) {
//        return (Collection<Unit>) getParentParties(accountabilityTypeEnum, Unit.class);
//    }

//    @Override
//    public Collection<Unit> getParentUnits(List<AccountabilityTypeEnum> accountabilityTypeEnums) {
//        return (Collection<Unit>) getParentParties(accountabilityTypeEnums, Unit.class);
//    }

//    @Override
//    public Collection<Unit> getSubUnits() {
//        return (Collection<Unit>) getChildParties(Unit.class);
//    }

//    public boolean hasSubUnit(final Unit unit) {
//        if (unit != null) {
//            for (final Unit child : getSubUnits()) {
//                if (child.equals(unit)) {
//                    return true;
//                }
//            }
//        }
//        return false;
//    }

//    public Collection<Unit> getSubUnits(AccountabilityTypeEnum accountabilityTypeEnum) {
//        return (Collection<Unit>) getChildParties(accountabilityTypeEnum, Unit.class);
//    }

    public Collection<Unit> getSubUnits(List<AccountabilityTypeEnum> accountabilityTypeEnums) {
        return (Collection<Unit>) getChildParties(accountabilityTypeEnums, Unit.class);
    }

    public Collection<Unit> getSubUnits(final PartyTypeEnum type) {
        return (Collection<Unit>) getChildParties(type, Unit.class);
    }

//    private boolean hasAnyParentUnits() {
//        return !getParentUnits().isEmpty();
//    }

//    public boolean hasAnySubUnits() {
//        return !getSubUnits().isEmpty();
//    }

//    public Collection<Unit> getCurrentParentByOrganizationalStructureAccountabilityType() {
//        return (Collection<Unit>) getCurrentParentParties(AccountabilityTypeEnum.ORGANIZATIONAL_STRUCTURE, Unit.class);
//    }
//
//    public Collection<Unit> getParentUnitsByOrganizationalStructureAccountabilityType() {
//        return (Collection<Unit>) getParentParties(AccountabilityTypeEnum.ORGANIZATIONAL_STRUCTURE, Unit.class);
//    }

    @Atomic
    /*
     * @See UnitMailSenderAction
     */
    public UnitBasedSender getOneUnitBasedSender() {
        if (!getUnitBasedSenderSet().isEmpty()) {
            return getUnitBasedSenderSet().iterator().next();
        } else {
            return UnitBasedSender.newInstance(this);
        }
    }

//    public int getUnitDepth() {
//        int depth = 0;
//
//        for (Unit unit : getParentUnits()) {
//            depth = Math.max(depth, 1 + unit.getUnitDepth());
//        }
//
//        return depth;
//    }

//    public Accountability addParentUnit(final Unit parentUnit, final LocalDate begin, final LocalDate end) {
//        final Accountability accountability =
//                addParentUnit(parentUnit, parentUnit.getAllowedAccountabilityType(this.getPartyType()));
//        accountability.setBeginLocalDate(begin);
//        accountability.setEndLocalDate(end);
//        return accountability;
//    }

//    protected void updateParentUnits(final Collection<Unit> newParentUnits, final LocalDate begin, final LocalDate end) {
//
//        final Collection<Unit> currentParentUnits = getParentUnits();
//        final Collection<Accountability> currentParentsAccountabilitiesToRemove = new HashSet<Accountability>();
//
//        for (final Accountability currentParentAccountability : getParentsSet()) {
//            if (!newParentUnits.contains(currentParentAccountability.getParentParty())) {
//                currentParentsAccountabilitiesToRemove.add(currentParentAccountability);
//            } else {
//                currentParentAccountability.setBeginLocalDate(begin);
//                currentParentAccountability.setEndLocalDate(end);
//            }
//        }
//
//        for (final Unit parentUnit : newParentUnits) {
//            if (!currentParentUnits.contains(parentUnit)) {
//                addParentUnit(parentUnit, begin, end);
//            }
//        }
//
//        for (final Accountability accountability : currentParentsAccountabilitiesToRemove) {
//            accountability.delete();
//        }
//    }

    public Accountability addParentUnit(Unit parentUnit, AccountabilityType accountabilityType) {
        if (this.equals(parentUnit)) {
            throw new DomainException("error.unit.equals.parentUnit");
        }
        if (getParentUnits(accountabilityType.getType()).contains(parentUnit)) {
            throw new DomainException("error.unit.parentUnit.is.already.parentUnit");
        }

        YearMonthDay currentDate = new YearMonthDay();
        List<Unit> subUnits =
                (parentUnit.isActive(currentDate)) ? getAllActiveSubUnits(currentDate) : getAllInactiveSubUnits(currentDate);
        if (subUnits.contains(parentUnit)) {
            throw new DomainException("error.unit.parentUnit.is.already.subUnit");
        }

        return new Accountability(parentUnit, this, accountabilityType);
    }

    public Unit getChildUnitByAcronym(String acronym) {
        for (Unit subUnit : getSubUnits()) {
            if ((subUnit.getAcronym() != null) && (subUnit.getAcronym().equals(acronym))) {
                return subUnit;
            }
        }
        return null;
    }

    public static List<Unit> readAllUnits() {
        final List<Unit> allUnits = new ArrayList<Unit>();
        for (final Party party : Bennu.getInstance().getPartysSet()) {
            if (party.isUnit()) {
                allUnits.add((Unit) party);
            }
        }
        return allUnits;
    }

//    /**
//     * This method should be used only for Unit types where acronyms are unique.
//     */
//    public static Unit readUnitByAcronymAndType(String acronym, PartyTypeEnum partyTypeEnum) {
//        if (acronym != null && !acronym.equals("") && partyTypeEnum != null
//                && (partyTypeEnum.equals(PartyTypeEnum.DEGREE_UNIT) || partyTypeEnum.equals(PartyTypeEnum.DEPARTMENT)
//                        || partyTypeEnum.equals(PartyTypeEnum.PLANET) || partyTypeEnum.equals(PartyTypeEnum.COUNTRY)
//                        || partyTypeEnum.equals(PartyTypeEnum.DEPARTMENT) || partyTypeEnum.equals(PartyTypeEnum.UNIVERSITY)
//                        || partyTypeEnum.equals(PartyTypeEnum.SCHOOL) || partyTypeEnum.equals(PartyTypeEnum.RESEARCH_UNIT))) {
//
//            UnitAcronym unitAcronymByAcronym = UnitAcronym.readUnitAcronymByAcronym(acronym);
//            if (unitAcronymByAcronym != null) {
//                for (Unit unit : unitAcronymByAcronym.getUnitsSet()) {
//                    if (unit.getType() != null && unit.getType().equals(partyTypeEnum)) {
//                        return unit;
//                    }
//                }
//            }
//        }
//        return null;
//    }

//    public static List<Unit> readUnitsByAcronym(String acronym, boolean shouldNormalize) {
//        List<Unit> result = new ArrayList<Unit>();
//        if (!StringUtils.isEmpty(acronym.trim())) {
//            UnitAcronym unitAcronymByAcronym = UnitAcronym.readUnitAcronymByAcronym(acronym, shouldNormalize);
//            if (unitAcronymByAcronym != null) {
//                result.addAll(unitAcronymByAcronym.getUnitsSet());
//            }
//        }
//        return result;
//    }
//
//    public static List<Unit> readUnitsByAcronym(String acronym) {
//        return readUnitsByAcronym(acronym, false);
//    }

//    public static Unit readByCostCenterCode(Integer costCenterCode) {
//        final UnitCostCenterCode unitCostCenterCode = UnitCostCenterCode.find(costCenterCode);
//        return unitCostCenterCode == null ? null : unitCostCenterCode.getUnit();
//    }

//    @Deprecated
//    public static Unit createNewUnit(LocalizedString unitName, String unitNameCard, Integer costCenterCode, String acronym,
//            YearMonthDay beginDate, YearMonthDay endDate, Unit parentUnit, AccountabilityType accountabilityType,
//            String webAddress, UnitClassification classification, AdministrativeOffice administrativeOffice,
//            Boolean canBeResponsibleOfSpaces, Space campus) {
//
//        Unit unit = new Unit();
//        unit.init(unitName, unitNameCard, costCenterCode, acronym, beginDate, endDate, webAddress, classification,
//                administrativeOffice, canBeResponsibleOfSpaces, campus);
//        if (parentUnit != null && accountabilityType != null) {
//            unit.addParentUnit(parentUnit, accountabilityType);
//        }
//        return unit;
//    }

//    public static Unit findFirstExternalUnitByName(final String unitName) {
//        if (unitName == null || unitName.length() == 0) {
//            return null;
//        }
//        for (final Party party : Bennu.getInstance().getExternalInstitutionUnit().getSubUnits()) {
//            if (!party.isPerson() && unitName.equalsIgnoreCase(party.getName())) {
//                final Unit unit = (Unit) party;
//                return unit;
//            }
//        }
//        return null;
//    }

//    public static Unit findFirstUnitByName(final String unitNameString) {
//        if (StringUtils.isEmpty(unitNameString)) {
//            return null;
//        }
//        final Collection<UnitName> unitNames = UnitName.find(unitNameString, Integer.MAX_VALUE);
//        for (final UnitName unitName : unitNames) {
//            final Unit unit = unitName.getUnit();
//            if (StringNormalizer.normalize(unitNameString).equalsIgnoreCase(StringNormalizer.normalize(unit.getName()))) {
//                return unit;
//            }
//        }
//        return null;
//    }

    public String getNameWithAcronym() {
        String name = getName().trim();
        return (getAcronym() == null || StringUtils.isEmpty(getAcronym().trim())) ? name : name + " (" + getAcronym().trim()
                + ")";
    }

    public String getPresentationName() {
        return getNameWithAcronym();
    }

    public String getPresentationNameWithParents() {
        String parentUnits = getParentUnitsPresentationName();
        return (!StringUtils.isEmpty(parentUnits.trim())) ? parentUnits + " - " + getPresentationName() : getPresentationName();
    }

//    public String getPresentationNameWithParentsAndBreakLine() {
//        String parentUnits = getParentUnitsPresentationNameWithBreakLine();
//        return (!StringUtils.isEmpty(parentUnits.trim())) ? parentUnits
//                + BundleUtil.getString(Bundle.APPLICATION, "label.html.breakLine")
//                + getPresentationName() : getPresentationName();
//    }

//    public String getParentUnitsPresentationNameWithBreakLine() {
//        return getParentUnitsPresentationName(BundleUtil.getString(Bundle.APPLICATION, "label.html.breakLine"));
//    }

    public String getParentUnitsPresentationName() {
        return getParentUnitsPresentationName(" - ");
    }

    private String getParentUnitsPresentationName(String separator) {
        StringBuilder builder = new StringBuilder();
        List<Unit> parentUnits = getParentUnitsPath();
        int index = 1;

        for (Unit unit : parentUnits) {
            if (!unit.isAggregateUnit()) {
                if (index == 1) {
                    builder.append(unit.getNameWithAcronym());
                } else {
                    builder.append(separator + unit.getNameWithAcronym());
                }
            }
            index++;
        }

        return builder.toString();
    }

//    public String getUnitPath(String separator) {
//        return getUnitPath(separator, true);
//    }

//    public String getUnitPath(String separator, boolean addInstitutionalUnit) {
//        StringBuilder builder = new StringBuilder();
//        List<Unit> parentUnits = getParentUnitsPath(addInstitutionalUnit);
//        int index = 1;
//
//        for (Unit unit : parentUnits) {
//            if (!unit.isAggregateUnit()) {
//                if (index == 1) {
//                    builder.append(unit.getAcronym());
//                } else {
//                    builder.append(separator + unit.getAcronym());
//                }
//            }
//            index++;
//        }
//
//        builder.append("/");
//        builder.append(this.getAcronym());
//        return builder.toString();
//    }

    public List<Unit> getParentUnitsPath() {
        return getParentUnitsPath(true);
    }

    private List<Unit> getParentUnitsPath(boolean addInstitutionalUnit) {

        List<Unit> parentUnits = new ArrayList<Unit>();
        Unit searchedUnit = this;
        Unit externalInstitutionUnit = UnitUtils.readExternalInstitutionUnit();
        Unit institutionUnit = UnitUtils.readInstitutionUnit();
        Unit earthUnit = UnitUtils.readEarthUnit();

        while (searchedUnit.getParentUnits().size() == 1) {
            Unit parentUnit = searchedUnit.getParentUnits().iterator().next();
            if (addInstitutionalUnit || parentUnit != institutionUnit) {
                parentUnits.add(0, parentUnit);
            }
            if (parentUnit != institutionUnit && parentUnit != externalInstitutionUnit && parentUnit != earthUnit) {
                searchedUnit = parentUnit;
                continue;
            }
            break;
        }

        if (searchedUnit.getParentUnits().size() > 1) {
            if (searchedUnit.isInternal() && addInstitutionalUnit) {
                parentUnits.add(0, institutionUnit);
            } else if (searchedUnit.isNoOfficialExternal()) {
                parentUnits.add(0, externalInstitutionUnit);
            } else {
                parentUnits.add(0, earthUnit);
            }
        }

        return parentUnits;
    }

//    public String getDirectParentUnitsPresentationName() {
//        StringBuilder builder = new StringBuilder();
//        for (Unit unit : getParentUnits()) {
//            if (!unit.isAggregateUnit()) {
//                builder.append(unit.getNameWithAcronym());
//            }
//        }
//        return builder.toString();
//    }

//    public String getShortPresentationName() {
//        final StringBuilder stringBuilder = new StringBuilder();
//        for (final Unit unit : getParentUnits()) {
//            if (!unit.isAggregateUnit() && unit != Bennu.getInstance().getInstitutionUnit()) {
//                stringBuilder.append(unit.getName());
//                stringBuilder.append(" - ");
//            }
//        }
//        stringBuilder.append(getName());
//        return stringBuilder.toString();
//    }

    /**
     * still used in: student/enrollment/bolonha/chooseExternalUnit.jsp
     * after that usage, method should be removed
     */
    @Deprecated
    public SortedSet<Unit> getSortedExternalChilds() {
        final SortedSet<Unit> result = new TreeSet<Unit>(Unit.COMPARATOR_BY_NAME_AND_ID);
        for (final Unit unit : getSubUnits()) {
            if (!unit.isInternal()) {
                result.add(unit);
            }
        }
        return result;
    }

//    public List<ExternalCurricularCourse> getAllExternalCurricularCourses() {
//        return new ArrayList<ExternalCurricularCourse>(getExternalCurricularCoursesSet());
//    }

    public LocalizedString getNameI18n() {
        return getPartyName();
    }

//    public boolean isEarth() {
//        return this.equals(Bennu.getInstance().getEarthUnit());
//    }

    @Override
    public String getPartyPresentationName() {
        return getPresentationNameWithParents();
    }

//    /**
//     * Used by messaging system
//     * 
//     * @return Groups to used as recipients
//     */
//    public List<Group> getGroups() {
//        List<Group> groups = new ArrayList<Group>();
//        groups.addAll(getDefaultGroups());
//        return groups;
//    }
//
//    protected List<Group> getDefaultGroups() {
//        return new ArrayList<Group>();
//    }

    static public LocalizedString getInstitutionName() {
        return Optional.ofNullable(Bennu.getInstance().getInstitutionUnit()).map(Unit::getNameI18n)
                .orElseGet(() -> BundleUtil.getLocalizedString(Bundle.GLOBAL, "error.institutionUnit.notconfigured"));
    }

    static public String getInstitutionAcronym() {
        return Optional.ofNullable(Bennu.getInstance().getInstitutionUnit()).map(Unit::getAcronym)
                .orElseGet(() -> BundleUtil.getString(Bundle.GLOBAL, "error.institutionUnit.notconfigured"));
    }

    @Override
    public Country getCountry() {
        if (super.getCountry() != null) {
            return super.getCountry();
        }
        for (final Unit unit : getParentUnits()) {
            final Country country = unit.getCountry();
            if (country != null) {
                return country;
            }
        }
        return null;
    }

//    public Boolean hasParentUnit(Unit parentUnit) {
//        for (Unit parent : getParentUnits()) {
//            if (parent.equals(parentUnit)) {
//                return Boolean.TRUE;
//            }
//        }
//        return Boolean.FALSE;
//    }

//    public static Unit getParentUnit(String unitNormalizedName, Class<? extends Unit> clazz) {
//        if (StringUtils.isEmpty(unitNormalizedName)) {
//            return null;
//        }
//
//        for (final UnitName unitName : UnitName.find(unitNormalizedName, Integer.MAX_VALUE)) {
//            final Unit unit = unitName.getUnit();
//            if (unit.getClass().equals(clazz)) {
//                return unit;
//            }
//        }
//        return null;
//    }

//    public static Unit getParentUnitByNormalizedName(Unit childUnit, String parentNormalizedName) {
//        for (Unit possibleParent : childUnit.getParentUnits()) {
//            if (parentNormalizedName.equalsIgnoreCase(StringNormalizer.normalize(possibleParent.getName()))) {
//                return possibleParent;
//            }
//        }
//        return null;
//    }

//    public void deleteParentUnitRelation(Unit parentUnit) {
//        for (Accountability relation : this.getParentsSet()) {
//            if (relation.getParentParty().equals(parentUnit)) {
//                relation.delete();
//                return;
//            }
//        }
//    }

    public boolean isOfficial() {
        return Boolean.TRUE.equals(getOfficial());
    }

    public void setOfficial(boolean official) {
        super.setOfficial(official);
    }

    @Override
    public boolean isAdministrativeOfficeUnit() {
        return getAdministrativeOffice() != null;
    }

}
