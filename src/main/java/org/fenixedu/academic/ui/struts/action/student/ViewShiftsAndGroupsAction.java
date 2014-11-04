/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Academic.
 *
 * FenixEdu Academic is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Academic is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Academic.  If not, see <http://www.gnu.org/licenses/>.
 */
/*
 * Created on 08/Set/2003
 *
 */
package org.fenixedu.academic.ui.struts.action.student;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.academic.dto.InfoExportGrouping;
import org.fenixedu.academic.dto.InfoSiteShiftsAndGroups;
import org.fenixedu.academic.service.services.exceptions.FenixServiceException;
import org.fenixedu.academic.service.services.exceptions.InvalidSituationServiceException;
import org.fenixedu.academic.service.services.exceptions.NotAuthorizedException;
import org.fenixedu.academic.service.services.student.ReadExportGroupingsByGrouping;
import org.fenixedu.academic.service.services.student.ReadShiftsAndGroups;
import org.fenixedu.academic.ui.struts.action.base.FenixAction;
import org.fenixedu.academic.ui.struts.action.exceptions.FenixActionException;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.struts.annotations.Forward;
import org.fenixedu.bennu.struts.annotations.Forwards;
import org.fenixedu.bennu.struts.annotations.Mapping;

/**
 * @author asnr and scpo
 * 
 */
@Mapping(module = "student", path = "/viewShiftsAndGroups", formBean = "enroledExecutionCoursesForm",
        functionality = ViewEnroledExecutionCoursesAction.class)
@Forwards(value = { @Forward(name = "sucess", path = "/student/viewShiftsAndGroups_bd.jsp"),
        @Forward(name = "insucess", path = "/student/viewEnroledExecutionCourses.do?method=prepare") })
public class ViewShiftsAndGroupsAction extends FenixAction {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws FenixActionException, FenixServiceException {

        User userView = getUserView(request);

        String groupPropertiesCodeString = request.getParameter("groupPropertiesCode");

        String username = userView.getUsername();

        List<InfoExportGrouping> infoExportGroupings = ReadExportGroupingsByGrouping.run(groupPropertiesCodeString);
        request.setAttribute("infoExportGroupings", infoExportGroupings);

        InfoSiteShiftsAndGroups infoSiteShiftsAndGroups;

        try {
            infoSiteShiftsAndGroups = (InfoSiteShiftsAndGroups) ReadShiftsAndGroups.run(groupPropertiesCodeString, username);

        } catch (InvalidSituationServiceException e) {
            ActionErrors actionErrors2 = new ActionErrors();
            ActionError error2 = null;
            error2 = new ActionError("error.noProject");
            actionErrors2.add("error.noProject", error2);
            saveErrors(request, actionErrors2);
            return mapping.findForward("viewExecutionCourseProjects");
        } catch (NotAuthorizedException e) {
            ActionErrors actionErrors2 = new ActionErrors();
            ActionError error2 = null;
            error2 = new ActionError("errors.noStudentInAttendsSet");
            actionErrors2.add("errors.noStudentInAttendsSet", error2);
            saveErrors(request, actionErrors2);
            return mapping.findForward("insucess");
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }

        request.setAttribute("infoSiteShiftsAndGroups", infoSiteShiftsAndGroups);

        return mapping.findForward("sucess");
    }
}