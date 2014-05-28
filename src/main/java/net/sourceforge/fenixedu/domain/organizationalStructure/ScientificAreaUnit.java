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
import java.util.SortedSet;
import java.util.TreeSet;

import net.sourceforge.fenixedu.domain.CompetenceCourse;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.ScientificAreaSite;
import net.sourceforge.fenixedu.domain.UnitSite;
import net.sourceforge.fenixedu.domain.accessControl.UnitGroup;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOffice;
import net.sourceforge.fenixedu.domain.degreeStructure.Context;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.injectionCode.AccessControl;

import org.apache.commons.lang.StringUtils;
import org.fenixedu.bennu.core.groups.Group;
import org.fenixedu.spaces.domain.Space;
import org.joda.time.YearMonthDay;

import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

public class ScientificAreaUnit extends ScientificAreaUnit_Base {

    private ScientificAreaUnit() {
        super();
        super.setType(PartyTypeEnum.SCIENTIFIC_AREA);
    }

    public static ScientificAreaUnit createNewInternalScientificArea(MultiLanguageString name, String unitNameCard,
            Integer costCenterCode, String acronym, YearMonthDay beginDate, YearMonthDay endDate, Unit parentUnit,
            AccountabilityType accountabilityType, String webAddress, UnitClassification classification,
            Boolean canBeResponsibleOfSpaces, Space campus) {

        ScientificAreaUnit scientificAreaUnit = new ScientificAreaUnit();
        scientificAreaUnit.init(name, unitNameCard, costCenterCode, acronym, beginDate, endDate, webAddress, classification,
                null, canBeResponsibleOfSpaces, campus);
        scientificAreaUnit.addParentUnit(parentUnit, accountabilityType);

        checkIfAlreadyExistsOneScientificAreaUnitWithSameAcronymAndName(scientificAreaUnit);

        return scientificAreaUnit;
    }

    @Override
    public void edit(MultiLanguageString unitName, String unitNameCard, Integer unitCostCenter, String acronym,
            YearMonthDay beginDate, YearMonthDay endDate, String webAddress, UnitClassification classification,
            Department department, Degree degree, AdministrativeOffice administrativeOffice, Boolean canBeResponsibleOfSpaces,
            Space campus) {

        super.edit(unitName, unitNameCard, unitCostCenter, acronym, beginDate, endDate, webAddress, classification, department,
                degree, administrativeOffice, canBeResponsibleOfSpaces, campus);

        checkIfAlreadyExistsOneScientificAreaUnitWithSameAcronymAndName(this);
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
    public Accountability addParentUnit(Unit parentUnit, AccountabilityType accountabilityType) {
        if (parentUnit != null && (!parentUnit.isInternal() || !parentUnit.isDepartmentUnit())) {
            throw new DomainException("error.unit.invalid.parentUnit");
        }
        return super.addParentUnit(parentUnit, accountabilityType);
    }

    @Override
    public boolean isScientificAreaUnit() {
        return true;
    }

    @Override
    public boolean hasCompetenceCourses(final CompetenceCourse competenceCourse) {
        for (Unit subUnit : getSubUnits()) {
            if (subUnit.hasCompetenceCourses(competenceCourse)) {
                return true;
            }
        }
        return false;
    }

    public List<CompetenceCourseGroupUnit> getCompetenceCourseGroupUnits() {
        final SortedSet<CompetenceCourseGroupUnit> result =
                new TreeSet<CompetenceCourseGroupUnit>(CompetenceCourseGroupUnit.COMPARATOR_BY_NAME_AND_ID);
        for (Unit unit : getSubUnits()) {
            if (unit.isCompetenceCourseGroupUnit()) {
                result.add((CompetenceCourseGroupUnit) unit);
            }
        }
        return new ArrayList<CompetenceCourseGroupUnit>(result);
    }

    public Double getScientificAreaUnitEctsCredits() {
        double result = 0.0;
        for (CompetenceCourseGroupUnit competenceCourseGroupUnit : getCompetenceCourseGroupUnits()) {
            for (CompetenceCourse competenceCourse : competenceCourseGroupUnit.getCompetenceCourses()) {
                result += competenceCourse.getEctsCredits();
            }
        }
        return result;
    }

    public Double getScientificAreaUnitEctsCredits(List<Context> contexts) {
        double result = 0.0;
        for (Context context : contexts) {
            if (context.getChildDegreeModule().isLeaf()) {
                CurricularCourse curricularCourse = (CurricularCourse) context.getChildDegreeModule();
                if (!curricularCourse.isOptional() && curricularCourse.getCompetenceCourse().getScientificAreaUnit().equals(this)) {
                    result += curricularCourse.getCompetenceCourse().getEctsCredits();
                }
            }
        }
        return result;
    }

    private static void checkIfAlreadyExistsOneScientificAreaUnitWithSameAcronymAndName(ScientificAreaUnit scientificAreaUnit) {
        for (Unit parentUnit : scientificAreaUnit.getParentUnits()) {
            for (Unit unit : parentUnit.getAllSubUnits()) {
                if (!unit.equals(scientificAreaUnit)
                        && (scientificAreaUnit.getName().equalsIgnoreCase(unit.getName()) || scientificAreaUnit.getAcronym()
                                .equalsIgnoreCase(unit.getAcronym()))) {
                    throw new DomainException("error.unit.already.exists.unit.with.same.name.or.acronym");
                }
            }
        }
    }

    @Override
    protected UnitSite createSite() {
        return new ScientificAreaSite(this);
    }

    public boolean isUserMemberOfScientificArea(Person person) {
        for (Contract contract : getWorkingContracts()) {
            if (contract.getPerson().equals(person)) {
                return true;
            }
        }
        return false;
    }

    public boolean isCurrentUserMemberOfScientificArea() {
        return isUserMemberOfScientificArea(AccessControl.getPerson());
    }

    @Override
    protected List<Group> getDefaultGroups() {
        List<Group> groups = super.getDefaultGroups();
        groups.add(UnitGroup.workers(this));
        return groups;
    }
}
