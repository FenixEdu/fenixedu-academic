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
package org.fenixedu.academic.ui.struts.action.residenceManagement;

import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.accounting.Event;
import org.fenixedu.academic.domain.accounting.ResidenceEvent;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.organizationalStructure.ResidenceManagementUnit;
import org.fenixedu.academic.domain.residence.ResidenceMonth;
import org.fenixedu.academic.dto.VariantBean;
import org.fenixedu.academic.dto.residenceManagement.ImportResidenceEventBean;
import org.fenixedu.academic.predicate.AccessControl;
import org.fenixedu.academic.service.services.accounting.CancelResidenceEvent;
import org.fenixedu.academic.service.services.exceptions.FenixServiceException;
import org.fenixedu.academic.service.services.residenceManagement.CreateResidencePaymentCodes;
import org.fenixedu.academic.service.services.residenceManagement.PayResidenceEvent;
import org.fenixedu.academic.ui.struts.action.base.FenixDispatchAction;
import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.struts.annotations.Forward;
import org.fenixedu.bennu.struts.annotations.Forwards;
import org.fenixedu.bennu.struts.annotations.Mapping;
import org.fenixedu.bennu.struts.portal.EntryPoint;
import org.fenixedu.bennu.struts.portal.StrutsFunctionality;
import org.joda.time.YearMonthDay;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixframework.FenixFramework;

@StrutsFunctionality(app = ResidenceManagerApplication.class, path = "event-management", titleKey = "title.event.management")
@Mapping(path = "/residenceEventManagement", module = "residenceManagement")
@Forwards({ @Forward(name = "manageResidenceEvents", path = "/residenceManagement/eventsManagement.jsp"),
        @Forward(name = "viewPersonResidenceEvents", path = "/residenceManagement/viewPersonResidenceEvents.jsp"),
        @Forward(name = "insertPayingDate", path = "/residenceManagement/insertPayingDate.jsp") })
public class ResidenceEventManagementDispatchAction extends FenixDispatchAction {

    @EntryPoint
    public ActionForward manageResidenceEvents(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {

        ImportResidenceEventBean importResidenceEventBean = getRenderedObject("searchEventMonth");
        if (importResidenceEventBean == null) {
            ResidenceMonth month = getResidenceMonth(request);
            importResidenceEventBean = month != null ? new ImportResidenceEventBean(month) : new ImportResidenceEventBean();
        }

        RenderUtils.invalidateViewState();

        request.setAttribute("currentResidence", getManagementUnit(request));
        request.setAttribute("searchBean", importResidenceEventBean);
        return mapping.findForward("manageResidenceEvents");
    }

    private ResidenceManagementUnit getManagementUnit(HttpServletRequest request) {
        return Bennu.getInstance().getResidenceManagementUnit();
    }

    public ActionForward generatePaymentCodes(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        ResidenceMonth month = getResidenceMonth(request);
        CreateResidencePaymentCodes.run(month.getEventsSet());

        return manageResidenceEvents(mapping, actionForm, request, response);
    }

    private ResidenceMonth getResidenceMonth(HttpServletRequest request) {
        String oid = request.getParameter("monthOID");
        return oid == null ? null : (ResidenceMonth) FenixFramework.getDomainObject(oid);
    }

    public ActionForward viewPersonResidenceEvents(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {

        ResidenceMonth month = getResidenceMonth(request);
        String personOID = request.getParameter("person");

        Person person = FenixFramework.getDomainObject(personOID);
        Set<Event> events = person.getNotPayedEventsPayableOn(null, ResidenceEvent.class, false);
        events.addAll(person.getNotPayedEventsPayableOn(null, ResidenceEvent.class, true));
        events.addAll(person.getPayedEvents(ResidenceEvent.class));

        request.setAttribute("month", month);
        request.setAttribute("person", person);
        request.setAttribute("residenceEvents", events);
        return mapping.findForward("viewPersonResidenceEvents");
    }

    public ActionForward cancelResidenceEvent(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {
        ResidenceEvent residenceEvent = FenixFramework.getDomainObject(request.getParameter("event"));

        try {
            CancelResidenceEvent.run(residenceEvent, AccessControl.getPerson());
        } catch (DomainException e) {
            addErrorMessage(request, e.getMessage(), e.getMessage());
        }

        return viewPersonResidenceEvents(mapping, actionForm, request, response);
    }

    public ActionForward preparePayResidenceEvent(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {

        ResidenceEvent residenceEvent = FenixFramework.getDomainObject(request.getParameter("event"));
        VariantBean bean = new VariantBean();
        bean.setYearMonthDay(new YearMonthDay());
        ResidenceMonth month = getResidenceMonth(request);

        request.setAttribute("month", month);
        request.setAttribute("residenceEvent", residenceEvent);
        request.setAttribute("bean", bean);
        return mapping.findForward("insertPayingDate");

    }

    public ActionForward payResidenceEvent(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {

        ResidenceEvent residenceEvent = (ResidenceEvent) FenixFramework.getDomainObject(request.getParameter("event"));
        YearMonthDay date = getRenderedObject("date");

        try {
            PayResidenceEvent.run(getLoggedPerson(request).getUser(), residenceEvent, date);
        } catch (DomainException e) {
            addErrorMessage(request, e.getMessage(), e.getMessage());
        }

        return viewPersonResidenceEvents(mapping, actionForm, request, response);
    }

}