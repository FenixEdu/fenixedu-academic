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
package org.fenixedu.academic.ui.struts.action.academicAdministration.executionCourseManagement;

import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.academic.domain.Degree;
import org.fenixedu.academic.domain.ExecutionCourse;
import org.fenixedu.academic.domain.ExecutionInterval;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.time.calendarStructure.AcademicInterval;
import org.fenixedu.academic.domain.time.calendarStructure.AcademicPeriod;
import org.fenixedu.academic.service.services.exceptions.FenixServiceException;
import org.fenixedu.academic.service.services.manager.MergeExecutionCourses;
import org.fenixedu.academic.ui.struts.action.academicAdministration.AcademicAdministrationApplication.AcademicAdminExecutionsApp;
import org.fenixedu.academic.ui.struts.action.base.FenixDispatchAction;
import org.fenixedu.academic.ui.struts.config.FenixErrorExceptionHandler;
import org.fenixedu.bennu.struts.annotations.ExceptionHandling;
import org.fenixedu.bennu.struts.annotations.Exceptions;
import org.fenixedu.bennu.struts.annotations.Forward;
import org.fenixedu.bennu.struts.annotations.Forwards;
import org.fenixedu.bennu.struts.annotations.Mapping;
import org.fenixedu.bennu.struts.portal.EntryPoint;
import org.fenixedu.bennu.struts.portal.StrutsFunctionality;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;

@StrutsFunctionality(app = AcademicAdminExecutionsApp.class, path = "merge-execution-courses",
        titleKey = "label.manager.executionCourseManagement.join.executionCourse",
        accessGroup = "academic(MANAGE_EXECUTION_COURSES_ADV)")
@Mapping(module = "academicAdministration", path = "/chooseDegreesForExecutionCourseMerge",
        input = "/chooseDegreesForExecutionCourseMerge.do?method=prepareChooseDegreesAndExecutionPeriod",
        formBean = "mergeExecutionCoursesForm")
@Forwards({
        @Forward(name = "chooseDegreesAndExecutionPeriod",
                path = "/academicAdministration/executionCourseManagement/chooseDegreesForExecutionCourseMerge.jsp"),
        @Forward(name = "chooseExecutionCourses",
                path = "/academicAdministration/executionCourseManagement/chooseExecutionCoursesForExecutionCourseMerge.jsp"),
        @Forward(name = "sucess",
                path = "/academicAdministration/chooseDegreesForExecutionCourseMerge.do?method=prepareChooseDegreesAndExecutionPeriod") })
@Exceptions({ @ExceptionHandling(type = MergeExecutionCourses.SourceAndDestinationAreTheSameException.class,
        key = "error.cannot.merge.execution.course.with.itself", handler = FenixErrorExceptionHandler.class, scope = "request"),
        @ExceptionHandling(type = MergeExecutionCourses.DuplicateShiftNameException.class, key = "error.duplicate.shift.names",
                handler = FenixErrorExceptionHandler.class, scope = "request") })
public class MergeExecutionCourseDA extends FenixDispatchAction {

    public ActionForward chooseDegreesAndExecutionPeriod(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {

        Boolean previousOrEqualSemester = false;

        DegreesMergeBean degreeBean = getRenderedObject("degreeBean");
        request.setAttribute("degreeBean", degreeBean);
        RenderUtils.invalidateViewState();

        AcademicInterval choosedSemester = degreeBean.getAcademicInterval();
        AcademicInterval actualSemester = ExecutionInterval.findCurrentChild(AcademicPeriod.SEMESTER, null).getAcademicInterval();

        previousOrEqualSemester = choosedSemester.isBefore(actualSemester) || choosedSemester.isEqualOrEquivalent(actualSemester);

        request.setAttribute("previousOrEqualSemester", previousOrEqualSemester);

        if (degreeBean.getDestinationDegree().getExecutionCourses(degreeBean.getAcademicInterval()).isEmpty()
                && degreeBean.getSourceDegree().getExecutionCourses(degreeBean.getAcademicInterval()).isEmpty()) {
            addActionMessage("error", request, "message.merge.execution.courses.degreesHasNoCourses");
            return mapping.findForward("chooseDegreesAndExecutionPeriod");
        } else {
            if (degreeBean.getDestinationDegree().getExecutionCourses(degreeBean.getAcademicInterval()).isEmpty()) {
                addActionMessage("error", request, "message.merge.execution.courses.destinationDegreeHasNoCourses");
                return mapping.findForward("chooseDegreesAndExecutionPeriod");
            } else {
                if (degreeBean.getSourceDegree().getExecutionCourses(degreeBean.getAcademicInterval()).isEmpty()) {
                    addActionMessage("error", request, "message.merge.execution.courses.sourceDegreeHasNoCourses");
                    return mapping.findForward("chooseDegreesAndExecutionPeriod");
                }
            }
        }
        return mapping.findForward("chooseExecutionCourses");
    }

    @EntryPoint
    public ActionForward prepareChooseDegreesAndExecutionPeriod(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixServiceException {
        DegreesMergeBean degreeBean = new DegreesMergeBean();
        request.setAttribute("degreeBean", degreeBean);
        return mapping.findForward("chooseDegreesAndExecutionPeriod");
    }

    public ActionForward mergeExecutionCourses(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {

        DegreesMergeBean degreeBean = getRenderedObject("degreeBean");
        RenderUtils.invalidateViewState();

        final ExecutionCourse sourceExecutionCourse = degreeBean.getSource().getSourceExecutionCourse();
        final ExecutionCourse destinationExecutionCourse = degreeBean.getDestination().getDestinationExecutionCourse();

        Boolean error = false;

        final String sourceName = degreeBean.getSource().getSourcePresentationName();
        final String destinationName = degreeBean.getDestination().getDestinationPresentationName();
        final String periodName = destinationExecutionCourse.getExecutionInterval().getQualifiedName();

        try {
            MergeExecutionCourses.merge(destinationExecutionCourse, sourceExecutionCourse);
        } catch (DomainException ex) {
            error = true;
            addActionMessage("error", request, ex.getLocalizedMessage());
        } catch (FenixServiceException ex) {
            error = true;
            addActionMessage("error", request, ex.getLocalizedMessage());
        }

        if (!error) {
            addActionMessage("success", request, "message.merge.execution.courses.success", sourceName, destinationName,
                    periodName);
        }
        return mapping.findForward("sucess");
    }

    @SuppressWarnings("serial")
    public static class DegreesMergeBean implements Serializable {

        private Degree sourceDegree;

        private Degree destinationDegree;

        private ExecutionCourseBean source;

        private ExecutionCourseBean destination;

        private AcademicInterval academicInterval;

        public ExecutionCourseBean getSource() {
            return source;
        }

        public void setSource(final ExecutionCourseBean input) {
            this.source = input;
        }

        public ExecutionCourseBean getDestination() {
            return destination;
        }

        public void setDestination(final ExecutionCourseBean input) {
            this.destination = input;
        }

        public AcademicInterval getAcademicInterval() {
            return academicInterval;
        }

        public void setAcademicInterval(AcademicInterval academicInterval) {
            this.academicInterval = academicInterval;
        }

        public Degree getSourceDegree() {
            return sourceDegree;
        }

        public void setSourceDegree(Degree sourceDegree) {
            this.sourceDegree = sourceDegree;
        }

        public Degree getDestinationDegree() {
            return destinationDegree;
        }

        public void setDestinationDegree(Degree destinationDegree) {
            this.destinationDegree = destinationDegree;
        }
    }
}