package net.sourceforge.fenixedu.presentationTier.Action.manager.manager;

import pt.ist.fenixWebFramework.struts.annotations.ExceptionHandling;
import pt.ist.fenixWebFramework.struts.annotations.Exceptions;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;

@Mapping(module = "manager", path = "/insertProfessorShipByNumber",
        input = "/insertProfessorShipByNumber.do?method=prepareInsert&page=0", attribute = "teacherForm",
        formBean = "teacherForm", scope = "request", parameter = "method")
@Forwards(value = {
        @Forward(name = "insertProfessorShip", path = "/manager/insertTeacherByNumber_bd.jsp", tileProperties = @Tile(
                navLocal = "/manager/teachersBodyModificationNavLocalManager.jsp")),
        @Forward(name = "readTeacherInCharge", path = "/readTeacherInCharge.do") })
@Exceptions(value = { @ExceptionHandling(
        type = net.sourceforge.fenixedu.presentationTier.Action.exceptions.NonExistingActionException.class,
        key = "resources.Action.exceptions.NonExistingActionException",
        handler = net.sourceforge.fenixedu.presentationTier.config.FenixErrorExceptionHandler.class, scope = "request") })
public class InsertProfessorShipByNumberDAForManager extends
        net.sourceforge.fenixedu.presentationTier.Action.manager.InsertProfessorShipByNumberDA {
}