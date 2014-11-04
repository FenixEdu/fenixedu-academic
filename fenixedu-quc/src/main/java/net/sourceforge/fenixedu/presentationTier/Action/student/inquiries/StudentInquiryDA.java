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
package org.fenixedu.academic.ui.struts.action.student.inquiries;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.fenixedu.academic.dto.VariantBean;
import org.fenixedu.academic.dto.inquiries.CurricularCourseInquiriesRegistryDTO;
import org.fenixedu.academic.dto.inquiries.InquiryBlockDTO;
import org.fenixedu.academic.dto.inquiries.InquiryGroupQuestionBean;
import org.fenixedu.academic.dto.inquiries.InquiryQuestionDTO;
import org.fenixedu.academic.dto.inquiries.StudentInquiryBean;
import org.fenixedu.academic.dto.inquiries.StudentTeacherInquiryBean;
import org.fenixedu.academic.domain.ExecutionSemester;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.inquiries.CurricularCourseInquiryTemplate;
import org.fenixedu.academic.domain.inquiries.InquiryBlock;
import org.fenixedu.academic.domain.inquiries.InquiryCourseAnswer;
import org.fenixedu.academic.domain.inquiries.InquiryNotAnsweredJustification;
import org.fenixedu.academic.domain.inquiries.StudentInquiryExecutionPeriod;
import org.fenixedu.academic.domain.inquiries.StudentInquiryRegistry;
import org.fenixedu.academic.domain.inquiries.StudentInquiryTemplate;
import org.fenixedu.academic.domain.inquiries.StudentTeacherInquiryTemplate;
import org.fenixedu.academic.domain.student.Student;
import org.fenixedu.academic.predicate.AccessControl;
import org.fenixedu.academic.ui.struts.action.base.FenixDispatchAction;
import org.fenixedu.academic.ui.struts.action.student.StudentApplication.StudentParticipateApp;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.fenixedu.bennu.struts.annotations.Forward;
import org.fenixedu.bennu.struts.annotations.Forwards;
import org.fenixedu.bennu.struts.annotations.Mapping;
import org.fenixedu.bennu.struts.portal.EntryPoint;
import org.fenixedu.bennu.struts.portal.StrutsFunctionality;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixframework.FenixFramework;

/**
 * @author - Ricardo Rodrigues (ricardo.rodrigues@ist.utl.pt)
 * 
 */
@StrutsFunctionality(app = StudentParticipateApp.class, path = "inquiry", titleKey = "link.inquiries",
        bundle = "InquiriesResources")
@Mapping(path = "/studentInquiry", module = "student", formBean = "inquiryNotAnsweredForm")
@Forwards({ @Forward(name = "chooseCourse", path = "/student/inquiries/chooseCourse.jsp"),
        @Forward(name = "inquiriesClosed", path = "/student/inquiries/inquiriesClosed.jsp"),
        @Forward(name = "showInquiry", path = "/student/inquiries/fillInInquiry.jsp"),
        @Forward(name = "showTeachersToAnswer", path = "/student/inquiries/showTeachersToAnswer.jsp"),
        @Forward(name = "chooseTeacher", path = "/student/inquiries/chooseTeacher.jsp"),
        @Forward(name = "showTeacherInquiry", path = "/student/inquiries/teacherInquiry.jsp"),
        @Forward(name = "showDontRespond", path = "/student/inquiries/dontRespond.jsp") })
public class StudentInquiryDA extends FenixDispatchAction {

    @EntryPoint
    public ActionForward showCoursesToAnswer(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        final CurricularCourseInquiryTemplate studentInquiryTemplate = CurricularCourseInquiryTemplate.getCurrentTemplate();
        if (studentInquiryTemplate == null) {
            return actionMapping.findForward("inquiriesClosed");
        }

        ExecutionSemester executionSemester = studentInquiryTemplate.getExecutionPeriod();

        final Student student = AccessControl.getPerson().getStudent();
        final Collection<StudentInquiryRegistry> coursesToAnswer =
                StudentInquiryRegistry.retrieveAndCreateMissingInquiryRegistriesForPeriod(student, executionSemester);

        boolean isAnyInquiryToAnswer = false;
        final List<CurricularCourseInquiriesRegistryDTO> courses = new ArrayList<CurricularCourseInquiriesRegistryDTO>();
        for (final StudentInquiryRegistry registry : coursesToAnswer) {
            courses.add(new CurricularCourseInquiriesRegistryDTO(registry));
            if (registry.isToAnswerLater() || registry.isToAnswerTeachers()) {
                isAnyInquiryToAnswer = true;
            }
        }

        if (courses.isEmpty() || (!isAnyInquiryToAnswer && StudentInquiryExecutionPeriod.isWeeklySpentHoursSubmittedForOpenInquiry(student))) {
            return actionMapping.findForward("inquiriesClosed");
        }

        request.setAttribute("executionSemester", executionSemester);
        Collections.sort(courses, new BeanComparator("curricularCourse.name"));
        request.setAttribute("courses", courses);
        request.setAttribute("student", student);
        final VariantBean weeklySpentHours = new VariantBean();
        weeklySpentHours.setInteger(null);
        request.setAttribute("weeklySpentHours", weeklySpentHours);
        return actionMapping.findForward("chooseCourse");
    }

    public ActionForward submitWeeklySpentHours(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        ExecutionSemester executionSemester = StudentInquiryTemplate.getCurrentTemplate().getExecutionPeriod();

        List<CurricularCourseInquiriesRegistryDTO> courses = getRenderedObject("hoursAndDaysByCourse");
        VariantBean weeklySpentHours = getRenderedObject("weeklySpentHours");

        Student student = AccessControl.getPerson().getStudent();
        try {
            StudentInquiryRegistry.setSpentTimeInPeriodForInquiry(student, courses, weeklySpentHours.getInteger(), executionSemester);
        } catch (DomainException e) {
            addActionMessage(request, e.getKey());
        }

        return showCoursesToAnswer(actionMapping, actionForm, request, response);
    }

    public ActionForward simulateAWH(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        ExecutionSemester executionSemester = StudentInquiryTemplate.getCurrentTemplate().getExecutionPeriod();

        List<CurricularCourseInquiriesRegistryDTO> courses = getRenderedObject("hoursAndDaysByCourse");
        VariantBean weeklySpentHours = getRenderedObject("weeklySpentHours");

        if (!StudentInquiryRegistry.checkTotalPercentageDistribution(courses)) {
            addActionMessage(request, "error.weeklyHoursSpentPercentage.is.not.100.percent");
        } else if (!StudentInquiryRegistry.checkTotalStudyDaysSpentInExamsSeason(courses)) {
            addActionMessage(request, "error.studyDaysSpentInExamsSeason.exceedsMaxDaysLimit");
        } else {
            RenderUtils.invalidateViewState("hoursAndDaysByCourse");
            for (CurricularCourseInquiriesRegistryDTO curricularCourseInquiriesRegistryDTO : courses) {
                curricularCourseInquiriesRegistryDTO.setAutonomousWorkHoursForSimulation(weeklySpentHours.getInteger());
            }
        }

        request.setAttribute("executionSemester", executionSemester);
        Collections.sort(courses, new BeanComparator("curricularCourse.name"));
        request.setAttribute("courses", courses);
        request.setAttribute("student", AccessControl.getPerson().getStudent());
        final VariantBean wsh = new VariantBean();
        wsh.setInteger(null);
        request.setAttribute("weeklySpentHours", wsh);
        request.setAttribute("estimatedWithSuccess", "true");
        return actionMapping.findForward("chooseCourse");
    }

    public ActionForward showJustifyNotAnswered(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        DynaActionForm form = (DynaActionForm) actionForm;
        String inquiryRegistryID = (String) getFromRequest(request, "inquiryRegistryID");
        form.set("inquiryRegistryID", inquiryRegistryID);

        request.setAttribute("inquiryRegistry", FenixFramework.getDomainObject(inquiryRegistryID));

        return actionMapping.findForward("showDontRespond");
    }

    public ActionForward justifyNotAnswered(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        DynaActionForm form = (DynaActionForm) actionForm;
        StudentInquiryRegistry inquiryRegistry = FenixFramework.getDomainObject(form.getString("inquiryRegistryID"));
        String notAnsweredJustification = (String) form.get("notAnsweredJustification");
        if (StringUtils.isEmpty(notAnsweredJustification)) {
            addActionMessage(request, "error.inquiries.notAnsweredFillAtLeastOneField");
            return handleDontRespondError(actionMapping, request, inquiryRegistry);
        }

        if (inquiryRegistry.getRegistration().getPerson() != AccessControl.getPerson()) {
            addActionMessage(request, "error.inquiries.notTheSamePerson");
            return handleDontRespondError(actionMapping, request, inquiryRegistry);
        }

        InquiryNotAnsweredJustification justification = InquiryNotAnsweredJustification.valueOf(notAnsweredJustification);
        String notAnsweredOtherJustification = form.getString("notAnsweredOtherJustification");

        if (justification == InquiryNotAnsweredJustification.OTHER) {
            if (StringUtils.isEmpty(notAnsweredOtherJustification)) {
                addActionMessage(request, "error.inquiries.fillOtherJustification");
                request.setAttribute("textAreaReadOnly", "false");
                return handleDontRespondError(actionMapping, request, inquiryRegistry);
            } else if (notAnsweredOtherJustification.length() > 200) {
                addActionMessage(request, "error.inquiries.fillOtherJustificationLengthOversized");
                request.setAttribute("textAreaReadOnly", "false");
                return handleDontRespondError(actionMapping, request, inquiryRegistry);
            }
        }

        InquiryCourseAnswer.createNotAnsweredInquiryCourse(inquiryRegistry, justification, notAnsweredOtherJustification);
        return showCoursesToAnswer(actionMapping, actionForm, request, response);
    }

    private ActionForward handleDontRespondError(ActionMapping actionMapping, HttpServletRequest request,
            StudentInquiryRegistry inquiryRegistry) {
        request.setAttribute("inquiryRegistryID", inquiryRegistry.getExternalId());
        request.setAttribute("inquiryRegistry", inquiryRegistry);
        return actionMapping.findForward("showDontRespond");
    }

    public ActionForward showCurricularInquiry(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        final StudentInquiryTemplate studentInquiryTemplate = CurricularCourseInquiryTemplate.getCurrentTemplate();
        if (studentInquiryTemplate == null) {
            return actionMapping.findForward("inquiriesClosed");
        }

        StudentInquiryBean inquiryBean = getRenderedObject("inquiryBean");

        if (inquiryBean == null) {
            String inquiryRegistryID = (String) getFromRequest(request, "inquiryRegistryID");
            StudentInquiryRegistry inquiryRegistry = FenixFramework.getDomainObject(inquiryRegistryID);

            Set<InquiryBlockDTO> inquiryBlocks = new TreeSet<InquiryBlockDTO>(new BeanComparator("inquiryBlock.blockOrder"));
            for (InquiryBlock inquiryBlock : studentInquiryTemplate.getInquiryBlocksSet()) {
                inquiryBlocks.add(new InquiryBlockDTO(inquiryBlock, inquiryRegistry));
            }

            inquiryBean = new StudentInquiryBean(StudentTeacherInquiryTemplate.getCurrentTemplate(), inquiryRegistry);
            inquiryBean.setCurricularCourseBlocks(inquiryBlocks);

        }

        request.setAttribute("inquiryBean", inquiryBean);
        return actionMapping.findForward("showInquiry");
    }

    public ActionForward showTeachersToAnswer(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        StudentInquiryBean inquiryBean = getRenderedObject("inquiryBean");
        RenderUtils.invalidateViewState();
        request.setAttribute("inquiryBean", inquiryBean);
        String validationResult = inquiryBean.validateCurricularInquiry();
        if (!Boolean.valueOf(validationResult)) {
            if (!validationResult.equalsIgnoreCase("false")) {
                addActionMessage(request, "error.inquiries.fillInQuestion", validationResult);
            } else {
                addActionMessage(request, "error.inquiries.fillAllRequiredFields");
            }
            return actionMapping.findForward("showInquiry");
        }

        return actionMapping.findForward("showTeachersToAnswer");
    }

    public ActionForward showTeachersToAnswerDirectly(ActionMapping actionMapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        StudentInquiryBean inquiryBean = getRenderedObject("inquiryBean");
        if (inquiryBean == null) {
            String inquiryRegistryID = (String) getFromRequest(request, "inquiryRegistryID");
            StudentInquiryRegistry inquiryRegistry = FenixFramework.getDomainObject(inquiryRegistryID);
            inquiryBean = new StudentInquiryBean(StudentTeacherInquiryTemplate.getCurrentTemplate(), inquiryRegistry);
        }
        RenderUtils.invalidateViewState();

        request.setAttribute("directTeachers", "true");
        request.setAttribute("inquiryBean", inquiryBean);
        return actionMapping.findForward("showTeachersToAnswer");
    }

    public ActionForward showTeacherInquiry(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        request.setAttribute("teacherInquiry", getRenderedObject("teacherInquiry"));
        StudentInquiryBean studentInquiryBean = getRenderedObject("inquiryBean");
        request.setAttribute("inquiryBean", studentInquiryBean);
        if (studentInquiryBean.getCurricularCourseBlocks() == null) {
            request.setAttribute("directTeachers", "true");
        }
        return actionMapping.findForward("showTeacherInquiry");
    }

    public ActionForward postBackTeacherInquiry(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        final StudentTeacherInquiryBean teacherInquiry = getRenderedObject("teacherInquiry");
        teacherInquiry.setGroupsVisibility(teacherInquiry.getTeacherInquiryBlocks());
        request.setAttribute("inquiryBean", getRenderedObject("inquiryBean"));
        request.setAttribute("teacherInquiry", teacherInquiry);
        RenderUtils.invalidateViewState();
        return actionMapping.findForward("showTeacherInquiry");
    }

    public ActionForward fillTeacherInquiry(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        final StudentTeacherInquiryBean teacherInquiry = getRenderedObject("teacherInquiry");
        StudentInquiryBean studentInquiryBean = getRenderedObject("inquiryBean");
        request.setAttribute("inquiryBean", studentInquiryBean);
        if (studentInquiryBean.getCurricularCourseBlocks() == null) {
            request.setAttribute("directTeachers", "true");
        }
        RenderUtils.invalidateViewState();

        String validationResult = teacherInquiry.validateTeacherInquiry();
        if (!Boolean.valueOf(validationResult)) {
            if (!validationResult.equalsIgnoreCase("false")) {
                addActionMessage(request, "error.inquiries.fillInQuestion", validationResult);
            } else {
                addActionMessage(request, "error.inquiries.fillAllRequiredFields");
            }
            request.setAttribute("teacherInquiry", teacherInquiry);
            return actionMapping.findForward("showTeacherInquiry");
        }
        teacherInquiry.setFilled(true);

        return actionMapping.findForward("showTeachersToAnswer");
    }

    public ActionForward resetTeacherInquiry(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        final StudentTeacherInquiryBean teacherInquiry = getRenderedObject("teacherInquiry");
        StudentInquiryBean studentInquiryBean = getRenderedObject("inquiryBean");
        request.setAttribute("inquiryBean", studentInquiryBean);
        if (studentInquiryBean.getCurricularCourseBlocks() == null) {
            request.setAttribute("directTeachers", "true");
        }
        RenderUtils.invalidateViewState();

        for (final InquiryBlockDTO inquiryBlock : teacherInquiry.getTeacherInquiryBlocks()) {
            for (final InquiryGroupQuestionBean groupQuestionBean : inquiryBlock.getInquiryGroups()) {
                for (InquiryQuestionDTO inquiryQuestionDTO : groupQuestionBean.getInquiryQuestions()) {
                    inquiryQuestionDTO.setResponseValue(null);
                }
            }
        }
        teacherInquiry.setFilled(false);
        return actionMapping.findForward("showTeachersToAnswer");
    }

    public ActionForward confirm(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        final StudentInquiryBean inquiryBean = getRenderedObject("inquiryBean");
        inquiryBean.setAnsweredInquiry();

        return showCoursesToAnswer(actionMapping, actionForm, request, response);
    }

}