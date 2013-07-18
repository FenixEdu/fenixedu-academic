package net.sourceforge.fenixedu.presentationTier.Action.academicAdministration.executionCourseManagement;

import net.sourceforge.fenixedu.presentationTier.Action.manager.executionCourseManagement.AnnouncementSwap;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(path = "/announcementSwap", module = "academicAdministration")
@Forwards({ @Forward(name = "chooseExecutionCourse",
        path = "/academicAdministration/executionCourseManagement/chooseExecutionCourse.jsp") })
public class AnnouncementSwapForAcademicAdmin extends AnnouncementSwap {
}
