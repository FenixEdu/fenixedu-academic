package net.sourceforge.fenixedu.presentationTier.Action.manager.executionCourseManagement;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

/*
 * 
 * @author Fernanda Quitério 23/Dez/2003
 *  
 */
@Mapping(module = "manager", path = "/editExecutionCourseManageCurricularCourses",
        input = "/editExecutionCourse.do?method=prepareEditExecutionCourse&page=0", attribute = "executionCourseForm",
        formBean = "executionCourseForm", scope = "request", parameter = "method")
@Forwards(value = {
        @Forward(name = "editExecutionCourse", path = "/editExecutionCourse.do?method=editExecutionCourse&page=0"),
        @Forward(name = "manageCurricularSeparation", path = "/seperateExecutionCourse.do?method=manageCurricularSeparation"),
        @Forward(name = "associateCurricularCourse", path = "/manager/executionCourseManagement/associateCurricularCourse.jsp"),
        @Forward(name = "prepareAssociateCurricularCourseChooseDegreeCurricularPlan",
                path = "/manager/executionCourseManagement/prepareAssociateCurricularCourseChooseDegreeCurricularPlan.jsp") })
public class EditExecutionCourseManageCurricularCoursesDispatchActionForManager extends
        EditExecutionCourseManageCurricularCoursesDispatchAction {
}
