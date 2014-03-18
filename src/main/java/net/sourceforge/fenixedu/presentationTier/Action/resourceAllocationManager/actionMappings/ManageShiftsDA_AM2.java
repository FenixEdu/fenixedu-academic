package net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.actionMappings;


import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.ExceptionHandling;
import pt.ist.fenixWebFramework.struts.annotations.Exceptions;

import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.ManageShiftsDA;


@Mapping(path = "/manageShift", module = "resourceAllocationManager", input = "/manageShift.do?method=prepareEditShift&page=0", formBean = "createShiftForm")
@Forwards(value = {
	@Forward(name = "EditShift", path = "/manageShift.jsp"),
	@Forward(name = "ViewStudentsEnroled", path = "/viewStudentsEnroledInShift.jsp")})
@Exceptions(value = {
	@ExceptionHandling(key = "resources.Action.exceptions.ExistingActionException", handler = net.sourceforge.fenixedu.presentationTier.config.FenixErrorExceptionHandler.class, type = net.sourceforge.fenixedu.presentationTier.Action.exceptions.ExistingActionException.class)})
public class ManageShiftsDA_AM2 extends ManageShiftsDA {

}
