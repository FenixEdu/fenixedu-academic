package net.sourceforge.fenixedu.presentationTier.Action.manager.actionMappings;


import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Forward;

import net.sourceforge.fenixedu.presentationTier.Action.manager.MergeExecutionCourseDispatchionAction;


@Mapping(path = "/chooseDegreesForExecutionCourseMerge", module = "resourceAllocationManager", formBean = "chooseDegreesForExecutionCourseMerge", validate = false)
@Forwards(value = {
	@Forward(name = "chooseDegreesAndExecutionPeriod", path = "df.page.chooseDegreesForExecutionCourseMerge"),
	@Forward(name = "chooseExecutionCourses", path = "df.page.chooseExecutionCoursesForExecutionCourseMerge")})
public class MergeExecutionCourseDispatchionAction_AM3 extends MergeExecutionCourseDispatchionAction {

}
