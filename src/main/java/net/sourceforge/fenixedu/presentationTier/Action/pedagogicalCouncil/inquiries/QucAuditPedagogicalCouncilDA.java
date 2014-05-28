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
package net.sourceforge.fenixedu.presentationTier.Action.pedagogicalCouncil.inquiries;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.dataTransferObject.inquiries.AuditSelectPersonsECBean;
import net.sourceforge.fenixedu.dataTransferObject.inquiries.CompetenceCourseResultsResume;
import net.sourceforge.fenixedu.dataTransferObject.inquiries.ExecutionCourseQucAuditSearchBean;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.inquiries.ExecutionCourseAudit;
import net.sourceforge.fenixedu.presentationTier.Action.pedagogicalCouncil.PedagogicalCouncilApp.PedagogicalControlApp;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.portal.EntryPoint;
import org.fenixedu.bennu.portal.StrutsFunctionality;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixframework.FenixFramework;

@StrutsFunctionality(app = PedagogicalControlApp.class, path = "quc-audit", titleKey = "link.inquiry.audit",
        bundle = "InquiriesResources")
@Mapping(path = "/qucAudit", module = "pedagogicalCouncil")
@Forwards({
        @Forward(name = "showExecutionCoursesForAudit", path = "/pedagogicalCouncil/inquiries/searchExecutionCourseToAudit.jsp"),
        @Forward(name = "selectPersons", path = "/pedagogicalCouncil/inquiries/selectPersons.jsp"),
        @Forward(name = "viewProcessDetails", path = "/pedagogicalCouncil/inquiries/viewProcessDetails.jsp") })
public class QucAuditPedagogicalCouncilDA extends ViewQucAuditProcessDA {

    @EntryPoint
    public ActionForward searchExecutionCourse(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        ExecutionCourseQucAuditSearchBean executionCourseSearchBean = getRenderedObject("executionCourseSearchBean");
        ExecutionSemester executionSemester = null;
        if (executionCourseSearchBean == null) {
            executionCourseSearchBean = new ExecutionCourseQucAuditSearchBean();
            executionSemester = ExecutionSemester.readActualExecutionSemester().getPreviousExecutionPeriod();
            executionCourseSearchBean.setExecutionPeriod(executionSemester);
        } else {
            executionSemester = executionCourseSearchBean.getExecutionPeriod();
        }
        final Collection<ExecutionCourse> executionCourses = executionCourseSearchBean.search();
        if (executionCourses != null) {
            request.setAttribute("executionCourses", executionCourses);
        }
        List<ExecutionCourseAudit> executionCoursesAudits = getExecutionCoursesAudits(executionSemester);

        request.setAttribute("executionCoursesAudits", executionCoursesAudits);
        request.setAttribute("executionCourseSearchBean", executionCourseSearchBean);
        return mapping.findForward("showExecutionCoursesForAudit");
    }

    private List<ExecutionCourseAudit> getExecutionCoursesAudits(ExecutionSemester executionSemester) {
        List<ExecutionCourseAudit> result = new ArrayList<ExecutionCourseAudit>();
        for (ExecutionCourseAudit executionCourseAudit : Bennu.getInstance().getExecutionCourseAuditsSet()) {
            if (executionCourseAudit.getExecutionCourse().getExecutionPeriod() == executionSemester) {
                result.add(executionCourseAudit);
            }
        }
        return result;
    }

    public ActionForward makeAvailableProcess(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        String executionCourseAuditOID = (String) getFromRequest(request, "executionCourseAuditOID");
        ExecutionCourseAudit executionCourseAudit = FenixFramework.getDomainObject(executionCourseAuditOID);

        executionCourseAudit.makeProcessAvailableToView();
        List<CompetenceCourseResultsResume> competenceCoursesToAudit = getCompetenceCourseResultsBeans(executionCourseAudit);

        request.setAttribute("competenceCoursesToAudit", competenceCoursesToAudit);
        request.setAttribute("executionCourseAudit", executionCourseAudit);
        request.setAttribute("success", "true");
        return mapping.findForward("viewProcessDetails");
    }

    public ActionForward makeUnavailableProcess(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        String executionCourseAuditOID = (String) getFromRequest(request, "executionCourseAuditOID");
        ExecutionCourseAudit executionCourseAudit = FenixFramework.getDomainObject(executionCourseAuditOID);

        executionCourseAudit.makeProcessUnavailableToView();
        List<CompetenceCourseResultsResume> competenceCoursesToAudit = getCompetenceCourseResultsBeans(executionCourseAudit);

        request.setAttribute("competenceCoursesToAudit", competenceCoursesToAudit);
        request.setAttribute("executionCourseAudit", executionCourseAudit);
        request.setAttribute("success", "true");
        return mapping.findForward("viewProcessDetails");
    }

    public ActionForward unsealProcess(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        String executionCourseAuditOID = (String) getFromRequest(request, "executionCourseAuditOID");
        ExecutionCourseAudit executionCourseAudit = FenixFramework.getDomainObject(executionCourseAuditOID);

        executionCourseAudit.unsealProcess();
        List<CompetenceCourseResultsResume> competenceCoursesToAudit = getCompetenceCourseResultsBeans(executionCourseAudit);

        request.setAttribute("competenceCoursesToAudit", competenceCoursesToAudit);
        request.setAttribute("executionCourseAudit", executionCourseAudit);
        request.setAttribute("success", "true");
        return mapping.findForward("viewProcessDetails");
    }

    public ActionForward prepareSelectPersons(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        ExecutionCourse executionCourse = null;
        if (request.getAttribute("executionCourse") != null) {
            executionCourse = (ExecutionCourse) request.getAttribute("executionCourse");
        } else {
            String executionCourseOID = request.getParameter("executionCourseOID");
            executionCourse = FenixFramework.getDomainObject(executionCourseOID);
        }

        ExecutionCourseAudit executionCourseAudit = executionCourse.getExecutionCourseAudit();

        AuditSelectPersonsECBean auditProcessBean = null;
        if (executionCourseAudit == null) {
            auditProcessBean = new AuditSelectPersonsECBean(executionCourse);
        } else {
            auditProcessBean = new AuditSelectPersonsECBean(executionCourseAudit);
        }

        request.setAttribute("auditProcessBean", auditProcessBean);
        return mapping.findForward("selectPersons");
    }

    public ActionForward selectPersons(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        AuditSelectPersonsECBean auditProcessBean = getRenderedObject("auditProcessBean");

        try {
            auditProcessBean.savePersons();
        } catch (DomainException e) {
            addActionMessage(request, e.getMessage());
            RenderUtils.invalidateViewState();
            request.setAttribute("auditProcessBean", auditProcessBean);
            return mapping.findForward("selectPersons");
        }

        request.setAttribute("success", "true");
        return searchExecutionCourse(mapping, actionForm, request, response);
    }
}
