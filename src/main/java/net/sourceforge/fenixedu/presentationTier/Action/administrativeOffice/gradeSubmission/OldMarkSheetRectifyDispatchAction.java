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

import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.degreeAdministrativeOffice.gradeSubmission.CurricularCourseMarksheetManagementBean;
import net.sourceforge.fenixedu.dataTransferObject.degreeAdministrativeOffice.gradeSubmission.MarkSheetManagementCreateBean;
import net.sourceforge.fenixedu.dataTransferObject.degreeAdministrativeOffice.gradeSubmission.MarkSheetRectifyBean;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.EnrolmentEvaluation;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.MarkSheetType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.DynaActionForm;
import org.fenixedu.bennu.core.domain.User;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.utl.ist.fenix.tools.util.DateFormatUtil;

@Mapping(path = "/rectifyOldMarkSheet", module = "academicAdministration", formBean = "markSheetManagementForm",
        input = "/gradeSubmission/oldMarkSheets/rectifyMarkSheetStep1.jsp",
        functionality = OldMarkSheetSearchDispatchAction.class)
@Forwards({
        @Forward(name = "rectifyMarkSheetStep1",
                path = "/academicAdministration/gradeSubmission/oldMarkSheets/rectifyMarkSheetStep1.jsp"),
        @Forward(name = "rectifyMarkSheetStep2",
                path = "/academicAdministration/gradeSubmission/oldMarkSheets/rectifyMarkSheetStep2.jsp"),
        @Forward(name = "searchMarkSheet",
                path = "/academicAdministration/oldMarkSheetManagement.do?method=prepareSearchMarkSheet"),
        @Forward(name = "searchMarkSheetFilled",
                path = "/academicAdministration/oldMarkSheetManagement.do?method=prepareSearchMarkSheetFilled"),
        @Forward(name = "showRectificationHistoric",
                path = "/academicAdministration/gradeSubmission/showRectificationHistoric.jsp"),
        @Forward(name = "rectifyMarkSheetStepOneByEvaluation",
                path = "/academicAdministration/gradeSubmission/oldMarkSheets/rectifyOldMarkSheetEvaluation.jsp") })
public class OldMarkSheetRectifyDispatchAction extends OldMarkSheetCreateDispatchAction {

    @Override
    public ActionForward prepareRectifyMarkSheet(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {

        MarkSheetManagementCreateBean markSheetManagementCreateBean = new MarkSheetManagementCreateBean();
        markSheetManagementCreateBean.setUrl("");

        request.setAttribute("edit", markSheetManagementCreateBean);

        return mapping.findForward("rectifyMarkSheetStep1");
    }

    public ActionForward rectifyMarkSheetStepOne(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {

        MarkSheetManagementCreateBean markSheetManagementCreateBean = getRenderedObject();
        List<EnrolmentEvaluation> enrolmentEvaluations =
                markSheetManagementCreateBean.getCurricularCourse().getEnrolmentEvaluationsForOldMarkSheet(
                        markSheetManagementCreateBean.getExecutionPeriod(), markSheetManagementCreateBean.getMarkSheetType());

        MarkSheetRectifyBean rectifyBean = new MarkSheetRectifyBean();
        rectifyBean.setCurricularCourseBean(new CurricularCourseMarksheetManagementBean(markSheetManagementCreateBean
                .getCurricularCourse(), markSheetManagementCreateBean.getExecutionPeriod()));
        rectifyBean.setDegree(markSheetManagementCreateBean.getDegree());
        rectifyBean.setDegreeCurricularPlan(markSheetManagementCreateBean.getDegreeCurricularPlan());
        rectifyBean.setExecutionPeriod(markSheetManagementCreateBean.getExecutionPeriod());
        rectifyBean.setUrl(buildSearchUrl(markSheetManagementCreateBean));
        rectifyBean.setMarkSheetType(markSheetManagementCreateBean.getMarkSheetType());

        return rectifyMarkSheetStepOne(mapping, actionForm, request, response, rectifyBean, enrolmentEvaluations);
    }

    private ActionForward rectifyMarkSheetStepOne(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response, MarkSheetRectifyBean rectifyBean, List<EnrolmentEvaluation> enrolmentEvaluations) {

        request.setAttribute("rectifyBean", rectifyBean);

        Collections.sort(enrolmentEvaluations, EnrolmentEvaluation.SORT_BY_STUDENT_NUMBER);
        request.setAttribute("enrolmentEvaluations", enrolmentEvaluations);
        return mapping.findForward("rectifyMarkSheetStep2");
    }

    private String buildSearchUrl(MarkSheetManagementCreateBean createBean) {

        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("&epID=").append(createBean.getExecutionPeriod().getExternalId());
        stringBuilder.append("&dID=").append(createBean.getDegree().getExternalId());
        stringBuilder.append("&dcpID=").append(createBean.getDegreeCurricularPlan().getExternalId());
        stringBuilder.append("&ccID=").append(createBean.getCurricularCourse().getExternalId());

        if (createBean.getTeacherId() != null) {
            stringBuilder.append("&tn=").append(createBean.getTeacherId());
        }
        if (createBean.getEvaluationDate() != null) {
            stringBuilder.append("&ed=").append(DateFormatUtil.format("dd/MM/yyyy", createBean.getEvaluationDate()));
        }
        if (createBean.getMarkSheetType() != null) {
            stringBuilder.append("&mst=").append(createBean.getMarkSheetType().getName());
        }
        return stringBuilder.toString();
    }

    public ActionForward rectifyMarkSheetStepOneByEvaluation(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) {
        DynaActionForm form = (DynaActionForm) actionForm;
        EnrolmentEvaluation enrolmentEvaluation = getDomainObject(form, "evaluationID");
        MarkSheetRectifyBean rectifyBean = new MarkSheetRectifyBean();
        rectifyBean.setEnrolmentEvaluation(enrolmentEvaluation);
        fillMarkSheetRectifyBean(actionForm, request, rectifyBean);

        request.setAttribute("rectifyBean", rectifyBean);
        return mapping.findForward("rectifyMarkSheetStepOneByEvaluation");

    }

    private void fillMarkSheetRectifyBean(ActionForm actionForm, HttpServletRequest request, MarkSheetRectifyBean markSheetBean) {
        DynaActionForm form = (DynaActionForm) actionForm;

        String markSheetType = form.getString("mst");

        final ExecutionSemester executionSemester = getDomainObject(form, "epID");
        final CurricularCourse curricularCourse = getDomainObject(form, "ccID");

        markSheetBean.setExecutionPeriod(executionSemester);
        markSheetBean.setDegree(this.<Degree> getDomainObject(form, "dID"));
        markSheetBean.setDegreeCurricularPlan(this.<DegreeCurricularPlan> getDomainObject(form, "dcpID"));
        markSheetBean.setCurricularCourseBean(new CurricularCourseMarksheetManagementBean(curricularCourse, executionSemester));
        markSheetBean.setMarkSheetType(MarkSheetType.valueOf(markSheetType));

    }

    public ActionForward rectifyMarkSheetStepTwoByEvaluation(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws FenixServiceException {
        MarkSheetRectifyBean rectifyBean = (MarkSheetRectifyBean) RenderUtils.getViewState().getMetaObject().getObject();

        ActionMessages actionMessages = new ActionMessages();
        User userView = getUserView(request);
        try {
            rectifyBean.createRectificationOldMarkSheet(userView.getPerson());
            return mapping.findForward("searchMarkSheetFilled");
        } catch (DomainException e) {
            addMessage(request, actionMessages, e.getMessage(), e.getArgs());
            List<EnrolmentEvaluation> enrolmentEvaluations =
                    rectifyBean
                            .getCurricularCourseBean()
                            .getCurricularCourse()
                            .getEnrolmentEvaluationsForOldMarkSheet(rectifyBean.getExecutionPeriod(),
                                    rectifyBean.getMarkSheetType());

            return rectifyMarkSheetStepOne(mapping, actionForm, request, response, rectifyBean, enrolmentEvaluations);
        }
    }

}
