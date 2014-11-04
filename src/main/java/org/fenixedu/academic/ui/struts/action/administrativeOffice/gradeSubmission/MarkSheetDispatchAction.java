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
package net.sourceforge.fenixedu.presentationTier.Action.administrativeOffice.gradeSubmission;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.administrativeOffice.gradeSubmission.ConfirmMarkSheet;
import net.sourceforge.fenixedu.applicationTier.Servico.administrativeOffice.gradeSubmission.DeleteMarkSheet;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.degreeAdministrativeOffice.gradeSubmission.CurricularCourseMarksheetManagementBean;
import net.sourceforge.fenixedu.dataTransferObject.degreeAdministrativeOffice.gradeSubmission.MarkSheetManagementBaseBean;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.MarkSheet;
import net.sourceforge.fenixedu.domain.MarkSheetType;
import net.sourceforge.fenixedu.domain.OccupationPeriod;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.exceptions.InDebtEnrolmentsException;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.DynaActionForm;
import org.fenixedu.bennu.core.domain.User;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;

abstract public class MarkSheetDispatchAction extends FenixDispatchAction {

    protected ActionMessages createActionMessages() {
        return new ActionMessages();
    }

    protected void addMessage(HttpServletRequest request, ActionMessages actionMessages, String keyMessage, String... args) {
        actionMessages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(keyMessage, args));
        saveMessages(request, actionMessages);
    }

    protected void fillMarkSheetBean(ActionForm actionForm, HttpServletRequest request, MarkSheetManagementBaseBean markSheetBean) {
        DynaActionForm form = (DynaActionForm) actionForm;

        final ExecutionSemester executionSemester = getDomainObject(form, "epID");
        final CurricularCourse curricularCourse = getDomainObject(form, "ccID");

        markSheetBean.setExecutionPeriod(executionSemester);
        markSheetBean.setDegree(this.<Degree> getDomainObject(form, "dID"));
        markSheetBean.setDegreeCurricularPlan(this.<DegreeCurricularPlan> getDomainObject(form, "dcpID"));
        markSheetBean.setCurricularCourseBean(new CurricularCourseMarksheetManagementBean(curricularCourse, executionSemester));

        request.setAttribute("edit", markSheetBean);
    }

    public ActionForward prepareSearchMarkSheetPostBack(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {

        Object object = RenderUtils.getViewState().getMetaObject().getObject();
        RenderUtils.invalidateViewState();
        request.setAttribute("edit", object);

        return mapping.getInputForward();
    }

    public ActionForward prepareSearchMarkSheetInvalid(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {

        request.setAttribute("edit", RenderUtils.getViewState().getMetaObject().getObject());
        return mapping.getInputForward();
    }

    public ActionForward viewMarkSheet(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {

        DynaActionForm form = (DynaActionForm) actionForm;
        MarkSheet markSheet = getDomainObject(form, "msID");

        request.setAttribute("markSheet", markSheet);
        request.setAttribute("url", buildUrl(form));

        return mapping.findForward("viewMarkSheet");
    }

    public ActionForward prepareDeleteMarkSheet(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {

        DynaActionForm form = (DynaActionForm) actionForm;
        request.setAttribute("markSheet", getDomainObject(form, "msID"));

        return mapping.findForward("removeMarkSheet");
    }

    public ActionForward deleteMarkSheet(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {

        ActionMessages actionMessages = createActionMessages();
        DynaActionForm form = (DynaActionForm) actionForm;
        MarkSheet markSheet = getDomainObject(form, "msID");

        try {
            DeleteMarkSheet.run(markSheet);
        } catch (DomainException e) {
            addMessage(request, actionMessages, e.getMessage(), e.getArgs());
        }

        return mapping.findForward("searchMarkSheetFilled");
    }

    public ActionForward prepareConfirmMarkSheet(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {
        DynaActionForm form = (DynaActionForm) actionForm;
        request.setAttribute("markSheet", getDomainObject(form, "msID"));

        return mapping.findForward("confirmMarkSheet");
    }

    public ActionForward confirmMarkSheet(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {

        DynaActionForm form = (DynaActionForm) actionForm;
        MarkSheet markSheet = getDomainObject(form, "msID");
        User userView = getUserView(request);
        ActionMessages actionMessages = new ActionMessages();
        try {
            ConfirmMarkSheet.run(markSheet, userView.getPerson());
        } catch (InDebtEnrolmentsException e) {
            for (Enrolment enrolment : e.getEnrolments()) {
                addMessage(request, actionMessages, e.getMessage(), enrolment.getRegistration().getStudent().getNumber()
                        .toString());
            }
            return prepareConfirmMarkSheet(mapping, actionForm, request, response);
        } catch (DomainException e) {
            addMessage(request, actionMessages, e.getMessage(), e.getArgs());
        }
        return mapping.findForward("searchMarkSheetFilled");
    }

    protected void checkIfEvaluationDateIsInExamsPeriod(DegreeCurricularPlan degreeCurricularPlan,
            ExecutionSemester executionSemester, Date evaluationDate, MarkSheetType markSheetType, HttpServletRequest request,
            ActionMessages actionMessages) {

        ExecutionDegree executionDegree = degreeCurricularPlan.getExecutionDegreeByYear(executionSemester.getExecutionYear());

        if (executionDegree == null) {
            if (!markSheetType.equals(MarkSheetType.IMPROVEMENT)
                    || !degreeCurricularPlan.canSubmitImprovementMarkSheets(executionSemester.getExecutionYear())) {
                addMessage(request, actionMessages, "error.evaluationDateNotInExamsPeriod");
            }

        } else if (!executionDegree.isEvaluationDateInExamPeriod(evaluationDate, executionSemester, markSheetType)) {

            OccupationPeriod occupationPeriod = executionDegree.getOccupationPeriodFor(executionSemester, markSheetType);
            if (occupationPeriod == null) {
                addMessage(request, actionMessages, "error.evaluationDateNotInExamsPeriod");
            } else {
                addMessage(request, actionMessages, "error.evaluationDateNotInExamsPeriodWithDates", occupationPeriod
                        .getStartYearMonthDay().toString("dd/MM/yyyy"),
                        occupationPeriod.getEndYearMonthDay().toString("dd/MM/yyyy"));
            }
        }
    }

    protected void checkIfTeacherIsResponsibleOrCoordinator(CurricularCourse curricularCourse,
            ExecutionSemester executionSemester, String teacherId, Teacher teacher, HttpServletRequest request,
            MarkSheetType markSheetType, ActionMessages actionMessages) {

        if (teacher == null) {
            addMessage(request, actionMessages, "error.noTeacher", teacherId);
        } else if (markSheetType == MarkSheetType.IMPROVEMENT
                && curricularCourse.getExecutionCoursesByExecutionPeriod(executionSemester).isEmpty()) {
            if (!teacher.getPerson().isResponsibleOrCoordinatorFor(curricularCourse,
                    executionSemester.getPreviousExecutionPeriod())
                    && !teacher.getPerson().isResponsibleOrCoordinatorFor(curricularCourse,
                            executionSemester.getPreviousExecutionPeriod().getPreviousExecutionPeriod())
                    && !teacher.getPerson().isResponsibleOrCoordinatorFor(
                            curricularCourse,
                            executionSemester.getPreviousExecutionPeriod().getPreviousExecutionPeriod()
                                    .getPreviousExecutionPeriod())) {
                addMessage(request, actionMessages, "error.teacherNotResponsibleOrNotCoordinator");
            }
        } else if (!teacher.getPerson().isResponsibleOrCoordinatorFor(curricularCourse, executionSemester)) {
            addMessage(request, actionMessages, "error.teacherNotResponsibleOrNotCoordinator");
        }
    }

    public ActionForward backSearchMarkSheet(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {
        return mapping.findForward("searchMarkSheetFilled");
    }

    protected String buildUrl(DynaActionForm form) {
        StringBuilder stringBuilder = new StringBuilder();

        if (form.get("epID") != null) {
            stringBuilder.append("&epID=").append(form.get("epID"));
        }
        if (form.get("dID") != null) {
            stringBuilder.append("&dID=").append(form.get("dID"));
        }
        if (form.get("dcpID") != null) {
            stringBuilder.append("&dcpID=").append(form.get("dcpID"));
        }
        if (form.get("ccID") != null) {
            stringBuilder.append("&ccID=").append(form.get("ccID"));
        }
        if (!StringUtils.isEmpty(form.getString("tn"))) {
            stringBuilder.append("&tn=").append(form.getString("tn"));
        }
        if (!StringUtils.isEmpty(form.getString("ed"))) {
            stringBuilder.append("&ed=").append(form.getString("ed"));
        }
        if (!StringUtils.isEmpty(form.getString("mss"))) {
            stringBuilder.append("&mss=").append(form.getString("mss"));
        }
        if (!StringUtils.isEmpty(form.getString("mst"))) {
            stringBuilder.append("&mst=").append(form.getString("mst"));
        }
        return stringBuilder.toString();
    }

    public ActionForward prepareSendMail(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {
        return mapping.getInputForward();
    }
}
