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

import java.util.Collections;
import java.util.List;

import org.fenixedu.academic.domain.ExecutionSemester;
import org.fenixedu.academic.domain.OccupationPeriod;
import org.fenixedu.academic.domain.Teacher;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.teacher.CategoryType;

import org.apache.commons.collections.comparators.ReverseComparator;
import org.fenixedu.bennu.core.domain.Bennu;
import org.joda.time.LocalDate;

import pt.ist.fenixframework.Atomic;
import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

public class ProfessionalCategory extends ProfessionalCategory_Base implements Comparable<ProfessionalCategory> {

    public ProfessionalCategory(final String giafId, final MultiLanguageString name, final CategoryType categoryType) {
        super();
        String[] args1 = {};
        if (giafId == null || giafId.isEmpty()) {
            throw new DomainException("", args1);
        }
        String[] args = {};
        if (name == null) {
            throw new DomainException("", args);
        }
        setRootDomainObject(Bennu.getInstance());
        setGiafId(giafId);
        setName(name);
        setCategoryType(categoryType);
    }

    @Atomic
    public void edit(final MultiLanguageString name, final CategoryType categoryType) {
        String[] args = {};
        if (name == null) {
            throw new DomainException("", args);
        }
        setName(name);
        setCategoryType(categoryType);
    }

    @Override
    public int compareTo(ProfessionalCategory otherProfessionalCategory) {
        int categoryTypeCompare = getCategoryType().compareTo(otherProfessionalCategory.getCategoryType());
        if (categoryTypeCompare == 0) {
            if (getWeight() == null) {
                return otherProfessionalCategory.getWeight() == null ? getName().compareTo(otherProfessionalCategory.getName()) : -1;
            } else {
                if (otherProfessionalCategory.getWeight() == null) {
                    return 1;
                } else {
                    final int weightCompare = getWeight().compareTo(otherProfessionalCategory.getWeight());
                    return weightCompare == 0 ? getName().compareTo(otherProfessionalCategory.getName()) : weightCompare;
                }
            }

        }
        return categoryTypeCompare;
    }

    protected boolean isTeacherCategoryType() {
        return getCategoryType().equals(CategoryType.TEACHER);
    }

    public boolean isTeacherCareerCategory() {
        return isTeacherCategoryType() && !isTeacherMonitorCategory();
    }

    public boolean isTeacherMonitorCategory() {
        return isTeacherCategoryType() && getName().getContent(MultiLanguageString.pt).matches("(?i).*Monitor.*");
    }

    public boolean isTeacherProfessorCategory() {
        return isTeacherCategoryType() && !isTeacherMonitorCategory() && !isTeacherInvitedCategory();
    }

    public boolean isTeacherInvitedCategory() {
        return isTeacherCategoryType()
                && !isTeacherMonitorCategory()
                && (getName().getContent(MultiLanguageString.pt).matches("(?i).*Convidado.*") || getName().getContent(
                        MultiLanguageString.pt).matches("(?i).*Equip.*"));
    }

    public boolean isTeacherInvitedProfessorCategory() {
        return isTeacherCategoryType() && !isTeacherMonitorCategory() && !isTeacherInvitedAssistantCategory()
                && isTeacherInvitedCategory();
    }

    public boolean isTeacherInvitedAssistantCategory() {
        return isTeacherCategoryType() && isTeacherAssistantCategory() && isTeacherInvitedCategory();
    }

    public boolean isTeacherProfessorCategoryAboveAssistant() {
        return isTeacherCategoryType() && !isTeacherMonitorCategory() && !isTeacherAssistantCategory()
                && isTeacherProfessorCategory();
    }

    public boolean isTeacherAssistantCategory() {
        return isTeacherCategoryType() && !isTeacherMonitorCategory() && !isTeacherInvitedCategory()
                && getName().getContent(MultiLanguageString.pt).matches("(?i).*Assistente.*");
    }

    public static ProfessionalCategory find(final String string, final CategoryType categoryType) {
        for (final ProfessionalCategory professionalCategory : Bennu.getInstance().getProfessionalCategoriesSet()) {
            if (professionalCategory.getCategoryType() == categoryType
                    && professionalCategory.getName().getContent().equalsIgnoreCase(string)) {
                return professionalCategory;
            }
        }
        return null;
    }

    public static boolean isMonitor(Teacher teacher, ExecutionSemester executionSemester) {
        if (executionSemester != null) {
            ProfessionalCategory category = getCategoryByPeriod(teacher, executionSemester);
            return (category != null && category.isTeacherMonitorCategory());
        }
        return false;
    }

    public static boolean isAssistant(Teacher teacher, ExecutionSemester executionSemester) {
        if (executionSemester != null) {
            ProfessionalCategory category = getCategoryByPeriod(teacher, executionSemester);
            return (category != null && category.isTeacherAssistantCategory());
        }
        return false;
    }

    public static boolean isTeacherCareerCategory(Teacher teacher, ExecutionSemester executionSemester) {
        if (executionSemester != null) {
            ProfessionalCategory category = getCategoryByPeriod(teacher, executionSemester);
            return (category != null && category.isTeacherCareerCategory());
        }
        return false;
    }

    public static boolean isTeacherProfessorCategory(Teacher teacher, ExecutionSemester executionSemester) {
        if (executionSemester != null) {
            ProfessionalCategory category = getCategoryByPeriod(teacher, executionSemester);
            return (category != null && category.isTeacherProfessorCategory());
        }
        return false;
    }

    public static ProfessionalCategory getCategory(Teacher teacher) {
        ProfessionalCategory category = ProfessionalCategory.getCurrentCategory(teacher);
        if (category == null) {
            PersonProfessionalData personProfessionalData = teacher.getPerson().getPersonProfessionalData();
            return personProfessionalData == null ? null : personProfessionalData
                    .getLastProfessionalCategoryByCategoryType(CategoryType.TEACHER);
        }
        return category;
    }

    public static ProfessionalCategory getCurrentCategory(Teacher teacher) {
        ProfessionalCategory professionalCategory = null;
        PersonProfessionalData personProfessionalData = teacher.getPerson().getPersonProfessionalData();
        if (personProfessionalData != null) {
            professionalCategory =
                    personProfessionalData.getProfessionalCategoryByCategoryType(CategoryType.TEACHER, new LocalDate());
        }
        if (professionalCategory == null) {
            professionalCategory =
                    teacher.getTeacherAuthorization().map(a -> a.getTeacherCategory().getProfessionalCategory()).orElse(null);
        }
        return professionalCategory;
    }

    public static ProfessionalCategory getLastCategory(Teacher teacher, LocalDate begin, LocalDate end) {
        ProfessionalCategory professionalCategory = null;
        PersonProfessionalData personProfessionalData = teacher.getPerson().getPersonProfessionalData();
        if (personProfessionalData != null) {
            professionalCategory =
                    personProfessionalData.getLastProfessionalCategoryByCategoryType(CategoryType.TEACHER, begin, end);
        }
        if (professionalCategory == null) {
            List<ExecutionSemester> executionSemesters = ExecutionSemester.readExecutionPeriodsInTimePeriod(begin, end);
            Collections.sort(executionSemesters, new ReverseComparator(ExecutionSemester.COMPARATOR_BY_SEMESTER_AND_YEAR));
            for (ExecutionSemester executionSemester : executionSemesters) {
                professionalCategory =
                        teacher.getTeacherAuthorization(executionSemester.getAcademicInterval())
                                .map(a -> a.getTeacherCategory().getProfessionalCategory()).orElse(null);
                if (professionalCategory != null) {
                    return professionalCategory;
                }
            }
        }
        return professionalCategory;
    }

    public static ProfessionalCategory getCategoryByPeriod(Teacher teacher, ExecutionSemester executionSemester) {
        OccupationPeriod lessonsPeriod = executionSemester.getLessonsPeriod();
        return getLastCategory(teacher, lessonsPeriod.getStartYearMonthDay().toLocalDate(), lessonsPeriod
                .getEndYearMonthDayWithNextPeriods().toLocalDate());
    }
}
