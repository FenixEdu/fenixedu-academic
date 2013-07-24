package net.sourceforge.fenixedu.presentationTier.Action.manager.executionCourseManagement;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(path = "/editExecutionCourseChooseExPeriod", module = "manager")
@Forwards({
        @Forward(name = "editChooseExecutionPeriod", path = "/manager/executionCourseManagement/editChooseExecutionPeriod.jsp"),
        @Forward(name = "editChooseCourseAndYear", path = "/manager/executionCourseManagement/editChooseCourseAndYear.jsp"),
        @Forward(name = "editExecutionCourse", path = "/manager/executionCourseManagement/listExecutionCourseActions.jsp") })
public class EditExecutionCourseDAForManager extends EditExecutionCourseDA {
}