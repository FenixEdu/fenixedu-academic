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
package net.sourceforge.fenixedu.presentationTier.Action.student.inquiries;

import java.util.List;

import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.inquiries.ExecutionCourseAudit;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.presentationTier.Action.departmentMember.QUCAuditorDA;
import net.sourceforge.fenixedu.presentationTier.Action.student.StudentApplication.StudentParticipateApp;

import org.fenixedu.bennu.portal.StrutsFunctionality;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

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
        return student.getExecutionCourseAudits(executionSemester);
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
