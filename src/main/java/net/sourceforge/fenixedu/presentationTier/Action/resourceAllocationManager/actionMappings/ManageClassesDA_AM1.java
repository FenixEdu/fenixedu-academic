package net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.actionMappings;

import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.ManageClassesDA;
import pt.ist.fenixWebFramework.struts.annotations.ExceptionHandling;
import pt.ist.fenixWebFramework.struts.annotations.Exceptions;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(path = "/manageClasses", module = "resourceAllocationManager", input = "/manageClasses.do?method=listClasses&page=0",
        formBean = "classForm")
@Forwards(value = { @Forward(name = "ShowClassList", path = "/manageClasses.jsp", useTile = false) })
@Exceptions(value = { @ExceptionHandling(key = "resources.Action.exceptions.ExistingActionException",
        handler = net.sourceforge.fenixedu.presentationTier.config.FenixErrorExceptionHandler.class,
        type = net.sourceforge.fenixedu.presentationTier.Action.exceptions.ExistingActionException.class) })
public class ManageClassesDA_AM1 extends ManageClassesDA {

}
