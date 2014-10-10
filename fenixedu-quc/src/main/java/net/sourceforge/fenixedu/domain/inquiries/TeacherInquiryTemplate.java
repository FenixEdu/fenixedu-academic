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
package net.sourceforge.fenixedu.domain.inquiries;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.NonRegularTeachingService;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.domain.ShiftType;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.personnelSection.contracts.ProfessionalCategory;
import net.sourceforge.fenixedu.domain.teacher.DegreeTeachingService;

import org.fenixedu.bennu.core.domain.Bennu;
import org.joda.time.DateTime;

public class TeacherInquiryTemplate extends TeacherInquiryTemplate_Base {

    public TeacherInquiryTemplate(DateTime begin, DateTime end) {
        super();
        init(begin, end);
    }

    public static TeacherInquiryTemplate getCurrentTemplate() {
        final Collection<InquiryTemplate> inquiryTemplates = Bennu.getInstance().getInquiryTemplatesSet();
        for (final InquiryTemplate inquiryTemplate : inquiryTemplates) {
            if (inquiryTemplate instanceof TeacherInquiryTemplate && inquiryTemplate.isOpen()) {
                return (TeacherInquiryTemplate) inquiryTemplate;
            }
        }
        return null;
    }

    public static TeacherInquiryTemplate getTemplateByExecutionPeriod(ExecutionSemester executionSemester) {
        final Collection<InquiryTemplate> inquiryTemplates = Bennu.getInstance().getInquiryTemplatesSet();
        for (final InquiryTemplate inquiryTemplate : inquiryTemplates) {
            if (inquiryTemplate instanceof TeacherInquiryTemplate && executionSemester == inquiryTemplate.getExecutionPeriod()) {
                return (TeacherInquiryTemplate) inquiryTemplate;
            }
        }
        return null;
    }

    public static boolean hasToAnswerTeacherInquiry(Person person, Professorship professorship) {
        if (!InquiriesRoot.isAvailableForInquiry(professorship.getExecutionCourse())) {
            return false;
        }
        final Teacher teacher = person.getTeacher();
        boolean mandatoryTeachingService = false;
        if (teacher != null && ProfessionalCategory.isTeacherProfessorCategory(teacher, professorship.getExecutionCourse().getExecutionPeriod())) {
            mandatoryTeachingService = true;
        }

        boolean isToAnswer = true;
        if (mandatoryTeachingService) {
            if (!professorship.getInquiryResultsSet().isEmpty()) {
                return isToAnswer;
            }

            isToAnswer = false;
            final Map<ShiftType, Double> shiftTypesPercentageMap = new HashMap<ShiftType, Double>();
            for (final DegreeTeachingService degreeTeachingService : professorship.getDegreeTeachingServicesSet()) {
                for (final ShiftType shiftType : degreeTeachingService.getShift().getTypes()) {
                    Double percentage = shiftTypesPercentageMap.get(shiftType);
                    if (percentage == null) {
                        percentage = degreeTeachingService.getPercentage();
                    } else {
                        percentage += degreeTeachingService.getPercentage();
                    }
                    shiftTypesPercentageMap.put(shiftType, percentage);
                }
            }
            for (final NonRegularTeachingService nonRegularTeachingService : professorship.getNonRegularTeachingServicesSet()) {
                for (final ShiftType shiftType : nonRegularTeachingService.getShift().getTypes()) {
                    Double percentage = shiftTypesPercentageMap.get(shiftType);
                    if (percentage == null) {
                        percentage = nonRegularTeachingService.getPercentage();
                    } else {
                        percentage += nonRegularTeachingService.getPercentage();
                    }
                    shiftTypesPercentageMap.put(shiftType, percentage);
                }
            }
            for (final ShiftType shiftType : shiftTypesPercentageMap.keySet()) {
                final Double percentage = shiftTypesPercentageMap.get(shiftType);
                if (percentage >= 20) {
                    isToAnswer = true;
                    break;
                }
            }
        }

        return isToAnswer;
    }

    public static Collection<ExecutionCourse> getExecutionCoursesWithTeachingInquiriesToAnswer(Person person) {
        final Collection<ExecutionCourse> result = new ArrayList<ExecutionCourse>();
        final TeacherInquiryTemplate currentTemplate = getCurrentTemplate();
        if (currentTemplate != null) {
            for (final Professorship professorship : person.getProfessorships(currentTemplate.getExecutionPeriod())) {
                final boolean isToAnswer = hasToAnswerTeacherInquiry(person, professorship);
                if (isToAnswer
                        && (professorship.getInquiryTeacherAnswer() == null
                                || professorship.getInquiryTeacherAnswer().hasRequiredQuestionsToAnswer(currentTemplate) || InquiryResultComment
                                    .hasMandatoryCommentsToMake(professorship))) {
                    result.add(professorship.getExecutionCourse());
                }
            }
        }
        return result;
    }
}
