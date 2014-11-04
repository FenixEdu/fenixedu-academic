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
package org.fenixedu.academic.ui.struts.action.manager.accounting.reports;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.QueueJob;
import org.fenixedu.academic.domain.accounting.report.GratuityReportBean;
import org.fenixedu.academic.domain.accounting.report.GratuityReportQueueJob;
import org.fenixedu.academic.domain.accounting.report.GratuityReportQueueJobLaunchService;
import org.fenixedu.academic.domain.accounting.report.events.EventReportQueueJob;
import org.fenixedu.academic.ui.struts.action.base.FenixDispatchAction;
import org.fenixedu.academic.ui.struts.action.manager.ManagerApplications.ManagerPaymentsApp;
import org.fenixedu.bennu.struts.annotations.Forward;
import org.fenixedu.bennu.struts.annotations.Forwards;
import org.fenixedu.bennu.struts.annotations.Mapping;
import org.fenixedu.bennu.struts.portal.EntryPoint;
import org.fenixedu.bennu.struts.portal.StrutsFunctionality;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;

@StrutsFunctionality(app = ManagerPaymentsApp.class, path = "gratuity-reports", titleKey = "title.gratuity.reports")
@Mapping(path = "/gratuityReports", module = "manager")
@Forwards({ @Forward(name = "prepare-generate-report", path = "/manager/accounting/reports/gratuity/prepareGenerateReport.jsp"),
        @Forward(name = "list-reports", path = "/manager/accounting/reports/gratuity/listReports.jsp") })
public class GratuityReportsDA extends FenixDispatchAction {

    @EntryPoint
    public ActionForward listReports(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        GratuityReportBean bean = getGratuityReportBean();

        if (bean == null) {
            bean = new GratuityReportBean(ExecutionYear.readCurrentExecutionYear());
        }

        RenderUtils.invalidateViewState("gratuity.report.bean");

        request.setAttribute("gratuityReportBean", bean);
        request.setAttribute("generatedReports", GratuityReportQueueJob.retrieveAllGeneratedReports(bean.getExecutionYear()));
        request.setAttribute("eventReports", EventReportQueueJob.retrieveAllGeneratedReports());
        request.setAttribute("notGeneratedReports", GratuityReportQueueJob.retrieveNotGeneratedReports(bean.getExecutionYear()));
        request.setAttribute("canRequestReportGeneration", GratuityReportQueueJob.canRequestReportGeneration());

        return mapping.findForward("list-reports");
    }

    private GratuityReportBean getGratuityReportBean() {
        return getRenderedObject("gratuity.report.bean");
    }

    public ActionForward prepareGenerateReport(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        GratuityReportBean bean = new GratuityReportBean(ExecutionYear.readCurrentExecutionYear());

        request.setAttribute("gratuityReportBean", bean);

        return mapping.findForward("prepare-generate-report");
    }

    public ActionForward prepareGenerateReportInvalid(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        GratuityReportBean bean = getGratuityReportBean();
        request.setAttribute("gratuityReportBean", bean);

        RenderUtils.invalidateViewState("gratuity.report.bean");

        return mapping.findForward("prepare-generate-report");
    }

    public ActionForward generateReportPostback(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        GratuityReportBean bean = getGratuityReportBean();
        request.setAttribute("gratuityReportBean", bean);

        RenderUtils.invalidateViewState("gratuity.report.bean");

        return mapping.findForward("prepare-generate-report");
    }

    public ActionForward generateReport(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        GratuityReportBean bean = getGratuityReportBean();
        GratuityReportQueueJobLaunchService.launchJob(bean);

        return listReports(mapping, form, request, response);
    }

    public ActionForward cancelQueueJob(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        QueueJob job = getDomainObject(request, "queueJobId");
        job.cancel();

        return listReports(mapping, form, request, response);
    }

}
