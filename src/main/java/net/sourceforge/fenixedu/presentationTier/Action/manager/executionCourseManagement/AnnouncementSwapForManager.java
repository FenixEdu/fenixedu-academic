package net.sourceforge.fenixedu.presentationTier.Action.manager.executionCourseManagement;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(path = "/announcementSwap", module = "manager")
@Forwards({ @Forward(name = "chooseExecutionCourse", path = "/manager/executionCourseManagement/chooseExecutionCourse.jsp") })
public class AnnouncementSwapForManager extends AnnouncementSwap {
}
