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
package org.fenixedu.academic.domain.time.calendarStructure;

import java.util.Iterator;
import java.util.List;

import org.fenixedu.academic.domain.ExecutionSemester;
import org.fenixedu.academic.domain.credits.CreditsPersonFunctionsSharedQueueJob;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.organizationalStructure.PartyTypeEnum;
import org.fenixedu.academic.domain.organizationalStructure.PersonFunction;
import org.fenixedu.academic.domain.organizationalStructure.Unit;
import org.fenixedu.academic.domain.organizationalStructure.UnitUtils;
import org.fenixedu.academic.domain.person.RoleType;
import org.fenixedu.academic.util.Bundle;

import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.joda.time.DateTime;

import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

public abstract class TeacherCreditsFillingCE extends TeacherCreditsFillingCE_Base {

    protected TeacherCreditsFillingCE() {
        super();
    }

    @Override
    public AcademicCalendarEntry edit(MultiLanguageString title, MultiLanguageString description, DateTime begin, DateTime end,
            AcademicCalendarRootEntry rootEntry, AcademicCalendarEntry templateEntry) {

        throw new DomainException("error.unsupported.operation");
    }

    @Override
    public void delete(AcademicCalendarRootEntry rootEntry) {
        throw new DomainException("error.unsupported.operation");
    }

    @Override
    protected AcademicCalendarEntry createVirtualEntry(AcademicCalendarEntry parentEntry) {
        throw new DomainException("error.unsupported.operation");
    }

    public void edit(DateTime begin, DateTime end) {
        setTimeInterval(begin, end);
        for (AcademicCalendarEntry childEntry : getParentEntry().getChildEntriesWithTemplateEntries(getClass())) {
            if (!childEntry.equals(this) && entriesTimeIntervalIntersection(begin, end)) {
                throw new DomainException("error.AcademicCalendarEntry.dates.intersection");
            }
        }
    }

    private boolean entriesTimeIntervalIntersection(DateTime begin, DateTime end) {
        return !getBegin().isAfter(end) && !getEnd().isBefore(begin);
    }

    @Override
    protected boolean areIntersectionsPossible(AcademicCalendarEntry entryToAdd) {
        return false;
    }

    @Override
    protected boolean isPossibleToChangeTimeInterval() {
        return false;
    }

    @Override
    protected boolean exceededNumberOfChildEntries(AcademicCalendarEntry childEntry) {
        return true;
    }

    @Override
    protected boolean isParentEntryInvalid(AcademicCalendarEntry parentEntry) {
        return !parentEntry.isAcademicSemester();
    }

    @Override
    public boolean isOfType(AcademicPeriod period) {
        return false;
    }

    public boolean containsNow() {
        return !getBegin().isAfterNow() && !getEnd().isBeforeNow();
    }

    @Override
    protected boolean associatedWithDomainEntities() {
        return false;
    }

    public static void editTeacherCreditsPeriod(ExecutionSemester executionSemester, DateTime begin, DateTime end) {

        TeacherCreditsFillingForTeacherCE creditsFillingCE =
                TeacherCreditsFillingForTeacherCE.getTeacherCreditsFillingForTeacher(executionSemester.getAcademicInterval());

        if (creditsFillingCE == null) {

            AcademicCalendarEntry parentEntry = executionSemester.getAcademicInterval().getAcademicCalendarEntry();
            AcademicCalendarRootEntry rootEntry = executionSemester.getAcademicInterval().getAcademicCalendar();

            new TeacherCreditsFillingForTeacherCE(parentEntry, new MultiLanguageString(BundleUtil.getString(Bundle.APPLICATION,
                    "label.TeacherCreditsFillingCE.entry.title")), null, begin, end, rootEntry);
            new CreditsPersonFunctionsSharedQueueJob(executionSemester);
        } else {
            creditsFillingCE.edit(begin, end);
        }
    }

    public static void editDepartmentOfficeCreditsPeriod(ExecutionSemester executionSemester, DateTime begin, DateTime end) {
        TeacherCreditsFillingForDepartmentAdmOfficeCE creditsFillingCE =
                getTeacherCreditsFillingForDepartmentAdmOfficePeriod(executionSemester);

        if (creditsFillingCE == null) {

            AcademicCalendarEntry parentEntry = executionSemester.getAcademicInterval().getAcademicCalendarEntry();
            AcademicCalendarRootEntry rootEntry = executionSemester.getAcademicInterval().getAcademicCalendar();

            new TeacherCreditsFillingForDepartmentAdmOfficeCE(parentEntry, new MultiLanguageString(BundleUtil.getString(
                    Bundle.APPLICATION, "label.TeacherCreditsFillingCE.entry.title")), null, begin, end, rootEntry);

        } else {
            creditsFillingCE.edit(begin, end);
        }
    }

    public static TeacherCreditsFillingForDepartmentAdmOfficeCE getTeacherCreditsFillingForDepartmentAdmOfficePeriod(
            ExecutionSemester executionSemester) {
        return TeacherCreditsFillingForDepartmentAdmOfficeCE
                .getTeacherCreditsFillingForDepartmentAdmOfficePeriod(executionSemester);
    }

    public static boolean isInValidCreditsPeriod(ExecutionSemester executionSemester, RoleType roleType) {
        if (roleType == null) {
            return false;
        }
        if (roleType == RoleType.SCIENTIFIC_COUNCIL) {
            return true;
        }
        TeacherCreditsFillingCE validCreditsPerid = getValidCreditsPeriod(executionSemester, roleType);
        return validCreditsPerid != null && validCreditsPerid.containsNow();
    }

    public static TeacherCreditsFillingCE getValidCreditsPeriod(ExecutionSemester executionSemester, RoleType roleType) {
        switch (roleType) {
        case DEPARTMENT_MEMBER:
            return TeacherCreditsFillingForTeacherCE.getTeacherCreditsFillingForTeacher(executionSemester.getAcademicInterval());
        case DEPARTMENT_ADMINISTRATIVE_OFFICE:
            return getTeacherCreditsFillingForDepartmentAdmOfficePeriod(executionSemester);
        default:
            throw new DomainException("invalid.role.type");
        }
    }

    public static void checkValidCreditsPeriod(ExecutionSemester executionSemester, RoleType roleType) {
        if (roleType != RoleType.SCIENTIFIC_COUNCIL) {
            TeacherCreditsFillingCE validCreditsPerid = getValidCreditsPeriod(executionSemester, roleType);
            if (validCreditsPerid == null) {
                throw new DomainException("message.invalid.credits.period2");
            } else if (!validCreditsPerid.containsNow()) {
                throw new DomainException("message.invalid.credits.period", validCreditsPerid.getBegin().toString(
                        "dd-MM-yyy HH:mm"), validCreditsPerid.getEnd().toString("dd-MM-yyy HH:mm"));
            }
        }
    }

    public static boolean getCanBeEditedByDepartmentAdministrativeOffice(PersonFunction personFunction) {
        ExecutionSemester executionSemester = ExecutionSemester.readByYearMonthDay(personFunction.getBeginDate());
        if (personFunction.isPersonFunctionShared()
                && TeacherCreditsFillingCE.isInValidCreditsPeriod(executionSemester, RoleType.DEPARTMENT_ADMINISTRATIVE_OFFICE)) {
            List<Unit> units = UnitUtils.readAllActiveUnitsByType(PartyTypeEnum.DEPARTMENT);
            units.addAll(UnitUtils.readAllActiveUnitsByType(PartyTypeEnum.DEGREE_UNIT));
            units.addAll(UnitUtils.readAllActiveUnitsByType(PartyTypeEnum.SCIENTIFIC_AREA));
            for (Iterator<Unit> iterator = units.iterator(); iterator.hasNext();) {
                Unit unit = iterator.next();
                if (unit.getUnitName().getIsExternalUnit()) {
                    iterator.remove();
                }
            }
            return units.contains(personFunction.getUnit());
        }
        return false;
    }

}
