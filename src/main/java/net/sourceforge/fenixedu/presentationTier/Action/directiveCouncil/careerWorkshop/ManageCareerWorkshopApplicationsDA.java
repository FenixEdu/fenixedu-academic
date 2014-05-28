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
package net.sourceforge.fenixedu.presentationTier.Action.directiveCouncil.careerWorkshop;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.careerWorkshop.CareerWorkshopApplicationEvent;
import net.sourceforge.fenixedu.domain.careerWorkshop.CareerWorkshopConfirmation;
import net.sourceforge.fenixedu.domain.careerWorkshop.CareerWorkshopConfirmationSpreadsheet;
import net.sourceforge.fenixedu.domain.careerWorkshop.CareerWorkshopSpreadsheet;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.directiveCouncil.DirectiveCouncilApplication.DirectiveCouncilCareerWorkshops;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.bennu.portal.EntryPoint;
import org.fenixedu.bennu.portal.StrutsFunctionality;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixframework.FenixFramework;

@StrutsFunctionality(app = DirectiveCouncilCareerWorkshops.class, path = "manage", titleKey = "label.title.careerWorkshop.simple")
@Mapping(path = "/careerWorkshopApplication", module = "directiveCouncil")
@Forwards({ @Forward(name = "manageCareerWorkshops", path = "/directiveCouncil/careerWorkshop/manageCareerWorkshops.jsp"),
        @Forward(name = "setConfirmationPeriod", path = "/directiveCouncil/careerWorkshop/setConfirmationPeriod.jsp") })
public class ManageCareerWorkshopApplicationsDA extends FenixDispatchAction {

    @EntryPoint
    public ActionForward prepare(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        ManageCareerWorkshopApplicationsBean applicationsBean = new ManageCareerWorkshopApplicationsBean();
        request.setAttribute("applicationsBean", applicationsBean);

        return actionMapping.findForward("manageCareerWorkshops");
    }

    public ActionForward addApplicationEvent(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        ManageCareerWorkshopApplicationsBean applicationsBean = getRenderedObject("applicationsBean");
        applicationsBean.addNewEvent();

        request.setAttribute("applicationsBean", applicationsBean);
        RenderUtils.invalidateViewState("applicationsBean");

        return actionMapping.findForward("manageCareerWorkshops");
    }

    public ActionForward deleteApplicationEvent(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        ManageCareerWorkshopApplicationsBean applicationsBean = new ManageCareerWorkshopApplicationsBean();
        final String eventExternalId = request.getParameter("eventId");
        CareerWorkshopApplicationEvent eventToDelete = FenixFramework.getDomainObject(eventExternalId);
        applicationsBean.deleteEvent(eventToDelete);

        request.setAttribute("applicationsBean", applicationsBean);
        RenderUtils.invalidateViewState("applicationsBean");

        return actionMapping.findForward("manageCareerWorkshops");
    }

    public ActionForward downloadApplications(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        final String eventExternalId = request.getParameter("eventId");
        CareerWorkshopApplicationEvent eventToDownload = FenixFramework.getDomainObject(eventExternalId);
        CareerWorkshopSpreadsheet spreadsheet = eventToDownload.getApplications();
        if (spreadsheet != null) {
            final ServletOutputStream writer = response.getOutputStream();
            try {
                response.setContentLength(spreadsheet.getSize().intValue());
                response.setContentType("application/csv");
                response.addHeader("Content-Disposition", "attachment; filename=" + spreadsheet.getFilename());
                writer.write(spreadsheet.getContent());
                writer.flush();
            } finally {
                writer.close();
            }
        }
        return null;
    }

    public ActionForward setConfirmationPeriod(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        ManageCareerWorkshopApplicationsBean eventsBean = new ManageCareerWorkshopApplicationsBean();
        final String eventExternalId = request.getParameter("eventId");
        CareerWorkshopApplicationEvent affectedEvent = FenixFramework.getDomainObject(eventExternalId);
        eventsBean.setAffectedEvent(affectedEvent);
        request.setAttribute("eventsBean", eventsBean);

        return actionMapping.findForward("setConfirmationPeriod");
    }

    public ActionForward addConfirmationPeriod(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        ManageCareerWorkshopApplicationsBean eventsBean = getRenderedObject("eventsBean");
        eventsBean.addConfirmationPeriod();

        request.setAttribute("applicationsBean", eventsBean);
        RenderUtils.invalidateViewState("applicationsBean");

        return actionMapping.findForward("manageCareerWorkshops");
    }

    public ActionForward downloadConfirmations(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        final String eventExternalId = request.getParameter("eventId");
        CareerWorkshopApplicationEvent application = FenixFramework.getDomainObject(eventExternalId);
        CareerWorkshopConfirmationSpreadsheet spreadsheet = application.getCareerWorkshopConfirmationEvent().getConfirmations();
        if (spreadsheet != null) {
            final ServletOutputStream writer = response.getOutputStream();
            try {
                response.setContentLength(spreadsheet.getSize().intValue());
                response.setContentType("application/csv");
                response.addHeader("Content-Disposition", "attachment; filename=" + spreadsheet.getFilename());
                writer.write(spreadsheet.getContent());
                writer.flush();
            } finally {
                writer.close();
            }
        }
        return null;
    }

    public ActionForward purgeConfirmations(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        final String eventExternalId = request.getParameter("eventId");
        CareerWorkshopApplicationEvent application = FenixFramework.getDomainObject(eventExternalId);
        List<CareerWorkshopConfirmation> confirmations =
                new ArrayList<CareerWorkshopConfirmation>(application.getCareerWorkshopConfirmationEvent()
                        .getCareerWorkshopConfirmationsSet());
        for (CareerWorkshopConfirmation confToDelete : confirmations) {
            confToDelete.delete();
        }

        return prepare(actionMapping, actionForm, request, response);
    }

}
