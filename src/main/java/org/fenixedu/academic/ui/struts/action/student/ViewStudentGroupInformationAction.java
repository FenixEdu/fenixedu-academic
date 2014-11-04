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
/*
 * Created on 26/Ago/2003
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
import org.fenixedu.academic.dto.InfoSiteStudentGroup;
import org.fenixedu.academic.service.services.exceptions.ExistingServiceException;
import org.fenixedu.academic.service.services.exceptions.FenixServiceException;
import org.fenixedu.academic.service.services.exceptions.InvalidArgumentsServiceException;
import org.fenixedu.academic.service.services.exceptions.InvalidSituationServiceException;
import org.fenixedu.academic.service.services.student.ReadExportGroupingsByGrouping;
import org.fenixedu.academic.service.services.student.ReadStudentGroupInformation;
import org.fenixedu.academic.service.services.student.VerifyGroupingAndStudentGroupWithoutShift;
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
@Mapping(module = "student", path = "/viewStudentGroupInformation", formBean = "enroledExecutionCoursesForm",
        functionality = ViewEnroledExecutionCoursesAction.class)
@Forwards(value = { @Forward(name = "sucess", path = "/student/viewStudentGroupInformation_bd.jsp"),
        @Forward(name = "viewShiftsAndGroups", path = "/student/viewShiftsAndGroups.do"),
        @Forward(name = "viewExecutionCourseProjects", path = "/student/viewExecutionCourseProjects.do") })
public class ViewStudentGroupInformationAction extends FenixAction {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws FenixActionException, FenixServiceException {

        User userView = getUserView(request);

        String studentGroupCodeString = request.getParameter("studentGroupCode");
        String shiftCodeString = request.getParameter("shiftCode");
        String groupPropertiesCodeString = request.getParameter("groupPropertiesCode");

        InfoSiteStudentGroup viewStudentGroup;
        try {
            Integer type =
                    VerifyGroupingAndStudentGroupWithoutShift.run(studentGroupCodeString, groupPropertiesCodeString,
                            shiftCodeString, userView.getUsername());
            viewStudentGroup = ReadStudentGroupInformation.run(studentGroupCodeString);
            request.setAttribute("ShiftType", type);
        } catch (ExistingServiceException e) {
            ActionErrors actionErrors = new ActionErrors();
            ActionError error = null;
            error = new ActionError("error.noProject");
            actionErrors.add("error.noProject", error);
            saveErrors(request, actionErrors);
            return mapping.findForward("viewExecutionCourseProjects");
        } catch (InvalidSituationServiceException e) {
            ActionErrors actionErrors = new ActionErrors();
            ActionError error = null;
            error = new ActionError("error.noGroup");
            actionErrors.add("error.noGroup", error);
            saveErrors(request, actionErrors);
            return mapping.findForward("viewShiftsAndGroups");
        } catch (InvalidArgumentsServiceException e) {
            ActionErrors actionErrors = new ActionErrors();
            ActionError error = null;
            error = new ActionError("error.StudentGroupShiftIsChanged");
            actionErrors.add("error.StudentGroupShiftIsChanged", error);
            saveErrors(request, actionErrors);
            return mapping.findForward("viewShiftsAndGroups");
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }

        InfoSiteStudentGroup infoSiteStudentGroup = viewStudentGroup;
        request.setAttribute("infoSiteStudentGroup", infoSiteStudentGroup);

        List<InfoExportGrouping> infoExportGroupings = ReadExportGroupingsByGrouping.run(groupPropertiesCodeString);
        request.setAttribute("infoExportGroupings", infoExportGroupings);

        return mapping.findForward("sucess");
    }
}