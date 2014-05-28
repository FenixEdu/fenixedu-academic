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
package net.sourceforge.fenixedu.presentationTier.Action.publico;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.dataTransferObject.oldInquiries.SearchInquiriesResultPageDTO;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.inquiries.InquiryResponsePeriodType;
import net.sourceforge.fenixedu.domain.oldInquiries.StudentInquiriesCourseResult;
import net.sourceforge.fenixedu.domain.oldInquiries.teacher.TeachingInquiry;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.commons.collections.comparators.ReverseComparator;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
@Mapping(path = "/searchInquiriesResultPage", module = "publico", formBeanClass = SearchInquiriesResultPageDTO.class)
@Forwards({ @Forward(name = "searchPage", path = "/executionCourse/inquiries/searchInquiriesResultPage.jsp", useTile = false) })
public class SearchInquiriesResultPageDA extends FenixDispatchAction {

    public ActionForward prepare(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {

        SearchInquiriesResultPageDTO searchPageDTO = (SearchInquiriesResultPageDTO) actionForm;
        if (searchPageDTO.isEmptyExecutionSemesterID()) {
            final ExecutionSemester executionSemester = ExecutionSemester.readActualExecutionSemester();
            if (executionSemester != null) {
                final ExecutionSemester previous = executionSemester.getPreviousExecutionPeriod();
                if (previous != null) {
                    searchPageDTO.setExecutionSemesterID(previous.getExternalId());
                }
            }
        }

        if (!searchPageDTO.isEmptyExecutionSemesterID()) {
            return selectExecutionSemester(actionMapping, actionForm, request, response);
        }

        request.setAttribute("executionCourses", Collections.EMPTY_LIST);
        request.setAttribute("executionDegrees", Collections.EMPTY_LIST);
        request.setAttribute("executionSemesters", getExecutionSemesters());

        return actionMapping.findForward("searchPage");
    }

    public ActionForward selectExecutionSemester(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {

        SearchInquiriesResultPageDTO searchPageDTO = (SearchInquiriesResultPageDTO) actionForm;

        ExecutionSemester executionSemester = searchPageDTO.getExecutionSemester();
        if (executionSemester == null) {
            return prepare(actionMapping, actionForm, request, response);
        }

        request.setAttribute("executionCourses", Collections.EMPTY_LIST);
        request.setAttribute("executionDegrees", getExecutionDegrees(executionSemester));
        request.setAttribute("executionSemesters", getExecutionSemesters());

        return actionMapping.findForward("searchPage");
    }

    public ActionForward selectExecutionDegree(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {

        SearchInquiriesResultPageDTO searchPageDTO = (SearchInquiriesResultPageDTO) actionForm;

        ExecutionSemester executionSemester = searchPageDTO.getExecutionSemester();
        if (executionSemester == null) {
            return prepare(actionMapping, actionForm, request, response);
        }

        ExecutionDegree executionDegree = searchPageDTO.getExecutionDegree();
        if (executionDegree == null) {
            return selectExecutionSemester(actionMapping, actionForm, request, response);
        }

        Collection<ExecutionCourse> executionCourses = new ArrayList<ExecutionCourse>();
        for (StudentInquiriesCourseResult studentInquiriesCourseResult : executionDegree.getStudentInquiriesCourseResults()) {
            final ExecutionCourse executionCourse = studentInquiriesCourseResult.getExecutionCourse();
            if (executionCourse != null && executionCourse.getExecutionPeriod() == executionSemester) {
                final Boolean publicDisclosure = studentInquiriesCourseResult.getPublicDisclosure();
                final TeachingInquiry responsibleTeachingInquiry = executionCourse.getResponsibleTeachingInquiry();
                if ((publicDisclosure != null && publicDisclosure.booleanValue())
                        || (responsibleTeachingInquiry != null && responsibleTeachingInquiry
                                .getResultsDisclosureToAcademicComunity())) {
                    executionCourses.add(studentInquiriesCourseResult.getExecutionCourse());
                }
            }
        }
        Collections.sort((List<ExecutionCourse>) executionCourses, ExecutionCourse.EXECUTION_COURSE_NAME_COMPARATOR);

        request.setAttribute("executionCourses", executionCourses);
        request.setAttribute("executionDegrees", getExecutionDegrees(executionSemester));
        request.setAttribute("executionSemesters", getExecutionSemesters());

        return actionMapping.findForward("searchPage");
    }

    public ActionForward selectExecutionCourse(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {

        request.setAttribute("executionCourse", ((SearchInquiriesResultPageDTO) actionForm).getExecutionCourse());
        return selectExecutionDegree(actionMapping, actionForm, request, response);
    }

    private Collection<ExecutionSemester> getExecutionSemesters() {
        Collection<ExecutionSemester> executionSemesters = new ArrayList<ExecutionSemester>();
        for (final ExecutionSemester executionSemester : ExecutionSemester.readNotClosedPublicExecutionPeriods()) {
            if (executionSemester.getInquiryResponsePeriod(InquiryResponsePeriodType.STUDENT) != null) {
                executionSemesters.add(executionSemester);
            }
        }
        Collections.sort((List<ExecutionSemester>) executionSemesters, new ReverseComparator(
                ExecutionSemester.COMPARATOR_BY_SEMESTER_AND_YEAR));
        return executionSemesters;
    }

    private Collection<ExecutionDegree> getExecutionDegrees(ExecutionSemester executionSemester) {
        Collection<ExecutionDegree> executionDegrees = new ArrayList<ExecutionDegree>();
        for (final ExecutionDegree executionDegree : executionSemester.getExecutionYear().getExecutionDegrees()) {
            if (!executionDegree.getStudentInquiriesCourseResults().isEmpty()) {
                executionDegrees.add(executionDegree);
            }
        }
        Collections.sort((List<ExecutionDegree>) executionDegrees,
                ExecutionDegree.EXECUTION_DEGREE_COMPARATORY_BY_DEGREE_TYPE_AND_NAME);
        return executionDegrees;
    }
}
