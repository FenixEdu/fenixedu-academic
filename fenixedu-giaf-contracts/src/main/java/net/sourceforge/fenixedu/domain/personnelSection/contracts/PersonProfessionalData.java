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

import java.util.HashSet;
import java.util.Set;

import org.fenixedu.academic.domain.ExecutionSemester;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.Teacher;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.person.RoleType;
import org.fenixedu.academic.domain.teacher.CategoryType;

import org.fenixedu.bennu.core.domain.Bennu;
import org.joda.time.Interval;
import org.joda.time.LocalDate;
import org.joda.time.PeriodType;

public class PersonProfessionalData extends PersonProfessionalData_Base {

    public PersonProfessionalData(Person person) {
        super();
        setPerson(person);
    }

    protected Bennu getRootDomainObject() {
        return getPerson().getRootDomainObject();
    }

    public GiafProfessionalData getGiafProfessionalDataByCategoryType(CategoryType categoryType) {
        for (GiafProfessionalData giafProfessionalData : getGiafProfessionalDatasSet()) {
            if (giafProfessionalData.getProfessionalCategory() != null
                    && giafProfessionalData.getProfessionalCategory().getCategoryType().equals(categoryType)) {
                return giafProfessionalData;
            }
        }
        return null;
    }

    public GiafProfessionalData getGiafProfessionalData() {
        if (getGiafProfessionalDatasSet().size() == 1) {
            return getGiafProfessionalDatasSet().iterator().next();
        }
        throw new DomainException("more.than.one.GiafProfessionalData");
    }

    public GiafProfessionalData getGiafProfessionalDataByGiafPersonIdentification(String giafPersonIdentification) {
        for (GiafProfessionalData giafProfessionalData : getGiafProfessionalDatasSet()) {
            if (giafProfessionalData.getGiafPersonIdentification().equalsIgnoreCase(giafPersonIdentification)) {
                return giafProfessionalData;
            }
        }
        return null;
    }

    public ProfessionalCategory getProfessionalCategoryByCategoryType(CategoryType categoryType, LocalDate date) {
        PersonContractSituation lastPersonContractSituation = null;
        GiafProfessionalData giafProfessionalData = getGiafProfessionalData();
        if (giafProfessionalData != null) {
            for (final PersonContractSituation situation : giafProfessionalData.getValidPersonContractSituations()) {
                if (situation.isActive(date) && situation.getProfessionalCategory() != null
                        && situation.getProfessionalCategory().getCategoryType().equals(categoryType)
                        && (lastPersonContractSituation == null || situation.isAfter(lastPersonContractSituation))) {
                    lastPersonContractSituation = situation;
                }
            }
        }
        return lastPersonContractSituation == null ? null : lastPersonContractSituation.getProfessionalCategory();
    }

    public ProfessionalCategory getLastProfessionalCategoryByCategoryType(CategoryType categoryType) {
        return getLastProfessionalCategoryByCategoryType(categoryType, null, null);
    }

    public ProfessionalCategory getLastProfessionalCategoryByCategoryType(CategoryType categoryType, LocalDate beginDate,
            LocalDate endDate) {
        Interval dateInterval = null;
        if (beginDate != null && endDate != null) {
            dateInterval = new Interval(beginDate.toDateTimeAtStartOfDay(), endDate.plusDays(1).toDateTimeAtStartOfDay());
        }
        PersonContractSituation lastPersonContractSituation = null;
        GiafProfessionalData giafProfessionalData = getGiafProfessionalData();
        if (giafProfessionalData != null) {
            for (final PersonContractSituation situation : giafProfessionalData.getValidPersonContractSituations()) {
                if ((dateInterval == null || situation.overlaps(dateInterval))
                        && (categoryType == null || (situation.getProfessionalCategory() != null && situation
                                .getProfessionalCategory().getCategoryType().equals(categoryType)))
                        && (lastPersonContractSituation == null || situation.isAfter(lastPersonContractSituation))) {
                    lastPersonContractSituation = situation;
                }
            }
        }
        return lastPersonContractSituation == null ? null : lastPersonContractSituation.getProfessionalCategory();
    }

    public Set<PersonContractSituation> getPersonContractSituationsByCategoryType(CategoryType categoryType) {
        GiafProfessionalData giafProfessionalData = getGiafProfessionalData();
        Set<PersonContractSituation> result = new HashSet<PersonContractSituation>();
        for (final PersonContractSituation situation : giafProfessionalData.getValidPersonContractSituations()) {
            if (situation.getProfessionalCategory() != null
                    && situation.getProfessionalCategory().getCategoryType().equals(categoryType)) {
                result.add(situation);
            }
        }
        return result;
    }

    public Set<PersonContractSituation> getValidPersonProfessionalExemptionByCategoryType(CategoryType categoryType,
            Interval intervalWithNextPeriods) {
        Set<PersonContractSituation> personProfessionalExemptions = new HashSet<PersonContractSituation>();
        Set<PersonContractSituation> personContractSituations = getPersonContractSituationsByCategoryType(categoryType);
        for (PersonContractSituation personContractSituation : personContractSituations) {
            if (personContractSituation.betweenDates(intervalWithNextPeriods)) {
                PersonProfessionalExemption personProfessionalExemption =
                        personContractSituation.getPersonProfessionalExemption();
                if (personContractSituation.getContractSituation().getServiceExemption()
                        && ((!personContractSituation.getContractSituation().getMustHaveAssociatedExemption()) || (personProfessionalExemption != null && personProfessionalExemption
                                .isLongDuration()))) {
                    personProfessionalExemptions.add(personContractSituation);
                }
            }
        }
        return personProfessionalExemptions;
    }

    public PersonContractSituation getLastPersonContractSituationByCategoryType(CategoryType categoryType) {
        GiafProfessionalData giafProfessionalData = getGiafProfessionalData();
        PersonContractSituation lastPersonContractSituation = null;
        if (giafProfessionalData != null) {
            for (final PersonContractSituation situation : giafProfessionalData.getValidPersonContractSituations()) {
                if (situation.getProfessionalCategory() != null
                        && situation.getProfessionalCategory().getCategoryType().equals(categoryType)
                        && (lastPersonContractSituation == null || situation.isAfter(lastPersonContractSituation))) {
                    lastPersonContractSituation = situation;
                }
            }
        }
        return lastPersonContractSituation;
    }

    public PersonContractSituation getCurrentPersonContractSituationByCategoryType(CategoryType categoryType) {
        LocalDate today = new LocalDate();
        PersonContractSituation currentPersonContractSituation = null;
        GiafProfessionalData giafProfessionalDataByCategoryType = getGiafProfessionalData();
        if (giafProfessionalDataByCategoryType != null) {
            for (final PersonContractSituation situation : giafProfessionalDataByCategoryType.getValidPersonContractSituations()) {
                if (situation.isActive(today)
                        && (situation.getProfessionalCategory() != null && situation.getProfessionalCategory().getCategoryType()
                                .equals(categoryType))
                        && (currentPersonContractSituation == null || situation.isAfter(currentPersonContractSituation))) {
                    currentPersonContractSituation = situation;
                }
            }
        }
        return currentPersonContractSituation;
    }

    public PersonContractSituation getCurrentOrLastPersonContractSituationByCategoryType(CategoryType categoryType) {
        return getCurrentOrLastPersonContractSituationByCategoryType(categoryType, null, null);
    }

    public PersonContractSituation getCurrentOrLastPersonContractSituationByCategoryType(CategoryType categoryType,
            LocalDate beginDate, LocalDate endDate) {
        LocalDate today = new LocalDate();
        Interval dateInterval = null;
        if (beginDate != null && endDate != null) {
            dateInterval = new Interval(beginDate.toDateTimeAtStartOfDay(), endDate.plusDays(1).toDateTimeAtStartOfDay());
        }
        PersonContractSituation lastPersonContractSituation = null;
        PersonContractSituation currentPersonContractSituation = null;
        GiafProfessionalData giafProfessionalDataByCategoryType = getGiafProfessionalData();
        if (giafProfessionalDataByCategoryType != null) {
            for (final PersonContractSituation situation : giafProfessionalDataByCategoryType.getValidPersonContractSituations()) {
                if ((categoryType == null || (situation.getProfessionalCategory() != null && situation.getProfessionalCategory()
                        .getCategoryType().equals(categoryType)))) {
                    if ((dateInterval == null || situation.overlaps(dateInterval))) {
                        if (situation.isActive(today)
                                && (currentPersonContractSituation == null || situation.isAfter(currentPersonContractSituation))) {
                            currentPersonContractSituation = situation;
                        }
                        lastPersonContractSituation = situation;
                    }
                }
            }
        }
        return currentPersonContractSituation != null ? currentPersonContractSituation : lastPersonContractSituation;
    }

    public PersonContractSituation getDominantPersonContractSituationByCategoryType(CategoryType categoryType,
            Interval dateInterval) {
        PersonContractSituation dominantContractSituation = null;
        int dominantContractSituationDays = 0;
        GiafProfessionalData giafProfessionalData = getGiafProfessionalData();
        if (giafProfessionalData != null) {
            for (final PersonContractSituation situation : giafProfessionalData.getValidPersonContractSituations()) {
                if (situation.overlaps(dateInterval)
                        && (situation.getProfessionalCategory() != null && situation.getProfessionalCategory().getCategoryType()
                                .equals(categoryType))) {
                    int thisSituationDays = situation.getDaysInInterval(dateInterval);
                    if (dominantContractSituationDays < thisSituationDays
                            || (dominantContractSituationDays == thisSituationDays && (dominantContractSituation == null || situation
                                    .isAfter(dominantContractSituation)))) {
                        dominantContractSituation = situation;
                        dominantContractSituationDays = thisSituationDays;
                    }
                }
            }
        }
        return dominantContractSituation;
    }

    public ProfessionalRegime getLastProfessionalRegime(GiafProfessionalData giafProfessionalData, LocalDate beginDate,
            LocalDate endDate) {
        PersonProfessionalRegime lastPersonProfessionalRegime = null;
        if (giafProfessionalData != null) {
            for (final PersonProfessionalRegime regime : giafProfessionalData.getValidPersonProfessionalRegimes()) {
                if (regime.betweenDates(beginDate, endDate)
                        && (lastPersonProfessionalRegime == null || regime.isAfter(lastPersonProfessionalRegime))) {
                    lastPersonProfessionalRegime = regime;
                }
            }
            if (lastPersonProfessionalRegime == null) {
                if (giafProfessionalData.getProfessionalRegimeDate() != null
                        && !giafProfessionalData.getProfessionalRegimeDate().isAfter(endDate)) {
                    return giafProfessionalData.getProfessionalRegime();
                }
            }
        }
        return lastPersonProfessionalRegime == null ? null : lastPersonProfessionalRegime.getProfessionalRegime();
    }

    public ProfessionalRegime getDominantProfessionalRegime(GiafProfessionalData giafProfessionalData, Interval interval,
            CategoryType categoryType) {
        PersonProfessionalRegime dominantPersonProfessionalRegime = null;
        int dominantPersonProfessionalRegimeDays = 0;
        if (giafProfessionalData != null) {
            for (final PersonProfessionalRegime regime : giafProfessionalData.getValidPersonProfessionalRegimes()) {
                if (regime.overlaps(interval)
                        && (regime.getProfessionalRegime().getCategoryType() == null || regime.getProfessionalRegime()
                                .getCategoryType().equals(categoryType))) {
                    int thisRegimeDays = regime.getDaysInInterval(interval);
                    if (dominantPersonProfessionalRegimeDays < thisRegimeDays
                            || (dominantPersonProfessionalRegimeDays == thisRegimeDays && (dominantPersonProfessionalRegime == null || regime
                                    .isAfter(dominantPersonProfessionalRegime)))) {
                        dominantPersonProfessionalRegime = regime;
                        dominantPersonProfessionalRegimeDays = thisRegimeDays;
                    }
                }
            }
            if (dominantPersonProfessionalRegime == null) {
                if (giafProfessionalData.getProfessionalRegimeDate() != null
                        && !giafProfessionalData.getProfessionalRegimeDate().isAfter(interval.getEnd().toLocalDate())) {
                    return giafProfessionalData.getProfessionalRegime();
                }
            }
        }
        return dominantPersonProfessionalRegime == null ? null : dominantPersonProfessionalRegime.getProfessionalRegime();
    }

    public ProfessionalRelation getLastProfessionalRelation(GiafProfessionalData giafProfessionalData, LocalDate beginDate,
            LocalDate endDate) {
        PersonProfessionalRelation lastPersonProfessionalRelation = null;
        if (giafProfessionalData != null) {
            for (final PersonProfessionalRelation relation : giafProfessionalData.getValidPersonProfessionalRelations()) {
                if (relation.betweenDates(beginDate, endDate)
                        && (lastPersonProfessionalRelation == null || relation.isAfter(lastPersonProfessionalRelation))) {
                    lastPersonProfessionalRelation = relation;
                }
            }
            if (lastPersonProfessionalRelation == null) {
                if (giafProfessionalData.getProfessionalRegimeDate() != null
                        && !giafProfessionalData.getProfessionalRegimeDate().isAfter(endDate)) {
                    return giafProfessionalData.getProfessionalRelation();
                }
            }
        }
        return lastPersonProfessionalRelation == null ? null : lastPersonProfessionalRelation.getProfessionalRelation();
    }

    public String getEmployer(final RoleType roleType) {
        final CategoryType categoryType = getCategoryTypeFor(roleType);
        final GiafProfessionalData giafProfessionalData =
                categoryType == null ? null : getGiafProfessionalDataByCategoryType(categoryType);
        return giafProfessionalData == null ? null : giafProfessionalData.getEmployer();
    }

    private static CategoryType getCategoryTypeFor(final RoleType roleType) {
        for (final CategoryType categoryType : CategoryType.values()) {
            if (categoryType.name().equals(roleType.name())) {
                return categoryType;
            }
        }
        return null;
    }

    public static boolean isTeacherActiveForSemester(Teacher teacher, ExecutionSemester executionSemester) {
        int minimumWorkingDays = 90;
        int activeDays = 0;
        Interval semesterInterval =
                new Interval(executionSemester.getBeginDateYearMonthDay().toLocalDate().toDateTimeAtStartOfDay(),
                        executionSemester.getEndDateYearMonthDay().toLocalDate().toDateTimeAtStartOfDay());
        PersonProfessionalData personProfessionalData = teacher.getPerson().getPersonProfessionalData();
        if (personProfessionalData != null) {
            GiafProfessionalData giafProfessionalData = personProfessionalData.getGiafProfessionalData();
            if (giafProfessionalData != null) {
                for (final PersonContractSituation situation : giafProfessionalData.getValidPersonContractSituations()) {
                    if (situation.overlaps(semesterInterval) && situation.getProfessionalCategory() != null
                            && situation.getProfessionalCategory().getCategoryType().equals(CategoryType.TEACHER)) {
                        LocalDate beginDate =
                                situation.getBeginDate().isBefore(semesterInterval.getStart().toLocalDate()) ? semesterInterval
                                        .getStart().toLocalDate() : situation.getBeginDate();
                        LocalDate endDate =
                                situation.getEndDate() == null
                                        || situation.getEndDate().isAfter(semesterInterval.getEnd().toLocalDate()) ? semesterInterval
                                        .getEnd().toLocalDate() : situation.getEndDate();
                        int days =
                                new Interval(beginDate.toDateTimeAtStartOfDay(), endDate.toDateTimeAtStartOfDay()).toPeriod(
                                        PeriodType.days()).getDays() + 1;
                        activeDays = activeDays + days;
                    }
                }
            }
        }
        return activeDays >= minimumWorkingDays;
    }

    public static boolean isTeacherActiveOrHasAuthorizationForSemester(Teacher teacher, ExecutionSemester executionSemester) {
        return isTeacherActiveForSemester(teacher, executionSemester)
                || teacher.hasTeacherAuthorization(executionSemester.getAcademicInterval());
    }

    public static boolean isTeacherInactive(Teacher teacher, ExecutionSemester executionSemester) {
        return !isTeacherActiveForSemester(teacher, executionSemester);
    }

    public static boolean hasAnyTeacherContractSituation(Teacher teacher) {
        PersonProfessionalData personProfessionalData = teacher.getPerson().getPersonProfessionalData();
        if (personProfessionalData != null) {
            return !personProfessionalData.getPersonContractSituationsByCategoryType(CategoryType.TEACHER).isEmpty();
        }
        return false;
    }

    public static boolean hasAnyTeacherContractSituation(Teacher teacher, LocalDate beginDate, LocalDate endDate) {
        PersonProfessionalData personProfessionalData = teacher.getPerson().getPersonProfessionalData();
        if (personProfessionalData != null) {
            for (PersonContractSituation personContractSituation : personProfessionalData
                    .getPersonContractSituationsByCategoryType(CategoryType.TEACHER)) {
                if (personContractSituation.betweenDates(beginDate, endDate)) {
                    return true;
                }
            }
        }
        return false;
    }

}
