package net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.actionMappings;

import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.ManageShiftsDA;
import pt.ist.fenixWebFramework.struts.annotations.ExceptionHandling;
import pt.ist.fenixWebFramework.struts.annotations.Exceptions;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(path = "/manageShifts", module = "resourceAllocationManager", input = "/manageShifts.do?method=listShifts&page=0",
        formBean = "createShiftForm")
@Forwards(value = { @Forward(name = "ShowShiftList", path = "/manageShifts.jsp", useTile = false),
        @Forward(name = "EditShift", path = "/manageShift.do?method=prepareEditShift&page=0") })
@Exceptions(value = { @ExceptionHandling(key = "resources.Action.exceptions.ExistingActionException",
        handler = net.sourceforge.fenixedu.presentationTier.config.FenixErrorExceptionHandler.class,
        type = net.sourceforge.fenixedu.presentationTier.Action.exceptions.ExistingActionException.class) })
public class ManageShiftsDA_AM1 extends ManageShiftsDA {

}
