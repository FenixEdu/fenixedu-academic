package net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.actionMappings;

import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.ManageClassDA;
import pt.ist.fenixWebFramework.struts.annotations.ExceptionHandling;
import pt.ist.fenixWebFramework.struts.annotations.Exceptions;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(path = "/manageClass", module = "resourceAllocationManager", input = "/manageClass.do?method=prepare&page=0",
        formBean = "classForm")
@Forwards(value = { @Forward(name = "EditClass", path = "/manageClass.jsp", useTile = false),
        @Forward(name = "AddShifts", path = "/addShifts.jsp", useTile = false),
        @Forward(name = "ViewSchedule", path = "/viewClassSchedule.jsp", useTile = false) })
@Exceptions(value = { @ExceptionHandling(key = "resources.Action.exceptions.ExistingActionException",
        handler = net.sourceforge.fenixedu.presentationTier.config.FenixErrorExceptionHandler.class,
        type = net.sourceforge.fenixedu.presentationTier.Action.exceptions.ExistingActionException.class) })
public class ManageClassDA_AM1 extends ManageClassDA {

}
