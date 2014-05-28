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
package net.sourceforge.fenixedu.presentationTier.Action.departmentMember;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.dataTransferObject.VariantBean;
import net.sourceforge.fenixedu.dataTransferObject.commons.OpenFileBean;
import net.sourceforge.fenixedu.dataTransferObject.inquiries.AuditProcessBean;
import net.sourceforge.fenixedu.dataTransferObject.inquiries.CompetenceCourseResultsResume;
import net.sourceforge.fenixedu.dataTransferObject.inquiries.CurricularCourseResumeResult;
import net.sourceforge.fenixedu.domain.CompetenceCourse;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.inquiries.ExecutionCourseAudit;
import net.sourceforge.fenixedu.domain.inquiries.ExecutionCourseAuditFile;
import net.sourceforge.fenixedu.domain.inquiries.ResultPersonCategory;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.bennu.portal.EntryPoint;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixframework.FenixFramework;

import com.google.common.io.ByteStreams;

public abstract class QUCAuditorDA extends FenixDispatchAction {

    @EntryPoint
    public ActionForward showAuditProcesses(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        VariantBean executionSemesterBean = getRenderedObject("executionSemesterBean");
        ExecutionSemester executionSemester = null;
        if (executionSemesterBean != null) {
            executionSemester = (ExecutionSemester) executionSemesterBean.getDomainObject();
        } else {
            executionSemester = ExecutionSemester.readActualExecutionSemester().getPreviousExecutionPeriod();
            executionSemesterBean = new VariantBean();
            executionSemesterBean.setDomainObject(executionSemester);
        }

        request.setAttribute("executionCoursesAudits", getExecutionCoursesAudit(executionSemester));
        request.setAttribute("executionSemesterBean", executionSemesterBean);
        return mapping.findForward("viewAuditProcesses");
    }

    protected abstract List<ExecutionCourseAudit> getExecutionCoursesAudit(ExecutionSemester executionSemester);

    public ActionForward viewProcessDetails(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        ExecutionCourseAudit executionCourseAudit = getExecutionCourseAudit(request);
        setCompetenceCourseResults(request, executionCourseAudit);

        request.setAttribute("approvedBySelf", getApprovedSelf());
        request.setAttribute("approvedByOther", getApprovedOther());
        request.setAttribute("executionCourseAudit", executionCourseAudit);
        return mapping.findForward("viewProcessDetails");
    }

    private ExecutionCourseAudit getExecutionCourseAudit(HttpServletRequest request) {
        if (getFromRequest(request, "executionCourseAuditOID") != null) {
            String executionCourseAuditOID = (String) getFromRequest(request, "executionCourseAuditOID");
            return FenixFramework.getDomainObject(executionCourseAuditOID);
        } else {
            Object object = getRenderedObject();
            if (object instanceof AuditProcessBean) {
                AuditProcessBean auditProcessBean = getRenderedObject();
                return auditProcessBean.getExecutionCourseAudit();
            } else {
                return (ExecutionCourseAudit) object;
            }
        }
    }

    public ActionForward prepareEditProcess(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        ExecutionCourseAudit executionCourseAudit = getExecutionCourseAudit(request);
        setCompetenceCourseResults(request, executionCourseAudit);

        request.setAttribute("auditProcessBean", new AuditProcessBean(executionCourseAudit));
        return mapping.findForward("editProcess");
    }

    public ActionForward sealProcess(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        ExecutionCourseAudit executionCourseAudit = getExecutionCourseAudit(request);
        executionCourseAudit.sealProcess(isTeacher());

        request.setAttribute("executionCourseAuditOID", executionCourseAudit.getExternalId());
        request.setAttribute("success", "true");
        return viewProcessDetails(mapping, form, request, response);
    }

    public ActionForward unsealProcess(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        ExecutionCourseAudit executionCourseAudit = getExecutionCourseAudit(request);
        executionCourseAudit.unsealProcess(isTeacher());

        request.setAttribute("executionCourseAuditOID", executionCourseAudit.getExternalId());
        request.setAttribute("success", "true");
        return viewProcessDetails(mapping, form, request, response);
    }

    private void setCompetenceCourseResults(HttpServletRequest request, ExecutionCourseAudit executionCourseAudit) {
        List<CompetenceCourseResultsResume> competenceCoursesToAudit = new ArrayList<CompetenceCourseResultsResume>();
        for (CompetenceCourse competenceCourse : executionCourseAudit.getExecutionCourse().getCompetenceCourses()) {
            CompetenceCourseResultsResume competenceCourseResultsResume = null;
            for (ExecutionDegree executionDegree : executionCourseAudit.getExecutionCourse().getExecutionDegrees()) {
                CurricularCourseResumeResult courseResumeResult =
                        new CurricularCourseResumeResult(executionCourseAudit.getExecutionCourse(), executionDegree,
                                "label.inquiry.execution", executionDegree.getDegree().getSigla(), AccessControl.getPerson(),
                                ResultPersonCategory.DEPARTMENT_PRESIDENT, false, true, true, true, true);
                if (courseResumeResult.getResultBlocks().size() > 1) {
                    if (competenceCourseResultsResume == null) {
                        competenceCourseResultsResume = new CompetenceCourseResultsResume(competenceCourse);
                        competenceCoursesToAudit.add(competenceCourseResultsResume);
                    }
                    competenceCourseResultsResume.addCurricularCourseResumeResult(courseResumeResult);
                }
            }
        }
        request.setAttribute("competenceCoursesToAudit", competenceCoursesToAudit);
    }

    protected abstract String getApprovedSelf();

    protected abstract String getApprovedOther();

    public ActionForward editProcess(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        AuditProcessBean auditProcessBean = getRenderedObject("auditProcessBean");
        auditProcessBean.saveComments();

        request.setAttribute("executionCourseAuditOID", auditProcessBean.getExecutionCourseAudit().getExternalId());
        request.setAttribute("success", "true");
        return viewProcessDetails(mapping, form, request, response);
    }

    protected abstract boolean isTeacher();

    public ActionForward prepareManageFiles(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        ExecutionCourseAudit executionCourseAudit = getExecutionCourseAudit(request);

        return forwardToFiles(mapping, request, executionCourseAudit);
    }

    private ActionForward forwardToFiles(ActionMapping mapping, HttpServletRequest request,
            ExecutionCourseAudit executionCourseAudit) {
        request.setAttribute("executionCourseAudit", executionCourseAudit);
        request.setAttribute("fileBean", new OpenFileBean());
        return mapping.findForward("manageAuditFiles");
    }

    public ActionForward uploadFile(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws IOException {

        ExecutionCourseAudit executionCourseAudit = getRenderedObject("executionCourseAudit");
        OpenFileBean fileBean = getRenderedObject("fileBean");
        if (fileBean.getInputStream() == null) {
            addActionMessage(request, "errors.fileRequired");
            RenderUtils.invalidateViewState();
            return forwardToFiles(mapping, request, executionCourseAudit);
        }
        executionCourseAudit.addFile(fileBean.getFileName(), ByteStreams.toByteArray(fileBean.getInputStream()));
        request.setAttribute("fileAdded", "true");

        return forwardToFiles(mapping, request, executionCourseAudit);
    }

    public ActionForward deleteFile(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws IOException {

        String executionCourseAuditFileOID = (String) getFromRequest(request, "executionCourseAuditFileOID");
        ExecutionCourseAuditFile executionCourseAuditFile = FenixFramework.getDomainObject(executionCourseAuditFileOID);
        ExecutionCourseAudit executionCourseAudit = executionCourseAuditFile.getExecutionCourseAudit();

        executionCourseAudit.deleteFile(executionCourseAuditFile);
        request.setAttribute("fileDeleted", "true");

        return forwardToFiles(mapping, request, executionCourseAudit);
    }
}
