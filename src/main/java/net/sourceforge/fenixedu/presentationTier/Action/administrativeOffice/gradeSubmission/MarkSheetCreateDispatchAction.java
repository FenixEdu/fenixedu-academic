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

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.degreeAdministrativeOffice.gradeSubmission.MarkSheetEnrolmentEvaluationBean;
import net.sourceforge.fenixedu.dataTransferObject.degreeAdministrativeOffice.gradeSubmission.MarkSheetManagementCreateBean;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.MarkSheet;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.injectionCode.IllegalDataAccessException;

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

@Mapping(path = "/createMarkSheet", module = "academicAdministration", formBean = "markSheetManagementForm",
        input = "/gradeSubmission/createMarkSheetStep1.jsp", functionality = MarkSheetSearchDispatchAction.class)
@Forwards({
        @Forward(name = "createMarkSheetStep1", path = "/academicAdministration/gradeSubmission/createMarkSheetStep1.jsp"),
        @Forward(name = "createMarkSheetStep2", path = "/academicAdministration/gradeSubmission/createMarkSheetStep2.jsp"),
        @Forward(name = "searchMarkSheetFilled",
                path = "/academicAdministration/markSheetManagement.do?method=prepareSearchMarkSheetFilled"),
        @Forward(name = "rectifyMarkSheetStep1", path = "/academicAdministration/gradeSubmission/rectifyMarkSheetStep1.jsp"),
        @Forward(name = "rectifyMarkSheetStep2", path = "/academicAdministration/gradeSubmission/rectifyMarkSheetStep2.jsp"),
        @Forward(name = "viewMarkSheet", path = "/academicAdministration/gradeSubmission/viewMarkSheet.jsp") })
public class MarkSheetCreateDispatchAction extends MarkSheetDispatchAction {

    public ActionForward prepareCreateMarkSheet(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {

        MarkSheetManagementCreateBean markSheetManagementCreateBean = new MarkSheetManagementCreateBean();
        markSheetManagementCreateBean.setExecutionPeriod(ExecutionSemester.readActualExecutionSemester());
        markSheetManagementCreateBean.setUrl("");

        request.setAttribute("edit", markSheetManagementCreateBean);

        return mapping.findForward("createMarkSheetStep1");
    }

    public ActionForward prepareCreateMarkSheetFilled(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {

        MarkSheetManagementCreateBean markSheetManagementCreateBean = new MarkSheetManagementCreateBean();
        fillMarkSheetBean(actionForm, request, markSheetManagementCreateBean);
        markSheetManagementCreateBean.setUrl(buildUrl((DynaActionForm) actionForm));

        request.setAttribute("edit", markSheetManagementCreateBean);

        return mapping.findForward("createMarkSheetStep1");
    }

    public ActionForward createMarkSheetStepOne(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {

        MarkSheetManagementCreateBean createBean =
                (MarkSheetManagementCreateBean) RenderUtils.getViewState().getMetaObject().getObject();
        request.setAttribute("edit", createBean);

        Teacher teacher = Teacher.readByIstId(createBean.getTeacherId());
        createBean.setTeacher(teacher);

        ActionMessages actionMessages = createActionMessages();
        checkIfTeacherIsResponsibleOrCoordinator(createBean.getCurricularCourse(), createBean.getExecutionPeriod(),
                createBean.getTeacherId(), teacher, request, createBean.getMarkSheetType(), actionMessages);
        if (!actionMessages.isEmpty()) {
            createBean.setTeacherId(null);
        }
        checkIfEvaluationDateIsInExamsPeriod(createBean.getDegreeCurricularPlan(), createBean.getExecutionPeriod(),
                createBean.getEvaluationDate(), createBean.getMarkSheetType(), request, actionMessages);

        prepareCreateEnrolmentEvaluationsForMarkSheet(createBean, request, actionMessages);

        if (!actionMessages.isEmpty()) {
            return mapping.findForward("createMarkSheetStep1");
        } else {
            return mapping.findForward("createMarkSheetStep2");
        }
    }

    protected void prepareCreateEnrolmentEvaluationsForMarkSheet(MarkSheetManagementCreateBean createBean,
            HttpServletRequest request, ActionMessages actionMessages) {

        final Collection<Enrolment> enrolments =
                createBean.getCurricularCourse().getEnrolmentsNotInAnyMarkSheet(createBean.getMarkSheetType(),
                        createBean.getExecutionPeriod());

        if (enrolments.isEmpty()) {
            addMessage(request, actionMessages, "error.allStudentsAreInMarkSheets");
        } else {
            final Set<MarkSheetEnrolmentEvaluationBean> impossibleEnrolmentEvaluationBeans =
                    new HashSet<MarkSheetEnrolmentEvaluationBean>();
            final Set<MarkSheetEnrolmentEvaluationBean> enrolmentEvaluationBeans =
                    new HashSet<MarkSheetEnrolmentEvaluationBean>();

            for (final Enrolment enrolment : enrolments) {
                final MarkSheetEnrolmentEvaluationBean markSheetEnrolmentEvaluationBean = new MarkSheetEnrolmentEvaluationBean();
                markSheetEnrolmentEvaluationBean.setEnrolment(enrolment);
                markSheetEnrolmentEvaluationBean.setEvaluationDate(createBean.getEvaluationDate());

                if (enrolment.isImpossible()) {
                    impossibleEnrolmentEvaluationBeans.add(markSheetEnrolmentEvaluationBean);
                } else {
                    enrolmentEvaluationBeans.add(markSheetEnrolmentEvaluationBean);
                }
            }
            createBean.setEnrolmentEvaluationBeans(enrolmentEvaluationBeans);
            createBean.setImpossibleEnrolmentEvaluationBeans(impossibleEnrolmentEvaluationBeans);
        }
    }

    public ActionForward createMarkSheetStepTwo(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {

        MarkSheetManagementCreateBean createBean =
                (MarkSheetManagementCreateBean) RenderUtils.getViewState("edit-invisible").getMetaObject().getObject();
        createBean.setTeacher(Teacher.readByIstId(createBean.getTeacherId()));

        ActionMessages actionMessages = createActionMessages();
        User userView = getUserView(request);
        try {
            MarkSheet markSheet = createMarkSheet(createBean, userView);
            ((DynaActionForm) actionForm).set("msID", markSheet.getExternalId());
            return viewMarkSheet(mapping, actionForm, request, response);
        } catch (final IllegalDataAccessException e) {
            addMessage(request, actionMessages, "error.notAuthorized");
        } catch (final DomainException e) {
            addMessage(request, actionMessages, e.getMessage(), e.getArgs());
        }

        request.setAttribute("edit", createBean);
        return mapping.findForward("createMarkSheetStep2");
    }

    public ActionForward createMarkSheetStepTwoInvalid(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {
        /*
         * - This method is used when a validation error occurs. Instead of
         * creating a new bean we use the existing one. - If we dont't use this
         * method, the createMarkSheetStep1 is called (input method) and a new
         * create bean is created.
         */
        request.setAttribute("edit", RenderUtils.getViewState("edit-invisible").getMetaObject().getObject());
        return mapping.findForward("createMarkSheetStep2");
    }

    public ActionForward prepareSearchMarkSheetFilled(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {
        return mapping.findForward("searchMarkSheetFilled");
    }

    protected MarkSheet createMarkSheet(MarkSheetManagementCreateBean createBean, User userView) {
        return createBean.createMarkSheet(userView.getPerson());
    }
}
