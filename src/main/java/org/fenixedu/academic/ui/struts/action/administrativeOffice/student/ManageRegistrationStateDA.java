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
package org.fenixedu.academic.ui.struts.action.administrativeOffice.student;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.student.Registration;
import org.fenixedu.academic.domain.student.RegistrationStateLog;
import org.fenixedu.academic.domain.student.registrationStates.RegistrationState;
import org.fenixedu.academic.dto.VariantBean;
import org.fenixedu.academic.dto.student.RegistrationStateBean;
import org.fenixedu.academic.predicate.AccessControl;
import org.fenixedu.academic.service.services.commons.FactoryExecutor;
import org.fenixedu.academic.service.services.exceptions.FenixServiceException;
import org.fenixedu.academic.ui.struts.action.base.FenixDispatchAction;
import org.fenixedu.bennu.struts.annotations.Forward;
import org.fenixedu.bennu.struts.annotations.Forwards;
import org.fenixedu.bennu.struts.annotations.Mapping;
import org.fenixedu.bennu.struts.portal.EntryPoint;

import pt.ist.fenixframework.FenixFramework;

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

    public static class RegistrationStateDeleter extends VariantBean implements FactoryExecutor {

        public RegistrationStateDeleter(String externalId) {
            super();
            setString(externalId);
        }

        @Override
        public Object execute() {
            FenixFramework.<RegistrationState> getDomainObject(getString()).delete();
            return null;
        }
    }

    public static class RegistrationStateCreator extends RegistrationStateBean implements FactoryExecutor {

        public RegistrationStateCreator(Registration registration) {
            super(registration);
        }

        @Override
        public Object execute() {
            final RegistrationState registrationState = RegistrationState.createRegistrationState(getRegistration(),
                    getResponsible(), getStateDateTime(), getStateType(), getExecutionInterval());
            registrationState.setRemarks(getRemarks());
            return registrationState;
        }
    }

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
