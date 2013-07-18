package net.sourceforge.fenixedu.presentationTier.Action.academicAdministration.executionCourseManagement;

import net.sourceforge.fenixedu.presentationTier.Action.manager.executionCourseManagement.EditExecutionCourseDA;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(path = "/editExecutionCourseChooseExPeriod", module = "academicAdministration")
@Forwards({
        @Forward(name = "editChooseExecutionPeriod",
                path = "/academicAdministration/executionCourseManagement/editChooseExecutionPeriod.jsp"),
        @Forward(name = "editChooseCourseAndYear",
                path = "/academicAdministration/executionCourseManagement/editChooseCourseAndYear.jsp"),
        @Forward(name = "editExecutionCourse",
                path = "/academicAdministration/executionCourseManagement/listExecutionCourseActions.jsp") })
public class EditExecutionCourseDAForAcademicAdmin extends EditExecutionCourseDA {
}
