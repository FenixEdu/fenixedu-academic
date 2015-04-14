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
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
import org.fenixedu.academic.domain.Teacher;
import org.fenixedu.academic.dto.degreeAdministrativeOffice.gradeSubmission.MarkSheetEnrolmentEvaluationBean;
import org.fenixedu.academic.dto.degreeAdministrativeOffice.gradeSubmission.MarkSheetManagementCreateBean;
import org.fenixedu.academic.dto.degreeAdministrativeOffice.gradeSubmission.MarkSheetRectifyBean;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.struts.annotations.Forward;
import org.fenixedu.bennu.struts.annotations.Forwards;
import org.fenixedu.bennu.struts.annotations.Mapping;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixframework.FenixFramework;

@Mapping(path = "/oldCreateMarkSheet", module = "academicAdministration", formBean = "markSheetManagementForm",
        input = "/gradeSubmission/oldMarkSheets/createMarkSheetStep1.jsp", functionality = OldMarkSheetSearchDispatchAction.class)
@Forwards({
        @Forward(name = "createMarkSheetStep1",
                path = "/academicAdministration/gradeSubmission/oldMarkSheets/createMarkSheetStep1.jsp"),
        @Forward(name = "createMarkSheetStep2",
                path = "/academicAdministration/gradeSubmission/oldMarkSheets/createMarkSheetStep2.jsp"),
        @Forward(name = "searchMarkSheetFilled",
                path = "/academicAdministration/oldMarkSheetManagement.do?method=prepareSearchMarkSheetFilled"),
        @Forward(name = "rectifyMarkSheetStep1", path = "/academicAdministration/gradeSubmission/rectifyMarkSheetStep1.jsp"),
        @Forward(name = "rectifyMarkSheetStep2", path = "/academicAdministration/gradeSubmission/rectifyMarkSheetStep2.jsp"),
        @Forward(name = "viewMarkSheet", path = "/academicAdministration/gradeSubmission/oldMarkSheets/viewMarkSheet.jsp") })
public class OldMarkSheetCreateDispatchAction extends MarkSheetCreateDispatchAction {

    @Override
    public ActionForward createMarkSheetStepOne(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {

        MarkSheetManagementCreateBean createBean =
                (MarkSheetManagementCreateBean) RenderUtils.getViewState().getMetaObject().getObject();
        request.setAttribute("edit", createBean);

        Teacher teacher = Teacher.readByIstId(createBean.getTeacherId());
        createBean.setTeacher(teacher);

        ActionMessages actionMessages = createActionMessages();

        prepareCreateEnrolmentEvaluationsForMarkSheet(createBean, request, actionMessages);

        if (!actionMessages.isEmpty()) {
            return mapping.findForward("createMarkSheetStep1");
        } else {
            return mapping.findForward("createMarkSheetStep2");
        }
    }

    @Override
    protected MarkSheet createMarkSheet(MarkSheetManagementCreateBean createBean, User userView) {
        return createBean.createOldMarkSheet(userView.getPerson());
    }

    @Override
    protected void prepareCreateEnrolmentEvaluationsForMarkSheet(MarkSheetManagementCreateBean createBean,
            HttpServletRequest request, ActionMessages actionMessages) {

        final Collection<Enrolment> enrolments =
                createBean.getCurricularCourse().getEnrolmentsNotInAnyMarkSheetForOldMarkSheets(createBean.getEvaluationSeason(),
                        createBean.getExecutionPeriod());

        if (enrolments.isEmpty()) {
            addMessage(request, actionMessages, "error.allStudentsAreInMarkSheets");
        } else {
            final Set<MarkSheetEnrolmentEvaluationBean> enrolmentEvaluationBeans =
                    new HashSet<MarkSheetEnrolmentEvaluationBean>();

            for (final Enrolment enrolment : enrolments) {
                final MarkSheetEnrolmentEvaluationBean markSheetEnrolmentEvaluationBean = new MarkSheetEnrolmentEvaluationBean();
                markSheetEnrolmentEvaluationBean.setEnrolment(enrolment);
                markSheetEnrolmentEvaluationBean.setEvaluationDate(createBean.getEvaluationDate());
                enrolmentEvaluationBeans.add(markSheetEnrolmentEvaluationBean);
            }
            createBean.setEnrolmentEvaluationBeans(enrolmentEvaluationBeans);
        }
    }

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

}
