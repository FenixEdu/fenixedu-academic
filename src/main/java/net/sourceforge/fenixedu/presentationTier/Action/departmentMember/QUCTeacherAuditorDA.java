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
package net.sourceforge.fenixedu.presentationTier.Action.departmentMember;

import java.util.List;

import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.inquiries.ExecutionCourseAudit;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.presentationTier.Action.departmentMember.DepartmentMemberApp.DepartmentMemberTeacherApp;

import org.fenixedu.bennu.portal.StrutsFunctionality;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@StrutsFunctionality(app = DepartmentMemberTeacherApp.class, path = "quc-audit", titleKey = "title.inquiry.quc.auditProcesses",
        bundle = "InquiriesResources")
@Mapping(path = "/qucAudit", module = "departmentMember")
@Forwards({ @Forward(name = "viewAuditProcesses", path = "/departmentMember/quc/viewAuditProcesses.jsp"),
        @Forward(name = "viewProcessDetails", path = "/departmentMember/quc/viewProcessDetails.jsp"),
        @Forward(name = "editProcess", path = "/departmentMember/quc/editProcess.jsp"),
        @Forward(name = "manageAuditFiles", path = "/departmentMember/quc/manageAuditFiles.jsp") })
public class QUCTeacherAuditorDA extends QUCAuditorDA {

    @Override
    protected List<ExecutionCourseAudit> getExecutionCoursesAudit(ExecutionSemester executionSemester) {
        Teacher teacher = AccessControl.getPerson().getTeacher();
        return teacher.getExecutionCourseAudits(executionSemester);
    }

    @Override
    protected boolean isTeacher() {
        return true;
    }

    @Override
    protected String getApprovedSelf() {
        return "approvedByTeacher";
    }

    @Override
    protected String getApprovedOther() {
        return "approvedByStudent";
    }
}
