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
package net.sourceforge.fenixedu.presentationTier.Action.coordinator.inquiries;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.dataTransferObject.oldInquiries.StudentInquiriesCourseResultBean;
import net.sourceforge.fenixedu.dataTransferObject.oldInquiries.TeachingInquiryDTO;
import net.sourceforge.fenixedu.dataTransferObject.oldInquiries.ViewInquiriesResultPageDTO;
import net.sourceforge.fenixedu.dataTransferObject.oldInquiries.YearDelegateCourseInquiryDTO;
import net.sourceforge.fenixedu.domain.CoordinatorExecutionDegreeCoursesReport;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.oldInquiries.StudentInquiriesCourseResult;
import net.sourceforge.fenixedu.domain.oldInquiries.StudentInquiriesTeachingResult;
import net.sourceforge.fenixedu.domain.oldInquiries.teacher.TeachingInquiry;
import net.sourceforge.fenixedu.domain.student.YearDelegateCourseInquiry;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.bennu.portal.servlet.PortalLayoutInjector;

import pt.ist.fenixframework.FenixFramework;

abstract public class ViewInquiriesResultsDA extends FenixDispatchAction {

    public ActionForward prepare(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {

        request.setAttribute("coursesResultResumeMap", "");
        request.setAttribute("executionPeriods", getExecutionSemesters(request, actionForm));
        request.setAttribute("otherExecutionCourses", Collections.EMPTY_LIST);
        request.setAttribute("excelentExecutionCourses", Collections.EMPTY_LIST);
        request.setAttribute("executionCoursesToImproove", Collections.EMPTY_LIST);
        ((ViewInquiriesResultPageDTO) actionForm).setDegreeCurricularPlanID(getStringFromRequest(request,
                "degreeCurricularPlanID"));

        return actionMapping.findForward("curricularUnitSelection");
    }

    public List<ExecutionSemester> getExecutionSemesters(HttpServletRequest request, ActionForm actionForm) {
        String degreeCurricularPlanID = getStringFromRequest(request, "degreeCurricularPlanID");
        if (degreeCurricularPlanID == null || degreeCurricularPlanID.equals("")) {
            degreeCurricularPlanID = ((ViewInquiriesResultPageDTO) actionForm).getDegreeCurricularPlanID();
        }
        final DegreeCurricularPlan degreeCurricularPlan =
                FenixFramework.getDomainObject(getStringFromRequest(request, "degreeCurricularPlanID"));
        final List<ExecutionSemester> executionSemesters = new ArrayList<ExecutionSemester>();
        for (final ExecutionDegree executionDegree : degreeCurricularPlan.getExecutionDegreesSet()) {
            final ExecutionYear executionYear = executionDegree.getExecutionYear();
            executionSemesters.addAll(executionYear.getExecutionPeriodsSet());
        }
        Collections.sort(executionSemesters);
        Collections.reverse(executionSemesters);
        return executionSemesters;
    }

    public ActionForward selectexecutionSemester(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {

        ViewInquiriesResultPageDTO resultPageDTO = (ViewInquiriesResultPageDTO) actionForm;
        final ExecutionSemester executionSemester = resultPageDTO.getExecutionSemester();
        if (executionSemester == null) {
            return prepare(actionMapping, actionForm, request, response);
        }
        final ExecutionYear executionYear = executionSemester.getExecutionYear();
        final DegreeCurricularPlan degreeCurricularPlan = resultPageDTO.getDegreeCurricularPlan();
        final ExecutionDegree executionDegree = degreeCurricularPlan.getExecutionDegreeByYear(executionYear);
        if (executionDegree == null) {
            return prepare(actionMapping, actionForm, request, response);
        }

        resultPageDTO.setExecutionDegreeID(executionDegree.getExternalId());

        Collection<StudentInquiriesCourseResult> otherExecutionCourses = new ArrayList<StudentInquiriesCourseResult>();
        Collection<StudentInquiriesCourseResult> executionCoursesToImproove = new ArrayList<StudentInquiriesCourseResult>();
        Collection<StudentInquiriesCourseResult> excelentExecutionCourses = new ArrayList<StudentInquiriesCourseResult>();

        fillExecutionCourses(executionSemester, executionDegree, otherExecutionCourses, executionCoursesToImproove,
                excelentExecutionCourses);

        Collections.sort((List<StudentInquiriesCourseResult>) otherExecutionCourses,
                StudentInquiriesCourseResult.EXECUTION_COURSE_NAME_COMPARATOR);
        Collections.sort((List<StudentInquiriesCourseResult>) executionCoursesToImproove,
                StudentInquiriesCourseResult.EXECUTION_COURSE_NAME_COMPARATOR);
        Collections.sort((List<StudentInquiriesCourseResult>) excelentExecutionCourses,
                StudentInquiriesCourseResult.EXECUTION_COURSE_NAME_COMPARATOR);

        if (getFromRequest(request, "courseResultsCoordinatorCommentEdit") != null) {
            request.setAttribute("courseResultsCoordinatorCommentEdit", true);
        }

        request.setAttribute("executionDegreeCoursesReport", getExecutionDegreeCoursesReports(executionSemester, executionDegree));
        request.setAttribute("executionSemester", executionSemester);
        request.setAttribute("canComment", coordinatorCanComment(executionDegree, executionSemester));
        request.setAttribute("executionPeriods", getExecutionSemesters(request, actionForm));
        request.setAttribute("otherExecutionCourses", otherExecutionCourses);
        request.setAttribute("excelentExecutionCourses", excelentExecutionCourses);
        request.setAttribute("executionCoursesToImproove", executionCoursesToImproove);
        request.setAttribute("executionDegreeID", resultPageDTO.getExecutionDegreeID());

        return actionMapping.findForward("curricularUnitSelection");
    }

    protected void fillExecutionCourses(final ExecutionSemester executionSemester, final ExecutionDegree executionDegree,
            Collection<StudentInquiriesCourseResult> otherExecutionCourses,
            Collection<StudentInquiriesCourseResult> executionCoursesToImproove,
            Collection<StudentInquiriesCourseResult> excelentExecutionCourses) {

        for (StudentInquiriesCourseResult studentInquiriesCourseResult : executionDegree.getStudentInquiriesCourseResults()) {
            final ExecutionCourse executionCourse = studentInquiriesCourseResult.getExecutionCourse();
            if (executionCourse != null && executionCourse.getExecutionPeriod() == executionSemester) {

                if (studentInquiriesCourseResult.isUnsatisfactory()
                        || hasTeachingResultsToImproove(executionDegree, executionCourse)) {
                    executionCoursesToImproove.add(getStudentInquiriesCourseResult(executionCourse, executionDegree));
                } else if (studentInquiriesCourseResult.isExcellent()
                        || hasExcellentTeachingResults(executionDegree, executionCourse)) {
                    excelentExecutionCourses.add(getStudentInquiriesCourseResult(executionCourse, executionDegree));
                } else {
                    otherExecutionCourses.add(getStudentInquiriesCourseResult(executionCourse, executionDegree));
                }

            }
        }
    }

    private CoordinatorExecutionDegreeCoursesReport getExecutionDegreeCoursesReports(final ExecutionSemester executionSemester,
            final ExecutionDegree executionDegree) {
        if (executionDegree.getDegreeType().isThirdCycle()) {
            return null;
        }

        final CoordinatorExecutionDegreeCoursesReport executionDegreeCoursesReports =
                executionDegree.getExecutionDegreeCoursesReports(executionSemester);
        try {
            return executionDegreeCoursesReports == null && coordinatorCanComment(executionDegree, executionSemester) ? CoordinatorExecutionDegreeCoursesReport
                    .makeNew(executionDegree, executionSemester) : executionDegreeCoursesReports;
        } catch (DomainException e) {
            return null;
        }
    }

    protected boolean hasTeachingResultsToImproove(final ExecutionDegree executionDegree, final ExecutionCourse executionCourse) {
        for (Professorship otherTeacherProfessorship : executionCourse.getProfessorships()) {
            for (StudentInquiriesTeachingResult studentInquiriesTeachingResult : otherTeacherProfessorship
                    .getStudentInquiriesTeachingResults()) {
                if (studentInquiriesTeachingResult.getExecutionDegree() == executionDegree
                        && studentInquiriesTeachingResult.isUnsatisfactory()) {
                    return true;
                }
            }
        }
        return false;
    }

    protected boolean hasExcellentTeachingResults(final ExecutionDegree executionDegree, final ExecutionCourse executionCourse) {
        for (Professorship otherTeacherProfessorship : executionCourse.getProfessorships()) {
            for (StudentInquiriesTeachingResult studentInquiriesTeachingResult : otherTeacherProfessorship
                    .getStudentInquiriesTeachingResults()) {
                if (studentInquiriesTeachingResult.getExecutionDegree() == executionDegree
                        && studentInquiriesTeachingResult.isExcellent()) {
                    return true;
                }
            }
        }
        return false;
    }

    public ActionForward selectExecutionCourse(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {

        StudentInquiriesCourseResult courseResult = getRenderedObject();

        final ExecutionCourse executionCourse =
                courseResult == null ? (ExecutionCourse) FenixFramework.getDomainObject(getStringFromRequest(request,
                        "executionCourseID")) : courseResult.getExecutionCourse();
        final ExecutionDegree executionDegree =
                courseResult == null ? (ExecutionDegree) FenixFramework.getDomainObject(getStringFromRequest(request,
                        "executionDegreeID")) : courseResult.getExecutionDegree();

        request.setAttribute("canComment", coordinatorCanComment(executionDegree, executionCourse.getExecutionPeriod()));
        request.setAttribute("executionCourse", executionCourse);

        if (executionCourse != null) {

            if (courseResult == null && getFromRequest(request, "courseResultsCoordinatorCommentEdit") != null) {
                request.setAttribute("courseResultsCoordinatorCommentEdit", true);
            }

            ((ViewInquiriesResultPageDTO) actionForm).setExecutionCourseID(executionCourse.getExternalId());
            ((ViewInquiriesResultPageDTO) actionForm).setExecutionDegreeID(executionDegree.getExternalId());
            // ((ViewInquiriesResultPageDTO)
            // actionForm).setDegreeCurricularPlanID(executionDegree.getDegreeCurricularPlan().getExternalId());

            final StudentInquiriesCourseResultBean courseResultBean =
                    populateStudentInquiriesCourseResults(executionCourse, executionDegree);
            request.setAttribute("isToImproove", courseResultBean.getStudentInquiriesCourseResult().isUnsatisfactory()
                    || hasTeachingResultsToImproove(executionDegree, executionCourse));
            request.setAttribute("studentInquiriesCourseResult", courseResultBean);

            return actionMapping.findForward("inquiryResults");
        }
        return selectexecutionSemester(actionMapping, actionForm, request, response);
    }

    abstract protected boolean coordinatorCanComment(final ExecutionDegree executionDegree,
            final ExecutionSemester executionPeriod);

    private StudentInquiriesCourseResultBean populateStudentInquiriesCourseResults(final ExecutionCourse executionCourse,
            final ExecutionDegree executionDegree) {

        StudentInquiriesCourseResultBean resultBean =
                new StudentInquiriesCourseResultBean(getStudentInquiriesCourseResult(executionCourse, executionDegree));

        for (Professorship otherTeacherProfessorship : executionCourse.getProfessorships()) {
            for (StudentInquiriesTeachingResult studentInquiriesTeachingResult : otherTeacherProfessorship
                    .getStudentInquiriesTeachingResults()) {
                if (studentInquiriesTeachingResult.getExecutionDegree() == executionDegree
                        && studentInquiriesTeachingResult.getInternalDegreeDisclosure()) {
                    resultBean.addStudentInquiriesTeachingResult(studentInquiriesTeachingResult);
                }
            }
        }

        return resultBean;
    }

    protected StudentInquiriesCourseResult getStudentInquiriesCourseResult(final ExecutionCourse executionCourse,
            final ExecutionDegree executionDegree) {
        for (StudentInquiriesCourseResult studentInquiriesCourseResult : executionCourse.getStudentInquiriesCourseResults()) {
            if (studentInquiriesCourseResult.getExecutionDegree() == executionDegree) {
                return studentInquiriesCourseResult;
            }
        }
        return null;
    }

    public ActionForward showInquiryCourseResult(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        request.setAttribute("inquiryResult", getDomainObject(request, "resultId"));
        PortalLayoutInjector.skipLayoutOn(request);
        return actionMapping.findForward("showCourseInquiryResult");
    }

    public ActionForward showInquiryTeachingResult(ActionMapping actionMapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        request.setAttribute("inquiryResult", getDomainObject(request, "resultId"));
        PortalLayoutInjector.skipLayoutOn(request);
        return actionMapping.findForward("showTeachingInquiryResult");
    }

    public ActionForward showFilledTeachingInquiry(ActionMapping actionMapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        final TeachingInquiry teachingInquiry = getDomainObject(request, "filledTeachingInquiryId");
        request.setAttribute("teachingInquiry", teachingInquiry);

        final ExecutionSemester executionPeriod = teachingInquiry.getProfessorship().getExecutionCourse().getExecutionPeriod();
        if (executionPeriod.getSemester() == 2 && executionPeriod.getYear().equals("2007/2008")) {
            return actionMapping.findForward("showFilledTeachingInquiry");
        }

        request.setAttribute("teachingInquiryDTO", new TeachingInquiryDTO(teachingInquiry.getProfessorship()));
        PortalLayoutInjector.skipLayoutOn(request);
        return actionMapping.findForward("showFilledTeachingInquiry_v2");

    }

    public ActionForward showFilledYearDelegateInquiry(ActionMapping actionMapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        final YearDelegateCourseInquiry delegateCourseInquiry = getDomainObject(request, "filledYearDelegateInquiryId");

        request.setAttribute("delegateInquiryDTO", new YearDelegateCourseInquiryDTO(delegateCourseInquiry));
        PortalLayoutInjector.skipLayoutOn(request);
        return actionMapping.findForward("showFilledDelegateInquiry");

    }

}
