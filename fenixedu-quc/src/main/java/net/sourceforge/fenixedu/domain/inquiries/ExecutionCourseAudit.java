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

import java.util.ArrayList;
import java.util.List;

import org.fenixedu.academic.domain.ExecutionCourse;
import org.fenixedu.academic.domain.ExecutionSemester;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.Teacher;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.person.RoleType;
import org.fenixedu.academic.domain.student.Student;

import org.fenixedu.bennu.core.domain.Bennu;

import pt.ist.fenixframework.Atomic;

public class ExecutionCourseAudit extends ExecutionCourseAudit_Base {

    public ExecutionCourseAudit(ExecutionCourse executionCourse) {
        super();
        setRootDomainObject(Bennu.getInstance());
        setExecutionCourse(executionCourse);
    }

    public void edit(Person teacher, Person student) {
        if (!student.hasRole(RoleType.STUDENT)) {
            throw new DomainException("error.inquiry.audit.hasToBeStudent");
        }
        if (!teacher.hasRole(RoleType.TEACHER)) {
            throw new DomainException("error.inquiry.audit.hasToBeTeacher");
        }
        setTeacherAuditor(teacher.getTeacher());
        setStudentAuditor(student.getStudent());
    }

    @Atomic
    public void sealProcess(boolean isTeacher) {
        if (isTeacher) {
            setApprovedByTeacher(true);
        } else {
            setApprovedByStudent(true);
        }
    }

    @Atomic
    public void unsealProcess(boolean isTeacher) {
        if (isTeacher) {
            setApprovedByTeacher(false);
        } else {
            setApprovedByStudent(false);
        }
    }

    @Atomic
    public void unsealProcess() {
        setApprovedByTeacher(false);
        setApprovedByStudent(false);
    }

    @Atomic
    public void addFile(String filename, byte[] file) {
        new ExecutionCourseAuditFile(this, filename, file);
    }

    @Atomic
    public void deleteFile(ExecutionCourseAuditFile executionCourseAuditFile) {
        executionCourseAuditFile.delete();
    }

    @Atomic
    public void makeProcessAvailableToView() {
        setAvailableProcess(true);
    }

    @Atomic
    public void makeProcessUnavailableToView() {
        setAvailableProcess(false);
    }

    public boolean isProcessAvailable() {
        if (getAvailableProcess() == null) {
            return false;
        }
        return getAvailableProcess();
    }

}
