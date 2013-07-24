package net.sourceforge.fenixedu.presentationTier.Action.academicAdministration.executionCourseManagement;

import net.sourceforge.fenixedu.presentationTier.Action.manager.executionCourseManagement.EditExecutionCourseManageCurricularCoursesDispatchAction;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

/*
 * 
 * @author Fernanda Quitério 23/Dez/2003
 *  
 */
@Mapping(module = "academicAdministration", path = "/editExecutionCourseManageCurricularCourses",
        input = "/editExecutionCourse.do?method=prepareEditExecutionCourse&page=0", attribute = "executionCourseForm",
        formBean = "executionCourseForm", scope = "request", parameter = "method")
@Forwards(
        value = {
                @Forward(name = "editExecutionCourse", path = "/editExecutionCourse.do?method=editExecutionCourse&page=0"),
                @Forward(name = "listExecutionCourseActions",
                        path = "/academicAdministration/executionCourseManagement/listExecutionCourseActions.jsp"),
                @Forward(name = "manageCurricularSeparation",
                        path = "/seperateExecutionCourse.do?method=manageCurricularSeparation"),
                @Forward(name = "associateCurricularCourse",
                        path = "/academicAdministration/executionCourseManagement/associateCurricularCourse.jsp"),
                @Forward(
                        name = "prepareAssociateCurricularCourseChooseDegreeCurricularPlan",
                        path = "/academicAdministration/executionCourseManagement/prepareAssociateCurricularCourseChooseDegreeCurricularPlan.jsp") })
public class EditExecutionCourseManageCurricularCoursesDispatchActionForAcademicAdmin extends
        EditExecutionCourseManageCurricularCoursesDispatchAction {
}
