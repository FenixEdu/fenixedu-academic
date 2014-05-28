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
package net.sourceforge.fenixedu.presentationTier.Action.student.administrativeOfficeServices;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accounting.Event;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.bennu.portal.EntryPoint;
import org.fenixedu.bennu.portal.StrutsFunctionality;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@StrutsFunctionality(app = StudentAcademicOfficeServices.class, path = "payments", titleKey = "link.title.payments")
@Mapping(module = "student", path = "/payments")
@Forwards({ @Forward(name = "showEvents", path = "/student/administrativeOfficeServices/payments/showStudentEvents.jsp"),
        @Forward(name = "showEventDetails", path = "/student/administrativeOfficeServices/payments/showEventDetails.jsp") })
public class StudentPaymentsDispatchAction extends FenixDispatchAction {

    @EntryPoint
    public ActionForward showEvents(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {

        final Person person = getUserView(request).getPerson();

        request.setAttribute("person", person);
        request.setAttribute("notPayedEvents", calculateNotPayedEvents(person));
        request.setAttribute("payedEntries", person.getPayments());
        request.setAttribute("totalPayed", person.getTotalPaymentsAmountWithAdjustment());

        return mapping.findForward("showEvents");
    }

    private List<Event> calculateNotPayedEvents(final Person person) {

        final List<Event> result = new ArrayList<Event>();

        result.addAll(person.getNotPayedEventsPayableOn(null, false));
        result.addAll(person.getNotPayedEventsPayableOn(null, true));

        return result;
    }

    public ActionForward showEventDetails(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {

        final Event event = readEvent(request);

        request.setAttribute("person", getUserView(request).getPerson());
        request.setAttribute("event", event);
        request.setAttribute("entryDTOs", event.calculateEntries());
        request.setAttribute("accountingEventPaymentCodes", event.getNonProcessedPaymentCodes());

        return mapping.findForward("showEventDetails");
    }

    private Event readEvent(final HttpServletRequest request) {

        final Person person = getUserView(request).getPerson();
        final String eventId = getStringFromRequest(request, "eventId");

        for (final Event event : person.getEventsSet()) {
            if (event.getExternalId().equals(eventId)) {
                return event;
            }
        }

        return null;
    }

}
