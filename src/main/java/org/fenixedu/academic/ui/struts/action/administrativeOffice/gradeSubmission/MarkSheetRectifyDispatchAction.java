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
package org.fenixedu.academic.ui.struts.action.administrativeOffice.gradeSubmission;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.DynaActionForm;
import org.fenixedu.academic.domain.Enrolment;
import org.fenixedu.academic.domain.EnrolmentEvaluation;
import org.fenixedu.academic.domain.MarkSheet;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.student.Student;
import org.fenixedu.academic.dto.degreeAdministrativeOffice.gradeSubmission.MarkSheetRectifyBean;
import org.fenixedu.academic.service.services.administrativeOffice.gradeSubmission.CreateRectificationMarkSheet;
import org.fenixedu.academic.service.services.exceptions.FenixServiceException;
import org.fenixedu.academic.util.EnrolmentEvaluationState;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.struts.annotations.Forward;
import org.fenixedu.bennu.struts.annotations.Forwards;
import org.fenixedu.bennu.struts.annotations.Mapping;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixframework.FenixFramework;

@Mapping(path = "/rectifyMarkSheet", module = "academicAdministration", formBean = "markSheetManagementForm",
        input = "/gradeSubmission/createMarkSheetStep1.jsp", functionality = MarkSheetSearchDispatchAction.class)
@Forwards({
        @Forward(name = "searchMarkSheetFilled",
                path = "/academicAdministration/markSheetManagement.do?method=prepareSearchMarkSheetFilled"),
        @Forward(name = "rectifyMarkSheetStep1", path = "/academicAdministration/gradeSubmission/rectifyMarkSheetStep1.jsp"),
        @Forward(name = "rectifyMarkSheetStep2", path = "/academicAdministration/gradeSubmission/rectifyMarkSheetStep2.jsp"),
        @Forward(name = "viewMarkSheet", path = "/academicAdministration/gradeSubmission/viewMarkSheet.jsp") })
public class MarkSheetRectifyDispatchAction extends MarkSheetDispatchAction {

    public ActionForward prepareRectifyMarkSheet(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {

        DynaActionForm form = (DynaActionForm) actionForm;
        MarkSheet markSheet = FenixFramework.getDomainObject((String) form.get("msID"));

        MarkSheetRectifyBean rectifyBean = new MarkSheetRectifyBean();
        fillMarkSheetBean(actionForm, request, rectifyBean);
        rectifyBean.setUrl(buildUrl(form));

        rectifyBean.setMarkSheet(markSheet);
        request.setAttribute("rectifyBean", rectifyBean);
        request.setAttribute("msID", form.get("msID"));

        List<EnrolmentEvaluation> enrolmentEvaluations =
                new ArrayList<EnrolmentEvaluation>(markSheet.getEnrolmentEvaluationsSet());
        Collections.sort(enrolmentEvaluations, EnrolmentEvaluation.SORT_BY_STUDENT_NUMBER);
        request.setAttribute("enrolmentEvaluations", enrolmentEvaluations);
        return mapping.findForward("rectifyMarkSheetStep1");
    }

    public ActionForward rectifyMarkSheetStepOneByEvaluation(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) {
        DynaActionForm form = (DynaActionForm) actionForm;
        EnrolmentEvaluation enrolmentEvaluation = getDomainObject(form, "evaluationID");
        MarkSheet markSheet = enrolmentEvaluation.getMarkSheet();
        MarkSheetRectifyBean rectifyBean = new MarkSheetRectifyBean();
        rectifyBean.setMarkSheet(markSheet);
        rectifyBean.setEnrolmentEvaluation(enrolmentEvaluation);
        return rectifyMarkSheetStepOne(mapping, actionForm, request, response, rectifyBean, enrolmentEvaluation);
    }

    public ActionForward rectifyMarkSheetStepOneByStudentNumber(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) {
        MarkSheetRectifyBean rectifyBean = (MarkSheetRectifyBean) RenderUtils.getViewState().getMetaObject().getObject();

        Integer studentNumber = rectifyBean.getStudentNumber();
        Student student = Student.readStudentByNumber(studentNumber);

        if (student == null) {
            ActionMessages actionMessages = new ActionMessages();
            addMessage(request, actionMessages, "error.no.student", studentNumber.toString());
            return prepareRectifyMarkSheet(mapping, actionForm, request, response);
        }
        MarkSheet markSheet = rectifyBean.getMarkSheet();
        EnrolmentEvaluation enrolmentEvaluation = markSheet.getEnrolmentEvaluationByStudent(student);

        if (enrolmentEvaluation == null) {
            ActionMessages actionMessages = new ActionMessages();
            addMessage(request, actionMessages, "error.no.student.in.markSheet", studentNumber.toString());
            return prepareRectifyMarkSheet(mapping, actionForm, request, response);
        }
        if (!enrolmentEvaluation.getEnrolmentEvaluationState().equals(EnrolmentEvaluationState.FINAL_OBJ)
                && !enrolmentEvaluation.getEnrolmentEvaluationState().equals(EnrolmentEvaluationState.RECTIFICATION_OBJ)) {
            ActionMessages actionMessages = new ActionMessages();
            addMessage(request, actionMessages, "error.markSheet.student.alreadyRectified", studentNumber.toString());
            return prepareRectifyMarkSheet(mapping, actionForm, request, response);
        }
        return rectifyMarkSheetStepOne(mapping, actionForm, request, response, rectifyBean, enrolmentEvaluation);
    }

    private ActionForward rectifyMarkSheetStepOne(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response, MarkSheetRectifyBean rectifyBean, EnrolmentEvaluation enrolmentEvaluation) {
        rectifyBean.setEnrolmentEvaluation(enrolmentEvaluation);
        request.setAttribute("rectifyBean", rectifyBean);

        return mapping.findForward("rectifyMarkSheetStep2");
    }

    public ActionForward rectifyMarkSheetStepTwo(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {
        MarkSheetRectifyBean rectifyBean = (MarkSheetRectifyBean) RenderUtils.getViewState().getMetaObject().getObject();

        ActionMessages actionMessages = new ActionMessages();
        User userView = getUserView(request);
        try {
            CreateRectificationMarkSheet.run(rectifyBean.getMarkSheet(), rectifyBean.getEnrolmentEvaluation(),
                    rectifyBean.getRectifiedGrade(), rectifyBean.getEvaluationDate(), rectifyBean.getReason(),
                    userView.getPerson());
            return mapping.findForward("searchMarkSheetFilled");
        } catch (DomainException e) {
            addMessage(request, actionMessages, e.getMessage(), e.getArgs());
            return rectifyMarkSheetStepOne(mapping, actionForm, request, response, rectifyBean,
                    rectifyBean.getEnrolmentEvaluation());
        }
    }

    public ActionForward validationError(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {
        String[] pages = request.getParameterValues("page");
        String page = pages[1];
        if (page.equals("1")) {
            return prepareRectifyMarkSheet(mapping, actionForm, request, response);
        } else {
            return rectifyMarkSheetStepOneByEvaluation(mapping, actionForm, request, response);
        }
    }

    public ActionForward prepareSearchMarkSheetFilled(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {
        return mapping.findForward("searchMarkSheetFilled");
    }

    public ActionForward showRectificationHistoric(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {

        DynaActionForm form = (DynaActionForm) actionForm;
        EnrolmentEvaluation enrolmentEvaluation = getDomainObject(form, "evaluationID");
        Enrolment enrolment = enrolmentEvaluation.getEnrolment();

        List<EnrolmentEvaluation> rectifiedAndRectificationEvaluations =
                enrolment.getConfirmedEvaluations(enrolmentEvaluation.getMarkSheet().getEvaluationSeason());
        if (!rectifiedAndRectificationEvaluations.isEmpty()) {
            request.setAttribute("enrolmentEvaluation", rectifiedAndRectificationEvaluations.remove(0));
            request.setAttribute("rectificationEvaluations", rectifiedAndRectificationEvaluations);
        }

        return mapping.findForward("showRectificationHistoric");
    }
}
