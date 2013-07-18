package net.sourceforge.fenixedu.presentationTier.Action.academicAdministration.executionCourseManagement;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(module = "academicAdministration", path = "/seperateExecutionCourse",
        input = "/editExecutionCourse.do?method=prepareEditECChooseExecDegreeAndCurYear&page=0",
        attribute = "separateExecutionCourseForm", formBean = "separateExecutionCourseForm", scope = "request",
        parameter = "method")
@Forwards(value = {
        @Forward(name = "returnFromTransfer",
                path = "/academicAdministration/executionCourseManagement/listExecutionCourseActions.jsp"),
        @Forward(name = "manageCurricularSeparation",
                path = "/academicAdministration/executionCourseManagement/manageCurricularSeparation.jsp"),
        @Forward(name = "showTransferPage",
                path = "/academicAdministration/executionCourseManagement/transferCurricularCourses.jsp"),
        @Forward(name = "showSeparationPage",
                path = "/academicAdministration/executionCourseManagement/separateExecutionCourse.jsp") })
public class SeperateExecutionCourseDispatchActionForAcademicAdmin extends
        net.sourceforge.fenixedu.presentationTier.Action.manager.executionCourseManagement.SeperateExecutionCourseDispatchAction {
}