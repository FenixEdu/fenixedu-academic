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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.dataTransferObject.inquiries.DepartmentTeacherDetailsBean;
import net.sourceforge.fenixedu.dataTransferObject.inquiries.DepartmentUCResultsBean;
import net.sourceforge.fenixedu.domain.organizationalStructure.DepartmentUnit;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.presentationTier.Action.departmentMember.DepartmentMemberApp.DepartmentMemberPresidentApp;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.bennu.portal.StrutsFunctionality;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@StrutsFunctionality(app = DepartmentMemberPresidentApp.class, path = "quc-results", titleKey = "title.department.quc.results")
@Mapping(path = "/viewQucResults", module = "departmentMember")
@Forwards({ @Forward(name = "viewResumeResults", path = "/departmentMember/quc/viewResumeResults.jsp"),
        @Forward(name = "viewCompetenceResults", path = "/departmentMember/quc/viewCompetenceResults.jsp"),
        @Forward(name = "viewTeachersResults", path = "/departmentMember/quc/viewTeachersResults.jsp"),
        @Forward(name = "departmentUCView", path = "/departmentMember/quc/departmentUCView.jsp"),
        @Forward(name = "departmentTeacherView", path = "/departmentMember/quc/departmentTeacherView.jsp") })
public class ViewQUCResultsDepartmentDA extends ViewQUCResultsDA {

    @Override
    protected DepartmentUnit getDepartmentUnit(HttpServletRequest request) {
        DepartmentUnit departmentUnit = AccessControl.getPerson().getTeacher().getCurrentWorkingDepartment().getDepartmentUnit();
        return departmentUnit;
    }

    public ActionForward saveTeacherComment(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        final DepartmentTeacherDetailsBean departmentTeacherDetailsBean = getRenderedObject("departmentTeacherDetailsBean");
        departmentTeacherDetailsBean.saveComment();

        RenderUtils.invalidateViewState();
        request.setAttribute("executionSemesterOID", departmentTeacherDetailsBean.getExecutionSemester().getExternalId());

        if (departmentTeacherDetailsBean.isBackToResume()) {
            return resumeResults(actionMapping, actionForm, request, response);
        } else {
            return teachersResults(actionMapping, actionForm, request, response);
        }
    }

    public ActionForward saveExecutionCourseComment(ActionMapping actionMapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        final DepartmentUCResultsBean departmentUCResultsBean = getRenderedObject("departmentUCResultsBean");
        departmentUCResultsBean.saveComment();

        RenderUtils.invalidateViewState();
        request.setAttribute("executionSemesterOID", departmentUCResultsBean.getExecutionCourse().getExecutionPeriod()
                .getExternalId());

        if (departmentUCResultsBean.isBackToResume()) {
            return resumeResults(actionMapping, actionForm, request, response);
        } else {
            return competenceResults(actionMapping, actionForm, request, response);
        }
    }

    @Override
    public boolean getShowAllComments() {
        return false;
    }
}
