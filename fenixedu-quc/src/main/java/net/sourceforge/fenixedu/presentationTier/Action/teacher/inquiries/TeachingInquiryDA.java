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
/**
 * 
 */
package org.fenixedu.academic.ui.struts.action.teacher.inquiries;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.fenixedu.academic.dto.inquiries.CurricularCourseResumeResult;
import org.fenixedu.academic.dto.inquiries.TeacherInquiryBean;
import org.fenixedu.academic.dto.inquiries.TeacherShiftTypeGroupsResumeResult;
import org.fenixedu.academic.domain.ExecutionCourse;
import org.fenixedu.academic.domain.ExecutionDegree;
import org.fenixedu.academic.domain.Professorship;
import org.fenixedu.academic.domain.ShiftType;
import org.fenixedu.academic.domain.inquiries.InquiryResponseState;
import org.fenixedu.academic.domain.inquiries.InquiryResult;
import org.fenixedu.academic.domain.inquiries.RegentInquiryTemplate;
import org.fenixedu.academic.domain.inquiries.ResultPersonCategory;
import org.fenixedu.academic.domain.inquiries.TeacherInquiryTemplate;
import org.fenixedu.academic.predicate.AccessControl;
import org.fenixedu.academic.ui.struts.action.publico.ViewTeacherInquiryPublicResults;
import org.fenixedu.academic.ui.struts.action.teacher.ManageExecutionCourseDA;
import org.fenixedu.academic.ui.struts.action.teacher.executionCourse.ExecutionCourseBaseAction;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.bennu.struts.annotations.Mapping;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixframework.FenixFramework;

@Mapping(path = "/teachingInquiry", module = "teacher", functionality = ManageExecutionCourseDA.class)
public class TeachingInquiryDA extends ExecutionCourseBaseAction {

    public ActionForward showInquiriesPrePage(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {

        ExecutionCourse executionCourse = readAndSaveExecutionCourse(request);
        Professorship professorship = getProfessorship(executionCourse);

        TeacherInquiryTemplate inquiryTemplate =
                TeacherInquiryTemplate.getTemplateByExecutionPeriod(executionCourse.getExecutionPeriod());
        // if the inquiry doesn't exist or is from the execution period right
        // before the current one and isn't open and has no results,
        // that means that is not to answer and has no data to see
        if (inquiryTemplate == null
                || (inquiryTemplate.getExecutionPeriod().getNextExecutionPeriod().isCurrent() && !inquiryTemplate.isOpen() && inquiryTemplate
                        .getExecutionPeriod().getInquiryResultsSet().isEmpty())) {
            return forward(request, "/teacher/inquiries/inquiriesClosed.jsp");
        } else if (!inquiryTemplate.isOpen()) {
            request.setAttribute("readMode", "readMode");
        }

        if (!TeacherInquiryTemplate.hasToAnswerTeacherInquiry(professorship.getPerson(), professorship)) {
            return forward(request, "/teacher/inquiries/inquiryUnavailable.jsp");
        }

        List<TeacherShiftTypeGroupsResumeResult> teacherResults = new ArrayList<TeacherShiftTypeGroupsResumeResult>();

        InquiryResponseState finalState = getFilledState(professorship, inquiryTemplate, teacherResults);
        if (professorship.isResponsibleFor()) {
            InquiryResponseState regentFilledState =
                    RegentInquiryDA.getFilledState(executionCourse, professorship,
                            RegentInquiryTemplate.getTemplateByExecutionPeriod(executionCourse.getExecutionPeriod()));
            if (!InquiryResponseState.COMPLETE.equals(regentFilledState)) {
                request.setAttribute("regentCompletionState", regentFilledState.getLocalizedName());
            }
        }

        request.setAttribute("isComplete", InquiryResponseState.COMPLETE.equals(finalState));
        request.setAttribute("completionState", finalState.getLocalizedName());
        Collections.sort(teacherResults, new BeanComparator("shiftType"));

        List<CurricularCourseResumeResult> coursesResultResume = new ArrayList<CurricularCourseResumeResult>();
        for (ExecutionDegree executionDegree : executionCourse.getExecutionDegrees()) {
            CurricularCourseResumeResult courseResumeResult =
                    new CurricularCourseResumeResult(executionCourse, executionDegree, "label.inquiry.degree", executionDegree
                            .getDegree().getSigla(), null, null, false, false, false, false, true);
            if (courseResumeResult.getResultBlocks().size() > 1) {
                coursesResultResume.add(courseResumeResult);
            }
        }
        Collections.sort(coursesResultResume, new BeanComparator("firstPresentationName"));

        request.setAttribute("professorship", professorship);
        request.setAttribute("executionSemester", executionCourse.getExecutionPeriod());
        request.setAttribute("teacherResults", teacherResults);
        request.setAttribute("coursesResultResume", coursesResultResume);

        ViewTeacherInquiryPublicResults.setTeacherScaleColorException(executionCourse.getExecutionPeriod(), request);
        return forward(request, "/teacher/inquiries/inquiryResultsResume.jsp");
    }

    static InquiryResponseState getFilledState(Professorship professorship, TeacherInquiryTemplate inquiryTemplate,
            List<TeacherShiftTypeGroupsResumeResult> teacherResults) {
        Collection<InquiryResult> professorshipResults = professorship.getInquiryResultsSet();
        InquiryResponseState finalState = InquiryResponseState.COMPLETE;
        if (professorship.getInquiryTeacherAnswer() != null
                && professorship.getInquiryTeacherAnswer().hasRequiredQuestionsToAnswer(inquiryTemplate)) {
            finalState = InquiryResponseState.PARTIALLY_FILLED;
        }
        if (!professorshipResults.isEmpty()) {
            for (ShiftType shiftType : getShiftTypes(professorshipResults)) {
                List<InquiryResult> teacherShiftResults = InquiryResult.getInquiryResults(professorship, shiftType);
                if (!teacherShiftResults.isEmpty()) {
                    TeacherShiftTypeGroupsResumeResult teacherShiftTypeGroupsResumeResult =
                            new TeacherShiftTypeGroupsResumeResult(professorship, shiftType, ResultPersonCategory.TEACHER,
                                    "label.inquiry.shiftType", RenderUtils.getEnumString(shiftType), false);
                    InquiryResponseState completionStateType = teacherShiftTypeGroupsResumeResult.getCompletionStateType();
                    finalState = finalState.compareTo(completionStateType) > 0 ? finalState : completionStateType;
                    teacherResults.add(teacherShiftTypeGroupsResumeResult);
                }
            }
        } else if (professorship.getInquiryTeacherAnswer() == null) {
            finalState = InquiryResponseState.EMPTY;
        } else if (professorship.getInquiryTeacherAnswer().hasRequiredQuestionsToAnswer(inquiryTemplate)) {
            finalState = InquiryResponseState.PARTIALLY_FILLED;
        }
        return finalState;
    }

    private static Set<ShiftType> getShiftTypes(Collection<InquiryResult> professorshipResults) {
        Set<ShiftType> shiftTypes = new HashSet<ShiftType>();
        for (InquiryResult inquiryResult : professorshipResults) {
            shiftTypes.add(inquiryResult.getShiftType());
        }
        return shiftTypes;
    }

    public ActionForward showTeacherInquiry(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        Professorship professorship = FenixFramework.getDomainObject(getFromRequest(request, "professorshipOID").toString());
        TeacherInquiryTemplate teacherInquiryTemplate = TeacherInquiryTemplate.getCurrentTemplate();

        TeacherInquiryBean teacherInquiryBean = new TeacherInquiryBean(teacherInquiryTemplate, professorship);

        request.setAttribute("executionPeriod", professorship.getExecutionCourse().getExecutionPeriod());
        request.setAttribute("executionCourse", professorship.getExecutionCourse());
        request.setAttribute("teacherInquiryBean", teacherInquiryBean);

        return forward(request, "/teacher/inquiries/teacherInquiry.jsp");
    }

    public ActionForward postBackTeacherInquiry(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        final TeacherInquiryBean teacherInquiryBean = getRenderedObject("teacherInquiryBean");
        teacherInquiryBean.setGroupsVisibility();

        request.setAttribute("executionPeriod", teacherInquiryBean.getProfessorship().getExecutionCourse().getExecutionPeriod());
        request.setAttribute("executionCourse", teacherInquiryBean.getProfessorship().getExecutionCourse());
        request.setAttribute("teacherInquiryBean", teacherInquiryBean);

        RenderUtils.invalidateViewState();
        return forward(request, "/teacher/inquiries/teacherInquiry.jsp");
    }

    public ActionForward saveChanges(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        final TeacherInquiryBean teacherInquiryBean = getRenderedObject("teacherInquiryBean");

        String validationResult = teacherInquiryBean.validateInquiry();
        if (!Boolean.valueOf(validationResult)) {
            RenderUtils.invalidateViewState();
            addActionMessage(request, "error.inquiries.fillInQuestion", validationResult);

            request.setAttribute("teacherInquiryBean", teacherInquiryBean);
            request.setAttribute("executionPeriod", teacherInquiryBean.getProfessorship().getExecutionCourse()
                    .getExecutionPeriod());
            request.setAttribute("executionCourse", teacherInquiryBean.getProfessorship().getExecutionCourse());
            return forward(request, "/teacher/inquiries/teacherInquiry.jsp");
        }

        RenderUtils.invalidateViewState("teacherInquiryBean");
        teacherInquiryBean.saveChanges(getUserView(request).getPerson(), ResultPersonCategory.TEACHER);

        request.setAttribute("executionCourse", teacherInquiryBean.getProfessorship().getExecutionCourse());
        request.setAttribute("updated", "true");
        return showInquiriesPrePage(actionMapping, actionForm, request, response);
    }

    private ExecutionCourse readAndSaveExecutionCourse(HttpServletRequest request) {
        ExecutionCourse executionCourse = getDomainObject(request, "executionCourseID");
        if (executionCourse == null) {
            return (ExecutionCourse) request.getAttribute("executionCourse");
        }
        request.setAttribute("executionCourse", executionCourse);
        return executionCourse;
    }

    private Professorship getProfessorship(ExecutionCourse executionCourse) {
        return AccessControl.getPerson().getProfessorshipByExecutionCourse(executionCourse);
    }
}
