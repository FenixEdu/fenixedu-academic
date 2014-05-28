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
package net.sourceforge.fenixedu.presentationTier.Action.student;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.commons.ReadExecutionCourseByOID;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.student.ReadExecutionCourseProjects;
import net.sourceforge.fenixedu.dataTransferObject.ISiteComponent;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteProjects;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.bennu.core.domain.User;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

/**
 * @author asnr & scpo
 * 
 */
@Mapping(module = "student", path = "/viewExecutionCourseProjects", functionality = ViewEnroledExecutionCoursesAction.class)
@Forwards(value = { @Forward(name = "sucess", path = "/student/viewExecutionCourseProjects_bd.jsp"),
        @Forward(name = "insucess", path = "/student/viewEnroledExecutionCourses.do?method=prepare"),
        @Forward(name = "noprojects", path = "/student/viewEnroledExecutionCourses.do?method=prepare") })
public class ViewExecutionCourseProjectsAction extends FenixAction {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws FenixActionException, FenixServiceException {

        User userView = getUserView(request);

        String executionCourseCodeString = request.getParameter("executionCourseCode");

        if (executionCourseCodeString == null || executionCourseCodeString.equals("")) {
            ActionErrors actionErrors1 = new ActionErrors();
            ActionError error1 = null;
            // Create an ACTION_ERROR
            error1 = new ActionError("errors.notSelected.executionCourse");
            actionErrors1.add("errors.notSelected.executionCourse", error1);
            saveErrors(request, actionErrors1);
            return mapping.findForward("insucess");
        }

        ISiteComponent viewProjectsComponent = ReadExecutionCourseProjects.run(executionCourseCodeString, userView.getUsername());

        InfoExecutionCourse infoExecutionCourse = ReadExecutionCourseByOID.run(executionCourseCodeString);
        request.setAttribute("infoExecutionCourse", infoExecutionCourse);

        InfoSiteProjects infoSiteProjects = (InfoSiteProjects) viewProjectsComponent;
        List infoGroupPropertiesList = new ArrayList();
        if (infoSiteProjects != null) {
            infoGroupPropertiesList = infoSiteProjects.getInfoGroupPropertiesList();
        } else {
            ActionErrors actionErrors1 = new ActionErrors();
            ActionError error1 = null;
            error1 = new ActionError("errors.noStudentInAttendsSet");
            actionErrors1.add("errors.noStudentInAttendsSet", error1);
            saveErrors(request, actionErrors1);
            return mapping.findForward("noprojects");
        }

        request.setAttribute("infoGroupPropertiesList", infoGroupPropertiesList);

        return mapping.findForward("sucess");

    }

}