package net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.actionMappings;


import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Forward;

import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.ChooseExecutionCourseAction;


@Mapping(path = "/chooseExecutionCourseForm", module = "resourceAllocationManager", input = "/chooseExecutionCourse.jsp", formBean = "chooseExecutionCourseForm", validate = false)
@Forwards(value = {
	@Forward(name = "forwardChoose", path = "/createExam.do?method=prepare&page=0"),
	@Forward(name = "showForm", path = "/chooseExecutionCourse.jsp")})
public class ChooseExecutionCourseAction_AM4 extends ChooseExecutionCourseAction {

}
