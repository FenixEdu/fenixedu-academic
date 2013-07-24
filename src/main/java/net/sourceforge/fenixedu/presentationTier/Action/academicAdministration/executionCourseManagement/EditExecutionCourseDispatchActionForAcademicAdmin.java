package net.sourceforge.fenixedu.presentationTier.Action.academicAdministration.executionCourseManagement;

import net.sourceforge.fenixedu.presentationTier.Action.manager.executionCourseManagement.EditExecutionCourseDispatchAction;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

/**
 * @author Fernanda Quitério 19/Dez/2003
 * 
 */
@Mapping(path = "/editExecutionCourse", module = "academicAdministration",
        input = "/editExecutionCourse.do?method=prepareEditECChooseExecDegreeAndCurYear&amp;page=0", scope = "request",
        parameter = "method", attribute = "executionCourseForm", formBean = "executionCourseForm")
@Forwards({
        @Forward(name = "editExecutionCourse", path = "/academicAdministration/executionCourseManagement/editExecutionCourse.jsp"),
        @Forward(name = "viewExecutionCourse", path = "/academicAdministration/executionCourseManagement/viewExecutionCourse.jsp"),
        @Forward(name = "listExecutionCourseActions",
                path = "/academicAdministration/executionCourseManagement/listExecutionCourseActions.jsp") })
public class EditExecutionCourseDispatchActionForAcademicAdmin extends EditExecutionCourseDispatchAction {
}