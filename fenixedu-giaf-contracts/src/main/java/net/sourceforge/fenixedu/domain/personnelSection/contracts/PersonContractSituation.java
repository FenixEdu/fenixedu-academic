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
package org.fenixedu.academic.domain.personnelSection.contracts;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import org.fenixedu.academic.domain.ExecutionSemester;
import org.fenixedu.academic.domain.Teacher;
import org.fenixedu.academic.domain.teacher.CategoryType;

import org.fenixedu.bennu.core.domain.Bennu;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.Interval;
import org.joda.time.LocalDate;
import org.joda.time.PeriodType;

public class PersonContractSituation extends PersonContractSituation_Base {

    public PersonContractSituation(final GiafProfessionalData giafProfessionalData, final LocalDate beginDate,
            final LocalDate endDate, final String step, final ContractSituation contractSituation,
            final String contractSituationGiafId, final ProfessionalCategory professionalCategory,
            final String professionalCategoryGiafId, final DateTime creationDate, final DateTime modifiedDate) {
        super();
        setRootDomainObject(Bennu.getInstance());
        setGiafProfessionalData(giafProfessionalData);
        setBeginDate(beginDate);
        setEndDate(endDate);
        setStep(step);
        setContractSituation(contractSituation);
        setContractSituationGiafId(contractSituationGiafId);
        setProfessionalCategory(professionalCategory);
        setProfessionalCategoryGiafId(professionalCategoryGiafId);
        setCreationDate(creationDate);
        setModifiedDate(modifiedDate);
        setImportationDate(new DateTime());
    }

    public boolean hasValidDates() {
        return getBeginDate() != null && (getEndDate() == null || !getBeginDate().isAfter(getEndDate()));
    }

    public boolean isValid() {
        return getContractSituation() != null && hasValidDates();
    }

    public boolean contains(final LocalDate date) {
        return getBeginDate() != null
                && (!getBeginDate().isAfter(date) && (getEndDate() == null || !getEndDate().isBefore(date)));
    }

    public boolean overlaps(final Interval interval) {
        Interval situationInterval = getInterval();
        return getBeginDate() != null
                && ((situationInterval != null && situationInterval.overlaps(interval)) || (situationInterval == null && !getBeginDate()
                        .isAfter(interval.getEnd().toLocalDate())));
    }

    private Interval getInterval() {
        return getBeginDate() != null && getEndDate() != null ? new Interval(getBeginDate().toDateTimeAtStartOfDay(),
                getEndDate().plusDays(1).toDateTimeAtStartOfDay()) : null;
    }

    public boolean isAfter(PersonContractSituation otherPersonContractSituation) {
        if (!isValid()) {
            return false;
        }
        if (!otherPersonContractSituation.isValid()) {
            return true;
        }
        if (getEndDate() == null) {
            return otherPersonContractSituation.getEndDate() == null ? getBeginDate().isAfter(
                    otherPersonContractSituation.getBeginDate()) : true;
        } else {
            return otherPersonContractSituation.getEndDate() == null ? false : getEndDate().isAfter(
                    otherPersonContractSituation.getEndDate());
        }
    }

    public boolean isActive(LocalDate date) {
        return !getBeginDate().isAfter(date) && (getEndDate() == null || !getEndDate().isBefore(date));
    }

    public boolean betweenDates(LocalDate begin, LocalDate end) {
        Interval dateInterval = new Interval(begin.toDateTimeAtStartOfDay(), end.toDateTimeAtStartOfDay().plusDays(1));
        return betweenDates(dateInterval);
    }

    public boolean betweenDates(Interval interval) {
        if (isValid()) {
            if (getEndDate() == null) {
                return !getBeginDate().isAfter(interval.getEnd().toLocalDate());
            }
            return getInterval().overlaps(interval);
        }
        return false;
    }

    public Double getWeeklyLessonHours(Interval interval) {
        if (getContractSituation().getEndSituation() || (getContractSituation().getServiceExemption() && !hasMandatoryCredits())
                || !getContractSituation().getInExercise()) {
            return Double.valueOf(0);
        }
        ProfessionalCategory professionalCategory = getProfessionalCategory();
        ProfessionalRegime professionalRegime = getDominantProfessionalRegime(interval);
        if (professionalCategory != null) {
            if (professionalRegime == null) {
                if (professionalCategory.isTeacherMonitorCategory()) {
                    return Double.valueOf(4);
                } else if (professionalCategory.isTeacherInvitedCategory()) {
                    return Double.valueOf(12);
                } else {
                    return Double.valueOf(9);
                }
            }
            BigDecimal fullTimeEquivalent = professionalRegime.getFullTimeEquivalent();
            if (fullTimeEquivalent == null) {
                Integer weighting = professionalRegime.getWeighting();
                if (weighting != null) {
                    if (weighting.compareTo(100) >= 0) {
                        fullTimeEquivalent = BigDecimal.ONE;
                    } else {
                        fullTimeEquivalent = new BigDecimal(weighting).divide(BigDecimal.valueOf(100));
                    }
                }
            }
            if (fullTimeEquivalent != null) {
                if (fullTimeEquivalent.equals(BigDecimal.ONE)) {
                    if (professionalCategory.isTeacherMonitorCategory()) {
                        return Double.valueOf(4);
                    } else if (professionalCategory.isTeacherInvitedCategory()) {
                        return Double.valueOf(12);
                    } else {
                        return Double.valueOf(9);
                    }
                } else {
                    return fullTimeEquivalent.multiply(new BigDecimal(12)).doubleValue();
                }
            }
        }
        return Double.valueOf(12);
    }

    private ProfessionalRegime getDominantProfessionalRegime(Interval interval) {
        return getGiafProfessionalData().getPersonProfessionalData().getDominantProfessionalRegime(getGiafProfessionalData(),
                interval, CategoryType.TEACHER);
    }

    public PersonProfessionalExemption getPersonProfessionalExemption() {
        PersonProfessionalExemption exemption = null;
        int exemptionDays = 0;
        for (PersonProfessionalExemption personProfessionalExemption : getGiafProfessionalData()
                .getValidPersonProfessionalExemption()) {
            if (personProfessionalExemption.getBeginDate().equals(getBeginDate())) {
                if (personProfessionalExemption instanceof PersonSabbatical) {
                    return personProfessionalExemption;
                }
                int personProfessionalExemptionDays =
                        Days.daysBetween(personProfessionalExemption.getBeginDate(), personProfessionalExemption.getEndDate())
                                .getDays();
                if (personProfessionalExemptionDays > exemptionDays) {
                    exemption = personProfessionalExemption;
                    exemptionDays = personProfessionalExemptionDays;
                }
            }
        }
        return exemption;
    }

    public boolean countForCredits(Interval interval) {
        PersonProfessionalExemption personProfessionalExemption = getPersonProfessionalExemption();
        return getContractSituation().getMustHaveAssociatedExemption() && personProfessionalExemption != null ? personProfessionalExemption
                .getGiveCredits() : (getContractSituation().getServiceExemption() && getContractSituation().getGiveCredits() && isLongDuration(interval));
    }

    private boolean hasMandatoryCredits() {
        PersonProfessionalExemption personProfessionalExemption = getPersonProfessionalExemption();
        return getContractSituation().getMustHaveAssociatedExemption() && personProfessionalExemption != null ? personProfessionalExemption
                .getHasMandatoryCredits() : (getContractSituation().getServiceExemption() && getContractSituation()
                .getHasMandatoryCredits());
    }

    public LocalDate getServiceExemptionEndDate() {
        PersonProfessionalExemption personProfessionalExemption = getPersonProfessionalExemption();
        return getContractSituation().getMustHaveAssociatedExemption() ? getEndDate() != null ? getEndDate() : personProfessionalExemption != null ? personProfessionalExemption
                .getEndDate() : null : getEndDate();
    }

    public int getDaysInInterval(Interval intervalWithNextPeriods) {
        LocalDate beginDate =
                getBeginDate().isBefore(intervalWithNextPeriods.getStart().toLocalDate()) ? intervalWithNextPeriods.getStart()
                        .toLocalDate() : getBeginDate();
        LocalDate endDate =
                getEndDate() == null || getEndDate().isAfter(intervalWithNextPeriods.getEnd().toLocalDate()) ? intervalWithNextPeriods
                        .getEnd().toLocalDate() : getEndDate();
        return Days.daysBetween(beginDate, endDate).getDays();
    }

    public boolean isLongDuration(Interval interval) {
        Integer daysBetween = interval.toPeriod(PeriodType.days()).getDays();
        return (daysBetween == null || daysBetween >= 90);
    }

    public static PersonContractSituation getCurrentOrLastTeacherContractSituation(Teacher teacher) {
        PersonProfessionalData personProfessionalData = teacher.getPerson().getPersonProfessionalData();
        return personProfessionalData != null ? personProfessionalData
                .getCurrentOrLastPersonContractSituationByCategoryType(CategoryType.TEACHER) : null;
    }

    public static PersonContractSituation getCurrentOrLastTeacherContractSituation(Teacher teacher, LocalDate begin, LocalDate end) {
        PersonProfessionalData personProfessionalData = teacher.getPerson().getPersonProfessionalData();
        return personProfessionalData != null ? personProfessionalData.getCurrentOrLastPersonContractSituationByCategoryType(
                CategoryType.TEACHER, begin, end) : null;
    }

    public static PersonContractSituation getDominantTeacherContractSituation(Teacher teacher, Interval interval) {
        PersonProfessionalData personProfessionalData = teacher.getPerson().getPersonProfessionalData();
        return personProfessionalData != null ? personProfessionalData.getDominantPersonContractSituationByCategoryType(
                CategoryType.TEACHER, interval) : null;
    }

    public static Set<PersonContractSituation> getValidTeacherServiceExemptions(Teacher teacher, Interval interval) {
        PersonProfessionalData personProfessionalData = teacher.getPerson().getPersonProfessionalData();
        if (personProfessionalData != null) {
            return personProfessionalData.getValidPersonProfessionalExemptionByCategoryType(CategoryType.TEACHER, interval);
        }
        return new HashSet<PersonContractSituation>();
    }

    public static PersonContractSituation getDominantTeacherServiceExemption(Teacher teacher, ExecutionSemester executionSemester) {
        PersonContractSituation dominantExemption = null;
        int daysInDominantExemption = 0;
        Interval semesterInterval =
                new Interval(executionSemester.getBeginDateYearMonthDay().toLocalDate().toDateTimeAtStartOfDay(),
                        executionSemester.getEndDateYearMonthDay().toLocalDate().toDateTimeAtStartOfDay());
        for (PersonContractSituation personContractSituation : PersonContractSituation.getValidTeacherServiceExemptions(teacher, executionSemester)) {
            int daysInInterval = personContractSituation.getDaysInInterval(semesterInterval);
            if (dominantExemption == null || daysInInterval > daysInDominantExemption) {
                dominantExemption = personContractSituation;
                daysInDominantExemption = daysInInterval;
            }
        }

        return dominantExemption;
    }

    public static Set<PersonContractSituation> getValidTeacherServiceExemptions(Teacher teacher,
            ExecutionSemester executionSemester) {
        PersonProfessionalData personProfessionalData = teacher.getPerson().getPersonProfessionalData();
        Interval semesterInterval =
                new Interval(executionSemester.getBeginDateYearMonthDay().toLocalDate().toDateTimeAtStartOfDay(),
                        executionSemester.getEndDateYearMonthDay().toLocalDate().toDateTimeAtStartOfDay());
        if (semesterInterval != null && personProfessionalData != null) {
            return personProfessionalData.getValidPersonProfessionalExemptionByCategoryType(CategoryType.TEACHER,
                    semesterInterval);
        }
        return new HashSet<PersonContractSituation>();
    }

}
