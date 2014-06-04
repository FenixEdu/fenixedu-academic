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
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.domain.Country;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.ExternalCurricularCourse;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOffice;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.util.Bundle;

import org.apache.commons.lang.StringUtils;
import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.fenixedu.spaces.domain.Space;
import org.joda.time.YearMonthDay;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.ist.fenixframework.Atomic;
import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

public class CountryUnit extends CountryUnit_Base {

    private static final Logger logger = LoggerFactory.getLogger(CountryUnit.class);

    private CountryUnit() {
        super();
        super.setType(PartyTypeEnum.COUNTRY);
    }

    public static CountryUnit createNewCountryUnit(MultiLanguageString countryName, String countryNameCard,
            Integer costCenterCode, String countryAcronym, YearMonthDay beginDate, YearMonthDay endDate, Unit parentUnit,
            String webAddress, UnitClassification classification, Boolean canBeResponsibleOfSpaces, Space campus) {

        CountryUnit countryUnit = new CountryUnit();
        countryUnit.init(countryName, countryNameCard, costCenterCode, countryAcronym, beginDate, endDate, webAddress,
                classification, null, canBeResponsibleOfSpaces, campus);
        countryUnit.addParentUnit(parentUnit, AccountabilityType.readByType(AccountabilityTypeEnum.GEOGRAPHIC));

        checkIfAlreadyExistsOneCountryWithSameAcronymAndName(countryUnit);

        return countryUnit;
    }

    @Override
    public void edit(MultiLanguageString name, String acronym) {
        super.edit(name, acronym);
        checkIfAlreadyExistsOneCountryWithSameAcronymAndName(this);
    }

    @Override
    public void edit(MultiLanguageString unitName, String unitNameCard, Integer unitCostCenter, String acronym,
            YearMonthDay beginDate, YearMonthDay endDate, String webAddress, UnitClassification classification,
            Department department, Degree degree, AdministrativeOffice administrativeOffice, Boolean canBeResponsibleOfSpaces,
            Space campus) {

        super.edit(unitName, unitNameCard, unitCostCenter, acronym, beginDate, endDate, webAddress, classification, department,
                degree, administrativeOffice, canBeResponsibleOfSpaces, campus);

        checkIfAlreadyExistsOneCountryWithSameAcronymAndName(this);
    }

    @Override
    public Accountability addParentUnit(Unit parentUnit, AccountabilityType accountabilityType) {
        if (parentUnit != null && (!parentUnit.isOfficialExternal() || !parentUnit.isPlanetUnit())) {
            throw new DomainException("error.exception.commons.institution.invalidParentUnit");
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
    public List<ExternalCurricularCourse> getAllExternalCurricularCourses() {
        final List<ExternalCurricularCourse> result = new ArrayList<ExternalCurricularCourse>(getExternalCurricularCourses());
        for (Unit subUnit : getSubUnits()) {
            if (subUnit.isUniversityUnit() || subUnit.isSchoolUnit()) {
                result.addAll(subUnit.getExternalCurricularCourses());
            }
        }
        return result;
    }

    @Override
    public boolean isCountryUnit() {
        return true;
    }

    private static void checkIfAlreadyExistsOneCountryWithSameAcronymAndName(CountryUnit countryUnit) {
        for (Unit parentUnit : countryUnit.getParentUnits()) {
            for (Unit unit : parentUnit.getAllSubUnits()) {
                if ((!unit.equals(countryUnit)) && unit.isCountryUnit()) {
                    if (countryUnit.getAcronym().equalsIgnoreCase(unit.getAcronym())) {
                        throw new DomainException("error.unit.already.exists.unit.with.same.name.or.acronym: "
                                + countryUnit.getAcronym());
                    }
                    if (countryUnit.getName().equalsIgnoreCase(unit.getName())) {
                        throw new DomainException("error.unit.already.exists.unit.with.same.name.or.acronym: "
                                + countryUnit.getName());
                    }
                }
            }
        }
    }

    transient static private CountryUnit defaultCountry;

    public static CountryUnit getDefault() {
        if (defaultCountry != null && defaultCountry.getRootDomainObject() == Bennu.getInstance()) {
            return defaultCountry;
        }
        defaultCountry =
                (CountryUnit) Unit.readUnitByAcronymAndType(BundleUtil.getString(Bundle.GLOBAL, "default.country.code"),
                        PartyTypeEnum.COUNTRY);
        return defaultCountry;
    }

    public static Set<CountryUnit> readAllCountryUnits() {
        final PartyType partyType = PartyType.readPartyTypeByType(PartyTypeEnum.COUNTRY);
        return (Set) partyType.getPartiesSet();
    }

    @Atomic
    public void associateCountry(Country country) {
        if (country == null) {
            throw new DomainException("error.country.unit.country.is.empty");
        }

        setCountry(country);
    }

    public static CountryUnit getCountryUnitByCountry(final Country country) {
        if (country == null) {
            return null;
        }

        final PartyType partyType = PartyType.readPartyTypeByType(PartyTypeEnum.COUNTRY);
        Set<Unit> units = (Set) partyType.getPartiesSet();

        for (Unit unit : units) {

            if (!(unit instanceof CountryUnit)) {
                logger.info(String.format("Unit is not country %s", unit.getName()));
                continue;
            }

            if (((CountryUnit) unit).getCountry() == country) {
                return (CountryUnit) unit;
            }
        }

        return null;
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.Formation> getAssociatedCountryUnitFormations() {
        return getAssociatedCountryUnitFormationsSet();
    }

    @Deprecated
    public boolean hasAnyAssociatedCountryUnitFormations() {
        return !getAssociatedCountryUnitFormationsSet().isEmpty();
    }

}
