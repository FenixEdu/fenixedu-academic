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
package org.fenixedu.academic.ui.struts.action.student.residence;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.accounting.ResidenceEvent;
import org.fenixedu.academic.ui.struts.action.base.FenixDispatchAction;
import org.fenixedu.academic.ui.struts.action.student.StudentApplication.StudentViewApp;
import org.fenixedu.bennu.struts.annotations.Forward;
import org.fenixedu.bennu.struts.annotations.Forwards;
import org.fenixedu.bennu.struts.annotations.Mapping;
import org.fenixedu.bennu.struts.portal.EntryPoint;
import org.fenixedu.bennu.struts.portal.StrutsFunctionality;

import pt.ist.fenixframework.FenixFramework;

@StrutsFunctionality(app = StudentViewApp.class, path = "residence-payments", titleKey = "link.title.residencePayments")
@Mapping(path = "/viewResidencePayments", module = "student")
@Forwards({ @Forward(name = "showEvents", path = "/student/residenceServices/showResidenceEvents.jsp"),
        @Forward(name = "eventDetails", path = "/student/residenceServices/showDetails.jsp") })
public class ViewResidencePayments extends FenixDispatchAction {

    @EntryPoint
    public ActionForward listEvents(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        Person person = getLoggedPerson(request);
        request.setAttribute("person", person);
        request.setAttribute("payedEntries", person.getPayments(ResidenceEvent.class));
        request.setAttribute("notPayedEvents", person.getNotPayedEventsPayableOn(null, ResidenceEvent.class, false));

        return mapping.findForward("showEvents");
    }

    public ActionForward showEventDetails(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        String eventOID = request.getParameter("event");
        ResidenceEvent residenceEvent = FenixFramework.getDomainObject(eventOID);

        request.setAttribute("person", getLoggedPerson(request));
        request.setAttribute("event", residenceEvent);
        request.setAttribute("entryDTOs", residenceEvent.calculateEntries());
        request.setAttribute("accountingEventPaymentCodes", residenceEvent.getNonProcessedPaymentCodes());

        return mapping.findForward("eventDetails");
    }
}
