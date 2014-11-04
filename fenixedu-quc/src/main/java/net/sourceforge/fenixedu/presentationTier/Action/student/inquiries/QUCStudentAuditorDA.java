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
package org.fenixedu.academic.ui.struts.action.student.inquiries;

import java.util.ArrayList;
import java.util.List;

import org.fenixedu.academic.domain.ExecutionSemester;
import org.fenixedu.academic.domain.inquiries.ExecutionCourseAudit;
import org.fenixedu.academic.domain.student.Student;
import org.fenixedu.academic.predicate.AccessControl;
import org.fenixedu.academic.ui.struts.action.departmentMember.QUCAuditorDA;
import org.fenixedu.academic.ui.struts.action.student.StudentApplication.StudentParticipateApp;

import org.fenixedu.bennu.struts.annotations.Forward;
import org.fenixedu.bennu.struts.annotations.Forwards;
import org.fenixedu.bennu.struts.annotations.Mapping;
import org.fenixedu.bennu.struts.portal.StrutsFunctionality;

@StrutsFunctionality(app = StudentParticipateApp.class, path = "quc-audit-processes",
        titleKey = "title.inquiry.quc.auditProcesses", bundle = "InquiriesResources")
@Mapping(path = "/qucAudit", module = "student")
@Forwards({ @Forward(name = "viewAuditProcesses", path = "/student/inquiries/viewAuditProcesses.jsp"),
        @Forward(name = "viewProcessDetails", path = "/student/inquiries/viewProcessDetails.jsp"),
        @Forward(name = "editProcess", path = "/student/inquiries/editProcess.jsp"),
        @Forward(name = "manageAuditFiles", path = "/student/inquiries/manageAuditFiles.jsp") })
public class QUCStudentAuditorDA extends QUCAuditorDA {

    @Override
    protected List<ExecutionCourseAudit> getExecutionCoursesAudit(ExecutionSemester executionSemester) {
        Student student = AccessControl.getPerson().getStudent();
        List<ExecutionCourseAudit> result = new ArrayList<ExecutionCourseAudit>();
        for (ExecutionCourseAudit executionCourseAudit : student.getExecutionCourseAuditsSet()) {
            if (executionCourseAudit.getExecutionCourse().getExecutionPeriod() == executionSemester) {
                result.add(executionCourseAudit);
            }
        }
        return result;
    }

    @Override
    protected boolean isTeacher() {
        return false;
    }

    @Override
    protected String getApprovedSelf() {
        return "approvedByStudent";
    }

    @Override
    protected String getApprovedOther() {
        return "approvedByTeacher";
    }
}
