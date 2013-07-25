package net.sourceforge.fenixedu.presentationTier.Action.manager.executionCourseManagement.manager;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(module = "manager", path = "/seperateExecutionCourse",
        input = "/editExecutionCourse.do?method=prepareEditECChooseExecDegreeAndCurYear&page=0",
        attribute = "separateExecutionCourseForm", formBean = "separateExecutionCourseForm", scope = "request",
        parameter = "method")
@Forwards(
        value = {
                @Forward(name = "returnFromTransfer", path = "/manager/executionCourseManagement/listExecutionCourseActions.jsp"),
                @Forward(name = "manageCurricularSeparation",
                        path = "/manager/executionCourseManagement/manageCurricularSeparation.jsp"),
                @Forward(name = "showTransferPage", path = "/manager/executionCourseManagement/transferCurricularCourses.jsp"),
                @Forward(name = "showSeparationPage", path = "/manager/executionCourseManagement/separateExecutionCourse.jsp") })
public class SeparateExecutionCourseDispatchActionForManager extends
        net.sourceforge.fenixedu.presentationTier.Action.manager.executionCourseManagement.SeperateExecutionCourseDispatchAction {
}