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

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.DynaActionForm;
import org.fenixedu.academic.domain.Enrolment;
import org.fenixedu.academic.domain.ExecutionSemester;
import org.fenixedu.academic.domain.MarkSheet;
import org.fenixedu.academic.domain.Teacher;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.student.Student;
import org.fenixedu.academic.dto.degreeAdministrativeOffice.gradeSubmission.MarkSheetEnrolmentEvaluationBean;
import org.fenixedu.academic.dto.degreeAdministrativeOffice.gradeSubmission.MarkSheetManagementCreateBean;
import org.fenixedu.academic.predicate.IllegalDataAccessException;
import org.fenixedu.academic.service.services.exceptions.FenixServiceException;
import org.fenixedu.academic.util.FileUtils;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.struts.annotations.Forward;
import org.fenixedu.bennu.struts.annotations.Forwards;
import org.fenixedu.bennu.struts.annotations.Mapping;

import com.google.common.io.Files;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;

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
                createBean.getTeacherId(), teacher, request, createBean.getEvaluationSeason(), actionMessages);
        if (!actionMessages.isEmpty()) {
            createBean.setTeacherId(null);
        }
        checkIfEvaluationDateIsInExamsPeriod(createBean.getDegreeCurricularPlan(), createBean.getExecutionPeriod(),
                createBean.getEvaluationDate(), createBean.getEvaluationSeason(), request, actionMessages);

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
                createBean.getCurricularCourse().getEnrolmentsNotInAnyMarkSheet(createBean.getEvaluationSeason(),
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

                final Integer currentGrade = enrolment.getFinalGrade();
                markSheetEnrolmentEvaluationBean.setCurrentGrade(currentGrade == null ? "-" : currentGrade.toString());

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
    
	public ActionForward uploadMarkSheetStepTwo(ActionMapping mapping, ActionForm actionForm,
			HttpServletRequest request, HttpServletResponse response) throws FenixServiceException {

		MarkSheetManagementCreateBean uploadBean = (MarkSheetManagementCreateBean) RenderUtils
				.getViewState("fileInputStream").getMetaObject().getObject();
		uploadBean.setTeacher(Teacher.readByIstId(uploadBean.getTeacherId()));
		ActionMessages actionMessages = createActionMessages();

		try {
			final Map<String, String> marks = loadMarks(uploadBean.getInputStream());
			uploadBean.getAllEnrolmentEvalutionBeans().stream().forEach(bean -> {
				bean.setGradeValue(null);
			});
			
			marks.keySet().forEach(studentId -> {
				MarkSheetEnrolmentEvaluationBean enrolmentEvaluationBean = uploadBean.getAllEnrolmentEvalutionBeans()
						.stream()
						.filter(enrolmentBean -> enrolmentBean.getEnrolment().getRegistration().getStudent().getPerson()
								.getUsername().equals(studentId)
								|| enrolmentBean.getEnrolment().getRegistration().getStudent().getNumber().toString()
										.equals(studentId))
						.findAny().orElse(null);
				if (enrolmentEvaluationBean == null) {
					addMessage(request, actionMessages, "error.no.studentId.in.markSheet", studentId);
				} else {
					String grade = marks.get(studentId);
					if (grade != null) {
						enrolmentEvaluationBean.setGradeValue(grade);
					}
				}
			});
		} catch (IOException e) {
			addMessage(request, actionMessages, "error.file.badFormat");
		}

		request.setAttribute("edit", uploadBean);
		return mapping.findForward("createMarkSheetStep2");
	}
    
    private Map<String, String> loadMarks(final InputStream inputStream) throws IOException {
        final Map<String, String> marks = new HashMap<>();
        File file = inputStream != null ? FileUtils.copyToTemporaryFile(inputStream) : null;
        try {
            final StringTokenizer stringTokenizer = new StringTokenizer(Files.asCharSource(file, StandardCharsets.UTF_8).read());
            while (true) {
                String studentNumber = getNextToken(stringTokenizer);
                if (studentNumber == null) {
                    return marks;
                }
                String mark = getNextToken(stringTokenizer);
                if (mark == null) {
                    throw new Exception();
                }
                marks.put(studentNumber, mark);
            }
        } catch (Exception e) {
            throw new IOException("error.file.badFormat");
        }
    }

    private String getNextToken(StringTokenizer stringTokenizer) {
        while (stringTokenizer.hasMoreTokens()) {
            String token = stringTokenizer.nextToken().trim();
            if (token.length() > 0) {
                return token;
            }
        }
        return null;
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
