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
package net.sourceforge.fenixedu.presentationTier.Action.student.residence;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accounting.ResidenceEvent;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.student.StudentApplication.StudentViewApp;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.bennu.portal.EntryPoint;
import org.fenixedu.bennu.portal.StrutsFunctionality;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
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
