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
 * Created on 8/Jan/2005
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
import org.fenixedu.academic.dto.InfoSiteStudentsAndGroups;
import org.fenixedu.academic.service.services.exceptions.FenixServiceException;
import org.fenixedu.academic.service.services.exceptions.InvalidSituationServiceException;
import org.fenixedu.academic.service.services.publico.ReadStudentsAndGroupsByShiftID;
import org.fenixedu.academic.service.services.student.ReadExportGroupingsByGrouping;
import org.fenixedu.academic.ui.struts.action.base.FenixAction;
import org.fenixedu.academic.ui.struts.action.exceptions.FenixActionException;
import org.fenixedu.bennu.struts.annotations.Forward;
import org.fenixedu.bennu.struts.annotations.Forwards;
import org.fenixedu.bennu.struts.annotations.Mapping;

/**
 * @author joaosa & rmalo
 * 
 */
@Mapping(module = "student", path = "/viewStudentsAndGroupsByShift", functionality = ViewEnroledExecutionCoursesAction.class)
@Forwards(value = { @Forward(name = "sucess", path = "/student/viewStudentsAndGroupsByShift_bd.jsp"),
        @Forward(name = "viewExecutionCourseProjects", path = "/student/viewExecutionCourseProjects.do") })
public class ViewStudentsAndGroupsByShiftAction extends FenixAction {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws FenixActionException, FenixServiceException {

        String groupPropertiesCodeString = request.getParameter("groupPropertiesCode");
        String shiftCodeString = request.getParameter("shiftCode");

        InfoSiteStudentsAndGroups infoSiteStudentsAndGroups = new InfoSiteStudentsAndGroups();

        try {
            infoSiteStudentsAndGroups = ReadStudentsAndGroupsByShiftID.run(groupPropertiesCodeString, shiftCodeString);

        } catch (InvalidSituationServiceException e) {
            ActionErrors actionErrors2 = new ActionErrors();
            ActionError error2 = null;
            error2 = new ActionError("error.noProject");
            actionErrors2.add("error.noProject", error2);
            saveErrors(request, actionErrors2);
            return mapping.findForward("viewExecutionCourseProjects");
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }

        request.setAttribute("infoSiteStudentsAndGroups", infoSiteStudentsAndGroups);

        List<InfoExportGrouping> infoExportGroupings = ReadExportGroupingsByGrouping.run(groupPropertiesCodeString);
        request.setAttribute("infoExportGroupings", infoExportGroupings);

        return mapping.findForward("sucess");
    }
}