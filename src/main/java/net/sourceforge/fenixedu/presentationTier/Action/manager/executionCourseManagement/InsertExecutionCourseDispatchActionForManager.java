package net.sourceforge.fenixedu.presentationTier.Action.manager.executionCourseManagement;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(module = "manager", path = "/insertExecutionCourse", attribute = "insertExecutionCourseForm",
        formBean = "insertExecutionCourseForm", scope = "request", parameter = "method")
@Forwards({ @Forward(name = "firstPage", path = "df.executionCourseManagement.page.firstPage"),
        @Forward(name = "insertExecutionCourse", path = "df.executionCourseManagement.page.insertExecutionCourse") })
public class InsertExecutionCourseDispatchActionForManager extends InsertExecutionCourseDispatchAction {
}
