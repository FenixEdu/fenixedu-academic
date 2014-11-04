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
package org.fenixedu.academic.domain.inquiries;

import org.fenixedu.academic.domain.ExecutionSemester;
import org.fenixedu.academic.domain.student.Student;

import org.fenixedu.bennu.core.domain.Bennu;

public class StudentInquiryExecutionPeriod extends StudentInquiryExecutionPeriod_Base {

    public StudentInquiryExecutionPeriod() {
        super();
        setRootDomainObject(Bennu.getInstance());
    }

    public StudentInquiryExecutionPeriod(Student student, ExecutionSemester executionSemester) {
        this();
        setStudent(student);
        setExecutionPeriod(executionSemester);
    }

    public static StudentInquiryExecutionPeriod getStudentInquiryExecutionPeriod(Student student,
            ExecutionSemester executionSemester) {
        for (final StudentInquiryExecutionPeriod studentInquiryExecutionPeriod : student
                .getStudentsInquiriesExecutionPeriodsSet()) {
            if (studentInquiryExecutionPeriod.getExecutionPeriod() == executionSemester) {
                return studentInquiryExecutionPeriod;
            }
        }
        return null;
    }

    public static boolean isWeeklySpentHoursSubmittedForOpenInquiry(Student student, ExecutionSemester executionSemester) {
        for (final StudentInquiryExecutionPeriod studentInquiryExecutionPeriod : student
                .getStudentsInquiriesExecutionPeriodsSet()) {
            if (studentInquiryExecutionPeriod.getExecutionPeriod() == executionSemester) {
                return studentInquiryExecutionPeriod.getWeeklyHoursSpentInClassesSeason() != null;
            }
        }
        return false;
    }

    public static boolean isWeeklySpentHoursSubmittedForOpenInquiry(Student student) {
        StudentInquiryTemplate inquiryTemplate = StudentInquiryTemplate.getCurrentTemplate();
        return inquiryTemplate == null ? false : isWeeklySpentHoursSubmittedForOpenInquiry(student,
                inquiryTemplate.getExecutionPeriod());
    }

    public static StudentInquiryExecutionPeriod getOpenStudentInquiryExecutionPeriod(Student student) {
        StudentInquiryTemplate inquiryTemplate = StudentInquiryTemplate.getCurrentTemplate();
        return inquiryTemplate == null ? null : getStudentInquiryExecutionPeriod(student, inquiryTemplate.getExecutionPeriod());
    }

}
