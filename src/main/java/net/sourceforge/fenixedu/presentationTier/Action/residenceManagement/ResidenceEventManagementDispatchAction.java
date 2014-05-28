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
package net.sourceforge.fenixedu.presentationTier.Action.residenceManagement;

import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.accounting.CancelResidenceEvent;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.residenceManagement.CreateResidencePaymentCodes;
import net.sourceforge.fenixedu.applicationTier.Servico.residenceManagement.PayResidenceEvent;
import net.sourceforge.fenixedu.dataTransferObject.VariantBean;
import net.sourceforge.fenixedu.dataTransferObject.residenceManagement.ImportResidenceEventBean;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accounting.Event;
import net.sourceforge.fenixedu.domain.accounting.ResidenceEvent;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.ResidenceManagementUnit;
import net.sourceforge.fenixedu.domain.residence.ResidenceMonth;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.portal.EntryPoint;
import org.fenixedu.bennu.portal.StrutsFunctionality;
import org.joda.time.YearMonthDay;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
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