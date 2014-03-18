package net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.actionMappings;

import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.ManipularSalasAction;
import pt.ist.fenixWebFramework.struts.annotations.ExceptionHandling;
import pt.ist.fenixWebFramework.struts.annotations.Exceptions;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(path = "/manipularSalas", module = "resourceAllocationManager", input = "/manipularSalas.jsp", attribute = "indexForm",
        formBean = "indexForm")
@Forwards(value = { @Forward(name = "VerSala", path = "/viewRoom.do?method=prepare"),
        @Forward(name = "EditarSala", path = "/editarSala.jsp", useTile = false) })
@Exceptions(value = { @ExceptionHandling(key = "resources.Action.exceptions.notAuthorizedActionDeleteException",
        handler = net.sourceforge.fenixedu.presentationTier.config.FenixErrorExceptionHandler.class,
        type = net.sourceforge.fenixedu.presentationTier.Action.exceptions.notAuthorizedActionDeleteException.class) })
public class ManipularSalasAction_AM1 extends ManipularSalasAction {

}
