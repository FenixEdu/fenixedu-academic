package net.sourceforge.fenixedu.presentationTier.Action.manager.scientificCouncil;

import pt.ist.fenixWebFramework.struts.annotations.ExceptionHandling;
import pt.ist.fenixWebFramework.struts.annotations.Exceptions;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;

@Mapping(module = "scientificCouncil", path = "/insertProfessorShipByNumber",
        input = "/insertProfessorShipByNumber.do?method=prepareInsert&page=0", attribute = "masterDegreeCreditsForm",
        formBean = "masterDegreeCreditsForm", scope = "request", parameter = "method")
@Forwards(value = {
        @Forward(name = "insertProfessorShip", path = "/manager/insertTeacherByNumber_bd.jsp", tileProperties = @Tile(
                title = "private.scientificcouncil.teachers")),
        @Forward(name = "readTeacherInCharge", path = "/readTeacherInCharge.do") })
@Exceptions(value = { @ExceptionHandling(
        type = net.sourceforge.fenixedu.presentationTier.Action.exceptions.NonExistingActionException.class,
        key = "presentationTier.Action.exceptions.NonExistingActionException",
        handler = net.sourceforge.fenixedu.presentationTier.config.FenixErrorExceptionHandler.class, scope = "request") })
public class InsertProfessorShipByNumberDAForScientificCouncil extends
        net.sourceforge.fenixedu.presentationTier.Action.manager.InsertProfessorShipByNumberDA {
}