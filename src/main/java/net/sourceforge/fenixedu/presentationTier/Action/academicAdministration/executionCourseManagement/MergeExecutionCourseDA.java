package net.sourceforge.fenixedu.presentationTier.Action.academicAdministration.executionCourseManagement;

import net.sourceforge.fenixedu.applicationTier.Servico.manager.MergeExecutionCourses;
import net.sourceforge.fenixedu.presentationTier.Action.academicAdministration.AcademicAdministrationApplication.AcademicAdminExecutionsApp;
import net.sourceforge.fenixedu.presentationTier.Action.manager.MergeExecutionCourseDispatchionAction;
import net.sourceforge.fenixedu.presentationTier.config.FenixErrorExceptionHandler;

import org.fenixedu.bennu.portal.StrutsFunctionality;

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
public class MergeExecutionCourseDA extends MergeExecutionCourseDispatchionAction {
}