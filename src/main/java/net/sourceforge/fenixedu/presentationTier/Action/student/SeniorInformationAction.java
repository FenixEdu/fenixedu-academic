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
 * Created on Dec 10, 2004
 *
 */
package net.sourceforge.fenixedu.presentationTier.Action.student;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.student.senior.ReadStudentSenior;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.student.Senior;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.student.StudentApplication.StudentSeniorsApp;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.bennu.portal.EntryPoint;
import org.fenixedu.bennu.portal.StrutsFunctionality;

import pt.ist.fenixWebFramework.renderers.components.state.IViewState;
import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
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