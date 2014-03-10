package net.sourceforge.fenixedu.presentationTier.Action.coordinator;

import net.sourceforge.fenixedu.presentationTier.Action.commons.ChooseCurricularCourseDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.NonExistingActionException;
import net.sourceforge.fenixedu.presentationTier.config.FenixErrorExceptionHandler;
import pt.ist.fenixWebFramework.struts.annotations.ExceptionHandling;
import pt.ist.fenixWebFramework.struts.annotations.Exceptions;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(path = "/listStudentsByCourse", module = "coordinator", functionality = DegreeCoordinatorIndex.class)
@Forwards({ @Forward(name = "ChooseSuccess", path = "/coordinator/student/displayStudentListByCourse_bd.jsp"),
        @Forward(name = "PrepareSuccess", path = "/coordinator/student/displayStudentListByDegree_bd.jsp"),
        @Forward(name = "NoStudents", path = "/coordinator/listStudentsForCoordinator.do?method=getCurricularCourses"),
        @Forward(name = "NotAuthorized", path = "/coordinator/student/notAuthorized_bd.jsp") })
@Exceptions(@ExceptionHandling(key = "resources.Action.exceptions.NonExistingActionException",
        handler = FenixErrorExceptionHandler.class, type = NonExistingActionException.class))
public class ChooseCurricularCourseDispatchActionForCoordinator extends ChooseCurricularCourseDispatchAction {

}
