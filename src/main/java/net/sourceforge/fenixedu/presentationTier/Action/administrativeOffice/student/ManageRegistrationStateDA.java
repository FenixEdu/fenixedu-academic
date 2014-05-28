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
package net.sourceforge.fenixedu.presentationTier.Action.administrativeOffice.student;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.student.RegistrationStateBean;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.exceptions.DomainExceptionWithLabelFormatter;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.student.RegistrationStateLog;
import net.sourceforge.fenixedu.domain.student.registrationStates.RegistrationState.RegistrationStateCreator;
import net.sourceforge.fenixedu.domain.student.registrationStates.RegistrationState.RegistrationStateDeleter;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.bennu.portal.EntryPoint;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
@Mapping(path = "/manageRegistrationState", module = "academicAdministration", functionality = SearchForStudentsDA.class)
@Forwards({
        @Forward(name = "showRegistrationStates", path = "/academicAdminOffice/student/registration/manageRegistrationState.jsp"),
        @Forward(name = "deleteActualInfoConfirm",
                path = "/academicAdminOffice/student/registration/deleteRegistrationActualInfo.jsp"),
        @Forward(name = "viewRegistrationStateLogChanges",
                path = "/academicAdminOffice/student/registration/viewRegistrationLogChanges.jsp"), })
public class ManageRegistrationStateDA extends FenixDispatchAction {

    @EntryPoint
    public ActionForward prepare(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {

        final Registration registration = getAndTransportRegistration(request);
        request.setAttribute("registrationStateBean", new RegistrationStateCreator(registration));
        return mapping.findForward("showRegistrationStates");
    }

    public ActionForward createNewState(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {

        try {
            final RegistrationStateCreator creator = (RegistrationStateCreator) getFactoryObject();
            creator.setResponsible(AccessControl.getPerson());
            executeFactoryMethod(creator);

            addActionMessage(request, "message.success.state.edit");
        } catch (DomainExceptionWithLabelFormatter e) {
            addActionMessage(request, e.getKey(), solveLabelFormatterArgs(request, e.getLabelFormatterArgs()));
        } catch (DomainException e) {
            addActionMessage(request, e.getMessage(), e.getArgs());
        }

        final Registration registration = ((RegistrationStateBean) getRenderedObject()).getRegistration();
        request.setAttribute("registration", registration);
        request.setAttribute("registrationStateBean", new RegistrationStateCreator(registration));
        return mapping.findForward("showRegistrationStates");
    }

    public ActionForward deleteState(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {

        try {
            executeFactoryMethod(new RegistrationStateDeleter(request.getParameter("registrationStateId")));
            addActionMessage(request, "message.success.state.delete");
        } catch (DomainException e) {
            addActionMessage(request, e.getMessage());
        }

        return prepare(mapping, actionForm, request, response);
    }

    private Registration getAndTransportRegistration(final HttpServletRequest request) {
        final Registration registration = getDomainObject(request, "registrationId");
        request.setAttribute("registration", registration);
        return registration;
    }

    public ActionForward viewRegistrationStateLog(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {
        final Registration registration = getDomainObject(request, "registrationId");

        Collection<RegistrationStateLog> logsList = registration.getRegistrationStateLogSet();
        request.setAttribute("registration", registration);
        request.setAttribute("logsList", logsList);
        return mapping.findForward("viewRegistrationStateLogChanges");
    }
}
