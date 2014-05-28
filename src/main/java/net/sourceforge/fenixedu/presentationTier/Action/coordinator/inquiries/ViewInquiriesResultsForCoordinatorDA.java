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
package net.sourceforge.fenixedu.presentationTier.Action.coordinator.inquiries;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.dataTransferObject.inquiries.CoordinatorInquiryBean;
import net.sourceforge.fenixedu.dataTransferObject.inquiries.CoordinatorResultsBean;
import net.sourceforge.fenixedu.dataTransferObject.inquiries.CurricularCourseResumeResult;
import net.sourceforge.fenixedu.dataTransferObject.oldInquiries.ViewInquiriesResultPageDTO;
import net.sourceforge.fenixedu.domain.Coordinator;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.degreeStructure.Context;
import net.sourceforge.fenixedu.domain.inquiries.CoordinatorInquiryTemplate;
import net.sourceforge.fenixedu.domain.inquiries.InquiryCoordinatorAnswer;
import net.sourceforge.fenixedu.domain.inquiries.InquiryResponseState;
import net.sourceforge.fenixedu.domain.inquiries.ResultPersonCategory;
import net.sourceforge.fenixedu.domain.oldInquiries.InquiryResponsePeriod;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.presentationTier.Action.coordinator.DegreeCoordinatorIndex;
import net.sourceforge.fenixedu.presentationTier.Action.publico.ViewTeacherInquiryPublicResults;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixframework.FenixFramework;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 *
 */

@Mapping(path = "/viewInquiriesResults", module = "coordinator", formBeanClass = ViewInquiriesResultPageDTO.class,
        functionality = DegreeCoordinatorIndex.class)
@Forwards({ @Forward(name = "inquiryResults", path = "/coordinator/inquiries/viewInquiriesResults.jsp"),
        @Forward(name = "curricularUnitSelection", path = "/coordinator/inquiries/curricularUnitSelection.jsp"),
        @Forward(name = "showFilledTeachingInquiry", path = "/inquiries/showFilledTeachingInquiry.jsp"),
        @Forward(name = "showFilledTeachingInquiry_v2", path = "/inquiries/showFilledTeachingInquiry_v2.jsp"),
        @Forward(name = "showFilledDelegateInquiry", path = "/inquiries/showFilledDelegateInquiry.jsp"),
        @Forward(name = "showCourseInquiryResult", path = "/inquiries/showCourseInquiryResult.jsp"),
        @Forward(name = "showTeachingInquiryResult", path = "/inquiries/showTeachingInquiryResult.jsp"),
        @Forward(name = "coordinatorUCView", path = "/coordinator/inquiries/coordinatorUCView.jsp"),
        @Forward(name = "coordinatorInquiry", path = "/coordinator/inquiries/coordinatorInquiry.jsp") })
public class ViewInquiriesResultsForCoordinatorDA extends ViewInquiriesResultsDA {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        DegreeCoordinatorIndex.setCoordinatorContext(request);
        return super.execute(mapping, actionForm, request, response);
    }

    @Override
    public ActionForward prepare(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {

        ExecutionSemester executionPeriod = getMostRecentExecutionPeriodWithResults();
        if (executionPeriod != null) {
            ((ViewInquiriesResultPageDTO) actionForm).setDegreeCurricularPlanID(getStringFromRequest(request,
                    "degreeCurricularPlanID"));
            ((ViewInquiriesResultPageDTO) actionForm).setExecutionSemesterID(executionPeriod.getExternalId());
            return selectexecutionSemester(actionMapping, actionForm, request, response);
        }

        return super.prepare(actionMapping, actionForm, request, response);
    }

    private ExecutionSemester getMostRecentExecutionPeriodWithResults() {
        ExecutionSemester oldQucExecutionSemester = ExecutionSemester.readBySemesterAndExecutionYear(2, "2009/2010");
        ExecutionSemester executionPeriod = ExecutionSemester.readActualExecutionSemester();
        while (oldQucExecutionSemester.isBefore(executionPeriod)) {
            if (executionPeriod.hasAnyInquiryResults()) {
                return executionPeriod;
            }
            executionPeriod = executionPeriod.getPreviousExecutionPeriod();
        }
        return null;
    }

    @Override
    public ActionForward selectexecutionSemester(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {

        ViewInquiriesResultPageDTO resultPageDTO = (ViewInquiriesResultPageDTO) actionForm;
        final ExecutionSemester executionSemester = resultPageDTO.getExecutionSemester();
        if (executionSemester == null) {
            return super.prepare(actionMapping, actionForm, request, response);
        } else {
            ExecutionSemester oldQucExecutionSemester = ExecutionSemester.readBySemesterAndExecutionYear(2, "2009/2010");
            if (!executionSemester.isAfter(oldQucExecutionSemester)) {
                return super.selectexecutionSemester(actionMapping, actionForm, request, response);
            }
        }

        Map<Integer, List<CurricularCourseResumeResult>> coursesResultResumeMap =
                new HashMap<Integer, List<CurricularCourseResumeResult>>();
        boolean coursesToAudit = false;
        boolean responsibleCoordinator = false;
        if (!resultPageDTO.getDegreeCurricularPlan().getDegreeType().isThirdCycle()) {

            CoordinatorInquiryTemplate coordinatorInquiryTemplate =
                    CoordinatorInquiryTemplate.getTemplateByExecutionPeriod(executionSemester);

            if (coordinatorInquiryTemplate == null) {
                request.setAttribute("coursesResultResumeMap", coursesResultResumeMap);
                return super.prepare(actionMapping, actionForm, request, response);
            }

            ExecutionDegree executionDegree =
                    resultPageDTO.getDegreeCurricularPlan().getExecutionDegreeByAcademicInterval(
                            executionSemester.getAcademicInterval());

            if (executionDegree == null) {
                return super.prepare(actionMapping, actionForm, request, response);
            }

            Coordinator coordinator = executionDegree.getCoordinatorByTeacher(AccessControl.getPerson());
            ExecutionSemester currentExecutionSemester = ExecutionSemester.readActualExecutionSemester();
            Coordinator currentCoordinator = null;
            if (currentExecutionSemester.getExecutionYear() != executionSemester.getExecutionYear()
                    && currentExecutionSemester.isAfter(executionSemester)) {
                ExecutionDegree currentExecutionDegree =
                        resultPageDTO.getDegreeCurricularPlan().getExecutionDegreeByAcademicInterval(
                                currentExecutionSemester.getAcademicInterval());
                // check if the course has been opened this semester
                if (currentExecutionDegree != null) {
                    currentCoordinator = currentExecutionDegree.getCoordinatorByTeacher(AccessControl.getPerson());
                }
            }

            // check if the chosen executionSemester has the same executionYear
            // of the next executionSemester of the chosen one
            ExecutionYear executionYear = executionSemester.getExecutionYear();
            ExecutionYear nextExecutionYear = executionSemester.getNextExecutionPeriod().getExecutionYear();
            boolean sameExecutionYear = executionYear == nextExecutionYear;

            Coordinator coordinatorAfter = null; // it can see the results in
            // the previous semesters of
            // its coordination
            if (coordinator == null && currentCoordinator == null) {
                if (!sameExecutionYear) {
                    ExecutionSemester executionSemesterIter = executionSemester.getNextExecutionPeriod();
                    while (executionSemesterIter.isBefore(currentExecutionSemester)) {
                        ExecutionDegree nextExecutionDegree =
                                resultPageDTO.getDegreeCurricularPlan().getExecutionDegreeByAcademicInterval(
                                        executionSemesterIter.getAcademicInterval());
                        coordinatorAfter = nextExecutionDegree.getCoordinatorByTeacher(AccessControl.getPerson());
                        if (coordinatorAfter != null) {
                            break;
                        }
                        executionSemesterIter = executionSemesterIter.getNextExecutionPeriod();
                    }
                }
            }

            if (coordinator == null && coordinatorAfter == null && currentCoordinator == null) {
                request.setAttribute("notCoordinator", "true");
                request.setAttribute("executionDegree", executionDegree);
                request.setAttribute("executionPeriods", getExecutionSemesters(request, actionForm));
                return actionMapping.findForward("curricularUnitSelection");
            }

            Coordinator finalCoordinatorToUse = null;
            if (sameExecutionYear) {
                responsibleCoordinator = coordinator != null ? coordinator.isResponsible() : false;
            } else {
                responsibleCoordinator = currentCoordinator != null ? currentCoordinator.isResponsible() : false;
            }
            if (coordinator != null) {
                finalCoordinatorToUse = coordinator;
            } else {
                finalCoordinatorToUse = currentCoordinator != null ? currentCoordinator : coordinatorAfter;
            }
            // responsibleCoordinator = coordinator.isResponsible();

            InquiryCoordinatorAnswer inquiryCoordinatorAnswer = null;
            if (coordinatorInquiryTemplate.getShared()) {
                inquiryCoordinatorAnswer = executionDegree.getInquiryCoordinationAnswers(executionSemester);
            } else {
                // TODO since in the 1rst semester more than one could fill in
                // the inquiry, it should show multiples links for each one, it
                // is only showing one link
                if (coordinator != null) {
                    inquiryCoordinatorAnswer = coordinator.getInquiryCoordinatorAnswer(executionSemester);
                }
            }
            if (inquiryCoordinatorAnswer == null
                    || inquiryCoordinatorAnswer.hasRequiredQuestionsToAnswer(coordinatorInquiryTemplate)) {
                request.setAttribute("completionState", InquiryResponseState.INCOMPLETE.getLocalizedName());
            } else {
                request.setAttribute("completionState", InquiryResponseState.COMPLETE.getLocalizedName());
            }

            request.setAttribute("executionDegree", executionDegree);
            request.setAttribute("degreeAcronym", executionDegree.getDegree().getSigla());
            request.setAttribute("coordinator", finalCoordinatorToUse);
            request.setAttribute("readMode", !coordinatorInquiryTemplate.isOpen());

            Set<ExecutionCourse> dcpExecutionCourses =
                    resultPageDTO.getDegreeCurricularPlan().getExecutionCoursesByExecutionPeriod(executionSemester);
            for (ExecutionCourse executionCourse : dcpExecutionCourses) {
                CurricularCourseResumeResult courseResumeResult =
                        new CurricularCourseResumeResult(executionCourse, executionDegree, "label.inquiry.curricularUnit",
                                executionCourse.getName(), AccessControl.getPerson(), ResultPersonCategory.DEGREE_COORDINATOR,
                                false, true, false, false, responsibleCoordinator);
                if (courseResumeResult.getResultBlocks().size() > 1) {

                    if (executionCourse.getForAudit(executionDegree) != null) {
                        coursesToAudit = true;
                    }
                    CurricularCourse curricularCourse =
                            executionCourse.getCurricularCourseFor(resultPageDTO.getDegreeCurricularPlan());

                    for (Context context : curricularCourse.getParentContextsByExecutionSemester(executionSemester)) {
                        Integer curricurlarYear = context.getCurricularYear();
                        List<CurricularCourseResumeResult> coursesResultResume = coursesResultResumeMap.get(curricurlarYear);
                        if (coursesResultResume == null) {
                            coursesResultResume = new ArrayList<CurricularCourseResumeResult>();
                            coursesResultResumeMap.put(curricurlarYear, coursesResultResume);
                        }
                        coursesResultResume.add(courseResumeResult);
                    }
                }
            }
        }

        for (Integer curricularYear : coursesResultResumeMap.keySet()) {
            List<CurricularCourseResumeResult> list = coursesResultResumeMap.get(curricularYear);
            Set<CurricularCourseResumeResult> set = new HashSet<CurricularCourseResumeResult>(list);
            List<CurricularCourseResumeResult> noRepetitionsList = new ArrayList<CurricularCourseResumeResult>(set);
            Collections.sort(noRepetitionsList, new BeanComparator("executionCourse.name"));
            coursesResultResumeMap.put(curricularYear, noRepetitionsList);
        }

        request.setAttribute("isResponsible", responsibleCoordinator);
        request.setAttribute("coursesToAudit", String.valueOf(coursesToAudit));
        request.setAttribute("executionPeriod", executionSemester);
        request.setAttribute("executionPeriods", getExecutionSemesters(request, actionForm));
        request.setAttribute("coursesResultResumeMap", coursesResultResumeMap);
        return actionMapping.findForward("curricularUnitSelection");
    }

    public ActionForward showUCResultsAndComments(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        ExecutionCourse executionCourse =
                FenixFramework.getDomainObject(getFromRequest(request, "executionCourseOID").toString());
        ExecutionDegree executionDegree =
                FenixFramework.getDomainObject(getFromRequest(request, "executionDegreeOID").toString());

        CoordinatorResultsBean coordinatorInquiryBean =
                new CoordinatorResultsBean(executionCourse, executionDegree, AccessControl.getPerson(), false);

        request.setAttribute("executionPeriod", executionCourse.getExecutionPeriod());
        request.setAttribute("executionCourse", executionCourse);
        request.setAttribute("coordinatorInquiryBean", coordinatorInquiryBean);
        request.setAttribute("allowComment", request.getParameter("allowComment"));
        ViewTeacherInquiryPublicResults.setTeacherScaleColorException(executionCourse.getExecutionPeriod(), request);
        return actionMapping.findForward("coordinatorUCView");
    }

    public ActionForward showCoordinatorInquiry(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        ExecutionDegree executionDegree =
                FenixFramework.getDomainObject(getFromRequest(request, "executionDegreeOID").toString());
        ExecutionSemester executionSemester =
                FenixFramework.getDomainObject(getFromRequest(request, "executionPeriodOID").toString());
        CoordinatorInquiryTemplate coordinatorInquiryTemplate =
                CoordinatorInquiryTemplate.getTemplateByExecutionPeriod(executionSemester);
        Coordinator coordinator = FenixFramework.getDomainObject(getFromRequest(request, "coordinatorOID").toString());
        InquiryCoordinatorAnswer inquiryCoordinatorAnswer = null;
        if (coordinatorInquiryTemplate.getShared()) {
            inquiryCoordinatorAnswer = executionDegree.getInquiryCoordinationAnswers(executionSemester);
        } else {
            inquiryCoordinatorAnswer = coordinator.getInquiryCoordinatorAnswer(executionSemester);
        }

        CoordinatorInquiryBean coordinatorInquiryBean =
                new CoordinatorInquiryBean(coordinatorInquiryTemplate, coordinator, inquiryCoordinatorAnswer, executionSemester,
                        executionDegree);

        request.setAttribute("degreeAcronym", executionDegree.getDegree().getSigla());
        request.setAttribute("executionPeriod", executionSemester);
        request.setAttribute("coordinatorInquiryBean", coordinatorInquiryBean);

        return actionMapping.findForward("coordinatorInquiry");
    }

    public ActionForward saveComment(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        final CoordinatorResultsBean coordinatorResultsBean = getRenderedObject("coordinatorInquiryBean");
        coordinatorResultsBean.saveComment();

        ((ViewInquiriesResultPageDTO) actionForm).setExecutionSemester(coordinatorResultsBean.getExecutionCourse()
                .getExecutionPeriod());
        ((ViewInquiriesResultPageDTO) actionForm).setDegreeCurricularPlanID(coordinatorResultsBean.getExecutionDegree()
                .getDegreeCurricularPlan().getExternalId());
        request.setAttribute("degreeCurricularPlanID", coordinatorResultsBean.getExecutionDegree().getDegreeCurricularPlan()
                .getExternalId().toString());

        DegreeCoordinatorIndex.setCoordinatorContext(request);
        return selectexecutionSemester(actionMapping, actionForm, request, response);
    }

    public ActionForward saveInquiry(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        final CoordinatorInquiryBean coordinatorInquiryBean = getRenderedObject("coordinatorInquiryBean");

        String validationResult = coordinatorInquiryBean.validateInquiry();
        if (!Boolean.valueOf(validationResult)) {
            RenderUtils.invalidateViewState();
            addActionMessage(request, "error.inquiries.fillInQuestion", validationResult);

            request.setAttribute("coordinatorInquiryBean", coordinatorInquiryBean);
            request.setAttribute("executionPeriod", coordinatorInquiryBean.getExecutionSemester());
            request.setAttribute("executionCourse", coordinatorInquiryBean.getCoordinator().getExecutionDegree().getDegree()
                    .getSigla());
            return actionMapping.findForward("coordinatorInquiry");
        }

        RenderUtils.invalidateViewState("coordinatorInquiry");
        coordinatorInquiryBean.saveInquiry();

        ((ViewInquiriesResultPageDTO) actionForm).setExecutionSemester(coordinatorInquiryBean.getExecutionSemester());
        ((ViewInquiriesResultPageDTO) actionForm).setDegreeCurricularPlanID(coordinatorInquiryBean.getCoordinator()
                .getExecutionDegree().getDegreeCurricularPlan().getExternalId());
        request.setAttribute("degreeCurricularPlanID", coordinatorInquiryBean.getCoordinator().getExecutionDegree()
                .getDegreeCurricularPlan().getExternalId().toString());

        DegreeCoordinatorIndex.setCoordinatorContext(request);
        return selectexecutionSemester(actionMapping, actionForm, request, response);
    }

    @Override
    protected boolean coordinatorCanComment(final ExecutionDegree executionDegree, final ExecutionSemester executionPeriod) {
        if (executionDegree.getDegreeType().isThirdCycle()) {
            return false;
        }

        final InquiryResponsePeriod coordinatorReportResponsePeriod = executionPeriod.getCoordinatorReportResponsePeriod();
        final Coordinator coordinator = executionDegree.getCoordinatorByTeacher(AccessControl.getPerson());
        return coordinator != null && coordinator.isResponsible() && coordinatorReportResponsePeriod != null
                && coordinatorReportResponsePeriod.isOpen();
    }

}
