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
package org.fenixedu.academic.ui.struts.action.departmentMember;

import java.util.ArrayList;
import java.util.List;

import org.fenixedu.academic.domain.ExecutionSemester;
import org.fenixedu.academic.domain.Teacher;
import org.fenixedu.academic.domain.inquiries.ExecutionCourseAudit;
import org.fenixedu.academic.predicate.AccessControl;
import org.fenixedu.academic.ui.struts.action.departmentMember.DepartmentMemberApp.DepartmentMemberTeacherApp;

import org.fenixedu.bennu.struts.annotations.Forward;
import org.fenixedu.bennu.struts.annotations.Forwards;
import org.fenixedu.bennu.struts.annotations.Mapping;
import org.fenixedu.bennu.struts.portal.StrutsFunctionality;

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
        List<ExecutionCourseAudit> result = new ArrayList<ExecutionCourseAudit>();
        for (ExecutionCourseAudit executionCourseAudit : teacher.getExecutionCourseAuditsSet()) {
            if (executionCourseAudit.getExecutionCourse().getExecutionPeriod() == executionSemester) {
                result.add(executionCourseAudit);
            }
        }
        return result;
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
