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
package net.sourceforge.fenixedu.presentationTier.Action.academicAdministration.executionCourseManagement;

import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.manager.MergeExecutionCourses;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.time.calendarStructure.AcademicInterval;
import net.sourceforge.fenixedu.presentationTier.Action.academicAdministration.AcademicAdministrationApplication.AcademicAdminExecutionsApp;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.config.FenixErrorExceptionHandler;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.bennu.portal.EntryPoint;
import org.fenixedu.bennu.portal.StrutsFunctionality;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.struts.annotations.ExceptionHandling;
import pt.ist.fenixWebFramework.struts.annotations.Exceptions;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

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
        @Forward(
                name = "sucess",
                path = "/academicAdministration/chooseDegreesForExecutionCourseMerge.do?method=prepareChooseDegreesAndExecutionPeriod") })
@Exceptions({
        @ExceptionHandling(type = MergeExecutionCourses.SourceAndDestinationAreTheSameException.class,
                key = "error.cannot.merge.execution.course.with.itself", handler = FenixErrorExceptionHandler.class,
                scope = "request"),
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
        AcademicInterval actualSemester = ExecutionSemester.readActualExecutionSemester().getAcademicInterval();

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

        ExecutionCourse sourceExecutionCourse = degreeBean.getSourceExecutionCourse();
        ExecutionCourse destinationExecutionCourse = degreeBean.getDestinationExecutionCourse();

        String sourceExecutionCourseId = sourceExecutionCourse.getExternalId();

        String destinationExecutionCourseId = destinationExecutionCourse.getExternalId();

        Boolean error = false;

        String sourceName = sourceExecutionCourse.getName() + " [" + sourceExecutionCourse.getDegreePresentationString() + "]";
        String destinationName =
                destinationExecutionCourse.getName() + " [" + destinationExecutionCourse.getDegreePresentationString() + "]";
        String periodName =
                destinationExecutionCourse.getExecutionPeriod().getName() + " "
                        + destinationExecutionCourse.getExecutionPeriod().getYear();

        try {
            MergeExecutionCourses.runMergeExecutionCourses(destinationExecutionCourseId, sourceExecutionCourseId);
        } catch (DomainException ex) {
            error = true;
            addActionMessage("error", request, ex.getMessage());
        }

        if (!error) {
            addActionMessage("success", request, "message.merge.execution.courses.success", sourceName, destinationName,
                    periodName);
        }
        return mapping.findForward("sucess");
    }

    public static class DegreesMergeBean implements Serializable {

        private static final long serialVersionUID = -5030417665530169855L;

        private Degree sourceDegree;

        private Degree destinationDegree;

        private ExecutionCourse sourceExecutionCourse;

        private ExecutionCourse destinationExecutionCourse;

        private AcademicInterval academicInterval;

        public ExecutionCourse getSourceExecutionCourse() {
            return sourceExecutionCourse;
        }

        public void setSourceExecutionCourse(ExecutionCourse sourceExecutionCourse) {
            this.sourceExecutionCourse = sourceExecutionCourse;
        }

        public ExecutionCourse getDestinationExecutionCourse() {
            return destinationExecutionCourse;
        }

        public void setDestinationExecutionCourse(ExecutionCourse destinationExecutionCourse) {
            this.destinationExecutionCourse = destinationExecutionCourse;
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