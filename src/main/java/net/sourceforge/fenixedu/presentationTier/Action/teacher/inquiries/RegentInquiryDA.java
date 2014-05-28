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
package net.sourceforge.fenixedu.presentationTier.Action.teacher.inquiries;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.dataTransferObject.inquiries.CurricularCourseResumeResult;
import net.sourceforge.fenixedu.dataTransferObject.inquiries.InquiryBlockDTO;
import net.sourceforge.fenixedu.dataTransferObject.inquiries.RegentInquiryBean;
import net.sourceforge.fenixedu.dataTransferObject.inquiries.RegentTeacherResultsResume;
import net.sourceforge.fenixedu.dataTransferObject.inquiries.TeacherShiftTypeGroupsResumeResult;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.domain.ShiftType;
import net.sourceforge.fenixedu.domain.inquiries.DelegateInquiryTemplate;
import net.sourceforge.fenixedu.domain.inquiries.InquiryBlock;
import net.sourceforge.fenixedu.domain.inquiries.InquiryDelegateAnswer;
import net.sourceforge.fenixedu.domain.inquiries.InquiryResponseState;
import net.sourceforge.fenixedu.domain.inquiries.InquiryResult;
import net.sourceforge.fenixedu.domain.inquiries.InquiryTeacherAnswer;
import net.sourceforge.fenixedu.domain.inquiries.RegentInquiryTemplate;
import net.sourceforge.fenixedu.domain.inquiries.ResultPersonCategory;
import net.sourceforge.fenixedu.domain.inquiries.TeacherInquiryTemplate;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.presentationTier.Action.publico.ViewTeacherInquiryPublicResults;
import net.sourceforge.fenixedu.presentationTier.Action.teacher.ManageExecutionCourseDA;
import net.sourceforge.fenixedu.presentationTier.Action.teacher.executionCourse.ExecutionCourseBaseAction;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
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
                || (inquiryTemplate.getExecutionPeriod().getNextExecutionPeriod().isCurrent() && !inquiryTemplate.isOpen() && !inquiryTemplate
                        .getExecutionPeriod().hasAnyInquiryResults())) {
            return forward(request, "/teacher/inquiries/regentInquiryClosed.jsp");
        } else if (!inquiryTemplate.isOpen()) {
            request.setAttribute("readMode", "readMode");
        }

        if (!professorship.getPerson().hasToAnswerRegentInquiry(professorship)) {
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
        for (Professorship teacherProfessorship : executionCourse.getProfessorships()) {
            Collection<InquiryResult> professorshipResults = teacherProfessorship.getInquiryResults();
            if (!professorshipResults.isEmpty()) {
                for (ShiftType shiftType : getShiftTypes(professorshipResults)) {
                    List<InquiryResult> teacherShiftResults = teacherProfessorship.getInquiryResults(shiftType);
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
        if (professorship.getPerson().hasToAnswerTeacherInquiry(professorship)) {
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
        if (!professorship.hasInquiryRegentAnswer()) {
            finalState = InquiryResponseState.EMPTY;
        } else if (professorship.getInquiryRegentAnswer().hasRequiredQuestionsToAnswer(inquiryTemplate)
                || professorship.getPerson().hasMandatoryCommentsToMakeAsRegentInUC(executionCourse)
                || professorship.hasMandatoryCommentsToMakeAsResponsible()) {
            finalState = InquiryResponseState.INCOMPLETE;
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

    public ActionForward showTeacherInquiry(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        Professorship professorship = FenixFramework.getDomainObject(getFromRequest(request, "professorshipOID").toString());

        TeacherInquiryTemplate teacherInquiryTemplate =
                TeacherInquiryTemplate.getTemplateByExecutionPeriod(professorship.getExecutionCourse().getExecutionPeriod());
        InquiryTeacherAnswer inquiryTeacherAnswer = professorship.getInquiryTeacherAnswer();

        Set<InquiryBlockDTO> teacherInquiryBlocks = new TreeSet<InquiryBlockDTO>(new BeanComparator("inquiryBlock.blockOrder"));
        for (InquiryBlock inquiryBlock : teacherInquiryTemplate.getInquiryBlocks()) {
            teacherInquiryBlocks.add(new InquiryBlockDTO(inquiryTeacherAnswer, inquiryBlock));
        }

        request.setAttribute("executionPeriod", professorship.getExecutionCourse().getExecutionPeriod());
        request.setAttribute("executionCourse", professorship.getExecutionCourse());
        request.setAttribute("person", professorship.getPerson());
        request.setAttribute("teacherInquiryBlocks", teacherInquiryBlocks);
        return actionMapping.findForward("teacherInquiry");
    }

    public ActionForward showDelegateInquiry(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        ExecutionCourse executionCourse =
                FenixFramework.getDomainObject(getFromRequest(request, "executionCourseOID").toString());
        ExecutionDegree executionDegree =
                FenixFramework.getDomainObject(getFromRequest(request, "executionDegreeOID").toString());

        DelegateInquiryTemplate delegateInquiryTemplate =
                DelegateInquiryTemplate.getTemplateByExecutionPeriod(executionCourse.getExecutionPeriod());
        InquiryDelegateAnswer inquiryDelegateAnswer = null;
        for (InquiryDelegateAnswer delegateAnswer : executionCourse.getInquiryDelegatesAnswers()) {
            if (delegateAnswer.getExecutionDegree() == executionDegree) {
                inquiryDelegateAnswer = delegateAnswer;
                break;
            }
        }

        Set<InquiryBlockDTO> delegateInquiryBlocks = new TreeSet<InquiryBlockDTO>(new BeanComparator("inquiryBlock.blockOrder"));
        for (InquiryBlock inquiryBlock : delegateInquiryTemplate.getInquiryBlocks()) {
            delegateInquiryBlocks.add(new InquiryBlockDTO(inquiryDelegateAnswer, inquiryBlock));
        }

        Integer year = inquiryDelegateAnswer != null ? inquiryDelegateAnswer.getDelegate().getCurricularYear().getYear() : null;
        request.setAttribute("year", year);
        request.setAttribute("executionPeriod", executionCourse.getExecutionPeriod());
        request.setAttribute("executionCourse", executionCourse);
        request.setAttribute("executionDegree", executionDegree);
        request.setAttribute("delegateInquiryBlocks", delegateInquiryBlocks);
        return actionMapping.findForward("delegateInquiry");
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
