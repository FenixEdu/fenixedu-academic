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
import java.util.SortedSet;
import java.util.TreeSet;

import net.sourceforge.fenixedu.domain.CompetenceCourse;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOffice;
import net.sourceforge.fenixedu.domain.degreeStructure.CompetenceCourseInformation;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

import org.fenixedu.spaces.domain.Space;
import org.joda.time.YearMonthDay;

import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

public class CompetenceCourseGroupUnit extends CompetenceCourseGroupUnit_Base {

    private CompetenceCourseGroupUnit() {
        super();
        super.setType(PartyTypeEnum.COMPETENCE_COURSE_GROUP);
    }

    public static Unit createNewInternalCompetenceCourseGroupUnit(MultiLanguageString name, String unitNameCard,
            Integer costCenterCode, String acronym, YearMonthDay beginDate, YearMonthDay endDate, Unit parentUnit,
            AccountabilityType accountabilityType, String webAddress, UnitClassification classification,
            Boolean canBeResponsibleOfSpaces, Space campus) {

        CompetenceCourseGroupUnit competenceCourseGroupUnit = new CompetenceCourseGroupUnit();
        competenceCourseGroupUnit.init(name, unitNameCard, costCenterCode, acronym, beginDate, endDate, webAddress,
                classification, null, canBeResponsibleOfSpaces, campus);
        competenceCourseGroupUnit.addParentUnit(parentUnit, accountabilityType);

        checkIfAlreadyExistsOneCompetenceCourseGroupUnitWithSameAcronymAndName(competenceCourseGroupUnit);

        return competenceCourseGroupUnit;
    }

    @Override
    public void edit(MultiLanguageString unitName, String unitNameCard, Integer unitCostCenter, String acronym,
            YearMonthDay beginDate, YearMonthDay endDate, String webAddress, UnitClassification classification,
            Department department, Degree degree, AdministrativeOffice administrativeOffice, Boolean canBeResponsibleOfSpaces,
            Space campus) {

        super.edit(unitName, unitNameCard, unitCostCenter, acronym, beginDate, endDate, webAddress, classification, department,
                degree, administrativeOffice, canBeResponsibleOfSpaces, campus);

        checkIfAlreadyExistsOneCompetenceCourseGroupUnitWithSameAcronymAndName(this);
    }

    @Override
    public Accountability addParentUnit(Unit parentUnit, AccountabilityType accountabilityType) {
        if (parentUnit != null && (!parentUnit.isInternal() || !parentUnit.isScientificAreaUnit())) {
            throw new DomainException("error.unit.invalid.parentUnit");
        }
        return super.addParentUnit(parentUnit, accountabilityType);
    }

    @Override
    public boolean isCompetenceCourseGroupUnit() {
        return true;
    }

    @Override
    public boolean hasCompetenceCourses(CompetenceCourse competenceCourse) {
        return competenceCourse.getCompetenceCourseGroupUnit().equals(this);
    }

    public ScientificAreaUnit getScientificAreaUnit() {
        if (hasAnyParentUnits()) {
            if (getParentUnits().size() > 1) {
                throw new DomainException("competence.course.group.should.have.only.one.scientific.area");
            }

            return (ScientificAreaUnit) getParentUnits().iterator().next();
        }
        return null;
    }

    @Override
    public DepartmentUnit getDepartmentUnit() {
        ScientificAreaUnit area = getScientificAreaUnit();
        if (area.hasAnyParentUnits()) {
            if (area.getParentUnits().size() > 1) {
                throw new DomainException("scientific.area.should.have.only.one.department");
            }

            return (DepartmentUnit) area.getParentUnits().iterator().next();
        }
        return null;
    }

    public Set<CompetenceCourse> getCompetenceCoursesSet() {
        final SortedSet<CompetenceCourse> result =
                new TreeSet<CompetenceCourse>(CompetenceCourse.COMPETENCE_COURSE_COMPARATOR_BY_NAME);
        for (CompetenceCourseInformation competenceInformation : getCompetenceCourseInformations()) {
            if (competenceInformation.getCompetenceCourse().getCompetenceCourseGroupUnit() == this) {
                result.add(competenceInformation.getCompetenceCourse());
            }
        }
        return result;
    }

    public List<CompetenceCourse> getCompetenceCourses() {
        return new ArrayList<CompetenceCourse>(getCompetenceCoursesSet());
    }

    public List<CompetenceCourse> getCurrentOrFutureCompetenceCourses() {
        final SortedSet<CompetenceCourse> result =
                new TreeSet<CompetenceCourse>(CompetenceCourse.COMPETENCE_COURSE_COMPARATOR_BY_NAME);
        for (CompetenceCourseInformation competenceInformation : getCompetenceCourseInformations()) {
            if (competenceInformation.getCompetenceCourse().getCompetenceCourseGroupUnit() == this) {
                result.add(competenceInformation.getCompetenceCourse());
            }
            if (competenceInformation.getCompetenceCourse().getCompetenceCourseGroupUnit(
                    ExecutionSemester.readLastExecutionSemester()) == this) {
                result.add(competenceInformation.getCompetenceCourse());
            }
        }
        return new ArrayList<CompetenceCourse>(result);
    }

    public List<CompetenceCourse> getOldCompetenceCourses() {
        final SortedSet<CompetenceCourse> result =
                new TreeSet<CompetenceCourse>(CompetenceCourse.COMPETENCE_COURSE_COMPARATOR_BY_NAME);
        for (CompetenceCourseInformation competenceInformation : getCompetenceCourseInformations()) {
            CompetenceCourse course = competenceInformation.getCompetenceCourse();
            if ((course.getDepartmentUnit() != getDepartmentUnit())
                    && (course.getMostRecentGroupInDepartment(getDepartmentUnit()) == this)) {
                result.add(competenceInformation.getCompetenceCourse());
            }
        }
        return new ArrayList<CompetenceCourse>(result);
    }

    @Override
    public void setType(PartyTypeEnum partyTypeEnum) {
        throw new DomainException("unit.impossible.set.type");
    }

    public List<CurricularCourse> getCurricularCourses() {
        List<CompetenceCourse> competenceCourses = getCompetenceCourses();
        List<CurricularCourse> curricularCourses = new ArrayList<CurricularCourse>();

        for (CompetenceCourse competenceCourse : competenceCourses) {
            curricularCourses.addAll(competenceCourse.getAssociatedCurricularCourses());
        }

        return curricularCourses;
    }

    public List<CompetenceCourse> getCompetenceCoursesByExecutionYear(ExecutionYear executionYear) {
        List<CompetenceCourse> competenceCourses = this.getCompetenceCourses();
        List<CompetenceCourse> competenceCoursesByExecutionYear = new ArrayList<CompetenceCourse>();
        for (CompetenceCourse competenceCourse : competenceCourses) {
            if (competenceCourse.hasActiveScopesInExecutionYear(executionYear)) {
                competenceCoursesByExecutionYear.add(competenceCourse);
            }

        }
        return competenceCoursesByExecutionYear;
    }

    @Override
    public void delete() {
        if (hasAnyCompetenceCourseInformations()) {
            throw new DomainException("error.unit.cannot.be.deleted");
        }
        super.delete();
    }

    private static void checkIfAlreadyExistsOneCompetenceCourseGroupUnitWithSameAcronymAndName(
            CompetenceCourseGroupUnit competenceCourseGroupUnit) {
        for (Unit parentUnit : competenceCourseGroupUnit.getParentUnits()) {
            for (Unit unit : parentUnit.getAllSubUnits()) {
                if (!unit.equals(competenceCourseGroupUnit)
                        && competenceCourseGroupUnit.getName().equalsIgnoreCase(unit.getName())) {
                    throw new DomainException("error.unit.already.exists.unit.with.same.name.or.acronym");
                }
            }
        }
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.degreeStructure.CompetenceCourseInformationChangeRequest> getCompetenceCourseInformationChangeRequests() {
        return getCompetenceCourseInformationChangeRequestsSet();
    }

    @Deprecated
    public boolean hasAnyCompetenceCourseInformationChangeRequests() {
        return !getCompetenceCourseInformationChangeRequestsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.degreeStructure.CompetenceCourseInformation> getCompetenceCourseInformations() {
        return getCompetenceCourseInformationsSet();
    }

    @Deprecated
    public boolean hasAnyCompetenceCourseInformations() {
        return !getCompetenceCourseInformationsSet().isEmpty();
    }

}
