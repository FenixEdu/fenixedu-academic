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
 * Created on Dec 10, 2004
 *
 */
package org.fenixedu.academic.ui.struts.action.student;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.academic.domain.student.Registration;
import org.fenixedu.academic.domain.student.Senior;
import org.fenixedu.academic.domain.student.Student;
import org.fenixedu.academic.service.services.student.senior.ReadStudentSenior;
import org.fenixedu.academic.ui.struts.action.base.FenixDispatchAction;
import org.fenixedu.academic.ui.struts.action.exceptions.FenixActionException;
import org.fenixedu.academic.ui.struts.action.student.StudentApplication.StudentSeniorsApp;
import org.fenixedu.bennu.struts.annotations.Forward;
import org.fenixedu.bennu.struts.annotations.Forwards;
import org.fenixedu.bennu.struts.annotations.Mapping;
import org.fenixedu.bennu.struts.portal.EntryPoint;
import org.fenixedu.bennu.struts.portal.StrutsFunctionality;

import pt.ist.fenixWebFramework.renderers.components.state.IViewState;
import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixframework.FenixFramework;

/**
 * @author Luis Egidio, luis.egidio@ist.utl.pt
 * 
 */
@StrutsFunctionality(app = StudentSeniorsApp.class, path = "senior-information", titleKey = "link.senior.info")
@Mapping(module = "student", path = "/seniorInformation", input = "/seniorInformation.do?method=prepare&page=0")
@Forwards(value = { @Forward(name = "chooseRegistration", path = "/student/senior/chooseRegistration.jsp"),
        @Forward(name = "show-result", path = "/student/senior/seniorInfo.jsp"),
        @Forward(name = "show-form", path = "/student/senior/seniorInfoManagement.jsp") })
public class SeniorInformationAction extends FenixDispatchAction {

    @EntryPoint
    public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        Registration registration = null;

        final String registrationOID;
        Object registrationOIDObj = getFromRequest(request, "registrationOID");
        if (registrationOIDObj != null) {
            registrationOID = registrationOIDObj.toString();
        } else {
            registrationOID = null;
        }
        final Student loggedStudent = getUserView(request).getPerson().getStudent();

        if (registrationOID != null) {
            registration = FenixFramework.getDomainObject(registrationOID);
        } else if (loggedStudent != null) {
            if (loggedStudent.getRegistrationsSet().size() == 1) {
                registration = loggedStudent.getRegistrationsSet().iterator().next();
            } else {
                request.setAttribute("student", loggedStudent);
                return mapping.findForward("chooseRegistration");
            }
        }

        if (registration == null) {
            throw new FenixActionException();
        } else {
            final Senior senior = ReadStudentSenior.run(registration);
            request.setAttribute("senior", senior);
            return mapping.findForward("show-form");
        }
    }

    public ActionForward change(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        final IViewState viewState = RenderUtils.getViewState("editSeniorExpectedInfoID");
        request.setAttribute("senior", viewState.getMetaObject().getObject());

        return mapping.findForward("show-result");
    }

}