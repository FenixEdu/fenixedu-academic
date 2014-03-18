package net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.actionMappings;


import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.ExceptionHandling;
import pt.ist.fenixWebFramework.struts.annotations.Exceptions;

import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.ManageShiftsDA;


@Mapping(path = "/manageShiftStudents", module = "resourceAllocationManager", input = "/manageShift.do?method=prepareEditShift&page=0", formBean = "changShiftOfStudents")
@Forwards(value = {
	@Forward(name = "Continue", path = "/manageShift.do?method=prepareEditShift&page=0")})
@Exceptions(value = {
	@ExceptionHandling(key = "message.unable.to.transfer.students", handler = net.sourceforge.fenixedu.presentationTier.config.FenixErrorExceptionHandler.class, type = net.sourceforge.fenixedu.applicationTier.Servico.resourceAllocationManager.ChangeStudentsShift.UnableToTransferStudentsException.class)})
public class ManageShiftsDA_AM3 extends ManageShiftsDA {

}
