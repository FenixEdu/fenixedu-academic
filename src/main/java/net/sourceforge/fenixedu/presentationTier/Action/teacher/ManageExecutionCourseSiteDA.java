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
package net.sourceforge.fenixedu.presentationTier.Action.teacher;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.Item;
import net.sourceforge.fenixedu.domain.Section;
import net.sourceforge.fenixedu.domain.Site;
import net.sourceforge.fenixedu.presentationTier.Action.manager.SiteManagementDA;
import net.sourceforge.fenixedu.presentationTier.Action.teacher.executionCourse.ExecutionCourseBaseAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(path = "/manageExecutionCourseSite", module = "teacher", functionality = ManageExecutionCourseDA.class)
@Forwards({ @Forward(name = "createSection", path = "/commons/sites/createSection.jsp"),
        @Forward(name = "sectionsManagement", path = "/teacher/executionCourse/site/sectionsManagement.jsp"),
        @Forward(name = "section", path = "/commons/sites/section.jsp"),
        @Forward(name = "edit-fileItem-name", path = "/commons/sites/editFileItemDisplayName.jsp"),
        @Forward(name = "editSection", path = "/commons/sites/editSection.jsp"),
        @Forward(name = "createItem", path = "/commons/sites/createItem.jsp"),
        @Forward(name = "editItem", path = "/commons/sites/editItem.jsp"),
        @Forward(name = "uploadFile", path = "/commons/sites/uploadFile.jsp"),
        @Forward(name = "editFile", path = "/commons/sites/editFile.jsp"),
        @Forward(name = "organizeItems", path = "/commons/sites/organizeItems.jsp"),
        @Forward(name = "organizeFiles", path = "/commons/sites/organizeFiles.jsp"),
        @Forward(name = "confirmSectionDelete", path = "/commons/sites/confirmSectionDelete.jsp"),
        @Forward(name = "editSectionPermissions", path = "/commons/sites/editSectionPermissions.jsp"),
        @Forward(name = "editItemPermissions", path = "/commons/sites/editItemPermissions.jsp"),
        @Forward(name = "addInstitutionSection", path = "/commons/sites/addInstitutionSection.jsp") })
public class ManageExecutionCourseSiteDA extends SiteManagementDA {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        ExecutionCourseBaseAction.propageContextIds(request);
        request.setAttribute("siteActionName", "/manageExecutionCourseSite.do");
        request.setAttribute("siteContextParam", "executionCourseID");
        request.setAttribute("siteContextParamValue", ((ExecutionCourse) request.getAttribute("executionCourse")).getExternalId());
        ActionForward forward = super.execute(mapping, actionForm, request, response);
        return ExecutionCourseBaseAction.forward(request, forward.getPath());
    }

    public ExecutionCourse getExecutionCourse(HttpServletRequest request) {
        return (ExecutionCourse) request.getAttribute("executionCourse");
    }

    @Override
    protected String getAuthorNameForFile(HttpServletRequest request) {
        return getExecutionCourse(request).getNome();
    }

    @Override
    protected Site getSite(HttpServletRequest request) {
        ExecutionCourse executionCourse = (ExecutionCourse) request.getAttribute("executionCourse");

        if (executionCourse != null) {
            return executionCourse.getSite();
        } else {
            return null;
        }
    }

    @Override
    protected String getItemLocationForFile(HttpServletRequest request, Item item, Section section) {
        return item.getFullPath();
    }

}
