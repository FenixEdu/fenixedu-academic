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
 * Created on Fev 10, 2004
 *  
 */
package net.sourceforge.fenixedu.presentationTier.Action.gesdis;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.gesdis.EditCourseInformation;
import net.sourceforge.fenixedu.applicationTier.Servico.gesdis.ReadCourseHistoric;
import net.sourceforge.fenixedu.applicationTier.Servico.gesdis.ReadCourseInformation;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.SiteView;
import net.sourceforge.fenixedu.dataTransferObject.gesdis.InfoCourseReport;
import net.sourceforge.fenixedu.dataTransferObject.gesdis.InfoSiteCourseInformation;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.coordinator.DegreeCoordinatorIndex;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.struts.Globals;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.security.Authenticate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Leonor Almeida
 * @author Sergio Montelobo
 */
public class TeachingReportAction extends FenixDispatchAction {

    private static final Logger logger = LoggerFactory.getLogger(TeachingReportAction.class);

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        DegreeCoordinatorIndex.setCoordinatorContext(request);
        return super.execute(mapping, actionForm, request, response);
    }

    public ActionForward edit(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        InfoCourseReport infoCourseReport = getInfoCourseReportFromForm(form);
        EditCourseInformation.runEditCourseInformation(infoCourseReport.getExternalId(), infoCourseReport,
                infoCourseReport.getReport());
        return read(mapping, form, request, response);
    }

    protected InfoCourseReport getInfoCourseReportFromForm(ActionForm form) throws FenixActionException {
        try {
            DynaActionForm dynaForm = (DynaActionForm) form;
            InfoCourseReport infoCourseReport = new InfoCourseReport();
            BeanUtils.copyProperties(infoCourseReport, dynaForm);

            final ExecutionCourse executionCourse = getDomainObject(dynaForm, "executionCourseId");
            infoCourseReport.setInfoExecutionCourse(InfoExecutionCourse.newInfoFromDomain(executionCourse));

            return infoCourseReport;
        } catch (Exception e) {
            throw new FenixActionException(e.getMessage());
        }
    }

    private boolean hasErrors(HttpServletRequest request) {

        return request.getAttribute(Globals.ERROR_KEY) != null;
    }

    protected void populateFormFromInfoCourseReport(ActionMapping mapping, InfoCourseReport infoCourseReport, ActionForm form,
            HttpServletRequest request) {
        try {
            DynaActionForm dynaForm = (DynaActionForm) form;
            BeanUtils.copyProperties(form, infoCourseReport);

            InfoExecutionCourse infoExecutionCourse = infoCourseReport.getInfoExecutionCourse();
            dynaForm.set("executionCourseId", infoExecutionCourse.getExternalId());
            dynaForm.set("executionPeriodId", infoExecutionCourse.getInfoExecutionPeriod().getExternalId());
            dynaForm.set("executionYearId", infoExecutionCourse.getInfoExecutionPeriod().getInfoExecutionYear().getExternalId());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    public ActionForward prepareEdit(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        SiteView siteView = readSiteView(mapping, form, request);
        List infoCoursesHistoric = readCoursesHistoric(mapping, form, request);
        if (!hasErrors(request) && siteView != null) {
            InfoSiteCourseInformation infoSiteCourseInformation = (InfoSiteCourseInformation) siteView.getComponent();
            populateFormFromInfoCourseReport(mapping, infoSiteCourseInformation.getInfoCourseReport(), form, request);
        }
        setSiteViewToRequest(request, siteView, mapping);
        setInfoCoursesHistoric(request, infoCoursesHistoric, mapping);
        return mapping.findForward("show-form");
    }

    public ActionForward read(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        SiteView siteView = readSiteView(mapping, form, request);
        setSiteViewToRequest(request, siteView, mapping);
        List infoCoursesHistoric = readCoursesHistoric(mapping, form, request);
        setInfoCoursesHistoric(request, infoCoursesHistoric, mapping);
        String degreeCurricularPlanID = null;
        if (request.getParameter("degreeCurricularPlanID") != null) {
            degreeCurricularPlanID = request.getParameter("degreeCurricularPlanID");
            request.setAttribute("degreeCurricularPlanID", degreeCurricularPlanID);
        }
        return mapping.findForward("successfull-read");
    }

    private void setInfoCoursesHistoric(HttpServletRequest request, List list, ActionMapping mapping) throws Exception {
        if (list != null) {
            request.setAttribute("infoCoursesHistoric", list);
        }
    }

    private List readCoursesHistoric(ActionMapping mapping, ActionForm form, HttpServletRequest request) throws Exception {
        User userView = Authenticate.getUser();
        String executionCourseId = request.getParameter("executionCourseId");

        return ReadCourseHistoric.runReadCourseHistoric(executionCourseId);
    }

    private SiteView readSiteView(ActionMapping mapping, ActionForm form, HttpServletRequest request)
            throws FenixServiceException {
        User userView = Authenticate.getUser();
        String executionCourseId = request.getParameter("executionCourseId");

        return ReadCourseInformation.runReadCourseInformation(executionCourseId);
    }

    private void setSiteViewToRequest(HttpServletRequest request, SiteView siteView, ActionMapping mapping) {
        if (siteView != null) {
            request.setAttribute("siteView", siteView);
        }
    }
}