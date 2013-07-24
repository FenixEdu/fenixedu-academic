package net.sourceforge.fenixedu.presentationTier.Action.academicAdministration.executionCourseManagement;

import net.sourceforge.fenixedu.presentationTier.Action.manager.MergeExecutionCourseDispatchionAction;
import pt.ist.fenixWebFramework.struts.annotations.ExceptionHandling;
import pt.ist.fenixWebFramework.struts.annotations.Exceptions;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(module = "academicAdministration", path = "/chooseDegreesForExecutionCourseMerge",
        input = "/chooseDegreesForExecutionCourseMerge.do?method=prepareChooseDegreesAndExecutionPeriod",
        attribute = "mergeExecutionCoursesForm", formBean = "mergeExecutionCoursesForm", scope = "request", parameter = "method")
@Forwards(
        value = {
                @Forward(name = "chooseDegreesAndExecutionPeriod",
                        path = "/academicAdministration/executionCourseManagement/chooseDegreesForExecutionCourseMerge.jsp"),
                @Forward(
                        name = "chooseExecutionCourses",
                        path = "/academicAdministration/executionCourseManagement/chooseExecutionCoursesForExecutionCourseMerge.jsp"),
                @Forward(name = "sucess",
                        path = "/chooseDegreesForExecutionCourseMerge.do?method=prepareChooseDegreesAndExecutionPeriod") })
@Exceptions(
        value = {
                @ExceptionHandling(
                        type = net.sourceforge.fenixedu.applicationTier.Servico.manager.MergeExecutionCourses.SourceAndDestinationAreTheSameException.class,
                        key = "error.cannot.merge.execution.course.with.itself",
                        handler = net.sourceforge.fenixedu.presentationTier.config.FenixErrorExceptionHandler.class,
                        scope = "request"),
                @ExceptionHandling(
                        type = net.sourceforge.fenixedu.applicationTier.Servico.manager.MergeExecutionCourses.DuplicateShiftNameException.class,
                        key = "error.duplicate.shift.names",
                        handler = net.sourceforge.fenixedu.presentationTier.config.FenixErrorExceptionHandler.class,
                        scope = "request") })
public class MergeExecutionCourseDA extends MergeExecutionCourseDispatchionAction {
}