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
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.fenixedu.academic.dto.inquiries.CurricularCourseResumeResult;
import org.fenixedu.academic.dto.inquiries.RegentInquiryBean;
import org.fenixedu.academic.dto.inquiries.RegentTeacherResultsResume;
import org.fenixedu.academic.dto.inquiries.TeacherShiftTypeGroupsResumeResult;
import org.fenixedu.academic.domain.ExecutionCourse;
import org.fenixedu.academic.domain.ExecutionDegree;
import org.fenixedu.academic.domain.Professorship;
import org.fenixedu.academic.domain.ShiftType;
import org.fenixedu.academic.domain.inquiries.InquiryResponseState;
import org.fenixedu.academic.domain.inquiries.InquiryResult;
import org.fenixedu.academic.domain.inquiries.InquiryResultComment;
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

@Mapping(path = "/regentInquiry", module = "teacher", functionality = ManageExecutionCourseDA.class)
public class RegentInquiryDA extends ExecutionCourseBaseAction {

    public ActionForward showInquiriesPrePage(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {

        ExecutionCourse executionCourse = readAndSaveExecutionCourse(request);
        Professorship professorship = getProfessorship(executionCourse);

        RegentInquiryTemplate inquiryTemplate =
                RegentInquiryTemplate.getTemplateByExecutionPeriod(executionCourse.getExecutionPeriod());
        // if the inquiry doesn't exist or is from the execution period right
        // before the current one and isn't open and has no results,
        // that means that is not to answer and has no data to see
        if (inquiryTemplate == null
                || (inquiryTemplate.getExecutionPeriod().getNextExecutionPeriod().isCurrent() && !inquiryTemplate.isOpen() && inquiryTemplate
                        .getExecutionPeriod().getInquiryResultsSet().isEmpty())) {
            return forward(request, "/teacher/inquiries/regentInquiryClosed.jsp");
        } else if (!inquiryTemplate.isOpen()) {
            request.setAttribute("readMode", "readMode");
        }
        if (!RegentInquiryTemplate.hasToAnswerRegentInquiry(professorship)) {
            return forward(request, "/teacher/inquiries/regentInquiryUnavailable.jsp");
        }

        List<CurricularCourseResumeResult> coursesResultResume = new ArrayList<CurricularCourseResumeResult>();
        for (ExecutionDegree executionDegree : executionCourse.getExecutionDegrees()) {
            CurricularCourseResumeResult courseResumeResult =
                    new CurricularCourseResumeResult(executionCourse, executionDegree, "label.inquiry.degree", executionDegree
                            .getDegree().getSigla(), professorship.getPerson(), ResultPersonCategory.TEACHER, true, false, false,
                            false, true);
            if (courseResumeResult.getResultBlocks().size() > 1) {
                coursesResultResume.add(courseResumeResult);
            }
        }
        Collections.sort(coursesResultResume, new BeanComparator("firstPresentationName"));

        Map<Professorship, RegentTeacherResultsResume> regentTeachersResumeMap =
                new HashMap<Professorship, RegentTeacherResultsResume>();

        List<Professorship> teachersWithNoResults = new ArrayList<Professorship>();
        for (Professorship teacherProfessorship : executionCourse.getProfessorshipsSet()) {
            Collection<InquiryResult> professorshipResults = teacherProfessorship.getInquiryResultsSet();
            if (!professorshipResults.isEmpty()) {
                for (ShiftType shiftType : getShiftTypes(professorshipResults)) {
                    List<InquiryResult> teacherShiftResults = InquiryResult.getInquiryResults(teacherProfessorship, shiftType);
                    if (!teacherShiftResults.isEmpty()) {
                        TeacherShiftTypeGroupsResumeResult teacherShiftTypeGroupsResumeResult =
                                new TeacherShiftTypeGroupsResumeResult(teacherProfessorship, shiftType,
                                        ResultPersonCategory.TEACHER, "label.inquiry.shiftType",
                                        RenderUtils.getEnumString(shiftType), teacherProfessorship == professorship);

                        RegentTeacherResultsResume regentTeachersResultsResume =
                                regentTeachersResumeMap.get(teacherProfessorship);
                        if (regentTeachersResultsResume == null) {
                            regentTeachersResultsResume = new RegentTeacherResultsResume(teacherProfessorship);
                            regentTeachersResumeMap.put(teacherProfessorship, regentTeachersResultsResume);
                        }
                        regentTeachersResultsResume.addTeacherShiftTypeGroupsResumeResult(teacherShiftTypeGroupsResumeResult);
                    }
                }
            } else {
                teachersWithNoResults.add(teacherProfessorship);
            }
        }

        InquiryResponseState finalState = getFilledState(executionCourse, professorship, inquiryTemplate);
        InquiryResponseState teacherFilledState = null;
        if (TeacherInquiryTemplate.hasToAnswerTeacherInquiry(professorship.getPerson(), professorship)) {
            teacherFilledState =
                    TeachingInquiryDA.getFilledState(professorship,
                            TeacherInquiryTemplate.getTemplateByExecutionPeriod(executionCourse.getExecutionPeriod()),
                            new ArrayList<TeacherShiftTypeGroupsResumeResult>());
        } else {
            teacherFilledState = InquiryResponseState.COMPLETE;
        }
        if (!InquiryResponseState.COMPLETE.equals(teacherFilledState)) {
            request.setAttribute("teacherCompletionState", teacherFilledState.getLocalizedName());
        }

        List<RegentTeacherResultsResume> regentTeachersResumeList =
                new ArrayList<RegentTeacherResultsResume>(regentTeachersResumeMap.values());

        request.setAttribute("isComplete", InquiryResponseState.COMPLETE.equals(finalState));
        request.setAttribute("completionState", finalState.getLocalizedName());
        Collections.sort(regentTeachersResumeList, new BeanComparator("professorship.person.name"));

        request.setAttribute("professorship", professorship);
        request.setAttribute("executionSemester", executionCourse.getExecutionPeriod());
        request.setAttribute("regentTeachersResumeList", regentTeachersResumeList);
        request.setAttribute("teachersWithNoResults", teachersWithNoResults);
        request.setAttribute("coursesResultResume", coursesResultResume);

        ViewTeacherInquiryPublicResults.setTeacherScaleColorException(executionCourse.getExecutionPeriod(), request);
        return forward(request, "/teacher/inquiries/regentInquiryResultsResume.jsp");
    }

    static InquiryResponseState getFilledState(ExecutionCourse executionCourse, Professorship professorship,
            RegentInquiryTemplate inquiryTemplate) {
        InquiryResponseState finalState = InquiryResponseState.COMPLETE;
        if (professorship.getInquiryRegentAnswer() == null) {
            finalState = InquiryResponseState.EMPTY;
        } else {
            final ExecutionCourse executionCourse1 = executionCourse;
            if (professorship.getInquiryRegentAnswer().hasRequiredQuestionsToAnswer(inquiryTemplate)
                    || InquiryResultComment.hasMandatoryCommentsToMakeAsRegentInUC(professorship.getPerson(), executionCourse1)
                    || InquiryResultComment.hasMandatoryCommentsToMakeAsResponsible(professorship)) {
                finalState = InquiryResponseState.INCOMPLETE;
            }
        }
        return finalState;
    }

    private Set<ShiftType> getShiftTypes(Collection<InquiryResult> professorshipResults) {
        Set<ShiftType> shiftTypes = new HashSet<ShiftType>();
        for (InquiryResult inquiryResult : professorshipResults) {
            shiftTypes.add(inquiryResult.getShiftType());
        }
        return shiftTypes;
    }

    public ActionForward showRegentInquiry(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        Professorship professorship = FenixFramework.getDomainObject(getFromRequest(request, "professorshipOID").toString());
        RegentInquiryTemplate regentInquiryTemplate = RegentInquiryTemplate.getCurrentTemplate();

        RegentInquiryBean regentInquiryBean = new RegentInquiryBean(regentInquiryTemplate, professorship);

        request.setAttribute("executionPeriod", professorship.getExecutionCourse().getExecutionPeriod());
        request.setAttribute("executionCourse", professorship.getExecutionCourse());
        request.setAttribute("regentInquiryBean", regentInquiryBean);

        return forward(request, "/teacher/inquiries/regentInquiry.jsp");
    }

    public ActionForward saveChanges(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        final RegentInquiryBean regentInquiryBean = getRenderedObject("regentInquiryBean");

        String validationResult = regentInquiryBean.validateInquiry();
        if (!Boolean.valueOf(validationResult)) {
            RenderUtils.invalidateViewState();
            addActionMessage(request, "error.inquiries.fillInQuestion", validationResult);

            request.setAttribute("regentInquiryBean", regentInquiryBean);
            request.setAttribute("executionPeriod", regentInquiryBean.getProfessorship().getExecutionCourse()
                    .getExecutionPeriod());
            request.setAttribute("executionCourse", regentInquiryBean.getProfessorship().getExecutionCourse());
            return forward(request, "/teacher/inquiries/regentInquiry.jsp");
        }

        RenderUtils.invalidateViewState("regentInquiryBean");
        regentInquiryBean.saveChanges(getUserView(request).getPerson(), ResultPersonCategory.REGENT);

        request.setAttribute("updated", "true");
        request.setAttribute("executionCourse", regentInquiryBean.getProfessorship().getExecutionCourse());
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
